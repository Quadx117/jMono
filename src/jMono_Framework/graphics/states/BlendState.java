package jMono_Framework.graphics.states;

import jMono_Framework.Color;
import jMono_Framework.graphics.ColorWriteChannels;
import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.GraphicsResource;

public class BlendState extends GraphicsResource
{
	private TargetBlendState[] _targetBlendState;

	private boolean _defaultStateObject;

	private Color _blendFactor;

	private int _multiSampleMask;

	private boolean _independentBlendEnable;

	public void bindToGraphicsDevice(GraphicsDevice device)
	{
		if (_defaultStateObject)
			throw new IllegalStateException("You cannot bind a default state object.");
		if (getGraphicsDevice() != null && getGraphicsDevice() != device)
			throw new IllegalStateException("This blend state is already bound to a different graphics device.");
		setGraphicsDevice(device);
	}

	protected void throwIfBound()
	{
		if (_defaultStateObject)
			throw new IllegalStateException("You cannot modify a default blend state object.");
		if (getGraphicsDevice() != null)
			throw new IllegalStateException(
					"You cannot modify the blend state after it has been bound to the graphics device!");
	}

	/**
	 * Returns the target specific blend state.
	 * 
	 * @param index
	 *        The 0 to 3 target blend state index.
	 * @return A target blend state.
	 */
	public TargetBlendState getTargetBlendState(int index)
	{
		return _targetBlendState[index];
	}

	public BlendFunction getAlphaBlendFunction()
	{
		return _targetBlendState[0].getAlphaBlendFunction();
	}

	public void setAlphaBlendFunction(BlendFunction value)
	{
		throwIfBound();
		_targetBlendState[0].setAlphaBlendFunction(value);
	}

	public Blend getAlphaDestinationBlend()
	{
		return _targetBlendState[0].getAlphaDestinationBlend();
	}

	public void setAlphaDestinationBlend(Blend value)
	{
		throwIfBound();
		_targetBlendState[0].setAlphaDestinationBlend(value);
	}

	public Blend getAlphaSourceBlend()
	{
		return _targetBlendState[0].getAlphaSourceBlend();
	}

	public void setAlphaSourceBlend(Blend value)
	{
		throwIfBound();
		_targetBlendState[0].setAlphaSourceBlend(value);
	}

	public BlendFunction getColorBlendFunction()
	{
		return _targetBlendState[0].getColorBlendFunction();
	}

	public void setColorBlendFunction(BlendFunction value)
	{
		throwIfBound();
		_targetBlendState[0].setColorBlendFunction(value);
	}

	public Blend getColorDestinationBlend()
	{
		return _targetBlendState[0].getColorDestinationBlend();
	}

	public void setColorDestinationBlend(Blend value)
	{
		throwIfBound();
		_targetBlendState[0].setColorDestinationBlend(value);
	}

	public Blend getColorSourceBlend()
	{
		return _targetBlendState[0].getColorSourceBlend();
	}

	public void setColorSourceBlend(Blend value)
	{
		throwIfBound();
		_targetBlendState[0].setColorSourceBlend(value);
	}

	public ColorWriteChannels getColorWriteChannels()
	{
		return _targetBlendState[0].getColorWriteChannels();
	}

	public void setColorWriteChannels(ColorWriteChannels value)
	{
		throwIfBound();
		_targetBlendState[0].setColorWriteChannels(value);
	}

	public ColorWriteChannels getColorWriteChannels1()
	{
		return _targetBlendState[1].getColorWriteChannels();
	}

	public void setColorWriteChannels1(ColorWriteChannels value)
	{
		throwIfBound();
		_targetBlendState[1].setColorWriteChannels(value);
	}

	public ColorWriteChannels getColorWriteChannels2()
	{
		return _targetBlendState[2].getColorWriteChannels();
	}

	public void setColorWriteChannels2(ColorWriteChannels value)
	{
		throwIfBound();
		_targetBlendState[2].setColorWriteChannels(value);
	}

	public ColorWriteChannels getColorWriteChannels3()
	{
		return _targetBlendState[3].getColorWriteChannels();
	}

	public void setColorWriteChannels3(ColorWriteChannels value)
	{
		throwIfBound();
		_targetBlendState[3].setColorWriteChannels(value);
	}

	public Color getBlendFactor()
	{
		return _blendFactor;
	}

	public void setBlendFactor(Color value)
	{
		throwIfBound();
		_blendFactor = value;
	}

	public int getMultiSampleMask()
	{
		return _multiSampleMask;
	}

	public void setMultiSampleMask(int value)
	{
		throwIfBound();
		_multiSampleMask = value;
	}

	/**
	 * Enables use of the per-target blend states.
	 * 
	 * @return {@code true} if independent blend is enabled, false otherwise
	 */
	public boolean isIndependentBlendEnable()
	{
		return _independentBlendEnable;
	}

	/**
	 * Enables use of the per-target blend states.
	 * 
	 * @param value
	 */
	public void setIndependentBlendEnable(boolean value)
	{
		throwIfBound();
		_independentBlendEnable = value;
	}

	public static final BlendState Additive;
	public static final BlendState AlphaBlend;
	public static final BlendState NonPremultiplied;
	public static final BlendState Opaque;

	public BlendState()
	{
		_targetBlendState = new TargetBlendState[4];
		_targetBlendState[0] = new TargetBlendState(this);
		_targetBlendState[1] = new TargetBlendState(this);
		_targetBlendState[2] = new TargetBlendState(this);
		_targetBlendState[3] = new TargetBlendState(this);

		_blendFactor = Color.White();
		_multiSampleMask = Integer.MAX_VALUE;
		_independentBlendEnable = false;
	}

	private BlendState(String name, Blend sourceBlend, Blend destinationBlend)
	{
		this();
		this.name = name;
		setColorSourceBlend(sourceBlend);
		setAlphaSourceBlend(sourceBlend);
		setColorDestinationBlend(destinationBlend);
		setAlphaDestinationBlend(destinationBlend);
		_defaultStateObject = true;
	}

	private BlendState(BlendState cloneSource)
	{
		this.name = cloneSource.name;

		_targetBlendState = new TargetBlendState[4];
		_targetBlendState[0] = cloneSource.getTargetBlendState(0).clone(this);
		_targetBlendState[1] = cloneSource.getTargetBlendState(1).clone(this);
		_targetBlendState[2] = cloneSource.getTargetBlendState(2).clone(this);
		_targetBlendState[3] = cloneSource.getTargetBlendState(3).clone(this);

		_blendFactor = cloneSource._blendFactor;
		_multiSampleMask = cloneSource._multiSampleMask;
		_independentBlendEnable = cloneSource._independentBlendEnable;
	}

	static
	{
		Additive = new BlendState("BlendState.Additive", Blend.SourceAlpha, Blend.One);
		AlphaBlend = new BlendState("BlendState.AlphaBlend", Blend.One, Blend.InverseSourceAlpha);
		NonPremultiplied = new BlendState("BlendState.NonPremultiplied", Blend.SourceAlpha, Blend.InverseSourceAlpha);
		Opaque = new BlendState("BlendState.Opaque", Blend.One, Blend.Zero);
	}

	public BlendState clone()
	{
		return new BlendState(this);
	}

}
