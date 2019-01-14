package jMono_Framework.graphics;

/**
 * Defines the buffers for clearing when calling {@link GraphicsDevice#clear} operation.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public enum ClearOptions
{
    /**
     * Color buffer.
     */
    Target(1),

    /**
     * Depth buffer.
     */
    DepthBuffer(2),

    /**
     * Stencil buffer.
     */
    Stencil(4);

    // NOTE(Eric): This is for flags
    private final int value;

    private ClearOptions(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
