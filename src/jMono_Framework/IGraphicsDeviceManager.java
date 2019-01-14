package jMono_Framework;

/**
 * Used by the platform code to control the graphics device.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public interface IGraphicsDeviceManager
{
    /**
     * Called at the start of rendering a frame.
     * 
     * @return Returns true if the frame should be rendered.
     */
    boolean beginDraw();

    /**
     * Called to create the graphics device.
     * <p>
     * Does nothing if the graphics device is already created.
     */
    void createDevice();

    /**
     * Called after rendering to present the frame to the screen.
     */
    void endDraw();
}
