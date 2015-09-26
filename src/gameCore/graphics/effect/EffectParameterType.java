package gameCore.graphics.effect;

import java.util.HashMap;
import java.util.Map;

public enum EffectParameterType {
	Void,				//
	Boolean,			//
	Integer,			//
	Single,				//
	String,				//
	Texture,			//
	Texture1D,			//
	Texture2D,			//
	Texture3D,			//
	TextureCube;		//

	private static Map<Integer, EffectParameterType> map = new HashMap<Integer, EffectParameterType>();

	static {
		for (EffectParameterType effectParameterType : EffectParameterType.values()) {
			map.put(effectParameterType.ordinal(), effectParameterType);
		}
	}

	public static EffectParameterType valueOf(int effectParameterType) {
		return map.get(effectParameterType);
	}
}
