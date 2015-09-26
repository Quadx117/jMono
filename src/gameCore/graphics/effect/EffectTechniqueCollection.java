package gameCore.graphics.effect;

import java.util.Arrays;
import java.util.Iterator;

public class EffectTechniqueCollection implements Iterable<EffectTechnique> {

	private EffectTechnique[] _techniques;

	public int getCount() { return _techniques.length; }

	protected EffectTechniqueCollection(EffectTechnique[] techniques) {
		_techniques = techniques;
	}

	protected EffectTechniqueCollection clone(Effect effect) {
		EffectTechnique[] techniques = new EffectTechnique[_techniques.length];
		for (int i = 0; i < _techniques.length; ++i)
			techniques[i] = new EffectTechnique(effect, _techniques[i]);

		return new EffectTechniqueCollection(techniques);
	}

	public EffectTechnique getEffectTechnique(int index) {
		return _techniques[index];
	}

	public EffectTechnique getEffectTechnique(String name) {
		// TODO: Add a name to technique lookup table.
		for (EffectTechnique technique : _techniques) {
			if (technique.getName().equals(name))
				return technique;
		}

		return null;
	}

	@Override
	public Iterator<EffectTechnique> iterator() {
		return Arrays.asList(_techniques).iterator();
	}

	// System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator() {
	// return _techniques.GetEnumerator();
	// }

}
