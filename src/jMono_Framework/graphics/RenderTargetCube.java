package jMono_Framework.graphics;

import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.dotNet.events.EventHandler;
import jMono_Framework.graphics.states.DepthFormat;

/**
 * Represents a texture cube that can be used as a render target.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class RenderTargetCube extends TextureCube implements IRenderTarget
{
    /**
     * The format of the depth-stencil buffer.
     */
    private DepthFormat _depthStencilFormat;

    /**
     * Returns the depth-stencil buffer format of this render target.
     * 
     * @return The format of the depth-stencil buffer.
     */
    public DepthFormat getDepthStencilFormat()
    {
        return _depthStencilFormat;
    }

    /**
     * The number of multisample locations.
     */
    private int _multiSampleCount;

    /**
     * Returns the number of multisample locations.
     * 
     * @return The number of multisample locations.
     */
    public int getMultiSampleCount()
    {
        return _multiSampleCount;
    }

    /**
     * The usage mode of the render target.
     */
    private RenderTargetUsage _renderTargetUsage;

    /**
     * Returns the usage mode of this render target.
     * 
     * @return The usage mode of this render target.
     */
    public RenderTargetUsage getRenderTargetUsage()
    {
        return _renderTargetUsage;
    }

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

    public boolean isContentLost()
    {
        return false;
    }

    public EventHandler<EventArgs> contentLost;

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
        _depthStencilFormat = preferredDepthFormat;
        _multiSampleCount = preferredMultiSampleCount;
        _renderTargetUsage = usage;

        platformConstruct(graphicsDevice, mipMap, preferredFormat, preferredDepthFormat, preferredMultiSampleCount, usage);
    }

    //
    // NOTE(Eric): Platform specific, see other RenderTargetCube files
    //

    private void platformConstruct(GraphicsDevice graphicsDevice, boolean mipMap, SurfaceFormat preferredFormat, DepthFormat preferredDepthFormat,
                                   int preferredMultiSampleCount, RenderTargetUsage usage)
    {
        // TODO Auto-generated method stub
    }

}
