package gameCore.graphics.states;

import gameCore.graphics.GraphicsDevice;
import gameCore.graphics.GraphicsResource;

public class DepthStencilState extends GraphicsResource
{
	private boolean _defaultStateObject;

	private boolean _depthBufferEnable;
	private boolean _depthBufferWriteEnable;
	private StencilOperation _counterClockwiseStencilDepthBufferFail;
	private StencilOperation _counterClockwiseStencilFail;
	private CompareFunction _counterClockwiseStencilFunction;
	private StencilOperation _counterClockwiseStencilPass;
	private CompareFunction _depthBufferFunction;
	private int _referenceStencil;
	private StencilOperation _stencilDepthBufferFail;
	private boolean _stencilEnable;
	private StencilOperation _stencilFail;
	private CompareFunction _stencilFunction;
	private int _stencilMask;
	private StencilOperation _stencilPass;
	private int _stencilWriteMask;
	private boolean _twoSidedStencilMode;

	public boolean getDepthBufferEnable()
	{
		return _depthBufferEnable;
	}

	public void setDepthBufferEnable(boolean value)
	{
		throwIfBound();
		_depthBufferEnable = value;
	}

	public boolean getDepthBufferWriteEnable()
	{
		return _depthBufferWriteEnable;
	}

	public void setDepthBufferWriteEnable(boolean value)
	{
		throwIfBound();
		_depthBufferWriteEnable = value;
	}

	public StencilOperation getCounterClockwiseStencilDepthBufferFail()
	{
		return _counterClockwiseStencilDepthBufferFail;
	}

	public void setCounterClockwiseStencilDepthBufferFail(StencilOperation value)
	{
		throwIfBound();
		_counterClockwiseStencilDepthBufferFail = value;
	}

	public StencilOperation getCounterClockwiseStencilFail()
	{
		return _counterClockwiseStencilFail;
	}

	public void setCounterClockwiseStencilFail(StencilOperation value)
	{
		throwIfBound();
		_counterClockwiseStencilFail = value;
	}

	public CompareFunction getCounterClockwiseStencilFunction()
	{
		return _counterClockwiseStencilFunction;
	}

	public void setCounterClockwiseStencilFunction(CompareFunction value)
	{
		throwIfBound();
		_counterClockwiseStencilFunction = value;
	}

	public StencilOperation getCounterClockwiseStencilPass()
	{
		return _counterClockwiseStencilPass;
	}

	public void setCounterClockwiseStencilPass(StencilOperation value)
	{
		throwIfBound();
		_counterClockwiseStencilPass = value;
	}

	public CompareFunction getDepthBufferFunction()
	{
		return _depthBufferFunction;
	}

	public void setDepthBufferFunction(CompareFunction value)
	{
		throwIfBound();
		_depthBufferFunction = value;
	}

	public int getReferenceStencil()
	{
		return _referenceStencil;
	}

	public void setReferenceStencil(int value)
	{
		throwIfBound();
		_referenceStencil = value;
	}

	public StencilOperation getStencilDepthBufferFail()
	{
		return _stencilDepthBufferFail;
	}

	public void setStencilDepthBufferFail(StencilOperation value)
	{
		throwIfBound();
		_stencilDepthBufferFail = value;
	}

	public boolean getStencilEnable()
	{
		return _stencilEnable;
	}

	public void setStencilEnable(boolean value)
	{
		throwIfBound();
		_stencilEnable = value;
	}

	public StencilOperation getStencilFail()
	{
		return _stencilFail;
	}

	public void setStencilFail(StencilOperation value)
	{
		throwIfBound();
		_stencilFail = value;
	}

	public CompareFunction getStencilFunction()
	{
		return _stencilFunction;
	}

	public void setStencilFunction(CompareFunction value)
	{
		throwIfBound();
		_stencilFunction = value;
	}

	public int getStencilMask()
	{
		return _stencilMask;
	}

	public void setStencilMask(int value)
	{
		throwIfBound();
		_stencilMask = value;
	}

	public StencilOperation getStencilPass()
	{
		return _stencilPass;
	}

