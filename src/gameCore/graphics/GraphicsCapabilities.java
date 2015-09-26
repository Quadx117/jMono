package gameCore.graphics;

/**
 * Provides information about the capabilities of the current graphics device. A very useful thread
 * for investigating GL extenion names :
 * http://stackoverflow.com/questions/3881197/opengl-es-2-0-extensions-on-android-devices
 * 
 * @author Eric
 *
 */
public class GraphicsCapabilities
{
	public GraphicsCapabilities(GraphicsDevice graphicsDevice)
	{
		initialize(graphicsDevice);
	}

	/**
	 * Whether the device fully supports non power-of-two textures, including mip maps and wrap
	 * modes other than CLAMP_TO_EDGE
	 */
	protected boolean supportsNonPowerOfTwo;
	public boolean supportsNonPowerOfTwo() { return supportsNonPowerOfTwo; }

	/**
	 * Whether the device supports anisotropic texture filtering
	 */
	protected boolean supportsTextureFilterAnisotropic;
	public boolean supportsTextureFilterAnisotropic() { return supportsTextureFilterAnisotropic; }

	protected boolean supportsDepth24;
	public boolean supportsDepth24() { return supportsDepth24; }

	protected boolean supportsPackedDepthStencil;
	public boolean supportsPackedDepthStencil() { return supportsPackedDepthStencil; }

	protected boolean supportsDepthNonLinear;
	public boolean supportsDepthNonLinear() { return supportsDepthNonLinear; }

	/**
	 * Whether the device supports DXT1
	 */
	protected boolean supportsDxt1;
	public boolean supportsDxt1() { return supportsDxt1; }

	/**
	 * Whether the device supports S3TC (DXT1, DXT3, DXT5)
	 */
	protected boolean supportsS3tc;
	public boolean supportsS3tc() { return supportsS3tc; }

	/**
	 * Whether the device supports PVRTC
	 */
	protected boolean supportsPvrtc;
	public boolean supportsPvrtc() { return supportsPvrtc; }

	/**
	 * Whether the device supports ETC1
	 */
	protected boolean supportsEtc1;
	public boolean supportsEtc1() { return supportsEtc1; }

	/**
	 * Whether the device supports ATITC
	 */
	protected boolean supportsAtitc;
	public boolean supportsAtitc() { return supportsAtitc; }

// #if OPENGL
	// / <summary>
	// / True, if GL_ARB_framebuffer_object is supported; false otherwise.
	// / </summary>
	protected boolean supportsFramebufferObjectARB;
	public boolean supportsFramebufferObjectARB() { return supportsFramebufferObjectARB; }

	// / <summary>
	// / True, if GL_EXT_framebuffer_object is supported; false otherwise.
	// / </summary>
	protected boolean supportsFramebufferObjectEXT;
	public boolean supportsFramebufferObjectEXT() { return supportsFramebufferObjectEXT; }

	// / <summary>
	// / Gets the max texture anisotropy. This value typically lies
	// / between 0 and 16, where 0 means anisotropic filtering is not
	// / supported.
	// / </summary>
	protected int maxTextureAnisotropy;
	public int getMaxTextureAnisotropy() { return maxTextureAnisotropy; }
// #endif

	protected boolean supportsTextureMaxLevel;
	public boolean supportsTextureMaxLevel() { return supportsTextureMaxLevel; }

	/// <summary>
    /// True, if sRGB is supported. On Direct3D platforms, this is always <code>true</code>.
    /// On OpenGL platforms, it is <code>true</code> if both framebuffer sRGB
    /// and texture sRGB are supported.
    /// </summary>
    protected boolean supportsSRgb;
    protected boolean supportsSRgb() { return supportsSRgb; }
    
    protected boolean supportsTextureArrays;
    protected boolean supportsTextureArrays() { return supportsTextureArrays; }
    
    protected boolean supportsDepthClamp;
    protected boolean supportsDepthClamp() { return supportsDepthClamp; }
    
    protected boolean supportsVertexTextures;
    protected boolean supportsVertexTextures() { return supportsVertexTextures; }
    
