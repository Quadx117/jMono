package gameCore.graphics.states;

import java.util.HashMap;
import java.util.Map;

public enum StencilOperation {
	Keep,						//
	Zero,						//
	Replace,					//
	Increment,					//
	Decrement,					//
	IncrementSaturation,		//
	DecrementSaturation,		//
	Invert;						//

	private static Map<Integer, StencilOperation> map = new HashMap<Integer, StencilOperation>();

	static {
		for (StencilOperation stencilOperation : StencilOperation.values()) {
			map.put(stencilOperation.ordinal(), stencilOperation);
		}
	}

	public static StencilOperation valueOf(int stencilOperation) {
		return map.get(stencilOperation);
	}
}
