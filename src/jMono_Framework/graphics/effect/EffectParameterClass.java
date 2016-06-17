package jMono_Framework.graphics.effect;

import java.util.HashMap;
import java.util.Map;

public enum EffectParameterClass {
	Scalar,		//
	Vector,		//
	Matrix,		//
	Object,		//
	Struct;		//

	private static Map<Integer, EffectParameterClass> map = new HashMap<Integer, EffectParameterClass>();

	static {
		for (EffectParameterClass effectParameterClass : EffectParameterClass.values()) {
			map.put(effectParameterClass.ordinal(), effectParameterClass);
		}
	}

	public static EffectParameterClass valueOf(int effectParameterClass) {
		return map.get(effectParameterClass);
	}
}