	protected void initialize(GraphicsDevice device)
	{
		supportsNonPowerOfTwo = getNonPowerOfTwo(device);

// #if OPENGL
		// supportsTextureFilterAnisotropic = device._extensions.Contains("GL_EXT_texture_filter_anisotropic");
// #else
		supportsTextureFilterAnisotropic = true;
// #endif
// #if GLES
		// supportsDepth24 = device._extensions.Contains("GL_OES_depth24");
		// supportsPackedDepthStencil = device._extensions.Contains("GL_OES_packed_depth_stencil");
		// supportsDepthNonLinear = device._extensions.Contains("GL_NV_depth_nonlinear");
		// supportsTextureMaxLevel = device._extensions.Contains("GL_APPLE_texture_max_level");
// #else
		supportsDepth24 = true;
		supportsPackedDepthStencil = true;
		supportsDepthNonLinear = false;
		supportsTextureMaxLevel = true;
// #endif

		// Texture compression
// #if DIRECTX
		// supportsDxt1 = true;
		// supportsS3tc = true;
// #elif OPENGL
		// supportsS3tc = device._extensions.Contains("GL_EXT_texture_compression_s3tc") ||
		// device._extensions.Contains("GL_OES_texture_compression_S3TC") ||
		// device._extensions.Contains("GL_EXT_texture_compression_dxt3") ||
		// device._extensions.Contains("GL_EXT_texture_compression_dxt5");
		// supportsDxt1 = SupportsS3tc ||
		// device._extensions.Contains("GL_EXT_texture_compression_dxt1");
		// supportsPvrtc = device._extensions.Contains("GL_IMG_texture_compression_pvrtc");
		// supportsEtc1 = device._extensions.Contains("GL_OES_compressed_ETC1_RGB8_texture");
		// supportsAtitc = device._extensions.Contains("GL_ATI_texture_compression_atitc") ||
		// device._extensions.Contains("GL_AMD_compressed_ATC_texture");
// #endif

		// OpenGL framebuffer objects
// #if OPENGL
   // #if GLES
		// supportsFramebufferObjectARB = true; // always supported on GLES 2.0+
		// supportsFramebufferObjectEXT = false;
   // #else
		// supportsFramebufferObjectARB = device._extensions.Contains("GL_ARB_framebuffer_object");
		// supportsFramebufferObjectEXT = device._extensions.Contains("GL_EXT_framebuffer_object");
   // #endif
// #endif

		// Anisotropic filtering
// #if OPENGL
		// int anisotropy = 0;
		// if (supportsTextureFilterAnisotropic)
		// {
		// GL.GetInteger((GetPName)All.MaxTextureMaxAnisotropyExt, out anisotropy);
		// GraphicsExtensions.CheckGLError();
		// }
		// maxTextureAnisotropy = anisotropy;
// #endif
		// sRGB
//#if DIRECTX
		supportsSRgb = true;
// #elif OPENGL
   // #if GLES
		// SupportsSRgb = device._extensions.Contains("GL_EXT_sRGB");
   // #else
		// SupportsSRgb = device._extensions.Contains("GL_EXT_texture_sRGB") && device._extensions.Contains("GL_EXT_framebuffer_sRGB");
   // #endif
// #endif

// #if DIRECTX
		supportsTextureArrays = device.getGraphicsProfile() == GraphicsProfile.HiDef;
// #elif OPENGL
		// TODO: Implement OpenGL support for texture arrays
		// once we can author shaders that use texture arrays.
		// SupportsTextureArrays = false;
// #endif

// #if DIRECTX
		supportsDepthClamp = device.getGraphicsProfile() == GraphicsProfile.HiDef;
// #elif OPENGL
		// supportsDepthClamp = device._extensions.Contains("GL_ARB_depth_clamp");
// #endif

// #if DIRECTX
		// TODO: fix that
		supportsVertexTextures = true;//device.getGraphicsProfile() == GraphicsProfile.HiDef;
// #elif OPENGL
		// supportsVertexTextures = false; // For now, until we implement vertex textures in OpenGL.
// #endif
	}

	boolean getNonPowerOfTwo(GraphicsDevice device)
	{
// #if OPENGL
   // #if GLES
		// return device._extensions.Contains("GL_OES_texture_npot")
		// || device._extensions.Contains("GL_ARB_texture_non_power_of_two")
		// || device._extensions.Contains("GL_IMG_texture_npot")
		// || device._extensions.Contains("GL_NV_texture_npot_2D_mipmap");
   // #else
		// Unfortunately non PoT texture support is patchy even on desktop systems and we can't
		// rely on the fact that GL2.0+ supposedly supports npot in the core.
		// Reference: http://aras-p.info/blog/2012/10/17/non-power-of-two-textures/
		// return device._maxTextureSize >= 8192;
   // #endif

// #else
		// NOTE: Arguments need to be in this order since getGraphicsProfile() can return null and
		// we can't call equals on a null Object
		return GraphicsProfile.HiDef.equals(device.getGraphicsProfile());
// #endif
	}
}
