package jMono_Framework.graphics;

public final class TextureCollection
{
    private final GraphicsDevice _graphicsDevice;
    private final Texture[] _textures;
    private final boolean _applyToVertexStage;
    private int _dirty;

    protected TextureCollection(GraphicsDevice graphicsDevice, int maxTextures, boolean applyToVertexStage)
    {
        _graphicsDevice = graphicsDevice;
        _textures = new Texture[maxTextures];
        _applyToVertexStage = applyToVertexStage;
        _dirty = Integer.MAX_VALUE;
        platformInit();
    }

    public Texture getTexture(int index)
    {
        return _textures[index];
    }

    public void setTexture(int index, Texture value)
    {
        if (_applyToVertexStage && !_graphicsDevice.getGraphicsCapabilities().supportsVertexTextures)
            throw new UnsupportedOperationException("Vertex textures are not supported on this device.");

        if (_textures[index] == value)
            return;

        _textures[index] = value;
        _dirty |= 1 << index;
    }

    protected void clear()
    {
        for (int i = 0; i < _textures.length; ++i)
            _textures[i] = null;

        platformClear();
        _dirty = Integer.MAX_VALUE;
    }

    /**
     * Marks all texture slots as dirty.
     */
    protected void dirty()
    {
        _dirty = Integer.MAX_VALUE;
    }

    protected void setTextures(GraphicsDevice device)
    {
        if (_applyToVertexStage && !device.getGraphicsCapabilities().supportsVertexTextures)
            return;
        platformSetTextures(device);
    }

    // @formatter:off
    // ########################################################################
    // #                        Platform specific code                        #
    // ########################################################################
    // @formatter:on

    // NOTE(Eric): Adapted from TextureCollection.DirectX.cs

    void platformInit()
    {}

    // @formatter:off
//    protected void clearTargets(GraphicsDevice device, RenderTargetBinding[] targets)
//    {
//        if (_applyToVertexStage && !_graphicsDevice.getGraphicsCapabilities().supportsVertexTextures)
//            return;
//
//        if (_applyToVertexStage)
//            clearTargets(targets, device._d3dContext.VertexShader);
//        else
//            clearTargets(targets, device._d3dContext.PixelShader);
//    }

//    private void clearTargets(RenderTargetBinding[] targets, SharpDX.Direct3D11.CommonShaderStage shaderStage)
//    {
//        // NOTE: We make the assumption here that the caller has
//        // locked the d3dContext for us to use.
//
//        // We assume 4 targets to avoid a loop within a loop below.
//        var target0 = targets[0].RenderTarget;
//        var target1 = targets[1].RenderTarget;
//        var target2 = targets[2].RenderTarget;
//        var target3 = targets[3].RenderTarget;
//
//        // Make one pass across all the texture slots.
//        for (var i = 0; i < _textures.Length; i++)
//        {
//            if (_textures[i] == null)
//                continue;
//
//            if (_textures[i] != target0 &&
//                _textures[i] != target1 &&
//                _textures[i] != target2 &&
//                _textures[i] != target3)
//                continue;
//
//            // Immediately clear the texture from the device.
//            _dirty &= ~(1 << i);
//            _textures[i] = null;
//            shaderStage.SetShaderResource(i, null);
//        }
//    }
    // @formatter:on

    void platformClear()
    {}

    void platformSetTextures(GraphicsDevice device)
    {
        // Skip out if nothing has changed.
        if (_dirty == 0)
            return;

        // NOTE: We make the assumption here that the caller has
        // locked the d3dContext for us to use.
        // SharpDX.Direct3D11.CommonShaderStage shaderStage;
        // if (_applyToVertexStage)
        // shaderStage = device._d3dContext.VertexShader;
        // else
        // shaderStage = device._d3dContext.PixelShader;

        for (int i = 0; i < _textures.length; i++)
        {
            int mask = 1 << i;
            if ((_dirty & mask) == 0)
                continue;

            // Texture tex = _textures[i];

            // if (_textures[i] == null || _textures[i].isDisposed())
            // shaderStage.SetShaderResource(i, null);
            // else
            // shaderStage.SetShaderResource(i, _textures[i].GetShaderResourceView());

            _dirty &= ~mask;
            if (_dirty == 0)
                break;
        }

        _dirty = 0;
    }
}
