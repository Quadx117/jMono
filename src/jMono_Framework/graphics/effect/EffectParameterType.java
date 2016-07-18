package jMono_Framework.graphics.effect;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines types for effect parameters and shader constants.
 * 
 * @author Eric Perron
 *
 */
public enum EffectParameterType
{
	/**
	 * Pointer to void type.
	 */
	Void,
	/**
	 * Boolean type. Any non-zero will be {@code true}; {@code false} otherwise.
	 */
	Boolean,
	/**
	 * 32-bit integer type.
	 */
	Integer,
	/**
	 * Float type.
	 */
	Single,
	/**
	 * String type.
	 */
	String,
	/**
	 * Any texture type.
	 */
	Texture,
	/**
	 * 1D-texture type.
	 */
	Texture1D,
	/**
	 * 2D-texture type.
	 */
	Texture2D,
	/**
	 * 3D-texture type.
	 */
	Texture3D,
	/**
	 * Cubic texture type.
	 */
	TextureCube;

	private static Map<Integer, EffectParameterType> map = new HashMap<Integer, EffectParameterType>();

	static
	{
		for (EffectParameterType effectParameterType : EffectParameterType.values())
		{
			map.put(effectParameterType.ordinal(), effectParameterType);
		}
	}

	public static EffectParameterType valueOf(int effectParameterType)
	{
		return map.get(effectParameterType);
	}
}
