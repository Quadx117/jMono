package jMono_Framework.graphics;

import jMono_Framework.graphics.vertices.VertexElementFormat;

public class GraphicsExtensions
{
    /*
     * // #if OPENGL
     * public static int OpenGLNumberOfElements(VertexElementFormat elementFormat)
     * {
     * switch (elementFormat)
     * {
     * case Single:
     * return 1;
     * 
     * case Vector2:
     * return 2;
     * 
     * case Vector3:
     * return 3;
     * 
     * case Vector4:
     * return 4;
     * 
     * case Color:
     * return 4;
     * 
     * case Byte4:
     * return 4;
     * 
     * case Short2:
     * return 2;
     * 
     * case Short4:
     * return 4;
     * 
     * case NormalizedShort2:
     * return 2;
     * 
     * case NormalizedShort4:
     * return 4;
     * 
     * case HalfVector2:
     * return 2;
     * 
     * case HalfVector4:
     * return 4;
     * }
     * 
     * throw new IllegalArgumentException();
     * }
     * 
     * public static VertexPointerType OpenGLVertexPointerType(VertexElementFormat elementFormat)
     * {
     * switch (elementFormat)
     * {
     * case Single:
     * return Float;
     * 
     * case Vector2:
     * return Float;
     * 
     * case Vector3:
     * return Float;
     * 
     * case Vector4:
     * return Float;
     * 
     * case Color:
     * return Short;
     * 
     * case Byte4:
     * return Short;
     * 
     * case Short2:
     * return Short;
     * 
     * case Short4:
     * return Short;
     * 
     * case NormalizedShort2:
     * return Short;
     * 
     * case NormalizedShort4:
     * return Short;
     * 
     * case HalfVector2:
     * return Float;
     * 
     * case HalfVector4:
     * return Float;
     * }
     * 
     * throw new IllegalArgumentException();
     * }
     * 
     * public static VertexAttribPointerType OpenGLVertexAttribPointerType(VertexElementFormat
     * elementFormat)
     * {
     * switch (elementFormat)
     * {
     * case Single:
     * return Float;
     * 
     * case Vector2:
     * return Float;
     * 
     * case Vector3:
     * return Float;
     * 
     * case Vector4:
     * return Float;
     * 
     * case Color:
     * return UnsignedByte;
     * 
     * case Byte4:
     * return UnsignedByte;
     * 
     * case Short2:
     * return Short;
     * 
     * case Short4:
     * return Short;
     * 
     * case NormalizedShort2:
     * return Short;
     * 
     * case NormalizedShort4:
     * return Short;
     * 
     * // #if MONOMAC || WINDOWS || DESKTOPGL
     * case HalfVector2:
     * return HalfFloat;
     * 
     * case HalfVector4:
     * return HalfFloat;
     * // #endif
     * }
     * 
     * throw new IllegalArgumentException();
     * }
     * 
     * public static boolean OpenGLVertexAttribNormalized(VertexElement element)
     * {
     * // TODO: This may or may not be the right behavior.
     * //
     * // For instance the Byte4 format is not supposed
     * // to be normalized, but this line makes it so.
     * //
     * // The question is in MS XNA are types normalized based on usage or
     * // normalized based to their format?
     * //
     * if (element.VertexElementUsage == VertexElementUsage.Color)
     * return true;
     * 
     * switch (element.VertexElementFormat)
     * {
     * case NormalizedShort2:
     * case NormalizedShort4:
     * return true;
     * 
     * default:
     * return false;
     * }
     * }
     * 
     * public static ColorPointerType OpenGLColorPointerType(VertexElementFormat elementFormat)
     * {
     * switch (elementFormat)
     * {
     * case Single:
     * return ColorPointerType.Float;
     * 
     * case Vector2:
     * return ColorPointerType.Float;
     * 
     * case Vector3:
     * return ColorPointerType.Float;
     * 
     * case Vector4:
     * return ColorPointerType.Float;
     * 
     * case Color:
     * return ColorPointerType.UnsignedByte;
     * 
     * case Byte4:
     * return ColorPointerType.UnsignedByte;
     * 
     * case Short2:
     * return ColorPointerType.Short;
     * 
     * case Short4:
     * return ColorPointerType.Short;
     * 
     * case NormalizedShort2:
     * return ColorPointerType.UnsignedShort;
     * 
     * case NormalizedShort4:
     * return ColorPointerType.UnsignedShort;
     * 
     * // #if MONOMAC
     * case HalfVector2:
     * return ColorPointerType.HalfFloat;
     * 
     * case HalfVector4:
     * return ColorPointerType.HalfFloat;
     * // #endif
     * }
     * 
     * throw new IllegalArgumentException();
     * }
     * 
     * public static NormalPointerType OpenGLNormalPointerType(VertexElementFormat elementFormat)
     * {
     * switch (elementFormat)
     * {
     * case Single:
     * return NormalPointerType.Float;
     * 
     * case Vector2:
     * return NormalPointerType.Float;
     * 
     * case Vector3:
     * return NormalPointerType.Float;
     * 
     * case Vector4:
     * return NormalPointerType.Float;
     * 
     * case Color:
     * return NormalPointerType.Byte;
     * 
     * case Byte4:
     * return NormalPointerType.Byte;
     * 
     * case Short2:
     * return NormalPointerType.Short;
     * 
     * case Short4:
     * return NormalPointerType.Short;
     * 
     * case NormalizedShort2:
     * return NormalPointerType.Short;
     * 
     * case NormalizedShort4:
     * return NormalPointerType.Short;
     * 
     * // #if MONOMAC
     * case HalfVector2:
     * return NormalPointerType.HalfFloat;
     * 
     * case HalfVector4:
     * return NormalPointerType.HalfFloat;
     * // #endif
     * }
     * 
     * throw new IllegalArgumentException();
     * }
     * 
     * public static TexCoordPointerType OpenGLTexCoordPointerType(VertexElementFormat
     * elementFormat)
     * {
     * switch (elementFormat)
     * {
     * case Single:
     * return TexCoordPointerType.Float;
     * 
     * case Vector2:
     * return TexCoordPointerType.Float;
     * 
     * case Vector3:
     * return TexCoordPointerType.Float;
     * 
     * case Vector4:
     * return TexCoordPointerType.Float;
     * 
     * case Color:
     * return TexCoordPointerType.Float;
     * 
     * case Byte4:
     * return TexCoordPointerType.Float;
     * 
     * case Short2:
     * return TexCoordPointerType.Short;
     * 
     * case Short4:
     * return TexCoordPointerType.Short;
     * 
     * case NormalizedShort2:
     * return TexCoordPointerType.Short;
     * 
     * case NormalizedShort4:
     * return TexCoordPointerType.Short;
     * 
     * // #if MONOMAC
     * case HalfVector2:
     * return TexCoordPointerType.HalfFloat;
     * 
     * case HalfVector4:
     * return TexCoordPointerType.HalfFloat;
     * // #endif
     * }
     * 
     * throw new IllegalArgumentException();
     * }
     * 
     * 
     * public static BlendEquationMode GetBlendEquationMode (BlendFunction function)
     * {
     * switch (function) {
     * case BlendFunction.Add:
     * return BlendEquationMode.FuncAdd;
     * // #if IOS
     * case BlendFunction.Max:
     * return (BlendEquationMode)All.MaxExt;
     * case BlendFunction.Min:
     * return (BlendEquationMode)All.MinExt;
     * // #elif MONOMAC || WINDOWS || DESKTOPGL
     * case BlendFunction.Max:
     * return BlendEquationMode.Max;
     * case BlendFunction.Min:
     * return BlendEquationMode.Min;
     * // #endif
     * case BlendFunction.ReverseSubtract:
     * return BlendEquationMode.FuncReverseSubtract;
     * case BlendFunction.Subtract:
     * return BlendEquationMode.FuncSubtract;
     * 
     * default:
     * throw new IllegalArgumentException();
     * }
     * }
     * 
     * public static BlendingFactorSrc GetBlendFactorSrc (Blend blend)
     * {
     * switch (blend) {
     * case Blend.DestinationAlpha:
     * return BlendingFactorSrc.DstAlpha;
     * case Blend.DestinationColor:
     * return BlendingFactorSrc.DstColor;
     * case Blend.InverseDestinationAlpha:
     * return BlendingFactorSrc.OneMinusDstAlpha;
     * case Blend.InverseDestinationColor:
     * return BlendingFactorSrc.OneMinusDstColor;
     * case Blend.InverseSourceAlpha:
     * return BlendingFactorSrc.OneMinusSrcAlpha;
     * case Blend.InverseSourceColor:
     * // #if MONOMAC
     * return (BlendingFactorSrc)All.OneMinusSrcColor;
     * // #else
     * return BlendingFactorSrc.OneMinusSrcColor;
     * // #endif
     * case Blend.One:
     * return BlendingFactorSrc.One;
     * case Blend.SourceAlpha:
     * return BlendingFactorSrc.SrcAlpha;
     * case Blend.SourceAlphaSaturation:
     * return BlendingFactorSrc.SrcAlphaSaturate;
     * case Blend.SourceColor:
     * // #if MONOMAC
     * return (BlendingFactorSrc)All.SrcColor;
     * // #else
     * return BlendingFactorSrc.SrcColor;
     * // #endif
     * case Blend.Zero:
     * return BlendingFactorSrc.Zero;
     * default:
     * return BlendingFactorSrc.One;
     * }
     * 
     * }
     * 
     * public static BlendingFactorDest GetBlendFactorDest (Blend blend)
     * {
     * switch (blend) {
     * case Blend.DestinationAlpha:
     * return BlendingFactorDest.DstAlpha;
     * // case Blend.DestinationColor:
     * // return BlendingFactorDest.DstColor;
     * case Blend.InverseDestinationAlpha:
     * return BlendingFactorDest.OneMinusDstAlpha;
     * // case Blend.InverseDestinationColor:
     * // return BlendingFactorDest.OneMinusDstColor;
     * case Blend.InverseSourceAlpha:
     * return BlendingFactorDest.OneMinusSrcAlpha;
     * case Blend.InverseSourceColor:
     * return BlendingFactorDest.OneMinusSrcColor;
     * case Blend.One:
     * return BlendingFactorDest.One;
     * case Blend.SourceAlpha:
     * return BlendingFactorDest.SrcAlpha;
     * // case Blend.SourceAlphaSaturation:
     * // return BlendingFactorDest.SrcAlphaSaturate;
     * case Blend.SourceColor:
     * return BlendingFactorDest.SrcColor;
     * case Blend.Zero:
     * return BlendingFactorDest.Zero;
     * default:
     * return BlendingFactorDest.One;
     * }
     * 
     * }
     * 
     * public static DepthFunction GetDepthFunction(CompareFunction compare)
     * {
     * switch (compare)
     * {
     * default:
     * case CompareFunction.Always:
     * return DepthFunction.Always;
     * case CompareFunction.Equal:
     * return DepthFunction.Equal;
     * case CompareFunction.Greater:
     * return DepthFunction.Greater;
     * case CompareFunction.GreaterEqual:
     * return DepthFunction.Gequal;
     * case CompareFunction.Less:
     * return DepthFunction.Less;
     * case CompareFunction.LessEqual:
     * return DepthFunction.Lequal;
     * case CompareFunction.Never:
     * return DepthFunction.Never;
     * case CompareFunction.NotEqual:
     * return DepthFunction.Notequal;
     * }
     * }
     * 
     * // #if WINDOWS || DESKTOPGL || ANGLE
     * /// <summary>
     * /// Convert a <see cref="SurfaceFormat"/> to an OpenTK.Graphics.ColorFormat.
     * /// This is used for setting up the backbuffer format of the OpenGL context.
     * /// </summary>
     * /// <returns>An OpenTK.Graphics.ColorFormat instance.</returns>
     * /// <param name="format">The <see cref="SurfaceFormat"/> to convert.</param>
     * protected static ColorFormat GetColorFormat(SurfaceFormat format)
     * {
     * switch (format)
     * {
     * case SurfaceFormat.Alpha8:
     * return new ColorFormat(0, 0, 0, 8);
     * case SurfaceFormat.Bgr565:
     * return new ColorFormat(5, 6, 5, 0);
     * case SurfaceFormat.Bgra4444:
     * return new ColorFormat(4, 4, 4, 4);
     * case SurfaceFormat.Bgra5551:
     * return new ColorFormat(5, 5, 5, 1);
     * case SurfaceFormat.Bgr32:
     * return new ColorFormat(8, 8, 8, 0);
     * case SurfaceFormat.Bgra32:
     * case SurfaceFormat.Color:
     * case SurfaceFormat.ColorSRgb:
     * return new ColorFormat(8, 8, 8, 8);
     * case SurfaceFormat.Rgba1010102:
     * return new ColorFormat(10, 10, 10, 2);
     * default:
     * // Floating point backbuffers formats could be implemented
     * // but they are not typically used on the backbuffer. In
     * // those cases it is better to create a render target instead.
     * throw new NotSupportedException();
     * }
     * }
     * 
     * // #endif
     * 
     * 
     * protected static void GetGLFormat (SurfaceFormat format,
     * GraphicsDevice graphicsDevice,
     * PixelInternalFormat glInternalFormat,
     * PixelFormat glFormat,
     * PixelType glType)
     * {
     * glInternalFormat = PixelInternalFormat.Rgba;
     * glFormat = PixelFormat.Rgba;
     * glType = PixelType.UnsignedByte;
     * 
     * var supportsSRgb = graphicsDevice.GraphicsCapabilities.SupportsSRgb;
     * 
     * switch (format) {
     * case SurfaceFormat.Color:
     * glInternalFormat = PixelInternalFormat.Rgba;
     * glFormat = PixelFormat.Rgba;
     * glType = PixelType.UnsignedByte;
     * break;
     * case SurfaceFormat.ColorSRgb:
     * if (!supportsSRgb)
     * goto case SurfaceFormat.Color;
     * glInternalFormat = (PixelInternalFormat) 0x8C40; // PixelInternalFormat.Srgb;
     * glFormat = PixelFormat.Rgba;
     * glType = PixelType.UnsignedByte;
     * break;
     * case SurfaceFormat.Bgr565:
     * glInternalFormat = PixelInternalFormat.Rgb;
     * glFormat = PixelFormat.Rgb;
     * glType = PixelType.UnsignedShort565;
     * break;
     * case SurfaceFormat.Bgra4444:
     * // #if IOS || ANDROID
     * glInternalFormat = PixelInternalFormat.Rgba;
     * // #else
     * glInternalFormat = PixelInternalFormat.Rgba4;
     * // #endif
     * glFormat = PixelFormat.Rgba;
     * glType = PixelType.UnsignedShort4444;
     * break;
     * case SurfaceFormat.Bgra5551:
     * glInternalFormat = PixelInternalFormat.Rgba;
     * glFormat = PixelFormat.Rgba;
     * glType = PixelType.UnsignedShort5551;
     * break;
     * case SurfaceFormat.Alpha8:
     * glInternalFormat = PixelInternalFormat.Luminance;
     * glFormat = PixelFormat.Luminance;
     * glType = PixelType.UnsignedByte;
     * break;
     * // #if !IOS && !ANDROID && !ANGLE
     * case SurfaceFormat.Dxt1:
     * glInternalFormat = PixelInternalFormat.CompressedRgbS3tcDxt1Ext;
     * glFormat = (PixelFormat)GLPixelFormat.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt1SRgb:
     * if (!supportsSRgb)
     * goto case SurfaceFormat.Dxt1;
     * glInternalFormat = PixelInternalFormat.CompressedSrgbS3tcDxt1Ext;
     * glFormat = (PixelFormat)GLPixelFormat.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt1a:
     * glInternalFormat = PixelInternalFormat.CompressedRgbaS3tcDxt1Ext;
     * glFormat = (PixelFormat)GLPixelFormat.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt3:
     * glInternalFormat = PixelInternalFormat.CompressedRgbaS3tcDxt3Ext;
     * glFormat = (PixelFormat)GLPixelFormat.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt3SRgb:
     * if (!supportsSRgb)
     * goto case SurfaceFormat.Dxt3;
     * glInternalFormat = PixelInternalFormat.CompressedSrgbAlphaS3tcDxt3Ext;
     * glFormat = (PixelFormat)GLPixelFormat.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt5:
     * glInternalFormat = PixelInternalFormat.CompressedRgbaS3tcDxt5Ext;
     * glFormat = (PixelFormat)GLPixelFormat.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt5SRgb:
     * if (!supportsSRgb)
     * goto case SurfaceFormat.Dxt5;
     * glInternalFormat = PixelInternalFormat.CompressedSrgbAlphaS3tcDxt5Ext;
     * glFormat = (PixelFormat)GLPixelFormat.CompressedTextureFormats;
     * break;
     * 
     * case SurfaceFormat.Single:
     * glInternalFormat = PixelInternalFormat.R32f;
     * glFormat = PixelFormat.Red;
     * glType = PixelType.Float;
     * break;
     * 
     * case SurfaceFormat.HalfVector2:
     * glInternalFormat = PixelInternalFormat.Rg16f;
     * glFormat = PixelFormat.Rg;
     * glType = PixelType.HalfFloat;
     * break;
     * 
     * // HdrBlendable implemented as HalfVector4 (see
     * http://blogs.msdn.com/b/shawnhar/archive/2010/07/09/surfaceformat-hdrblendable.aspx)
     * case SurfaceFormat.HdrBlendable:
     * case SurfaceFormat.HalfVector4:
     * glInternalFormat = PixelInternalFormat.Rgba16f;
     * glFormat = PixelFormat.Rgba;
     * glType = PixelType.HalfFloat;
     * break;
     * 
     * case SurfaceFormat.HalfSingle:
     * glInternalFormat = PixelInternalFormat.R16f;
     * glFormat = PixelFormat.Red;
     * glType = PixelType.HalfFloat;
     * break;
     * 
     * case SurfaceFormat.Vector2:
     * glInternalFormat = PixelInternalFormat.Rg32f;
     * glFormat = PixelFormat.Rg;
     * glType = PixelType.Float;
     * break;
     * 
     * case SurfaceFormat.Vector4:
     * glInternalFormat = PixelInternalFormat.Rgba32f;
     * glFormat = PixelFormat.Rgba;
     * glType = PixelType.Float;
     * break;
     * 
     * case SurfaceFormat.NormalizedByte2:
     * glInternalFormat = PixelInternalFormat.Rg8i;
     * glFormat = PixelFormat.Rg;
     * glType = PixelType.Byte;
     * break;
     * 
     * case SurfaceFormat.NormalizedByte4:
     * glInternalFormat = PixelInternalFormat.Rgba8i;
     * glFormat = PixelFormat.Rgba;
     * glType = PixelType.Byte;
     * break;
     * 
     * case SurfaceFormat.Rg32:
     * glInternalFormat = PixelInternalFormat.Rg16ui;
     * glFormat = PixelFormat.Rg;
     * glType = PixelType.UnsignedShort;
     * break;
     * 
     * case SurfaceFormat.Rgba64:
     * glInternalFormat = PixelInternalFormat.Rgba16ui;
     * glFormat = PixelFormat.Rgba;
     * glType = PixelType.UnsignedShort;
     * break;
     * 
     * case SurfaceFormat.Rgba1010102:
     * glInternalFormat = PixelInternalFormat.Rgb10A2ui;
     * glFormat = PixelFormat.Rgba;
     * glType = PixelType.UnsignedInt1010102;
     * break;
     * // #endif
     * 
     * // #if ANDROID
     * case SurfaceFormat.Dxt1:
     * // 0x83F0 is the RGB version, 0x83F1 is the RGBA version (1-bit alpha)
     * // XNA uses the RGB version.
     * glInternalFormat = (PixelInternalFormat)0x83F0;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt1SRgb:
     * if (!supportsSRgb)
     * goto case SurfaceFormat.Dxt1;
     * glInternalFormat = (PixelInternalFormat)0x8C4C;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt1a:
     * // 0x83F0 is the RGB version, 0x83F1 is the RGBA version (1-bit alpha)
     * glInternalFormat = (PixelInternalFormat)0x83F1;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt3:
     * glInternalFormat = (PixelInternalFormat)0x83F2;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt3SRgb:
     * if (!supportsSRgb)
     * goto case SurfaceFormat.Dxt3;
     * glInternalFormat = (PixelInternalFormat)0x8C4E;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt5:
     * glInternalFormat = (PixelInternalFormat)0x83F3;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.Dxt5SRgb:
     * if (!supportsSRgb)
     * goto case SurfaceFormat.Dxt5;
     * glInternalFormat = (PixelInternalFormat)0x8C4F;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.RgbaAtcExplicitAlpha:
     * glInternalFormat = (PixelInternalFormat)All.AtcRgbaExplicitAlphaAmd;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.RgbaAtcInterpolatedAlpha:
     * glInternalFormat = (PixelInternalFormat)All.AtcRgbaInterpolatedAlphaAmd;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.RgbEtc1:
     * glInternalFormat = (PixelInternalFormat)0x8D64; // GL_ETC1_RGB8_OES
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * // #endif
     * // #if IOS || ANDROID
     * case SurfaceFormat.RgbPvrtc2Bpp:
     * glInternalFormat = (PixelInternalFormat)All.CompressedRgbPvrtc2Bppv1Img;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.RgbPvrtc4Bpp:
     * glInternalFormat = (PixelInternalFormat)All.CompressedRgbPvrtc4Bppv1Img;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.RgbaPvrtc2Bpp:
     * glInternalFormat = (PixelInternalFormat)All.CompressedRgbaPvrtc2Bppv1Img;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * case SurfaceFormat.RgbaPvrtc4Bpp:
     * glInternalFormat = (PixelInternalFormat)All.CompressedRgbaPvrtc4Bppv1Img;
     * glFormat = (PixelFormat)All.CompressedTextureFormats;
     * break;
     * // #endif
     * default:
     * throw new NotSupportedException();
     * }
     * }
     * 
     * // #endif // OPENGL
     */

