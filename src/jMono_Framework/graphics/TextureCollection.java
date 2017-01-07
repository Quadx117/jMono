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

	// ########################################################################
	// #                        Platform specific code                        #
	// ########################################################################

	void platformInit() { }

	void platformClear() { }

	void platformSetTextures(GraphicsDevice device)
	{
		// Skip out if nothing has changed.
		if (_dirty == 0)
			return;

		// NOTE: We make the assumption here that the caller has
		// locked the d3dContext for us to use.
//		SharpDX.Direct3D11.CommonShaderStage shaderStage;
//		if (_applyToVertexStage)
//			shaderStage = device._d3dContext.VertexShader;
//		else
//			shaderStage = device._d3dContext.PixelShader;

		for (int i = 0; i < _textures.length; i++)
		{
			int mask = 1 << i;
			if ((_dirty & mask) == 0)
				continue;

//			Texture tex = _textures[i];

//			if (_textures[i] == null || _textures[i].isDisposed())
//				shaderStage.SetShaderResource(i, null);
//			else
//				shaderStage.SetShaderResource(i, _textures[i].GetShaderResourceView());

			_dirty &= ~mask;
			if (_dirty == 0)
				break;
		}

		_dirty = 0;
	}
}
