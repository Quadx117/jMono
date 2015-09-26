package gameCore.graphics;

public class TextureCollection
{
	private GraphicsDevice _graphicsDevice;
	private Texture[] _textures;
	private boolean _applyToVertexStage;
	private int _dirty;

	protected TextureCollection(GraphicsDevice graphicsDevice, int maxTextures, boolean applyToVertexStage)
	{
		_graphicsDevice = graphicsDevice;
		_textures = new Texture[maxTextures];
		_applyToVertexStage = applyToVertexStage;
		_dirty = Integer.MAX_VALUE;
		// TODO: See other TextureCollection files
		// PlatformInit();
	}

	// TODO: Not sure about the names of these two
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

		// TODO: See other TextureCollection files
		// PlatformClear();
		_dirty = Integer.MAX_VALUE;
	}

	// / <summary>
	// / Marks all texture slots as dirty.
	// / </summary>
	protected void dirty()
	{
		_dirty = Integer.MAX_VALUE;
	}

	protected void setTextures(GraphicsDevice device)
	{
		if (_applyToVertexStage && !device.getGraphicsCapabilities().supportsVertexTextures)
			return;
		// PlatformSetTextures(device);
	}
}
