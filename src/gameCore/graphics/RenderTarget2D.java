package gameCore.graphics;

import gameCore.events.EventArgs;
import gameCore.events.EventHandler;
import gameCore.graphics.states.DepthFormat;

public class RenderTarget2D extends Texture2D implements IRenderTarget
{
	private DepthFormat depthStencilFormat;

	public DepthFormat getDepthStencilFormat()
	{
		return depthStencilFormat;
	}

	private int multiSampleCount;

	public int getMultiSampleCount()
	{
		return multiSampleCount;
	}

	private RenderTargetUsage renderTargetUsage;

	public RenderTargetUsage getRenderTargetUsage()
	{
		return renderTargetUsage;
	}

	public boolean isContentLost()
	{
		return false;
	}

	public EventHandler<EventArgs> contentLost;

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

		// TODO: PlatformConstruct
		// PlatformConstruct(graphicsDevice, width, height, mipMap, preferredFormat,
		// preferredDepthFormat, preferredMultiSampleCount, usage, shared);
	}

	public RenderTarget2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipMap,
			SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat, int preferredMultiSampleCount,
			RenderTargetUsage usage, boolean shared)
	{
		this(graphicsDevice, width, height, mipMap, preferredFormat, preferredDepthFormat, preferredMultiSampleCount,
				usage, shared, 1);
	}

	public RenderTarget2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipMap,
			SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat, int preferredMultiSampleCount,
			RenderTargetUsage usage)
	{
		this(graphicsDevice, width, height, mipMap, preferredFormat, preferredDepthFormat, preferredMultiSampleCount,
				usage, false);
	}

	public RenderTarget2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipMap,
			SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat)
	{
		this(graphicsDevice, width, height, mipMap, preferredFormat, preferredDepthFormat, 0,
				RenderTargetUsage.DiscardContents);
	}

	public RenderTarget2D(GraphicsDevice graphicsDevice, int width, int height)
	{
		this(graphicsDevice, width, height, false, SurfaceFormat.Color, DepthFormat.None, 0,
				RenderTargetUsage.DiscardContents);
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
		// TODO: PlatformGraphicsDeviceResetting()
		// platformGraphicsDeviceResetting();
		super.graphicsDeviceResetting();
	}
}
