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

        if (_samplers[index].equals(value))
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

        // TODO(Eric): Platform specific code, see other SamplerStateCollection files if needed.
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

        // TODO(Eric): Platform specific code, see other SamplerStateCollection files if needed.
        // platformClear();
    }

    /**
     * Mark all the sampler slots as dirty.
     */
    protected void dirty()
    {
        // TODO(Eric): Platform specific code, see other SamplerStateCollection files if needed.
        // platformDirty();
    }

}
