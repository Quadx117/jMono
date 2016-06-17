package jMono_Framework.graphics.effect;

import java.util.Arrays;
import java.util.Iterator;

public class EffectParameterCollection implements Iterable<EffectParameter> {

    protected final static EffectParameterCollection Empty = new EffectParameterCollection(new EffectParameter[0]);

    private EffectParameter[] _parameters;

    protected EffectParameterCollection(EffectParameter[] parameters)
    {
        _parameters = parameters;
    }

    protected EffectParameterCollection clone()
    {
        if (_parameters.length == 0)
            return Empty;

        EffectParameter[] parameters = new EffectParameter[_parameters.length];
        for (int i = 0; i < _parameters.length; ++i)
            parameters[i] = new EffectParameter(_parameters[i]);

        return new EffectParameterCollection(parameters);
    }

    public int getCount() {
    	return _parameters.length;
	}
	
	public EffectParameter getEffectParameter(int index) {
		return _parameters[index];
	}
	
	public EffectParameter getEffectParameter(String name) {
		// TODO: Add a name to parameter lookup table.
		for (EffectParameter parameter : _parameters) {
			if (parameter.getName().equals(name)) 
				return parameter;
		}
		return null;
    }

    @Override
    public Iterator<EffectParameter> iterator() {
    	return Arrays.asList(_parameters).iterator();
    }

//	System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator() {
//		return _parameters.GetEnumerator();
//	}

}
