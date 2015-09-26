package gameCore.graphics.states;

import gameCore.Color;
import gameCore.graphics.GraphicsDevice;
import gameCore.graphics.GraphicsResource;

public class SamplerState extends GraphicsResource
{

	static
	{
		AnisotropicClamp = new SamplerState("SamplerState.AnisotropicClamp", TextureFilter.Anisotropic,
				TextureAddressMode.Clamp);
		AnisotropicWrap = new SamplerState("SamplerState.AnisotropicWrap", TextureFilter.Anisotropic,
				TextureAddressMode.Wrap);
		LinearClamp = new SamplerState("SamplerState.LinearClamp", TextureFilter.Linear, TextureAddressMode.Clamp);
		LinearWrap = new SamplerState("SamplerState.LinearWrap", TextureFilter.Linear, TextureAddressMode.Wrap);
		PointClamp = new SamplerState("SamplerState.PointClamp", TextureFilter.Point, TextureAddressMode.Clamp);
		PointWrap = new SamplerState("SamplerState.PointWrap", TextureFilter.Point, TextureAddressMode.Wrap);
	}

	public static SamplerState AnisotropicClamp;
	public static SamplerState AnisotropicWrap;
	public static SamplerState LinearClamp;
	public static SamplerState LinearWrap;
	public static SamplerState PointClamp;
	public static SamplerState PointWrap;

	private boolean _defaultStateObject;

	private TextureAddressMode _addressU;
	private TextureAddressMode _addressV;
	private TextureAddressMode _addressW;
	private Color _borderColor;
	private TextureFilter _filter;
	private int _maxAnisotropy;
	private int _maxMipLevel;
	private float _mipMapLevelOfDetailBias;
	private CompareFunction _comparisonFunction;

	public TextureAddressMode getAddressU()
	{
		return _addressU;
	}

	public void setAddressU(TextureAddressMode value)
	{
		throwIfBound();
		_addressU = value;
	}

	public TextureAddressMode getAddressV()
	{
		return _addressV;
	}

	public void setAddressV(TextureAddressMode value)
	{
		throwIfBound();
		_addressV = value;
	}

	public TextureAddressMode getAddressW()
	{
		return _addressW;
	}

	public void setAddressW(TextureAddressMode value)
	{
		throwIfBound();
		_addressW = value;
	}

	public Color getBorderColor()
	{
		return _borderColor;
	}

	public void setBorderColor(Color value)
	{
		throwIfBound();
		_borderColor = value;
	}

	public TextureFilter getFilter()
	{
		return _filter;
	}

	public void setFilter(TextureFilter value)
	{
		throwIfBound();
		_filter = value;
	}

	public int getMaxAnisotropy()
	{
		return _maxAnisotropy;
	}

	public void setMaxAnisotropy(int value)
	{
		throwIfBound();
		_maxAnisotropy = value;
	}

	public int getMaxMipLevel()
	{
		return _maxMipLevel;
	}

	public void setMaxMipLevel(int value)
	{
		throwIfBound();
		_maxMipLevel = value;
	}

	public float getMipMapLevelOfDetailBias()
	{
		return _mipMapLevelOfDetailBias;
	}

	public void setMipMapLevelOfDetailBias(float value)
	{
		throwIfBound();
		_mipMapLevelOfDetailBias = value;
	}

	public CompareFunction getComparisonFunction()
	{
		return _comparisonFunction;
	}

	public void setComparisonFunction(CompareFunction value)
	{
		throwIfBound();
		_comparisonFunction = value;
	}

	public void bindToGraphicsDevice(GraphicsDevice device)
	{
		if (_defaultStateObject)
			throw new IllegalStateException("You cannot bind a default state object.");
		if (getGraphicsDevice() != null && getGraphicsDevice() != device)
			throw new IllegalStateException("This sampler state is already bound to a different graphics device.");
		setGraphicsDevice(device);
	}

	protected void throwIfBound()
	{
		if (_defaultStateObject)
			throw new IllegalStateException("You cannot modify a default sampler state object.");
		if (getGraphicsDevice() != null)
			throw new IllegalStateException(
					"You cannot modify the sampler state after it has been bound to the graphics device!");
	}

	public SamplerState()
	{
		setFilter(TextureFilter.Linear);
		setAddressU(TextureAddressMode.Wrap);
		setAddressV(TextureAddressMode.Wrap);
		setAddressW(TextureAddressMode.Wrap);
		setBorderColor(Color.White);
		setMaxAnisotropy(4);
		setMaxMipLevel(0);
		setMipMapLevelOfDetailBias(0.0f);
		setComparisonFunction(CompareFunction.Never);
	}

	private SamplerState(String name, TextureFilter filter, TextureAddressMode addressMode)
	{
		this();
		this.name = name;
		_filter = filter;
		_addressU = addressMode;
		_addressV = addressMode;
		_addressW = addressMode;
		_defaultStateObject = true;
	}

	private SamplerState(SamplerState cloneSource)
	{
		this.name = cloneSource.name;
		_filter = cloneSource._filter;
		_addressU = cloneSource._addressU;
		_addressV = cloneSource._addressV;
		_addressW = cloneSource._addressW;
		_borderColor = cloneSource._borderColor;
		_maxAnisotropy = cloneSource._maxAnisotropy;
		_maxMipLevel = cloneSource._maxMipLevel;
		_mipMapLevelOfDetailBias = cloneSource._mipMapLevelOfDetailBias;
		_comparisonFunction = cloneSource._comparisonFunction;
	}

	public SamplerState clone()
	{
		return new SamplerState(this);
	}

}
