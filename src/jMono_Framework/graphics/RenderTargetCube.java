package jMono_Framework.graphics;

import jMono_Framework.graphics.states.DepthFormat;

/**
 * Represents a texture cube that can be used as a render target.
 * 
 * @author Eric
 *
 */
public class RenderTargetCube extends TextureCube implements IRenderTarget
{
// #if DIRECTX
	// vprivate RenderTargetView[] _renderTargetViews;
	// private DepthStencilView _depthStencilView;
// #endif

	private DepthFormat depthStencilFormat;
	// / <summary>
	// / Gets the depth-stencil buffer format of this render target.
	// / </summary>
	// / <value>The format of the depth-stencil buffer.</value>
	public DepthFormat getDepthStencilFormat() { return depthStencilFormat; }

	private int multiSampleCount;
	// / <summary>
	// / Gets the number of multisample locations.
	// / </summary>
	// / <value>The number of multisample locations.</value>
	public int getMultiSampleCount() { return multiSampleCount; }
	
	private RenderTargetUsage renderTargetUsage;
	// / <summary>
	// / Gets the usage mode of this render target.
	// / </summary>
	// / <value>The usage mode of the render target.</value>
	public RenderTargetUsage getRenderTargetUsage() { return renderTargetUsage; }

	/**
	 * {@inheritDoc}
	 */
	public int getWidth()
	{
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHeight()
	{
		return size;
	}

	// / <summary>
	// / Initializes a new instance of the <see cref="RenderTargetCube"/> class.
	// / </summary>
	// / <param name="graphicsDevice">The graphics device.</param>
	// / <param name="size">The width and height of a texture cube face in pixels.</param>
	// / <param name="mipMap"><see langword="true"/> to generate a full mipmap chain; otherwise <see
	// langword="false"/>.</param>
	// / <param name="preferredFormat">The preferred format of the surface.</param>
	// / <param name="preferredDepthFormat">The preferred format of the depth-stencil
	// buffer.</param>
	public RenderTargetCube(GraphicsDevice graphicsDevice, int size, boolean mipMap, SurfaceFormat preferredFormat,
			DepthFormat preferredDepthFormat)
	{
		this(graphicsDevice, size, mipMap, preferredFormat, preferredDepthFormat, 0, RenderTargetUsage.DiscardContents);
	}

	// / <summary>
	// / Initializes a new instance of the <see cref="RenderTargetCube"/> class.
	// / </summary>
	// / <param name="graphicsDevice">The graphics device.</param>
	// / <param name="size">The width and height of a texture cube face in pixels.</param>
	// / <param name="mipMap"><see langword="true"/> to generate a full mipmap chain; otherwise <see
	// langword="false"/>.</param>
	// / <param name="preferredFormat">The preferred format of the surface.</param>
	// / <param name="preferredDepthFormat">The preferred format of the depth-stencil
	// buffer.</param>
	// / <param name="preferredMultiSampleCount">The preferred number of multisample
	// locations.</param>
	// / <param name="usage">The usage mode of the render target.</param>
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
