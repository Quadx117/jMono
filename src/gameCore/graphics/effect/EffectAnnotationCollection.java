package gameCore.graphics.effect;

import java.util.Arrays;
import java.util.Iterator;

public class EffectAnnotationCollection implements Iterable<EffectAnnotation>
{
	protected static EffectAnnotationCollection Empty = new EffectAnnotationCollection(new EffectAnnotation[0]);

	private EffectAnnotation[] _annotations;

	protected EffectAnnotationCollection(EffectAnnotation[] annotations)
	{
		_annotations = annotations;
	}

	public int getCount()
	{
		return _annotations.length;
	}

	public EffectAnnotation getEffectAnnotation(int index)
	{
		return _annotations[index];
	}

	public EffectAnnotation getEffectAnnotation(String name)
	{
		for (EffectAnnotation annotation : _annotations)
		{
			if (annotation.getName().equals(name))
				return annotation;
		}
		return null;
	}

	@Override
	public Iterator<EffectAnnotation> iterator()
	{
		return Arrays.asList(_annotations).iterator();
	}

	// System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator() {
	// return _annotations.GetEnumerator();
	// }

}
