package jMono_Framework.graphics.states;

import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.GraphicsResource;

public class RasterizerState extends GraphicsResource
{
	private boolean _defaultStateObject;

	private CullMode _cullMode;
	private float _depthBias;
	private FillMode _fillMode;
	private boolean _multiSampleAntiAlias;
	private boolean _scissorTestEnable;
	private float _slopeScaleDepthBias;
	private boolean _depthClipEnable;

	public CullMode getCullMode()
	{
		return _cullMode;
	}

	public void setCullMode(CullMode value)
	{
		throwIfBound();
		_cullMode = value;
	}

	public float getDepthBias()
	{
		return _depthBias;
	}

	public void setDepthBias(float value)
	{
		throwIfBound();
		_depthBias = value;
	}

	public FillMode getFillMode()
	{
		return _fillMode;
	}

	public void setFillMode(FillMode value)
	{
		throwIfBound();
		_fillMode = value;
	}

	public boolean getMultiSampleAntiAlias()
	{
		return _multiSampleAntiAlias;
	}

	public void setMultiSampleAntiAlias(boolean value)
	{
		throwIfBound();
		_multiSampleAntiAlias = value;
	}

	public boolean getScissorTestEnable()
	{
		return _scissorTestEnable;
	}

	public void setScissorTestEnable(boolean value)
	{
		throwIfBound();
		_scissorTestEnable = value;
	}

	public float getSlopeScaleDepthBias()
	{
		return _slopeScaleDepthBias;
	}

	public void setSlopeScaleDepthBias(float value)
	{
		throwIfBound();
		_slopeScaleDepthBias = value;
	}

	public boolean getDepthClipEnable()
	{
		return _depthClipEnable;
	}

	public void setDepthClipEnable(boolean value)
	{
		throwIfBound();
		_depthClipEnable = value;
	}

	public void bindToGraphicsDevice(GraphicsDevice device)
	{
		if (_defaultStateObject)
			throw new IllegalStateException("You cannot bind a default state object.");
		if (getGraphicsDevice() != null && getGraphicsDevice() != device)
			throw new IllegalStateException("This rasterizer state is already bound to a different graphics device.");
		setGraphicsDevice(device);
		;
	}

	protected void throwIfBound()
	{
		if (_defaultStateObject)
			throw new IllegalStateException("You cannot modify a default rasterizer state object.");
		if (getGraphicsDevice() != null)
			throw new IllegalStateException(
					"You cannot modify the rasterizer state after it has been bound to the graphics device!");
	}

	public static RasterizerState CullClockwise;
	public static RasterizerState CullCounterClockwise;
	public static RasterizerState CullNone;

	public RasterizerState()
	{
		setCullMode(CullMode.CullCounterClockwiseFace);
		setFillMode(FillMode.Solid);
		setDepthBias(0);
		setMultiSampleAntiAlias(true);
		setScissorTestEnable(false);
		setSlopeScaleDepthBias(0);
		setDepthClipEnable(true);
	}

	private RasterizerState(String name, CullMode cullMode)
	{
		this();
		this.name = name;
		_cullMode = cullMode;
		_defaultStateObject = true;
	}

	private RasterizerState(RasterizerState cloneSource)
	{
		this.name = cloneSource.name;
		_cullMode = cloneSource._cullMode;
		_fillMode = cloneSource._fillMode;
		_depthBias = cloneSource._depthBias;
		_multiSampleAntiAlias = cloneSource._multiSampleAntiAlias;
		_scissorTestEnable = cloneSource._scissorTestEnable;
		_slopeScaleDepthBias = cloneSource._slopeScaleDepthBias;
		_depthClipEnable = cloneSource._depthClipEnable;
	}

	static
	{
		CullClockwise = new RasterizerState("RasterizerState.CullClockwise", CullMode.CullClockwiseFace);
		CullCounterClockwise = new RasterizerState("RasterizerState.CullCounterClockwise",
				CullMode.CullCounterClockwiseFace);
		CullNone = new RasterizerState("RasterizerState.CullNone", CullMode.None);
	}

	public RasterizerState clone()
	{
		return new RasterizerState(this);
	}

}
