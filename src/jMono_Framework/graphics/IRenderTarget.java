package jMono_Framework.graphics;

/**
 * Represents a render target.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public interface IRenderTarget
{
    /**
     * Returns the width of the render target in pixels.
     * 
     * @return The width of the render target in pixels.
     */
    int getWidth();

    /**
     * Returns the height of the render target in pixels.
     * 
     * @return The height of the render target in pixels.
     */
    int getHeight();

    /**
     * Returns the usage mode of the render target.
     * 
     * @return The usage mode of the render target.
     */
    RenderTargetUsage getRenderTargetUsage();

// #if DIRECTX
    /**
     * Returns the {@link RenderTargetView} for the specified array slice.
     * <p>
     * For texture cubes: The array slice is the index of the cube map face.
     * 
     * @param arraySlice
     *        The array slice.
     * @return The {@code RenderTargetView}.
     */
    // RenderTargetView getRenderTargetView(int arraySlice);

    /**
     * Returns the <see cref="DepthStencilView"/>.
     * 
     * @return The {@link DepthStencilView}. Can be {@code null}.
     */
    // DepthStencilView getDepthStencilView();
// #endif

// #if OPENGL
    // int getGLTexture();
    // TextureTarget getGLTarget();
    // int getGLColorBuffer();
    // void setGLColorBuffer(int value);
    // int getGLDepthBuffer();
    // void setGLDepthBuffer(int value);
    // int getGLStencilBuffer();
    // void setGLStencilBuffer(int value);
    // int getMultiSampleCount();
    // int getLevelCount();

    // TextureTarget getFramebufferTarget(RenderTargetBinding renderTargetBinding);
// #endif
}
