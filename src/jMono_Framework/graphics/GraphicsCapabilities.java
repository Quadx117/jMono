package jMono_Framework.graphics;

/**
 * Provides information about the capabilities of the current graphics device. A very useful thread
 * for investigating GL extension names :
 * http://stackoverflow.com/questions/3881197/opengl-es-2-0-extensions-on-android-devices
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class GraphicsCapabilities
{
    protected void initialize(GraphicsDevice device)
    {
        platformInitialize(device);
    }

    // For figuring out some capabilities we need native resources for querying
    protected void initializeAfterResources(GraphicsDevice device)
    {
        platformInitializeAfterResources(device);
    }

    /**
     * Whether the device fully supports non power-of-two textures, including mip maps and wrap
     * modes other than CLAMP_TO_EDGE
     */
    protected boolean supportsNonPowerOfTwo;

    public boolean supportsNonPowerOfTwo()
    {
        return supportsNonPowerOfTwo;
    }

    /**
     * Whether the device supports anisotropic texture filtering
     */
    protected boolean supportsTextureFilterAnisotropic;

    public boolean supportsTextureFilterAnisotropic()
    {
        return supportsTextureFilterAnisotropic;
    }

    protected boolean supportsDepth24;

    public boolean supportsDepth24()
    {
        return supportsDepth24;
    }

    protected boolean supportsPackedDepthStencil;

    public boolean supportsPackedDepthStencil()
    {
        return supportsPackedDepthStencil;
    }

    protected boolean supportsDepthNonLinear;

    public boolean supportsDepthNonLinear()
    {
        return supportsDepthNonLinear;
    }

    /**
     * Whether the device supports DXT1
     */
    protected boolean supportsDxt1;

    public boolean supportsDxt1()
    {
        return supportsDxt1;
    }

    /**
     * Whether the device supports S3TC (DXT1, DXT3, DXT5)
     */
    protected boolean supportsS3tc;

    public boolean supportsS3tc()
    {
        return supportsS3tc;
    }

    /**
     * Whether the device supports PVRTC
     */
    protected boolean supportsPvrtc;

    public boolean supportsPvrtc()
    {
        return supportsPvrtc;
    }

    /**
     * Whether the device supports ETC1
     */
    protected boolean supportsEtc1;

    public boolean supportsEtc1()
    {
        return supportsEtc1;
    }

    /**
     * Whether the device supports ATITC
     */
    protected boolean supportsAtitc;

    public boolean supportsAtitc()
    {
        return supportsAtitc;
    }

    protected boolean supportsTextureMaxLevel;

    public boolean supportsTextureMaxLevel()
    {
        return supportsTextureMaxLevel;
    }

    /**
     * {@code true}, if sRGB is supported. On Direct3D platforms, this is always {@code true}. On
     * OpenGL platforms, it is {@code true} if both framebuffer sRGB and texture sRGB are supported.
     */
    protected boolean supportsSRgb;

    protected boolean supportsSRgb()
    {
        return supportsSRgb;
    }

    protected boolean supportsTextureArrays;

    protected boolean supportsTextureArrays()
    {
        return supportsTextureArrays;
    }

    protected boolean supportsDepthClamp;

    protected boolean supportsDepthClamp()
    {
        return supportsDepthClamp;
    }

    protected boolean supportsVertexTextures;

    protected boolean supportsVertexTextures()
    {
        return supportsVertexTextures;
    }

    // The highest possible MSCount
    private final int multiSampleCountLimit = 32;
    private int _maxMultiSampleCount;

    public int getMaxMultiSampleCount()
    {
        return _maxMultiSampleCount;
    }

    //
    // NOTE(Eric): From GraphicsCapabilities.DirectX.cs
    //

    private void platformInitialize(GraphicsDevice device)
    {
        supportsNonPowerOfTwo = device.getGraphicsProfile() == GraphicsProfile.HiDef;
        supportsTextureFilterAnisotropic = true;

        supportsDepth24 = true;
        supportsPackedDepthStencil = true;
        supportsDepthNonLinear = false;
        supportsTextureMaxLevel = true;

        // TODO(Eric): Validate support for texture compression
        // Texture compression
        // supportsDxt1 = true;
        // supportsS3tc = true;

        // supportsSRgb = true;

        supportsTextureArrays = device.getGraphicsProfile() == GraphicsProfile.HiDef;
        supportsDepthClamp = device.getGraphicsProfile() == GraphicsProfile.HiDef;
        supportsVertexTextures = device.getGraphicsProfile() == GraphicsProfile.HiDef;
    }

    private void platformInitializeAfterResources(GraphicsDevice device)
    {
        // TODO(Eric): How to support this with the software renderer
        _maxMultiSampleCount = getMaxMultiSampleCount(device);
    }

    private int getMaxMultiSampleCount(GraphicsDevice device)
    {
        SurfaceFormat format = device.getPresentationParameters().getBackBufferFormat();
        // Find the maximum supported level starting with the game's requested multisampling level
        // and halving each time until reaching 0 (meaning no multisample support).
        int qualityLevels = 0;
        int maxLevel = multiSampleCountLimit;
        while (maxLevel > 0)
        {
            // qualityLevels = device._d3dDevice.CheckMultisampleQualityLevels(format, maxLevel);
            if (qualityLevels > 0)
                break;
            maxLevel /= 2;
        }
        return maxLevel;
    }

}
