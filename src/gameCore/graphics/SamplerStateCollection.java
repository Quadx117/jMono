package gameCore.graphics;

import gameCore.graphics.states.SamplerState;

public class SamplerStateCollection
{
	private GraphicsDevice _graphicsDevice;

	private SamplerState _samplerStateAnisotropicClamp;
	private SamplerState _samplerStateAnisotropicWrap;
	private SamplerState _samplerStateLinearClamp;
	private SamplerState _samplerStateLinearWrap;
	private SamplerState _samplerStatePointClamp;
	private SamplerState _samplerStatePointWrap;

	private SamplerState[] _samplers;
	private SamplerState[] _actualSamplers;
	private boolean _applyToVertexStage;

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

		// TODO: See other SamplerStateCollection files.
		// platformSetSamplerState(index);
	}

	protected void clear()
	{
		for (int i = 0; i < _samplers.length; ++i)
		{
			_samplers[i] = SamplerState.LinearWrap;

			_samplerStateLinearWrap.bindToGraphicsDevice(_graphicsDevice);
			_actualSamplers[i] = _samplerStateLinearWrap;
		}

		// TODO: See other SamplerStateCollection files.
		// platformClear();
	}

	/**
	 * Mark all the sampler slots as dirty.
	 */
	protected void dirty()
	{
		// TODO: See other SamplerStateCollection files.
		// platformDirty();
	}

}
