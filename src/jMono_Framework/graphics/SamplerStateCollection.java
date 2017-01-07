package jMono_Framework.graphics;

import jMono_Framework.graphics.states.SamplerState;

public final class SamplerStateCollection
{
	private final GraphicsDevice _graphicsDevice;

	private final SamplerState _samplerStateAnisotropicClamp;
	private final SamplerState _samplerStateAnisotropicWrap;
	private final SamplerState _samplerStateLinearClamp;
	private final SamplerState _samplerStateLinearWrap;
	private final SamplerState _samplerStatePointClamp;
	private final SamplerState _samplerStatePointWrap;

	private final SamplerState[] _samplers;
	private final SamplerState[] _actualSamplers;
	private final boolean _applyToVertexStage;

	protected SamplerStateCollection(GraphicsDevice device, int maxSamplers, boolean applyToVertexStage)
	{
		_graphicsDevice = device;

		_samplerStateAnisotropicClamp = SamplerState.AnisotropicClamp.clone();
		_samplerStateAnisotropicWrap = SamplerState.AnisotropicWrap.clone();
		_samplerStateLinearClamp = SamplerState.LinearClamp.clone();
		_samplerStateLinearWrap = SamplerState.LinearWrap.clone();
		_samplerStatePointClamp = SamplerState.PointClamp.clone();
		_samplerStatePointWrap = SamplerState.PointWrap.clone();

		_samplers = new SamplerState[maxSamplers];
		_actualSamplers = new SamplerState[maxSamplers];
		_applyToVertexStage = applyToVertexStage;

		clear();
	}

	public SamplerState getSamplerStateCollection(int index)
	{
		return _samplers[index];
	}

	public void setSamplerStateCollection(int index, SamplerState value)
	{
		if (value == null)
			throw new NullPointerException("value");

		if (_samplers[index] == value)
			return;

		_samplers[index] = value;

		// Static state properties never actually get bound;
		// instead we use our GraphicsDevice-specific version of them.
		SamplerState newSamplerState = value;
		if (value == SamplerState.AnisotropicClamp)
			newSamplerState = _samplerStateAnisotropicClamp;
		else if (value == SamplerState.AnisotropicWrap)
			newSamplerState = _samplerStateAnisotropicWrap;
		else if (value == SamplerState.LinearClamp)
			newSamplerState = _samplerStateLinearClamp;
		else if (value == SamplerState.LinearWrap)
			newSamplerState = _samplerStateLinearWrap;
		else if (value == SamplerState.PointClamp)
			newSamplerState = _samplerStatePointClamp;
		else if (value == SamplerState.PointWrap)
			newSamplerState = _samplerStatePointWrap;

		newSamplerState.bindToGraphicsDevice(_graphicsDevice);

		_actualSamplers[index] = newSamplerState;

		platformSetSamplerState(index);
	}

	protected void clear()
	{
		for (int i = 0; i < _samplers.length; ++i)
		{
			_samplers[i] = SamplerState.LinearWrap;

			_samplerStateLinearWrap.bindToGraphicsDevice(_graphicsDevice);
			_actualSamplers[i] = _samplerStateLinearWrap;
		}

		platformClear();
	}

	/**
	 * Mark all the sampler slots as dirty.
	 */
	protected void dirty()
	{
		platformDirty();
	}

	// ########################################################################
	// #                        Platform specific code                        #
	// ########################################################################

	private int _d3dDirty;

	private void platformSetSamplerState(int index)
	{
		_d3dDirty |= 1 << index;
	}

	private void platformClear()
	{
		_d3dDirty = Integer.MAX_VALUE;
	}

	private void platformDirty()
	{
		_d3dDirty = Integer.MAX_VALUE;
	}

	protected void platformSetSamplers(GraphicsDevice device)
	{
		if (_applyToVertexStage && !device.getGraphicsCapabilities().supportsVertexTextures())
			return;

		// Skip out if nothing has changed.
		if (_d3dDirty == 0)
			return;

		// NOTE: We make the assumption here that the caller has
		// locked the d3dContext for us to use.
//		SharpDX.Direct3D11.CommonShaderStage shaderStage;
//		if (_applyToVertexStage)
//			shaderStage = device._d3dContext.VertexShader;
//		else
//			shaderStage = device._d3dContext.PixelShader;

		for (int i = 0; i < _actualSamplers.length; ++i)
		{
			int mask = 1 << i;
			if ((_d3dDirty & mask) == 0)
				continue;

//			var sampler = _actualSamplers[i];
//			SharpDX.Direct3D11.SamplerState state = null;
//			if (sampler != null)
//				state = sampler.GetState(device);

//			shaderStage.SetSampler(i, state);

			_d3dDirty &= ~mask;
			if (_d3dDirty == 0)
				break;
		}

		_d3dDirty = 0;
	}
}
