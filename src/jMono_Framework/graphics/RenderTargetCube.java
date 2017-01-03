package jMono_Framework.graphics;

import jMono_Framework.graphics.states.DepthFormat;

/**
 * Represents a texture cube that can be used as a render target.
 * 
 * @author Eric Perron
 *
 */
public class RenderTargetCube extends TextureCube implements IRenderTarget
{
// #if DIRECTX
	// private RenderTargetView[] _renderTargetViews;
	// private DepthStencilView _depthStencilView;
// #endif

	/**
	 * The format of the depth-stencil buffer.
	 */
	private DepthFormat depthStencilFormat;

	/**
	 * Returns the depth-stencil buffer format of this render target.
	 * 
	 * @return The format of the depth-stencil buffer.
	 */
	public DepthFormat getDepthStencilFormat() { return depthStencilFormat; }

	/**
	 * The number of multisample locations.
	 */
	private int multiSampleCount;

	/**
	 * Returns the number of multisample locations.
	 * 
	 * @return The number of multisample locations.
	 */
	public int getMultiSampleCount() { return multiSampleCount; }

	/**
	 * The usage mode of the render target.
	 */
	private RenderTargetUsage renderTargetUsage;

	/**
	 * Returns the usage mode of this render target.
	 * 
	 * @return The usage mode of this render target.
	 */
	public RenderTargetUsage getRenderTargetUsage() { return renderTargetUsage; }

	/**
	 * {@inheritDoc}
	 */
	public int getWidth() { return size; }

	/**
	 * {@inheritDoc}
	 */
	public int getHeight() { return size; }

	/**
	 * Initializes a new instance of the {@code RenderTargetCube} class.
	 * 
	 * @param graphicsDevice
	 *        The graphics device.
	 * @param size
	 *        The width and height of a texture cube face in pixels.
	 * @param mipMap
	 *        {@code true} to generate a full mipmap chain, {@code false} otherwise.
	 * @param preferredFormat
	 *        The preferred format of the surface.
	 * @param preferredDepthFormat
	 *        The preferred format of the depth-stencil buffer.
	 */
	public RenderTargetCube(GraphicsDevice graphicsDevice, int size, boolean mipMap, SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat)
	{
		this(graphicsDevice, size, mipMap, preferredFormat, preferredDepthFormat, 0, RenderTargetUsage.DiscardContents);
	}

	/**
	 * Initializes a new instance of the {@code RenderTargetCube} class.
	 * 
	 * @param graphicsDevice
	 *        The graphics device.
	 * @param size
	 *        The width and height of a texture cube face in pixels.
	 * @param mipMap
	 *        {@code true} to generate a full mipmap chain, {@code false} otherwise.
	 * @param preferredFormat
	 *        The preferred format of the surface.
	 * @param preferredDepthFormat
	 *        The preferred format of the depth-stencil buffer.
	 * @param preferredMultiSampleCount
	 *        The preferred number of multisample locations.
	 * @param usage
	 *        The usage mode of the render target.
	 */
	public RenderTargetCube(GraphicsDevice graphicsDevice, int size, boolean mipMap, SurfaceFormat preferredFormat,
			DepthFormat preferredDepthFormat, int preferredMultiSampleCount, RenderTargetUsage usage)
	{
		super(graphicsDevice, size, mipMap, preferredFormat, true);
		depthStencilFormat = preferredDepthFormat;
		multiSampleCount = preferredMultiSampleCount;
		renderTargetUsage = usage;

// #if DIRECTX
		// Create one render target view per cube map face.
		/*
		 * _renderTargetViews = new RenderTargetView[6];
		 * for (int i = 0; i < _renderTargetViews.Length; i++)
		 * {
		 * var renderTargetViewDescription = new RenderTargetViewDescription
		 * {
		 * Dimension = RenderTargetViewDimension.Texture2DArray,
		 * Format = SharpDXHelper.ToFormat(preferredFormat),
		 * Texture2DArray =
		 * {
		 * ArraySize = 1,
		 * FirstArraySlice = i,
		 * MipSlice = 0
		 * }
		 * };
		 * 
		 * _renderTargetViews[i] = new RenderTargetView(graphicsDevice._d3dDevice, GetTexture(),
		 * renderTargetViewDescription);
		 * }
		 * 
		 * // If we don't need a depth buffer then we're done.
		 * if (preferredDepthFormat == DepthFormat.None)
		 * return;
		 * 
		 * var sampleDescription = new SampleDescription(1, 0);
		 * if (preferredMultiSampleCount > 1)
		 * {
		 * sampleDescription.Count = preferredMultiSampleCount;
		 * sampleDescription.Quality =
		 * (int)StandardMultisampleQualityLevels.StandardMultisamplePattern;
		 * }
		 * 
		 * var depthStencilDescription = new Texture2DDescription
		 * {
		 * Format = SharpDXHelper.ToFormat(preferredDepthFormat),
		 * ArraySize = 1,
		 * MipLevels = 1,
		 * Width = size,
		 * Height = size,
		 * SampleDescription = sampleDescription,
		 * BindFlags = BindFlags.DepthStencil,
		 * };
		 * 
		 * using (var depthBuffer = new SharpDX.Direct3D11.Texture2D(graphicsDevice._d3dDevice,
		 * depthStencilDescription))
		 * {
		 * var depthStencilViewDescription = new DepthStencilViewDescription
		 * {
		 * Dimension = DepthStencilViewDimension.Texture2D,
		 * Format = SharpDXHelper.ToFormat(preferredDepthFormat),
		 * };
		 * _depthStencilView = new DepthStencilView(graphicsDevice._d3dDevice, depthBuffer,
		 * depthStencilViewDescription);
		 * }
		 */
// #else
		throw new UnsupportedOperationException();
// #endif
	}

	@Override
	protected void dispose(boolean disposing)
	{
		if (disposing)
		{
// #if DIRECTX
			// if (_renderTargetViews != null)
			// {
			// for (var i = 0; i < _renderTargetViews.Length; i++)
			// _renderTargetViews[i].Dispose();
			//
			// _renderTargetViews = null;
			// SharpDX.Utilities.Dispose(ref _depthStencilView);
			// }
// #endif
		}

		super.dispose(disposing);
	}

// #if DIRECTX
	// / <inheritdoc/>
	/*
	 * [CLSCompliant(false)]
	 * public RenderTargetView GetRenderTargetView(int arraySlice)
	 * {
	 * return _renderTargetViews[arraySlice];
	 * }
	 * 
	 * /// <inheritdoc/>
	 * [CLSCompliant(false)]
	 * public DepthStencilView GetDepthStencilView()
	 * {
	 * return _depthStencilView;
	 * }
	 */
	// #endif

}
