package jMono_Framework.graphics.effect;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines classes for effect parameters and shader constants.
 * 
 * @author Eric Perron
 *
 */
public enum EffectParameterClass
{
	/**
	 * Scalar class type.
	 */
	Scalar,
	/**
	 * Vector class type.
	 */
	Vector,
	/**
	 * Matrix class type.
	 */
	Matrix,
	/**
	 * Class type for textures, shaders or strings.
	 */
	Object,
	/**
	 * Structure class type.
	 */
	Struct;

	private static Map<Integer, EffectParameterClass> map = new HashMap<Integer, EffectParameterClass>();

	static
	{
		for (EffectParameterClass effectParameterClass : EffectParameterClass.values())
		{
			map.put(effectParameterClass.ordinal(), effectParameterClass);
		}
	}

	public static EffectParameterClass valueOf(int effectParameterClass)
	{
		return map.get(effectParameterClass);
	}
}
