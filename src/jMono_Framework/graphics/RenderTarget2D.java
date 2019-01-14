package jMono_Framework.graphics;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.graphics.states.DepthFormat;

public class RenderTarget2D extends Texture2D implements IRenderTarget
{
    private DepthFormat _depthStencilFormat;

    public DepthFormat getDepthStencilFormat()
    {
        return _depthStencilFormat;
    }

    private int _multiSampleCount;

    public int getMultiSampleCount()
    {
        return _multiSampleCount;
    }

    private RenderTargetUsage _renderTargetUsage;

    public RenderTargetUsage getRenderTargetUsage()
    {
        return _renderTargetUsage;
    }

    public boolean isContentLost()
    {
        return false;
    }

    public Event<EventArgs> contentLost = new Event<EventArgs>();

    @SuppressWarnings("unused")
    private boolean suppressEventHandlerWarningsUntilEventsAreProperlyImplemented()
    {
        return contentLost != null;
    }

    public RenderTarget2D(GraphicsDevice graphicsDevice, int width, int height, boolean mipMap,
                          SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat, int preferredMultiSampleCount,
                          RenderTargetUsage usage, boolean shared, int arraySize)
    {
        super(graphicsDevice, width, height, mipMap, preferredFormat, SurfaceType.RenderTarget, shared, arraySize);
        _depthStencilFormat = preferredDepthFormat;
        _multiSampleCount = preferredMultiSampleCount;
        _renderTargetUsage = usage;

        // TODO(Eric): Platform specific code, see other RenderTarget2D files if needed.
        // platformConstruct(graphicsDevice, width, height, mipMap, preferredFormat, preferredDepthFormat, preferredMultiSampleCount, usage, shared);
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

    /**
     * Allows child class to specify the surface type, example: a swap chain.
     * 
     * @param graphicsDevice
     * @param width
     * @param height
     * @param mipMap
     * @param format
     * @param depthFormat
     * @param preferredMultiSampleCount
     * @param usage
     * @param surfaceType
     */
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
        _depthStencilFormat = depthFormat;
        _multiSampleCount = preferredMultiSampleCount;
        _renderTargetUsage = usage;
    }

    @Override
    protected void graphicsDeviceResetting()
    {
        // TODO(Eric): Platform specific code, see other RenderTarget2D files if needed.
        // platformGraphicsDeviceResetting();
        super.graphicsDeviceResetting();
    }

}
