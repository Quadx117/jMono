package jMono_Framework.graphics.states;

import jMono_Framework.graphics.ColorWriteChannels;

public class TargetBlendState
{
	private BlendState _parent;
	private BlendFunction _alphaBlendFunction;
	private Blend _alphaDestinationBlend;
	private Blend _alphaSourceBlend;
	private BlendFunction _colorBlendFunction;
	private Blend _colorDestinationBlend;
	private Blend _colorSourceBlend;
	private ColorWriteChannels _colorWriteChannels;

	protected TargetBlendState(BlendState parent)
	{
		_parent = parent;
		setAlphaBlendFunction(BlendFunction.Add);
		setAlphaDestinationBlend(Blend.Zero);
		setAlphaSourceBlend(Blend.One);
		setColorBlendFunction(BlendFunction.Add);
		setColorDestinationBlend(Blend.Zero);
		setColorSourceBlend(Blend.One);
		setColorWriteChannels(ColorWriteChannels.All);
	}

	protected TargetBlendState clone(BlendState parent)
	{
		TargetBlendState clone = new TargetBlendState(parent);

		clone.setAlphaBlendFunction(getAlphaBlendFunction());
		clone.setAlphaDestinationBlend(getAlphaDestinationBlend());
		clone.setAlphaSourceBlend(getAlphaSourceBlend());
		clone.setColorBlendFunction(getColorBlendFunction());
		clone.setColorDestinationBlend(getColorDestinationBlend());
		clone.setColorSourceBlend(getColorSourceBlend());
		clone.setColorWriteChannels(getColorWriteChannels());

		return clone;
	}

	public BlendFunction getAlphaBlendFunction()
	{
		return _alphaBlendFunction;
	}

	public void setAlphaBlendFunction(BlendFunction value)
	{
		_parent.throwIfBound();
		_alphaBlendFunction = value;
	}

	public Blend getAlphaDestinationBlend()
	{
		return _alphaDestinationBlend;
	}

	public void setAlphaDestinationBlend(Blend value)
	{
		_parent.throwIfBound();
		_alphaDestinationBlend = value;
	}

	public Blend getAlphaSourceBlend()
	{
		return _alphaSourceBlend;
	}

	public void setAlphaSourceBlend(Blend value)
	{
		_parent.throwIfBound();
		_alphaSourceBlend = value;
	}

	public BlendFunction getColorBlendFunction()
	{
		return _colorBlendFunction;
	}

	public void setColorBlendFunction(BlendFunction value)
	{
		_parent.throwIfBound();
		_colorBlendFunction = value;
	}

	public Blend getColorDestinationBlend()
	{
		return _colorDestinationBlend;
	}

	public void setColorDestinationBlend(Blend value)
	{
		_parent.throwIfBound();
		_colorDestinationBlend = value;
	}

	public Blend getColorSourceBlend()
	{
		return _colorSourceBlend;
	}

	public void setColorSourceBlend(Blend value)
	{
		_parent.throwIfBound();
		_colorSourceBlend = value;
	}

	public ColorWriteChannels getColorWriteChannels()
	{
		return _colorWriteChannels;
	}

	public void setColorWriteChannels(ColorWriteChannels value)
	{
		_parent.throwIfBound();
		_colorWriteChannels = value;
	}