    public static int GetSyncInterval(PresentInterval interval)
    {
        switch (interval)
        {
            case Immediate:
                return 0;

            case Two:
                return 2;

            default:
                return 1;
        }
    }

    public static int GetSize(VertexElementFormat elementFormat)
    {
        switch (elementFormat)
        {
            case Single:
                return 4;

            case Vector2:
                return 8;

            case Vector3:
                return 12;

            case Vector4:
                return 16;

            case Color:
                return 4;

            case Byte4:
                return 4;

            case Short2:
                return 4;

            case Short4:
                return 8;

            case NormalizedShort2:
                return 4;

            case NormalizedShort4:
                return 8;

            case HalfVector2:
                return 4;

            case HalfVector4:
                return 8;
        }
        return 0;
    }

    /*
     * // #if OPENGL
     * public static int GetBoundTexture2D()
     * {
     * var prevTexture = 0;
     * GL.GetInteger(GetPName.TextureBinding2D, prevTexture);
     * GraphicsExtensions.LogGLError("GraphicsExtensions.GetBoundTexture2D() GL.GetInteger");
     * return prevTexture;
     * }
     * 
     * [Conditional("DEBUG")]
     * [DebuggerHidden]
     * public static void CheckGLError()
     * {
     * // #if GLES
     * var error = GL.GetErrorCode();
     * // #else
     * var error = GL.GetError();
     * // #endif
     * //Console.WriteLine(error);
     * if (error != ErrorCode.NoError)
     * throw new MonoGameGLException("GL.GetError() returned " + error.ToString());
     * }
     * // #endif
     * 
     * // #if OPENGL
     * [Conditional("DEBUG")]
     * public static void LogGLError(String location)
     * {
     * try
     * {
     * GraphicsExtensions.CheckGLError();
     * }
     * catch (MonoGameGLException ex)
     * {
     * // #if ANDROID
     * // Todo: Add generic MonoGame logging interface
     * Android.Util.Log.Debug("MonoGame", "MonoGameGLException at " + location + " - " +
     * ex.Message);
     * // #else
     * Debug.WriteLine("MonoGameGLException at " + location + " - " + ex.Message);
     * // #endif
     * }
     * }
     * // #endif
     * }
     * 
     * 
     * @SuppressWarnings("serial")
     * class MonoGameGLException extends Exception
     * {
     * public MonoGameGLException(String message)
     * {
     * super (message);
     * }
     * }
     */
}
