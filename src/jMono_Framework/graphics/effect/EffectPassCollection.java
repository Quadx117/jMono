package jMono_Framework.graphics.effect;

import java.util.Arrays;
import java.util.Iterator;

public class EffectPassCollection implements Iterable<EffectPass> {

	private EffectPass[] _passes;
	
	protected EffectPassCollection(EffectPass[] passes)
	{
	    _passes = passes;
	}
	
	protected EffectPassCollection clone(Effect effect)
	{
		EffectPass[] passes = new EffectPass[_passes.length];
	    for (int i = 0; i < _passes.length; ++i)
	        passes[i] = new EffectPass(effect, _passes[i]);
	
	    return new EffectPassCollection(passes);
	}

	public EffectPass getEffectPass(int index) {
		return _passes[index];
	}
	
	public EffectPass getEffectPass(String name) {
		// TODO: Add a name to pass lookup table.
		for (EffectPass pass : _passes) {
				if (pass.getName().equals(name))
					return pass;
		}
		return null;
	}
	
	public int getCount() {
		return _passes.length;
	}
	
	@Override
	public Iterator<EffectPass> iterator() {
		return Arrays.asList(_passes).iterator();
	}
	
//	System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator() {
//		return _passes.GetEnumerator();
//	}
	
}