	// #if DIRECTX
	/*
	 * protected void GetState(ref SharpDX.Direct3D11.RenderTargetBlendDescription desc)
	 * {
	 * // We're blending if we're not in the opaque state.
	 * desc.IsBlendEnabled = !( ColorSourceBlend == Blend.One &&
	 * ColorDestinationBlend == Blend.Zero &&
	 * AlphaSourceBlend == Blend.One &&
	 * AlphaDestinationBlend == Blend.Zero);
	 * 
	 * desc.BlendOperation = GetBlendOperation(ColorBlendFunction);
	 * desc.SourceBlend = GetBlendOption(ColorSourceBlend, false);
	 * desc.DestinationBlend = GetBlendOption(ColorDestinationBlend, false);
	 * 
	 * desc.AlphaBlendOperation = GetBlendOperation(AlphaBlendFunction);
	 * desc.SourceAlphaBlend = GetBlendOption(AlphaSourceBlend, true);
	 * desc.DestinationAlphaBlend = GetBlendOption(AlphaDestinationBlend, true);
	 * 
	 * desc.RenderTargetWriteMask = GetColorWriteMask(ColorWriteChannels);
	 * }
	 * 
	 * static private SharpDX.Direct3D11.BlendOperation GetBlendOperation(BlendFunction blend)
	 * {
	 * switch (blend)
	 * {
	 * case BlendFunction.Add:
	 * return SharpDX.Direct3D11.BlendOperation.Add;
	 * 
	 * case BlendFunction.Max:
	 * return SharpDX.Direct3D11.BlendOperation.Maximum;
	 * 
	 * case BlendFunction.Min:
	 * return SharpDX.Direct3D11.BlendOperation.Minimum;
	 * 
	 * case BlendFunction.ReverseSubtract:
	 * return SharpDX.Direct3D11.BlendOperation.ReverseSubtract;
	 * 
	 * case BlendFunction.Subtract:
	 * return SharpDX.Direct3D11.BlendOperation.Subtract;
	 * 
	 * default:
	 * throw new ArgumentException("Invalid blend function!");
	 * }
	 * }
	 * 
	 * static private SharpDX.Direct3D11.BlendOption GetBlendOption(Blend blend, bool alpha)
	 * {
	 * switch (blend)
	 * {
	 * case Blend.BlendFactor:
	 * return SharpDX.Direct3D11.BlendOption.BlendFactor;
	 * 
	 * case Blend.DestinationAlpha:
	 * return SharpDX.Direct3D11.BlendOption.DestinationAlpha;
	 * 
	 * case Blend.DestinationColor:
	 * return alpha ? SharpDX.Direct3D11.BlendOption.DestinationAlpha :
	 * SharpDX.Direct3D11.BlendOption.DestinationColor;
	 * 
	 * case Blend.InverseBlendFactor:
	 * return SharpDX.Direct3D11.BlendOption.InverseBlendFactor;
	 * 
	 * case Blend.InverseDestinationAlpha:
	 * return SharpDX.Direct3D11.BlendOption.InverseDestinationAlpha;
	 * 
	 * case Blend.InverseDestinationColor:
	 * return alpha ? SharpDX.Direct3D11.BlendOption.InverseDestinationAlpha :
	 * SharpDX.Direct3D11.BlendOption.InverseDestinationColor;
	 * 
	 * case Blend.InverseSourceAlpha:
	 * return SharpDX.Direct3D11.BlendOption.InverseSourceAlpha;
	 * 
	 * case Blend.InverseSourceColor:
	 * return alpha ? SharpDX.Direct3D11.BlendOption.InverseSourceAlpha :
	 * SharpDX.Direct3D11.BlendOption.InverseSourceColor;
	 * 
	 * case Blend.One:
	 * return SharpDX.Direct3D11.BlendOption.One;
	 * 
	 * case Blend.SourceAlpha:
	 * return SharpDX.Direct3D11.BlendOption.SourceAlpha;
	 * 
	 * case Blend.SourceAlphaSaturation:
	 * return SharpDX.Direct3D11.BlendOption.SourceAlphaSaturate;
	 * 
	 * case Blend.SourceColor:
	 * return alpha ? SharpDX.Direct3D11.BlendOption.SourceAlpha :
	 * SharpDX.Direct3D11.BlendOption.SourceColor;
	 * 
	 * case Blend.Zero:
	 * return SharpDX.Direct3D11.BlendOption.Zero;
	 * 
	 * default:
	 * throw new ArgumentException("Invalid blend!");
	 * }
	 * }
	 * 
	 * static private SharpDX.Direct3D11.ColorWriteMaskFlags GetColorWriteMask(ColorWriteChannels
	 * mask)
	 * {
	 * return ((mask & ColorWriteChannels.Red) != 0 ? SharpDX.Direct3D11.ColorWriteMaskFlags.Red :
	 * 0) |
	 * ((mask & ColorWriteChannels.Green) != 0 ? SharpDX.Direct3D11.ColorWriteMaskFlags.Green : 0) |
	 * ((mask & ColorWriteChannels.Blue) != 0 ? SharpDX.Direct3D11.ColorWriteMaskFlags.Blue : 0) |
	 * ((mask & ColorWriteChannels.Alpha) != 0 ? SharpDX.Direct3D11.ColorWriteMaskFlags.Alpha : 0);
	 * }
	 */
	// #endif

}
