package jMono_Framework.graphics;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a texture resource.
 * 
 * @author Eric Perron (inspired by XNA Framework from Microsoft and MonoGame)
 *
 */
public abstract class Texture extends GraphicsResource
{
	protected SurfaceFormat _format;
	protected int _levelCount;

	private final int _sortingKey = _lastSortingKey.incrementAndGet();
	private static AtomicInteger _lastSortingKey = new AtomicInteger();

	/**
	 * Returns a unique identifier of this texture for sorting purposes.
	 * 
	 * <p>
	 * For example, this value is used by {@link SpriteBatch} when drawing with {@link SpriteSortMode#Texture}.
	 * 
	 * <p>
	 * The value is an implementation detail and may change between application launches or MonoGame versions.
	 * It is only guaranteed to stay consistent during application lifetime.
	 * 
	 * @return A unique identifier of this texture for sorting purposes.
	 */
	protected int getSortingKey() { return _sortingKey; }

	public SurfaceFormat getFormat() { return _format; }

	public int getLevelCount() { return _levelCount; }

	protected static int calculateMipLevels(int width)
	{
		return calculateMipLevels(width, 0, 0);
	}

	protected static int calculateMipLevels(int width, int height)
	{
		return calculateMipLevels(width, height, 0);
	}

	// TODO: Find a way to create overloads if I ever need this one
	// protected static int calculateMipLevels(int width, int depth) {
	// return calculateMipLevels(width, 0, depth);
	// }

	protected static int calculateMipLevels(int width, int height, int depth)
	{
		int levels = 1;
		int size = Math.max(Math.max(width, height), depth);
		while (size > 1)
		{
			size = size / 2;
			++levels;
		}
		return levels;
	}

	protected int getPitch(int width)
	{
		assert (width > 0) : "The width is negative!";

		int pitch;

		switch (_format)
		{
			case Dxt1:
			case Dxt1SRgb:
			case Dxt1a:
			case RgbPvrtc2Bpp:
			case RgbaPvrtc2Bpp:
			case RgbEtc1:
			case Dxt3:
			case Dxt3SRgb:
			case Dxt5:
			case Dxt5SRgb:
			case RgbPvrtc4Bpp:
			case RgbaPvrtc4Bpp:
				pitch = ((width + 3) / 4) * _format.getSize();
				break;

			default:
				pitch = width * _format.getSize();
				break;
		}

		return pitch;
	}

	@Override
	protected void graphicsDeviceResetting()
	{
		platformGraphicsDeviceResetting();
	}

	// ########################################################################
	// #                        Platform specific code                        #
	// ########################################################################

	// TODO: I added this array to hold the image data
	// TODO: This is handled by ShaprDX and I don't know how it works yet.
	// TODO: I should maybe make this private and implement getData in Texture2D
	protected int[] _texture;
//	internal SharpDX.Direct3D11.Resource _texture;

//	private SharpDX.Direct3D11.ShaderResourceView _resourceView;

//	internal abstract SharpDX.Direct3D11.Resource CreateTexture();
	protected abstract int[] createTexture();

//	internal SharpDX.Direct3D11.Resource GetTexture()
//	{
//		if (_texture == null)
//			_texture = CreateTexture();

//		return _texture;
//	}

	protected int[] getTexture()
	{
		if (_texture == null)
			_texture = createTexture();

		return _texture;
	}

	private void platformGraphicsDeviceResetting()
	{
//		SharpDX.Utilities.Dispose(ref _resourceView);
//		SharpDX.Utilities.Dispose(ref _texture);
	}

	@Override
	protected void dispose(boolean disposing)
	{
		if (disposing)
		{
//			SharpDX.Utilities.Dispose(ref _resourceView);
//			SharpDX.Utilities.Dispose(ref _texture);
		}

		super.dispose(disposing);
	}
}
