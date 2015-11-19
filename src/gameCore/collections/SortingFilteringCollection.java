package gameCore.collections;

import gameCore.dotNet.events.EventArgs;
import gameCore.dotNet.events.EventHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * The SortingFilteringCollection class provides efficient, reusable sorting and
 * filtering based on a configurable sort comparer, filter predicate, and
 * associate change events.
 * 
 * @param <T>
 *        The type of data this collection will hold.
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class SortingFilteringCollection<T> implements Collection<T>
{
	private List<T> _items;
	private List<AddJournalEntry<T>> _addJournal;
	private Comparator<AddJournalEntry<T>> _addJournalSortComparison;
	private List<Integer> _removeJournal;
	private List<T> _cachedFilteredItems;
	private boolean _shouldRebuildCache;

	private Predicate<T> _filter;
	private Comparator<T> _sort;

	private BiConsumer<T, EventHandler<EventArgs>> _filterChangedSubscriber;
	private BiConsumer<T, EventHandler<EventArgs>> _filterChangedUnsubscriber;
	private BiConsumer<T, EventHandler<EventArgs>> _sortChangedSubscriber;
	private BiConsumer<T, EventHandler<EventArgs>> _sortChangedUnsubscriber;

	public SortingFilteringCollection(
			Predicate<T> filter,
			BiConsumer<T, EventHandler<EventArgs>> filterChangedSubscriber,
			BiConsumer<T, EventHandler<EventArgs>> filterChangedUnsubscriber,
			Comparator<T> sort,
			BiConsumer<T, EventHandler<EventArgs>> sortChangedSubscriber,
			BiConsumer<T, EventHandler<EventArgs>> sortChangedUnsubscriber)
	{
		_items = new ArrayList<T>();
		_addJournal = new ArrayList<AddJournalEntry<T>>();
		_removeJournal = new ArrayList<Integer>();
		_cachedFilteredItems = new ArrayList<T>();
		_shouldRebuildCache = true;

		_filter = filter;
		_filterChangedSubscriber = filterChangedSubscriber;
		_filterChangedUnsubscriber = filterChangedUnsubscriber;
		_sort = (Comparator<T>) sort;
		_sortChangedSubscriber = sortChangedSubscriber;
		_sortChangedUnsubscriber = sortChangedUnsubscriber;

		_addJournalSortComparison = new Comparator<AddJournalEntry<T>>() {
			@Override
			public int compare(AddJournalEntry<T> x, AddJournalEntry<T> y)
			{
				int result = _sort.compare(x.item, y.item);
				if (result != 0)
					return result;
				return x.order - y.order;
			}
		};
	}

	/*
	 * Replaced by the anonymous Comparator in the constructor
	 * private int compareAddJournalEntry(AddJournalEntry<T> x, AddJournalEntry<T> y) {
	 * int result = _sort.compare(x.item, y.item);
	 * if (result != 0) return result;
	 * return x.order - y.order;
	 * }
	 */

	public <TUserData> void forEachFilteredItem(BiConsumer<T, TUserData> action, TUserData userData)
	{
		if (_shouldRebuildCache)
		{
			processRemoveJournal();
			processAddJournal();

			// Rebuild the cache
			_cachedFilteredItems.clear();
			for (int i = 0; i < _items.size(); ++i)
			{
				if (_filter.test(_items.get(i)))
				{
					_cachedFilteredItems.add(_items.get(i));
				}
			}

			_shouldRebuildCache = false;
		}

		for (int i = 0; i < _cachedFilteredItems.size(); ++i)
		{
			action.accept(_cachedFilteredItems.get(i), userData);
		}

		// If the cache was invalidated as a result of processing items,
		// now is a good time to clear it and give the GC (more of) a
		// chance to do its thing.
		if (_shouldRebuildCache)
		{
			_cachedFilteredItems.clear();
		}
	}

	@Override
	public boolean add(T item)
	{
		// NOTE: We subscribe to item events after items in _addJournal
		// have been merged.
		boolean result = _addJournal.add(new AddJournalEntry<T>(_addJournal.size(), (T) item));
		invalidateCache();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object item)
	{
		if (_addJournal.remove(createKey((T) item)))
			return true;

		int index = _items.indexOf(item);
		if (index >= 0)
		{
			unsubscribeFromItemEvents((T) item);
			_removeJournal.add(index);
			invalidateCache();
			return true;
		}
		return false;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < _items.size(); ++i)
		{
			_filterChangedUnsubscriber.accept(_items.get(i), this::item_FilterPropertyChanged);
			_sortChangedUnsubscriber.accept(_items.get(i), this::item_SortPropertyChanged);
		}

		_addJournal.clear();
		_removeJournal.clear();
		_items.clear();

		invalidateCache();
	}

	@Override
	public boolean contains(Object item)
	{
		return _items.contains(item);
	}

	public void copyTo(T[] array, int arrayIndex)
	{
		System.arraycopy(_items.toArray(), 0, array, arrayIndex, _items.toArray().length);
		// C# code : _items.copyTo(array, arrayIndex);
	}

	public int getSize()
	{
		return _items.size();
	}

	public boolean isReadOnly()
	{
		return false;
	}

	public Iterator<T> iterator()
	{
		return _items.iterator();
	}

	// TODO: ???
	// System.Collections.IEnumerator
	// System.Collections.IEnumerable.GetEnumerator()
	// {
	// return ((System.Collections.IEnumerable)_items).GetEnumerator();
	// }

	private static Comparator<Integer> removeJournalSortComparison = new Comparator<Integer>() {
		@Override
		public int compare(Integer x, Integer y)
		{
			return y.compareTo(x);
		}
	};

	private void processRemoveJournal()
	{
		if (_removeJournal.size() == 0)
			return;

		// Remove items in reverse. (Technically there exist faster
		// ways to bulk-remove froma a variable-length array, but List<T>
		// does not provide such method.)
		Collections.sort(_removeJournal, removeJournalSortComparison);
		for (int i = 0; i < _removeJournal.size(); ++i)
		{
			_items.remove(_removeJournal.get(i).intValue());
		}
		_removeJournal.clear();
	}

	private void processAddJournal()
	{
		if (_addJournal.size() == 0)
			return;

		// Prepare the _addJournal to be merge-sorted with _items.
		// _items is already sorted (because it is always sorted).
		_addJournal.sort(_addJournalSortComparison);

		int iAddJournal = 0;
		int iItems = 0;

		while (iItems < _items.size() && iAddJournal < _addJournal.size())
		{
			T addJournalItem = _addJournal.get(iAddJournal).item;
			// If addJournalItem is less than (belongs before) _items[iItems], insert it.
			if (_sort.compare(addJournalItem, (T) _items.get(iItems)) < 0)
			{
				subscribeToItemEvents(addJournalItem);
				_items.add(iItems, (T) addJournalItem);
				++iAddJournal;
			}
			// Always increment iItems, either because we inserted and
			// need to move past the insertion, or because we didn't
			// insert and need to consider the next element.
			++iItems;
		}

		// If _addJournal had any "tail" items, append them all now.
		for (; iAddJournal < _addJournal.size(); ++iAddJournal)
		{
			T addJournalItem = _addJournal.get(iAddJournal).item;
			subscribeToItemEvents(addJournalItem);
			_items.add((T) addJournalItem);
		}

		_addJournal.clear();
	}

	private void subscribeToItemEvents(T item)
	{
		_filterChangedSubscriber.accept(item, this::item_FilterPropertyChanged);
		_sortChangedSubscriber.accept(item, this::item_SortPropertyChanged);
	}

	private void unsubscribeFromItemEvents(T item)
	{
		_filterChangedUnsubscriber.accept(item, this::item_FilterPropertyChanged);
		_sortChangedUnsubscriber.accept(item, this::item_SortPropertyChanged);
	}

	private void invalidateCache()
	{
		_shouldRebuildCache = true;
	}

	// private BiConsumer<Object, Object> item_FilterPropertyChanged = (sender, e) ->
	// invalidateCache();
	private void item_FilterPropertyChanged(Object sender, EventArgs e)
	{
		invalidateCache();
	}

	// private BiConsumer<Object, Object> Item_SortPropertyChanged = (sender, e) ->
	// Item_SortPropertyChanged(sender, e);
	private void item_SortPropertyChanged(Object sender, EventArgs e)
	{
		@SuppressWarnings("unchecked")
		T item = (T) sender;
		int index = _items.indexOf(item);

		_addJournal.add(new AddJournalEntry<T>(_addJournal.size(), (T) item));
		_removeJournal.add(index);

		// Until the item is back in place, we don't care about its
		// events. We will re-subscribe when _addJournal is processed.
		unsubscribeFromItemEvents(item);
		invalidateCache();
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <V> V[] toArray(V[] arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	// NOTE : Had to move this out of the AddJournalEntry class.
	private AddJournalEntry<T> createKey(T item)
	{
		return new AddJournalEntry<T>(-1, item);
	}

	// C# struct
	// TODO: Since this is a struct, should I add a no-arguments constructor ?
	private class AddJournalEntry<E>
	{
		public int order;
		public E item;

		public AddJournalEntry(int order, E item)
		{
			this.order = order;
			this.item = item;
		}

		// NOTE : Can't make it static here so moved it out of this class.
		// public static AddJournalEntry<E> createKey(E item) {
		// return new AddJournalEntry<E>(-1, item);
		// }

		@SuppressWarnings("unused")
		public int getHashCode()
		{
			return item.hashCode();
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof AddJournalEntry))
				return false;

			return this.item.equals(((AddJournalEntry<E>) obj).item);
		}
	}
}
