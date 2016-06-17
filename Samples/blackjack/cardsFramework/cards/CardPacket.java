package blackjack.cardsFramework.cards;

import jMono_Framework.dotNet.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import blackjack.cardsFramework.cards.TraditionalCard.CardSuit;
import blackjack.cardsFramework.cards.TraditionalCard.CardValue;

/**
 * A packet of cards. A card packet may be initialized with a collection of
 * cards. It may lose cards or deal them to {@link Hand}, but may not
 * receive new cards unless derived and overridden.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class CardPacket
{
	protected List<TraditionalCard> cards;

	// <summary>
	// An event which triggers when a card is removed from the collection.
	// </summary>
	public Event<CardEventArgs> lostCard = new Event<CardEventArgs>();

	public int getCount() { return cards.size(); }
	
	/**
	 * Initializes a card collection by simply allocating a new card list.
	 */
	protected CardPacket()
	{
		cards = new ArrayList<TraditionalCard>();
	}

	/**
	 * Initializes a new instance of the {@link CardPacket} class.
	 * 
	 * @param numberOfDecks
	 *        The number of decks to add to the collection.
	 * @param jokersInDeck
	 *        The amount of jokers in each deck.
	 * @param suits
	 *        The suits to add to each decks. Suits are specified as flags
	 *        and several can be added.
	 * @param cardValues
	 *        The card values which will appear in each deck. values are
	 *        specified as flags and several can be added.
	 */
	public CardPacket(int numberOfDecks, int jokersInDeck, int suits, int cardValues)
	{
		cards = new ArrayList<TraditionalCard>();

		for (int deckIndex = 0; deckIndex < numberOfDecks; ++deckIndex)
		{
			addSuit(suits, cardValues);

			for (int j = 0; j < jokersInDeck / 2; ++j)
			{
				cards.add(new TraditionalCard(CardSuit.Club, CardValue.FirstJoker, this));
				cards.add(new TraditionalCard(CardSuit.Club, CardValue.SecondJoker, this));
			}

			if (jokersInDeck % 2 == 1)
			{
				cards.add(new TraditionalCard(CardSuit.Club, CardValue.FirstJoker, this));
			}
		}
	}

	/**
	 * Adds suits of cards to the collection.
	 * 
	 * @param suits
	 *        The suits to add to each decks. Suits are specified as flags
	 *        and several can be added.
	 * @param cardValues
	 *        The card values which will appear in each deck. Values are
	 *        specified as flags and several can be added.
	 */
	private void addSuit(int suits, int cardValues)
	{
		if ((suits & CardSuit.Club.getValue()) == CardSuit.Club.getValue())
		{
			addCards(CardSuit.Club, cardValues);
		}

		if ((suits & CardSuit.Diamond.getValue()) == CardSuit.Diamond.getValue())
		{
			addCards(CardSuit.Diamond, cardValues);
		}

		if ((suits & CardSuit.Heart.getValue()) == CardSuit.Heart.getValue())
		{
			addCards(CardSuit.Heart, cardValues);
		}

		if ((suits & CardSuit.Spade.getValue()) == CardSuit.Spade.getValue())
		{
			addCards(CardSuit.Spade, cardValues);
		}
	}

	/**
	 * Adds cards to the collection.
	 * 
	 * @param suit
	 *        The suit of the added cards.
	 * @param cardValues
	 *        The card values which will appear in each deck. Values are
	 *        specified as flags and several can be added.
	 */
	private void addCards(CardSuit suit, int cardValues)
	{
		if ((cardValues & CardValue.Ace.getValue()) == CardValue.Ace.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Ace, this));
		}

		if ((cardValues & CardValue.Two.getValue()) == CardValue.Two.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Two, this));
		}

		if ((cardValues & CardValue.Three.getValue()) == CardValue.Three.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Three, this));
		}

		if ((cardValues & CardValue.Four.getValue()) == CardValue.Four.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Four, this));
		}

		if ((cardValues & CardValue.Five.getValue()) == CardValue.Five.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Five, this));
		}

		if ((cardValues & CardValue.Six.getValue()) == CardValue.Six.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Six, this));
		}

		if ((cardValues & CardValue.Seven.getValue()) == CardValue.Seven.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Seven, this));
		}

		if ((cardValues & CardValue.Eight.getValue()) == CardValue.Eight.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Eight, this));
		}

		if ((cardValues & CardValue.Nine.getValue()) == CardValue.Nine.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Nine, this));
		}

		if ((cardValues & CardValue.Ten.getValue()) == CardValue.Ten.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Ten, this));
		}

		if ((cardValues & CardValue.Jack.getValue()) == CardValue.Jack.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Jack, this));
		}

		if ((cardValues & CardValue.Queen.getValue()) == CardValue.Queen.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.Queen, this));
		}

		if ((cardValues & CardValue.King.getValue()) == CardValue.King.getValue())
		{
			cards.add(new TraditionalCard(suit, CardValue.King, this));
		}
	}

	/**
	 * Shuffles the cards in the packet by randomly changing card placement.
	 */
	public void shuffle()
	{
		Random random = new Random();
		List<TraditionalCard> shuffledDeck = new ArrayList<TraditionalCard>();

		while (cards.size() > 0)
		{
			TraditionalCard card = cards.get(random.nextInt(cards.size()));
			cards.remove(card);
			shuffledDeck.add(card);
		}

		cards = shuffledDeck;
	}

	/**
	 * Removes the specified card from the packet. The first matching card will
	 * be removed.
	 * 
	 * <p>
	 * Please note that removing a card from a packet may only be performed internally by other
	 * card-framework classes to maintain the principle that a card may only be held by one
	 * {@link CardPacket} only at any given time.
	 * 
	 * @param card
	 *        The card to remove.
	 * @return The card that was removed from the collection.
	 */
	TraditionalCard remove(TraditionalCard card)
	{
		if (cards.contains(card))
		{
			cards.remove(card);

			if (lostCard != null)
			{
				lostCard.handleEvent(this, new CardEventArgs(card));
			}

			return card;
		}
		return null;
	}

	/**
	 * Removes all the cards from the collection.
	 * 
	 * @return A list of all the cards that were removed.
	 */
	List<TraditionalCard> remove()
	{
		List<TraditionalCard> cards = this.cards;
		this.cards = new ArrayList<TraditionalCard>();
		return cards;
	}

	/**
	 * Deals the first card from the collection to a specified hand.
	 * 
	 * @param destinationHand
	 *        The destination hand.
	 * @return The card that was moved to the hand.
	 */
	public TraditionalCard dealCardToHand(Hand destinationHand)
	{
		TraditionalCard firstCard = cards.get(0);

		firstCard.moveToHand(destinationHand);

		return firstCard;
	}

	/**
	 * Deals several cards to a specified hand.
	 * 
	 * @param destinationHand
	 *        The destination hand.
	 * @param count
	 *        The amount of cards to deal.
	 * @return A list of the cards that were moved to the hand.
	 */
	public List<TraditionalCard> dealCardsToHand(Hand destinationHand, int count)
	{
		List<TraditionalCard> dealtCards = new ArrayList<TraditionalCard>();

		for (int cardIndex = 0; cardIndex < count; ++cardIndex)
		{
			dealtCards.add(dealCardToHand(destinationHand));
		}

		return dealtCards;
	}

	
	// TODO: Do I keep these ?
	// ++++++++++ GETTERS ++++++++++ //

	protected List<TraditionalCard> getCards()
	{
		return cards;
	}

	/**
	 * Returns a card at a specified index in the collection.
	 * 
	 * @param index
	 *        The card's index.
	 * @return The card at the specified index.
	 */
	public TraditionalCard getTraditionalCard(int index)
	{
		return cards.get(index);
	}

	// ++++++++++ SETTERS ++++++++++ //

	protected void setcards(List<TraditionalCard> cards)
	{
		this.cards = cards;
	}
}
