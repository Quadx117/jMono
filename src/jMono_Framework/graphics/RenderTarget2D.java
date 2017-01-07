package jMono_Framework.graphics;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.graphics.states.DepthFormat;

public class RenderTarget2D extends Texture2D implements IRenderTarget
{
	private DepthFormat depthStencilFormat;
	public DepthFormat getDepthStencilFormat() { return depthStencilFormat; }

	private int multiSampleCount;
	public int getMultiSampleCount() { return multiSampleCount; }

	private RenderTargetUsage renderTargetUsage;
	public RenderTargetUsage getRenderTargetUsage() { return renderTargetUsage; }

	public boolean isContentLost() { return false; }

	public Event<EventArgs> contentLost = new Event<EventArgs>();

	private boolean suppressEventHandlerWarningsUntilEventsAreProperlyImplemented()
	{
		return contentLost != null;
	}

	public RenderTarget2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipMap,
			SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat, int preferredMultiSampleCount,
			RenderTargetUsage usage, boolean shared, int arraySize)
	{
		super(graphicsDevice, width, height, mipMap, preferredFormat, SurfaceType.RenderTarget, shared, arraySize);
		this.depthStencilFormat = preferredDepthFormat;
		this.multiSampleCount = preferredMultiSampleCount;
		this.renderTargetUsage = usage;

		platformConstruct(graphicsDevice, width, height, mipMap, preferredFormat, preferredDepthFormat, preferredMultiSampleCount, usage, shared);
	}

	public RenderTarget2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipMap,
			SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat, int preferredMultiSampleCount,
			RenderTargetUsage usage, boolean shared)
	{
		this(graphicsDevice, width, height, mipMap, preferredFormat, preferredDepthFormat, preferredMultiSampleCount, usage, shared, 1);
	}

	public RenderTarget2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipMap,
			SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat, int preferredMultiSampleCount,
			RenderTargetUsage usage)
	{
		this(graphicsDevice, width, height, mipMap, preferredFormat, preferredDepthFormat, preferredMultiSampleCount, usage, false);
	}

	public RenderTarget2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipMap,
			SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat)
	{
		this(graphicsDevice, width, height, mipMap, preferredFormat, preferredDepthFormat, 0, RenderTargetUsage.DiscardContents);
	}

	public RenderTarget2D(GraphicsDevice graphicsDevice, int width, int height)
	{
		this(graphicsDevice, width, height, false, SurfaceFormat.Color, DepthFormat.None, 0, RenderTargetUsage.DiscardContents);
	}

	// / <summary>
	// / Allows child class to specify the surface type, eg: a swap chain.
	// / </summary>
	protected RenderTarget2D(GraphicsDevice graphicsDevice,
							 int width,
							 int height,
							 boolean mipMap,
							 SurfaceFormat format,
							 DepthFormat depthFormat,
							 int preferredMultiSampleCount,
							 RenderTargetUsage usage,
							 SurfaceType surfaceType)
	{
		super(graphicsDevice, width, height, mipMap, format, surfaceType);
		this.depthStencilFormat = depthFormat;
		this.multiSampleCount = preferredMultiSampleCount;
		this.renderTargetUsage = usage;
	}

	@Override
	protected void graphicsDeviceResetting()
	{
		platformGraphicsDeviceResetting();
		super.graphicsDeviceResetting();
	}

	// ########################################################################
	// #                        Platform specific code                        #
	// ########################################################################

	private void platformConstruct(GraphicsDevice graphicsDevice, int width, int height, boolean mipMap,
								   SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat,
								   int preferredMultiSampleCount, RenderTargetUsage usage, boolean shared)
	{
		generateIfRequired();
	}

	private void generateIfRequired()
	{
//		if (_renderTargetViews != null)
//			return;

		// Create a view interface on the rendertarget to use on bind.
//		if (arraySize > 1)
//		{
//			_renderTargetViews = new RenderTargetView[ArraySize];
//			for (int i = 0; i < arraySize; ++i)
//			{
//				var renderTargetViewDescription = new RenderTargetViewDescription();
//				if (multiSampleCount > 1)
//				{
//					renderTargetViewDescription.Dimension = RenderTargetViewDimension.Texture2DMultisampledArray;
//					renderTargetViewDescription.Texture2DMSArray.ArraySize = 1;
//					renderTargetViewDescription.Texture2DMSArray.FirstArraySlice = i;
//				}
//				else
//				{
//					renderTargetViewDescription.Dimension = RenderTargetViewDimension.Texture2DArray;
//					renderTargetViewDescription.Texture2DArray.ArraySize = 1;
//					renderTargetViewDescription.Texture2DArray.FirstArraySlice = i;
//					renderTargetViewDescription.Texture2DArray.MipSlice = 0;
//				}
//				_renderTargetViews[i] = new RenderTargetView(
//						GraphicsDevice._d3dDevice, GetTexture(),
//						renderTargetViewDescription);
//			}
//		}
//		else
//		{
//			_renderTargetViews = new[] { new RenderTargetView(GraphicsDevice._d3dDevice, GetTexture()) };
//		}

		// If we don't need a depth buffer then we're done.
//		if (depthStencilFormat.equals(DepthFormat.None))
//			return;

		// Setup the multisampling description.
//		var multisampleDesc = new SharpDX.DXGI.SampleDescription(1, 0);
//		if (multiSampleCount > 1)
//		{
//			multisampleDesc.Count = MultiSampleCount;
//			multisampleDesc.Quality = (int)StandardMultisampleQualityLevels.StandardMultisamplePattern;
//		}

		// Create a descriptor for the depth/stencil buffer.
		// Allocate a 2-D surface as the depth/stencil buffer.
		// Create a DepthStencil view on this surface to use on bind.
//		using (var depthBuffer = new SharpDX.Direct3D11.Texture2D(GraphicsDevice._d3dDevice, new Texture2DDescription
//		{
//			Format = SharpDXHelper.ToFormat(DepthStencilFormat),
//			ArraySize = 1,
//			MipLevels = 1,
//			Width = width,
//			Height = height,
//			SampleDescription = multisampleDesc,
//			BindFlags = BindFlags.DepthStencil,
//		}))
//		{
			// Create the view for binding to the device.
//			_depthStencilView = new DepthStencilView(GraphicsDevice._d3dDevice, depthBuffer,
//				new DepthStencilViewDescription()
//			{
//				Format = SharpDXHelper.ToFormat(DepthStencilFormat),
//				Dimension = DepthStencilViewDimension.Texture2D
//			});
//		}
	}

	private void platformGraphicsDeviceResetting()
    {
//        if (_renderTargetViews != null)
//        {
//            for (var i = 0; i < _renderTargetViews.Length; i++)
//                _renderTargetViews[i].Dispose();
//            _renderTargetViews = null;
//        }
//        SharpDX.Utilities.Dispose(ref _depthStencilView);
    }
}