	public void setStencilPass(StencilOperation value)
	{
		throwIfBound();
		_stencilPass = value;
	}

	public int getStencilWriteMask()
	{
		return _stencilWriteMask;
	}

	public void setStencilWriteMask(int value)
	{
		throwIfBound();
		_stencilWriteMask = value;
	}

	public boolean getTwoSidedStencilMode()
	{
		return _twoSidedStencilMode;
	}

	public void setTwoSidedStencilMode(boolean value)
	{
		throwIfBound();
		_twoSidedStencilMode = value;
	}

	public void bindToGraphicsDevice(GraphicsDevice device)
	{
		if (_defaultStateObject)
			throw new IllegalStateException("You cannot bind a default state object.");
		if (getGraphicsDevice() != null && getGraphicsDevice() != device)
			throw new IllegalStateException("This depth stencil state is already bound to a different graphics device.");
		setGraphicsDevice(device);
	}

	protected void throwIfBound()
	{
		if (_defaultStateObject)
			throw new IllegalStateException("You cannot modify a default depth stencil state object.");
		if (getGraphicsDevice() != null)
			throw new IllegalStateException(
					"You cannot modify the depth stencil state after it has been bound to the graphics device!");
	}

	public DepthStencilState()
	{
		setDepthBufferEnable(true);
		setDepthBufferWriteEnable(true);
		setDepthBufferFunction(CompareFunction.LessEqual);
		setStencilEnable(false);
		setStencilFunction(CompareFunction.Always);
		setStencilPass(StencilOperation.Keep);
		setStencilFail(StencilOperation.Keep);
		setStencilDepthBufferFail(StencilOperation.Keep);
		setTwoSidedStencilMode(false);
		setCounterClockwiseStencilFunction(CompareFunction.Always);
		setCounterClockwiseStencilFail(StencilOperation.Keep);
		setCounterClockwiseStencilPass(StencilOperation.Keep);
		setCounterClockwiseStencilDepthBufferFail(StencilOperation.Keep);
		setStencilMask(Integer.MAX_VALUE);
		setStencilWriteMask(Integer.MAX_VALUE);
		setReferenceStencil(0);
	}

	private DepthStencilState(String name, boolean depthBufferEnable, boolean depthBufferWriteEnable)
	{
		this();
		this.name = name;
		_depthBufferEnable = depthBufferEnable;
		_depthBufferWriteEnable = depthBufferWriteEnable;
		_defaultStateObject = true;
	}

	private DepthStencilState(DepthStencilState cloneSource)
	{
		this.name = cloneSource.name;
		_depthBufferEnable = cloneSource._depthBufferEnable;
		_depthBufferWriteEnable = cloneSource._depthBufferWriteEnable;
		_counterClockwiseStencilDepthBufferFail = cloneSource._counterClockwiseStencilDepthBufferFail;
		_counterClockwiseStencilFail = cloneSource._counterClockwiseStencilFail;
		_counterClockwiseStencilFunction = cloneSource._counterClockwiseStencilFunction;
		_counterClockwiseStencilPass = cloneSource._counterClockwiseStencilPass;
		_depthBufferFunction = cloneSource._depthBufferFunction;
		_referenceStencil = cloneSource._referenceStencil;
		_stencilDepthBufferFail = cloneSource._stencilDepthBufferFail;
		_stencilEnable = cloneSource._stencilEnable;
		_stencilFail = cloneSource._stencilFail;
		_stencilFunction = cloneSource._stencilFunction;
		_stencilMask = cloneSource._stencilMask;
		_stencilPass = cloneSource._stencilPass;
		_stencilWriteMask = cloneSource._stencilWriteMask;
		_twoSidedStencilMode = cloneSource._twoSidedStencilMode;
	}

	public static DepthStencilState Default;
	public static DepthStencilState DepthRead;
	public static DepthStencilState None;

	static
	{
		Default = new DepthStencilState("DepthStencilState.Default", true, true);
		DepthRead = new DepthStencilState("DepthStencilState.DepthRead", true, false);
		None = new DepthStencilState("DepthStencilState.None", false, false);
	}

	public DepthStencilState clone()
	{
		return new DepthStencilState(this);
	}

}
