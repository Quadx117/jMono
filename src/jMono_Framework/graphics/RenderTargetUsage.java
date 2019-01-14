package jMono_Framework.graphics;

/**
 * Defines if the previous content in a render target is preserved when it set on the graphics
 * device.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public enum RenderTargetUsage
{
    /**
     * The render target content will not be preserved.
     */
    DiscardContents,

    /**
     * The render target content will be preserved even if it is slow or requires extra memory.
     */
    PreserveContents,

    /**
     * The render target content might be preserved if the platform can do so without a penalty
     * in performance or memory usage.
     */
    PlatformContents
}
