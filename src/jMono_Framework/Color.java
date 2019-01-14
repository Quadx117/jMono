package jMono_Framework;

import jMono_Framework.math.MathHelper;
import jMono_Framework.math.Vector3;
import jMono_Framework.math.Vector4;

// C# struct
/**
 * Describes a 32-bit packed color.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class Color // implements IEquatable<Color>
{
    /**
     * The color TransparentBlack. In the ABGR space.
     * A:0, B:0, G:0, R:0
     */
    private final static Color TransparentBlack = new Color(0);

    /**
     * The color Transparent. In the ABGR space.
     * A:0, B:0, G:0, R:0
     */
    private final static Color Transparent = new Color(0);

    /**
     * The color AliceBlue. In the ABGR space.
     * A:255, B:255, G:248, R:240
     */
    private final static Color AliceBlue = new Color(0xfffff8f0);

    /**
     * The color AntiqueWhite. In the ABGR space.
     * A:255, B:215, G:235, R:250
     */
    private final static Color AntiqueWhite = new Color(0xffd7ebfa);

    /**
     * The color Aqua. In the ABGR space.
     * A:255, B:255, G:255, R:0
     */
    private final static Color Aqua = new Color(0xffffff00);

    /**
     * The color Aquamarine. In the ABGR space.
     * A:255, B:212, G:255, R:127
     */
    private final static Color Aquamarine = new Color(0xffd4ff7f);

    /**
     * The color Azure. In the ABGR space.
     * A:255, B:255, G:255, R:240
     */
    private final static Color Azure = new Color(0xfffffff0);

    /**
     * The color Beige. In the ABGR space.
     * A:255, B:220, G:245, R:245
     */
    private final static Color Beige = new Color(0xffdcf5f5);

    /**
     * The color Bisque. In the ABGR space.
     * A:255, B:196 ,G:228, R:255
     */
    private final static Color Bisque = new Color(0xffc4e4ff);

    /**
     * The color Black. In the ABGR space.
     * A:255, B:0, G:0, R:0
     */
    private final static Color Black = new Color(0xff000000);

    /**
     * The color BlanchedAlmond. In the ABGR space.
     * A:255, B:205, G:235, R:255
     */
    private final static Color BlanchedAlmond = new Color(0xffcdebff);

    /**
     * The color Blue. In the ABGR space.
     * A:255, B:255 ,G:0, R:0
     */
    private final static Color Blue = new Color(0xffff0000);

    /**
     * The color BlueViolet. In the ABGR space.
     * A:255, B:226, G:43, R:138
     */
    private final static Color BlueViolet = new Color(0xffe22b8a);

    /**
     * The color Brown. In the ABGR space.
     * A:255, B:42, G:42, R:165
     */
    private final static Color Brown = new Color(0xff2a2aa5);

    /**
     * The color BurlyWood. In the ABGR space.
     * A:255, B:135, G:184, R:222
     */
    private final static Color BurlyWood = new Color(0xff87b8de);

    /**
     * The color CadetBlue. In the ABGR space.
     * A:255, B:160, G:158, R:95
     */
    private final static Color CadetBlue = new Color(0xffa09e5f);

    /**
     * The color Chartreuse. In the ABGR space.
     * A:255, B:0, G:255, R:127
     */
    private final static Color Chartreuse = new Color(0xff00ff7f);

    /**
     * The color Chocolate. In the ABGR space.
     * A:255, B:30, G:105, R:210
     */
    private final static Color Chocolate = new Color(0xff1e69d2);

    /**
     * The color Coral. In the ABGR space.
     * A:255, B:80, G:127, R:255
     */
    private final static Color Coral = new Color(0xff507fff);

    /**
     * The color CornflowerBlue. In the ABGR space.
     * A:255, B:237, G:149, R:100
     */
    private final static Color CornflowerBlue = new Color(0xffed9564);

    /**
     * The color Cornsilk. In the ABGR space.
     * A:255, B:220, G:248, R:255
     */
    private final static Color Cornsilk = new Color(0xffdcf8ff);

    /**
     * The color Crimson. In the ABGR space.
     * A:255, B:60, G:20, R:220
     */
    private final static Color Crimson = new Color(0xff3c14dc);

    /**
     * The color Cyan. In the ABGR space.
     * A:255, B:255, G:255, R:0
     */
    private final static Color Cyan = new Color(0xffffff00);

    /**
     * The color DarkBlue. In the ABGR space.
     * A:255, B:139, G:0, R:0
     */
    private final static Color DarkBlue = new Color(0xff8b0000);

    /**
     * The color DarkCyan. In the ABGR space.
     * A:255, B:139, G:139, R:0
     */
    private final static Color DarkCyan = new Color(0xff8b8b00);

    /**
     * The color DarkGoldenrod. In the ABGR space.
     * A:255, B:11, G:134, R:184
     */
    private final static Color DarkGoldenrod = new Color(0xff0b86b8);

    /**
     * The color DarkGray. In the ABGR space.
     * A:255, B:169, G:169, R:169
     */
    private final static Color DarkGray = new Color(0xffa9a9a9);

    /**
     * The color DarkGreen. In the ABGR space.
     * A:255, B:0, G:100, R:0
     */
    private final static Color DarkGreen = new Color(0xff006400);

    /**
     * The color DarkKhaki. In the ABGR space.
     * A:255, B:107, G:183, R:189
     */
    private final static Color DarkKhaki = new Color(0xff6bb7bd);

    /**
     * The color DarkMagenta. In the ABGR space.
     * A:255, B:139, G:0, R:139
     */
    private final static Color DarkMagenta = new Color(0xff8b008b);

    /**
     * The color DarkOliveGreen. In the ABGR space.
     * A:255, B:47, G:107, R:85
     */
    private final static Color DarkOliveGreen = new Color(0xff2f6b55);

    /**
     * The color DarkOrange. In the ABGR space.
     * A:255, B:0, G:140, R:255
     */
    private final static Color DarkOrange = new Color(0xff008cff);

    /**
     * The color DarkOrchid. In the ABGR space.
     * A:255, B:204, G:50, R:153
     */
    private final static Color DarkOrchid = new Color(0xffcc3299);

    /**
     * The color DarkRed. In the ABGR space.
     * A:255, B:0, G:0,R:139
     */
    private final static Color DarkRed = new Color(0xff00008b);

    /**
     * The color DarkSalmon. In the ABGR space.
     * A:255, B:122, G:150, R:233
     */
    private final static Color DarkSalmon = new Color(0xff7a96e9);

    /**
     * The color DarkSeaGreen. In the ABGR space.
     * A:255, B:139, G:188, R:143
     */
    private final static Color DarkSeaGreen = new Color(0xff8bbc8f);

    /**
     * The color DarkSlateBlue. In the ABGR space.
     * A:255, B:139, G:61, R:72
     */
    private final static Color DarkSlateBlue = new Color(0xff8b3d48);

    /**
     * The color DarkSlateGray. In the ABGR space.
     * A:255, B:79, G:79, R:47
     */
    private final static Color DarkSlateGray = new Color(0xff4f4f2f);

    /**
     * The color DarkTurquoise. In the ABGR space.
     * A:255, B:209, G:206, R:0
     */
    private final static Color DarkTurquoise = new Color(0xffd1ce00);

    /**
     * The color DarkViolet. In the ABGR space.
     * A:255, B:211, G:0, R:148
     */
    private final static Color DarkViolet = new Color(0xffd30094);

    /**
     * The color DeepPink. In the ABGR space.
     * A:255, B:147, G:20, R:255
     */
    private final static Color DeepPink = new Color(0xff9314ff);

    /**
     * The color DeepSkyBlue. In the ABGR space.
     * A:255, B:255, G:191, R:0
     */
    private final static Color DeepSkyBlue = new Color(0xffffbf00);

    /**
     * The color DimGray. In the ABGR space.
     * A:255, B:105, G:105, R:105
     */
    private final static Color DimGray = new Color(0xff696969);

    /**
     * The color DodgerBlue. In the ABGR space.
     * A:255, B:255, G:144, R:30
     */
    private final static Color DodgerBlue = new Color(0xffff901e);

    /**
     * The color Firebrick. In the ABGR space.
     * A:255, B:34, G:34, R:178
     */
    private final static Color Firebrick = new Color(0xff2222b2);

    /**
     * The color FloralWhite. In the ABGR space.
     * A:255, B:240, G:250, R:255
     */
    private final static Color FloralWhite = new Color(0xfff0faff);

    /**
     * The color ForestGreen. In the ABGR space.
     * A:255, B:34, G:139,R:34
     */
    private final static Color ForestGreen = new Color(0xff228b22);

    /**
     * The color Fuchsia. In the ABGR space.
     * A:255, B:255, G:0, R:255
     */
    private final static Color Fuchsia = new Color(0xffff00ff);

    /**
     * The color Gainsboro. In the ABGR space.
     * A:255, B:220, G:220, R:220
     */
    private final static Color Gainsboro = new Color(0xffdcdcdc);

    /**
     * The color GhostWhite. In the ABGR space.
     * A:255, B:255, G:248, R:248
     */
    private final static Color GhostWhite = new Color(0xfffff8f8);

    /**
     * The color Goldenrod. In the ABGR space.
     * A:255, B:0, G:215, R:255
     */
    private final static Color Gold = new Color(0xff00d7ff);

    /**
     * The color Goldenrod. In the ABGR space.
     * A:255, B:32, G:165, R:218
     */
    private final static Color Goldenrod = new Color(0xff20a5da);

    /**
     * The color Gray. In the ABGR space.
     * A:255, B:128, G:128, R:128
     */
    private final static Color Gray = new Color(0xff808080);

    /**
     * The color Green. In the ABGR space.
     * A:255, B:0, G:128, R:0
     */
    private final static Color Green = new Color(0xff008000);

    /**
     * The color GreenYellow. In the ABGR space.
     * A:255, B:47, G:255, R:173
     */
    private final static Color GreenYellow = new Color(0xff2fffad);

    /**
     * The color Honeydew. In the ABGR space.
     * A:255, B:240, G:255, R:240
     */
    private final static Color Honeydew = new Color(0xfff0fff0);

    /**
     * The color HotPink. In the ABGR space.
     * A:255, B:180, G:105, R:255
     */
    private final static Color HotPink = new Color(0xffb469ff);

    /**
     * The color IndianRed. In the ABGR space.
     * A:255, B:92, G:92, R:205
     */
    private final static Color IndianRed = new Color(0xff5c5ccd);

    /**
     * The color Indigo. In the ABGR space.
     * A:255, B:130, G:0, R:75
     */
    private final static Color Indigo = new Color(0xff82004b);

    /**
     * The color Ivory. In the ABGR space.
     * A:255, B:240, G:255, R:255
     */
    private final static Color Ivory = new Color(0xfff0ffff);

    /**
     * The color Khaki. In the ABGR space.
     * A:255, B:140, G:230, R:240
     */
    private final static Color Khaki = new Color(0xff8ce6f0);

    /**
     * The color Lavender. In the ABGR space.
     * A:255, B:250, G:230, R:230
     */
    private final static Color Lavender = new Color(0xfffae6e6);

    /**
     * The color LavenderBlush. In the ABGR space.
     * A:255, B:245, G:240, R:255
     */
    private final static Color LavenderBlush = new Color(0xfff5f0ff);

    /**
     * The color LawnGreen. In the ABGR space.
     * A:255, B:0, G:252, R:124
     */
    private final static Color LawnGreen = new Color(0xff00fc7c);

    /**
     * The color LemonChiffon. In the ABGR space.
     * A:255, B:205, G:250, R:255
     */
    private final static Color LemonChiffon = new Color(0xffcdfaff);

    /**
     * The color LightBlue. In the ABGR space.
     * A:255, B:230, G:216, R:173
     */
    private final static Color LightBlue = new Color(0xffe6d8ad);

    /**
     * The color LightCoral. In the ABGR space.
     * A:255, B:128, G:128, R:240
     */
    private final static Color LightCoral = new Color(0xff8080f0);

    /**
     * The color LightCyan. In the ABGR space.
     * A:255, B:255, G:255, R:224
     */
    private final static Color LightCyan = new Color(0xffffffe0);

    /**
     * The color LightGoldenrodYellow. In the ABGR space.
     * A:255, B:210, G:250, R:250
     */
    private final static Color LightGoldenrodYellow = new Color(0xffd2fafa);

    /**
     * The color LightGray. In the ABGR space.
     * A:255, B:211, G:211, R:211
     */
    private final static Color LightGray = new Color(0xffd3d3d3);

    /**
     * The color LightGreen. In the ABGR space.
     * A:255, B:144, G:238, R:144
     */
    private final static Color LightGreen = new Color(0xff90ee90);

    /**
     * The color LightPink. In the ABGR space.
     * A:255, B:193, G:182, R:255
     */
    private final static Color LightPink = new Color(0xffc1b6ff);

    /**
     * The color LightSalmon. In the ABGR space.
     * A:255, B:122, G:160, R:255
     */
    private final static Color LightSalmon = new Color(0xff7aa0ff);

    /**
     * The color LightSeaGreen. In the ABGR space.
     * A:255, B:170, G:178, R:32
     */
    private final static Color LightSeaGreen = new Color(0xffaab220);

    /**
     * The color LightSkyBlue. In the ABGR space.
     * A:255, B:250, G:206, R:135
     */
    private final static Color LightSkyBlue = new Color(0xffface87);

    /**
     * The color LightSlateGray. In the ABGR space.
     * A:255, B:153, G:136, R:119
     */
    private final static Color LightSlateGray = new Color(0xff998877);

    /**
     * The color LightSteelBlue. In the ABGR space.
     * A:255, B:222, G:196, R:176
     */
    private final static Color LightSteelBlue = new Color(0xffdec4b0);

    /**
     * The color LightYellow. In the ABGR space.
     * A:255, B:224, G:255, R:255
     */
    private final static Color LightYellow = new Color(0xffe0ffff);

    /**
     * The color Lime. In the ABGR space.
     * A:255, B:0, G:255, R:0
     */
    private final static Color Lime = new Color(0xff00ff00);

    /**
     * The color LimeGreen. In the ABGR space.
     * A:255, B:50, G:205, R:50
     */
    private final static Color LimeGreen = new Color(0xff32cd32);

    /**
     * The color Linen. In the ABGR space.
     * A:255, B:230, G:240, R:250
     */
    private final static Color Linen = new Color(0xffe6f0fa);

    /**
     * The color Magenta. In the ABGR space.
     * A:255, B:255, G:0, R:255
     */
    private final static Color Magenta = new Color(0xffff00ff);

    /**
     * The color Maroon. In the ABGR space.
     * A:255, B:0, G:0, R:128
     */
    private final static Color Maroon = new Color(0xff000080);

    /**
     * The color MediumAquamarine. In the ABGR space.
     * A:255, B:170, G:205, R:102
     */
    private final static Color MediumAquamarine = new Color(0xffaacd66);

    /**
     * The color MediumBlue. In the ABGR space.
     * A:255, B:205, G:0, R:0
     */
    private final static Color MediumBlue = new Color(0xffcd0000);

    /**
     * The color MediumOrchid. In the ABGR space.
     * A:255, B:211, G:85, R:186
     */
    private final static Color MediumOrchid = new Color(0xffd355ba);

    /**
     * The color MediumPurple. In the ABGR space.
     * A:255, B:219, G:112, R:147
     */
    private final static Color MediumPurple = new Color(0xffdb7093);

    /**
     * The color MediumSeaGreen. In the ABGR space.
     * A:255, B:113, G:179, R:60
     */
    private final static Color MediumSeaGreen = new Color(0xff71b33c);

    /**
     * The color MediumSlateBlue. In the ABGR space.
     * A:255, B:238, G:104, R:123
     */
    private final static Color MediumSlateBlue = new Color(0xffee687b);

    /**
     * The color MediumSpringGreen. In the ABGR space.
     * A:255, B:154, G:250, R:0
     */
    private final static Color MediumSpringGreen = new Color(0xff9afa00);

    /**
     * The color MediumTurquoise. In the ABGR space.
     * A:255, B:204, G:209, R:72
     */
    private final static Color MediumTurquoise = new Color(0xffccd148);

    /**
     * The color MediumVioletRed. In the ABGR space.
     * A:255, B:133, G:21, R:199
     */
    private final static Color MediumVioletRed = new Color(0xff8515c7);

    /**
     * The color MidnightBlue. In the ABGR space.
     * A:255, B:112, G:25, R:25
     */
    private final static Color MidnightBlue = new Color(0xff701919);

    /**
     * The color MintCream. In the ABGR space.
     * A:255, B:250, G:255, R:245
     */
    private final static Color MintCream = new Color(0xfffafff5);

    /**
     * The color MistyRose. In the ABGR space.
     * A:255, B:225, G:228, R:255
     */
    private final static Color MistyRose = new Color(0xffe1e4ff);

    /**
     * The color Moccasin. In the ABGR space.
     * A:255, B:181, G:228, R:255
     */
    private final static Color Moccasin = new Color(0xffb5e4ff);

    /**
     * The color Moccasin. In the ABGR space.
     * A:255, B:181, G:228, R:255
     */
    private final static Color MonoGameOrange = new Color(0xff003ce7);

    /**
     * The color NavajoWhite. In the ABGR space.
     * A:255, B:173, G:222, R:255
     */
    private final static Color NavajoWhite = new Color(0xffaddeff);

    /**
     * The color Navy. In the ABGR space.
     * A:255, B:128, G:0, R:0
     */
    private final static Color Navy = new Color(0xff800000);

    /**
     * The color OldLace. In the ABGR space.
     * A:255, B:230, G:245,R:253
     */
    private final static Color OldLace = new Color(0xffe6f5fd);

    /**
     * The color Olive. In the ABGR space.
     * A:255, B:0, G:128, R:128
     */
    private final static Color Olive = new Color(0xff008080);

    /**
     * The color OliveDrab. In the ABGR space.
     * A:255, B:35, G:142, R:107
     */
    private final static Color OliveDrab = new Color(0xff238e6b);

    /**
     * The color Orange. In the ABGR space.
     * A:255, B:0, G:165, R:255
     */
    private final static Color Orange = new Color(0xff00a5ff);

    /**
     * The color OrangeRed. In the ABGR space.
     * A:255, B:0, G:69, R:255
     */
    private final static Color OrangeRed = new Color(0xff0045ff);

    /**
     * The color Orchid. In the ABGR space.
     * A:255, B:214, G:112, R:218
     */
    private final static Color Orchid = new Color(0xffd670da);

    /**
     * The color PaleGoldenrod. In the ABGR space.
     * A:255, B:170, G:232, R:238
     */
    private final static Color PaleGoldenrod = new Color(0xffaae8ee);

    /**
     * The color PaleGreen. In the ABGR space.
     * A:255, B:152, G:251, R:152
     */
    private final static Color PaleGreen = new Color(0xff98fb98);

    /**
     * The color PaleTurquoise. In the ABGR space.
     * A:255, B:238, G:238, R:175
     */
    private final static Color PaleTurquoise = new Color(0xffeeeeaf);

    /**
     * The color PaleVioletRed. In the ABGR space.
     * A:255, B:147, G:112, R:219
     */
    private final static Color PaleVioletRed = new Color(0xff9370db);

    /**
     * The color PapayaWhip. In the ABGR space.
     * A:255, B:213, G:239, R:255
     */
    private final static Color PapayaWhip = new Color(0xffd5efff);

    /**
     * The color PeachPuff. In the ABGR space.
     * A:255, B:185, G:218, R:255
     */
    private final static Color PeachPuff = new Color(0xffb9daff);

    /**
     * The color Peru. In the ABGR space.
     * A:255, B:63, G:133, R:205
     */
    private final static Color Peru = new Color(0xff3f85cd);

    /**
     * The color Pink. In the ABGR space.
     * A:255, B:203, G:192, R:255
     */
    private final static Color Pink = new Color(0xffcbc0ff);

    /**
     * The color Plum. In the ABGR space.
     * A:255, B:221, G:160, R:221
     */
    private final static Color Plum = new Color(0xffdda0dd);

    /**
     * The color PowderBlue. In the ABGR space.
     * A:255, B:230, G:224, R:176
     */
    private final static Color PowderBlue = new Color(0xffe6e0b0);

    /**
     * The color Purple. In the ABGR space.
     * A:255, B:128, G:0, R:128
     */
    private final static Color Purple = new Color(0xff800080);

    /**
     * The color Red. In the ABGR space.
     * A:255, B:0, G:0, R:255
     */
    private final static Color Red = new Color(0xff0000ff);

    /**
     * The color RosyBrown. In the ABGR space.
     * A:255, B:143, G:143, R:188
     */
    private final static Color RosyBrown = new Color(0xff8f8fbc);

    /**
     * The color RoyalBlue. In the ABGR space.
     * A:255, B:225, G:105, R:65
     */
    private final static Color RoyalBlue = new Color(0xffe16941);

    /**
     * The color SaddleBrown. In the ABGR space.
     * A:255, B:19, G:69, R:139
     */
    private final static Color SaddleBrown = new Color(0xff13458b);

    /**
     * The color Salmon. In the ABGR space.
     * A:255, B:114, G:128, R:250
     */
    private final static Color Salmon = new Color(0xff7280fa);

    /**
     * The color SandyBrown. In the ABGR space.
     * A:255, B:96, G:164, R:244
     */
    private final static Color SandyBrown = new Color(0xff60a4f4);

    /**
     * The color SeaGreen. In the ABGR space.
     * A:255, B:87, G:139, R:46
     */
    private final static Color SeaGreen = new Color(0xff578b2e);

    /**
     * The color SeaShell. In the ABGR space.
     * A:255, B:238, G:245, R:255
     */
    private final static Color SeaShell = new Color(0xffeef5ff);

    /**
     * The color Sienna. In the ABGR space.
     * A:255, B:45, G:82, R:160
     */
    private final static Color Sienna = new Color(0xff2d52a0);

    /**
     * The color Silver. In the ABGR space.
     * A:255, B:192, G:192, R:192
     */
    private final static Color Silver = new Color(0xffc0c0c0);

    /**
     * The color SkyBlue. In the ABGR space.
     * A:255, B:235, G:206, R:135
     */
    private final static Color SkyBlue = new Color(0xffebce87);

    /**
     * The color SlateBlue. In the ABGR space.
     * A:255, B:205, G:90, R:106
     */
    private final static Color SlateBlue = new Color(0xffcd5a6a);

    /**
     * The color SlateGray. In the ABGR space.
     * A:255, B:144, G:128, R:112
     */
    private final static Color SlateGray = new Color(0xff908070);

    /**
     * The color Snow. In the ABGR space.
     * A:255, B:250, G:250, R:255
     */
    private final static Color Snow = new Color(0xfffafaff);

    /**
     * The color SpringGreen. In the ABGR space.
     * A:255, B:127, G:255, R:0
     */
    private final static Color SpringGreen = new Color(0xff7fff00);

    /**
     * The color SteelBlue. In the ABGR space.
     * A:255, B:180, G:130, R:70
     */
    private final static Color SteelBlue = new Color(0xffb48246);

    /**
     * The color Tan. In the ABGR space.
     * A:255, B:140, G:180, R:210
     */
    private final static Color Tan = new Color(0xff8cb4d2);

    /**
     * The color Teal. In the ABGR space.
     * A:255, B:128, G:128, R:0
     */
    private final static Color Teal = new Color(0xff808000);

    /**
     * The color Thistle. In the ABGR space.
     * A:255, B:216, G:191, R:216
     */
    private final static Color Thistle = new Color(0xffd8bfd8);

    /**
     * The color Tomato. In the ABGR space.
     * A:255, B:71, G:99, R:255
     */
    private final static Color Tomato = new Color(0xff4763ff);

    /**
     * The color Turquoise. In the ABGR space.
     * A:255, B:208, G:224, R:64
     */
    private final static Color Turquoise = new Color(0xffd0e040);

    /**
     * The color Violet. In the ABGR space.
     * A:255, B:238, G:130, R:238
     */
    private final static Color Violet = new Color(0xffee82ee);

    /**
     * The color Wheat. In the ABGR space.
     * A:255, B:179, G:222, R:245
     */
    private final static Color Wheat = new Color(0xffb3def5);

    /**
     * The color White. In the ABGR space.
     * A:255, B:255, G:255, R:255
     */
    private final static Color White = new Color(0xffffffff);

    /**
     * The color WhiteSmoke. In the ABGR space.
     * A:255, B:245, G:245, R:245
     */
    private final static Color WhiteSmoke = new Color(0xfff5f5f5);

    /**
     * The color Yellow. In the ABGR space.
     * A:255, B:0, G:255, R:255
     */
    private final static Color Yellow = new Color(0xff00ffff);

    /**
     * The color YellowGreen. In the ABGR space.
     * A:255, B:50, G:205, R:154
     */
    private final static Color YellowGreen = new Color(0xff32cd9a);

    // TODO(Eric): _packedValue is a uint, test to see if it matters (use long instead ?)
    // Stored as ABGR with R in the least significant byte:
    // @formatter:off
    // |-------|-------|-------|-------
    // A       B       G       R
    // @formatter:on
    private int _packedValue;

    // NOTE(Eric): Added this since it is provided by default for struct in C#
    /**
     * Creates an RGBA color with all components set to 0.
     */
    public Color()
    {
        _packedValue = 0;
    }

    // NOTE(Eric): Added this since C# struct are copied on assignment. This makes it easier
    // to mimick that behavior by creating a new Color from another.
    public Color(Color color)
    {
        this(color, color.getAlpha());
    }

    /**
     * Creates an ABGR color with the specified combined RGBA value consisting
     * of the alpha component in bits 24-31, the red component in bits 16-23,
     * the green component in bits 8-15, and the blue component in bits 0-7.
     *
     * @param packedValue
     *        the combined ABGR components
     */
    public Color(int packedValue)
    {
        this(packedValue, true);
    }

    /**
     * Creates an RGBA color from the XYZW unit length components of a vector.
     * 
     * @param color
     *        A {@link Vector4} representing the color.
     */
    public Color(Vector4 color)
    {
        this((int) (color.x * 255), (int) (color.y * 255), (int) (color.z * 255), (int) (color.w * 255));
    }

    /**
     * Creates an RGBA color from the XYZ unit length components of a vector. Alpha value will be
     * opaque.
     * 
     * @param color
     *        A {@link Vector3} representing the color.
     */
    public Color(Vector3 color)
    {
        this((int) (color.x * 255), (int) (color.y * 255), (int) (color.z * 255));
    }

    /**
     * Creates an RGBA color from a {@link Color} and an alpha value.
     * 
     * @param color
     *        A {@code Color} for the RGB values of new the {@code Color} instance.
     * @param alpha
     *        The alpha component value from 0 to 255.
     */
    public Color(Color color, int alpha)
    {
        if ((alpha & 0xFFFFFF00) != 0)
        {
            int clampedA = (int) MathHelper.clamp(alpha, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE);

            _packedValue = (color._packedValue & 0x00FFFFFF) | (clampedA << 24);
        }
        else
        {
            _packedValue = (color._packedValue & 0x00FFFFFF) | (alpha << 24);
        }
    }

    /**
     * Creates an RGBA color from color and alpha value.
     * 
     * @param color
     *        A {@code Color} for the RGB values of the new {@code Color} instance.
     * @param alpha
     *        Alpha component value from 0.0f to 1.0f.
     */
    public Color(Color color, float alpha)
    {
        this(color, (int) (alpha * 255));
    }

    /**
     * Creates an opaque ABGR color with the specified red, green, and blue
     * values in the range (0.0 - 1.0). Alpha is defaulted to 1.0.
     *
     * @param r
     *        Red component value from 0.0f to 1.0f.
     * @param g
     *        Green component value from 0.0f to 1.0f.
     * @param b
     *        Blue component value from 0.0f to 1.0f.
     */
    public Color(float r, float g, float b)
    {
        this((int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    /**
     * Creates an opaque ABGR color with the specified red, green, blue, and alpha values in the
     * range (0.0 - 1.0).
     *
     * @param r
     *        Red component value from 0.0f to 1.0f.
     * @param g
     *        Green component value from 0.0f to 1.0f.
     * @param b
     *        Blue component value from 0.0f to 1.0f.
     * @param alpha
     *        Alpha component value from 0.0f to 1.0f.
     */
    public Color(float r, float g, float b, float alpha)
    {
        this((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (alpha * 255));
    }

    /**
     * Creates an opaque ABGR color with the specified red, green, and blue values in the range
     * (0 - 255). Alpha is defaulted to 255.
     *
     * @param r
     *        Red component value from 0 to 255.
     * @param g
     *        Green component value from 0 to 255.
     * @param b
     *        Blue component value from 0 to 255.
     */
    public Color(int r, int g, int b)
    {
        _packedValue = 0xFF000000; // A = 255

        if (((r | g | b) & 0xFFFFFF00) != 0)
        {
            int clampedR = (int) MathHelper.clamp(r, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE);
            int clampedG = (int) MathHelper.clamp(g, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE);
            int clampedB = (int) MathHelper.clamp(b, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE);

            _packedValue |= (clampedB << 16) | (clampedG << 8) | (clampedR);
        }
        else
        {
            _packedValue |= ((int) b << 16) | ((int) g << 8) | ((int) r);
        }
    }

    /**
     * Creates an opaque ABGR color with the specified red, green, and blue values in the range
     * (0 - 255).
     *
     * @param r
     *        Red component value from 0 to 255.
     * @param g
     *        Green component value from 0 to 255.
     * @param b
     *        Blue component value from 0 to 255.
     * @param alpha
     *        Alpha component value from 0 to 255.
     */
    public Color(int r, int g, int b, int alpha)
    {
        if (((r | g | b | alpha) & 0xFFFFFF00) != 0)
        {
            int clampedR = (int) MathHelper.clamp(r, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE);
            int clampedG = (int) MathHelper.clamp(g, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE);
            int clampedB = (int) MathHelper.clamp(b, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE);
            int clampedA = (int) MathHelper.clamp(alpha, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE);

            _packedValue = (clampedA << 24) | (clampedB << 16) | (clampedG << 8) | (clampedR);
        }
        else
        {
            _packedValue = ((int) alpha << 24) | ((int) b << 16) | ((int) g << 8) | ((int) r);
        }
    }

    /**
     * Creates an RGBA color from scalars representing red, green, blue and alpha values.
     * <p>
     * This overload sets the values directly without clamping, and may therefore be faster than the
     * other overloads.
     * 
     * @param r
     *        Red component value from 0 to 255.
     * @param g
     *        Green component value from 0 to 255.
     * @param b
     *        Blue component value from 0 to 255.
     * @param alpha
     *        Alpha component value from 0 to 255.
     */
    public Color(short r, short g, short b, short alpha)
    {
        _packedValue = ((int) alpha << 24) | ((int) b << 16) | ((int) g << 8) | (r);
    }

    /**
     * Creates an ABGR color with the specified combined ABGR value consisting
     * of the alpha component in bits 24-31, the blue component in bits 16-23,
     * the green component in bits 8-15, and the red component in bits 0-7.
     * If the <code>hasalpha</code> argument is <code>false</code>, alpha
     * is defaulted to 255.
     *
     * @param abgr
     *        the combined RGBA components
     * @param hasalpha
     *        <code>true</code> if the alpha bits are valid; <code>false</code> otherwise
     */
    public Color(int abgr, boolean hasalpha)
    {
        if (hasalpha)
        {
            _packedValue = abgr;
        }
        else
        {
            _packedValue = 0xff000000 | abgr;
        }
    }

    /**
     * Returns the blue component of this {@link Color} in the range 0-255 in the ABGR space.
     * 
     * @return The blue component of this {@code Color}.
     */
    public short getBlue()
    {
        return (short) (this._packedValue >> 16 & 0xff);
    }

    /**
     * Sets the blue component of this {@link Color} to the specified value.
     * 
     * @param value
     *        The new value for the blue component.
     */
    public void setBlue(short value)
    {
        this._packedValue = (this._packedValue & 0xff00ffff) | ((int) value << 16);
    }

    /**
     * Returns the green component of this {@link Color} in the range 0-255 in the ABGR space.
     * 
     * @return The green component of this {@code Color}.
     */
    public short getGreen()
    {
        return (short) (this._packedValue >> 8 & 0xff);
    }

    /**
     * Sets the green component of this {@link Color} to the specified value.
     * 
     * @param value
     *        The new value for the green component.
     */
    public void setGreen(short value)
    {
        this._packedValue = (this._packedValue & 0xffff00ff) | ((int) value << 8);
    }

    /**
     * Returns the red component of this {@link Color} in the range 0-255 in the ABGR space.
     * 
     * @return The red component of this {@code Color}.
     */
    public short getRed()
    {
        return (short) (this._packedValue & 0xff);
    }

    /**
     * Sets the red component of this {@link Color} to the specified value.
     * 
     * @param value
     *        The new value for the red component.
     */
    public void setRed(short value)
    {
        this._packedValue = (this._packedValue & 0xffffff00) | value;
    }

    /**
     * Returns the alpha component of this {@link Color} in the range 0-255.
     * 
     * @return The alpha component of this {@code Color}.
     */
    public short getAlpha()
    {
        return (short) (this._packedValue >> 24 & 0xff);
    }

    /**
     * Sets the alpha component of this {@link Color} to the specified value.
     * 
     * @param value
     *        The new value for the alpha component.
     */
    public void setAlpha(short value)
    {
        this._packedValue = (this._packedValue & 0x00ffffff) | ((int) value << 24);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The result is <code>true</code> if and only if the argument is not <code>null</code> and is a
     * <code>Color</code> object that has the same red, green, blue, and alpha values as this
     * object.
     * 
     * @param obj
     *        the object to test for equality with this <code>Color</code>
     * @return <code>true</code> if the objects are the same; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj.getClass() != this.getClass())
        {
            return false;
        }
        return this.equals((Color) obj);
    }

    // #region IEquatable<Color> Members

    /**
     * Indicates whether the current instance is equal to specified {@link Color}.
     * 
     * @param other
     *        The {@link Color} to test for equality with this {@code Color}.
     * @return {@code true} if the instances are the same; {@code false} otherwise.
     */
    public boolean equals(Color other)
    {
        return this.getPackedValue() == other.getPackedValue();
    }

    // #endregion

    /**
     * Indicates whether some other {@link Object} is "not equal to" this one.
     * 
     * @param obj
     *        The reference {@code Object} with which to compare.
     * @return {@code false} if this object is the same as the specified argument; {@code true}
     *         otherwise.
     * @see #equals(Object)
     */
    public boolean notEquals(Object obj)
    {
        return !this.equals(obj);
    }

    /**
     * Computes the hash code value for this {@link Color}.
     * 
     * @return A hash code value for this {@code Color}.
     */
    @Override
    public int hashCode()
    {
        return Integer.hashCode(_packedValue);
    }

    // #region Color Bank

    /**
     * TransparentBlack color (R:0, G:0, B:0, A:0).
     * 
     * @return TransparentBlack color (R:0, G:0, B:0, A:0).
     */
    public static Color TransparentBlack()
    {
        return new Color(TransparentBlack);
    }

    /**
     * Transparent color (R:0, G:0, B:0, A:0).
     * 
     * @return Transparent color (R:0, G:0, B:0, A:0).
     */
    public static Color Transparent()
    {
        return new Color(Transparent);
    }

    /**
     * AliceBlue color (R:240, G:248, B:255, A:255).
     * 
     * @return AliceBlue color (R:240, G:248, B:255, A:255).
     */
    public static Color AliceBlue()
    {
        return new Color(AliceBlue);
    }

    /**
     * AntiqueWhite color (R:250, G:235, B:215, A:255).
     * 
     * @return AntiqueWhite color (R:250, G:235, B:215, A:255).
     */
    public static Color AntiqueWhite()
    {
        return new Color(AntiqueWhite);
    }

    /**
     * Aqua color (R:0, G:255, B:255, A:255).
     * 
     * @return Aqua color (R:0, G:255, B:255, A:255).
     */
    public static Color Aqua()
    {
        return new Color(Aqua);
    }

    /**
     * Aquamarine color (R:127, G:255, B:212, A:255).
     * 
     * @return Aquamarine color (R:127, G:255, B:212, A:255).
     */
    public static Color Aquamarine()
    {
        return new Color(Aquamarine);
    }

    /**
     * Azure color (R:240, G:255, B:255, A:255).
     * 
     * @return Azure color (R:240, G:255, B:255, A:255).
     */
    public static Color Azure()
    {
        return new Color(Azure);
    }

    /**
     * Beige color (R:245, G:245, B:220, A:255).
     * 
     * @return Beige color (R:245, G:245, B:220, A:255).
     */
    public static Color Beige()
    {
        return new Color(Beige);
    }

    /**
     * Bisque color (R:255, G:228, B:196, A:255).
     * 
     * @return Bisque color (R:255, G:228, B:196, A:255).
     */
    public static Color Bisque()
    {
        return new Color(Bisque);
    }

    /**
     * Black color (R:0, G:0, B:0, A:255).
     * 
     * @return Black color (R:0, G:0, B:0, A:255).
     */
    public static Color Black()
    {
        return new Color(Black);
    }

    /**
     * BlanchedAlmond color (R:255, G:235, B:205, A:255).
     * 
     * @return BlanchedAlmond color (R:255, G:235, B:205, A:255).
     */
    public static Color BlanchedAlmond()
    {
        return new Color(BlanchedAlmond);
    }

    /**
     * Blue color (R:0, G:0, B:255, A:255).
     * 
     * @return Blue color (R:0, G:0, B:255, A:255).
     */
    public static Color Blue()
    {
        return new Color(Blue);
    }

    /**
     * BlueViolet color (R:138, G:43, B:226, A:255).
     * 
     * @return BlueViolet color (R:138, G:43, B:226, A:255).
     */
    public static Color BlueViolet()
    {
        return new Color(BlueViolet);
    }

    /**
     * Brown color (R:165, G:42, B:42, A:255).
     * 
     * @return Brown color (R:165, G:42, B:42, A:255).
     */
    public static Color Brown()
    {
        return new Color(Brown);
    }

    /**
     * BurlyWood color (R:222, G:184, B:135, A:255).
     * 
     * @return BurlyWood color (R:222, G:184, B:135, A:255).
     */
    public static Color BurlyWood()
    {
        return new Color(BurlyWood);
    }

    /**
     * CadetBlue color (R:95, G:158, B:160, A:255).
     * 
     * @return CadetBlue color (R:95, G:158, B:160, A:255).
     */
    public static Color CadetBlue()
    {
        return new Color(CadetBlue);
    }

    /**
     * Chartreuse color (R:127, G:255, B:0, A:255).
     * 
     * @return Chartreuse color (R:127, G:255, B:0, A:255).
     */
    public static Color Chartreuse()
    {
        return new Color(Chartreuse);
    }

    /**
     * Chocolate color (R:210, G:105, B:30, A:255).
     * 
     * @return Chocolate color (R:210, G:105, B:30, A:255).
     */
    public static Color Chocolate()
    {
        return new Color(Chocolate);
    }

    /**
     * Coral color (R:255, G:127, B:80, A:255).
     * 
     * @return Coral color (R:255, G:127, B:80, A:255).
     */
    public static Color Coral()
    {
        return new Color(Coral);
    }

    /**
     * CornflowerBlue color (R:100, G:149, B:237, A:255).
     * 
     * @return CornflowerBlue color (R:100, G:149, B:237, A:255).
     */
    public static Color CornflowerBlue()
    {
        return new Color(CornflowerBlue);
    }

    /**
     * Cornsilk color (R:255, G:248, B:220, A:255).
     * 
     * @return Cornsilk color (R:255, G:248, B:220, A:255).
     */
    public static Color Cornsilk()
    {
        return new Color(Cornsilk);
    }

    /**
     * Crimson color (R:220, G:20, B:60, A:255).
     * 
     * @return Crimson color (R:220, G:20, B:60, A:255).
     */
    public static Color Crimson()
    {
        return new Color(Crimson);
    }

    /**
     * Cyan color (R:0, G:255, B:255, A:255).
     * 
     * @return Cyan color (R:0, G:255, B:255, A:255).
     */
    public static Color Cyan()
    {
        return new Color(Cyan);
    }

    /**
     * DarkBlue color (R:0, G:0, B:139, A:255).
     * 
     * @return DarkBlue color (R:0, G:0, B:139, A:255).
     */
    public static Color DarkBlue()
    {
        return new Color(DarkBlue);
    }

    /**
     * DarkCyan color (R:0, G:139, B:139, A:255).
     * 
     * @return DarkCyan color (R:0, G:139, B:139, A:255).
     */
    public static Color DarkCyan()
    {
        return new Color(DarkCyan);
    }

    /**
     * DarkGoldenrod color (R:184, G:134, B:11, A:255).
     * 
     * @return DarkGoldenrod color (R:184, G:134, B:11, A:255).
     */
    public static Color DarkGoldenrod()
    {
        return new Color(DarkGoldenrod);
    }

    /**
     * DarkGray color (R:169, G:169, B:169, A:255).
     * 
     * @return DarkGray color (R:169, G:169, B:169, A:255).
     */
    public static Color DarkGray()
    {
        return new Color(DarkGray);
    }

    /**
     * DarkGreen color (R:0, G:100, B:0, A:255).
     * 
     * @return DarkGreen color (R:0, G:100, B:0, A:255).
     */
    public static Color DarkGreen()
    {
        return new Color(DarkGreen);
    }

    /**
     * DarkKhaki color (R:189, G:183, B:107, A:255).
     * 
     * @return DarkKhaki color (R:189, G:183, B:107, A:255).
     */
    public static Color DarkKhaki()
    {
        return new Color(DarkKhaki);
    }

    /**
     * DarkMagenta color (R:139, G:0, B:139, A:255).
     * 
     * @return DarkMagenta color (R:139, G:0, B:139, A:255).
     */
    public static Color DarkMagenta()
    {
        return new Color(DarkMagenta);
    }

    /**
     * DarkOliveGreen color (R:85, G:107, B:47, A:255).
     * 
     * @return DarkOliveGreen color (R:85, G:107, B:47, A:255).
     */
    public static Color DarkOliveGreen()
    {
        return new Color(DarkOliveGreen);
    }

    /**
     * DarkOrange color (R:255, G:140, B:0, A:255).
     * 
     * @return DarkOrange color (R:255, G:140, B:0, A:255).
     */
    public static Color DarkOrange()
    {
        return new Color(DarkOrange);
    }

    /**
     * DarkOrchid color (R:153, G:50, B:204, A:255).
     * 
     * @return DarkOrchid color (R:153, G:50, B:204, A:255).
     */
    public static Color DarkOrchid()
    {
        return new Color(DarkOrchid);
    }

    /**
     * DarkRed color (R:139, G:0, B:0, A:255).
     * 
     * @return DarkRed color (R:139, G:0, B:0, A:255).
     */
    public static Color DarkRed()
    {
        return new Color(DarkRed);
    }

    /**
     * DarkSalmon color (R:233, G:150, B:122, A:255).
     * 
     * @return DarkSalmon color (R:233, G:150, B:122, A:255).
     */
    public static Color DarkSalmon()
    {
        return new Color(DarkSalmon);
    }

    /**
     * DarkSeaGreen color (R:143, G:188, B:139, A:255).
     * 
     * @return DarkSeaGreen color (R:143, G:188, B:139, A:255).
     */
    public static Color DarkSeaGreen()
    {
        return new Color(DarkSeaGreen);
    }

    /**
     * DarkSlateBlue color (R:72, G:61, B:139, A:255).
     * 
     * @return DarkSlateBlue color (R:72, G:61, B:139, A:255).
     */
    public static Color DarkSlateBlue()
    {
        return new Color(DarkSlateBlue);
    }

    /**
     * DarkSlateGray color (R:47, G:79, B:79, A:255).
     * 
     * @return DarkSlateGray color (R:47, G:79, B:79, A:255).
     */
    public static Color DarkSlateGray()
    {
        return new Color(DarkSlateGray);
    }

    /**
     * DarkTurquoise color (R:0, G:206, B:209, A:255).
     * 
     * @return DarkTurquoise color (R:0, G:206, B:209, A:255).
     */
    public static Color DarkTurquoise()
    {
        return new Color(DarkTurquoise);
    }

    /**
     * DarkViolet color (R:148, G:0, B:211, A:255).
     * 
     * @return DarkViolet color (R:148, G:0, B:211, A:255).
     */
    public static Color DarkViolet()
    {
        return new Color(DarkViolet);
    }

    /**
     * DeepPink color (R:255, G:20, B:147, A:255).
     * 
     * @return DeepPink color (R:255, G:20, B:147, A:255).
     */
    public static Color DeepPink()
    {
        return new Color(DeepPink);
    }

    /**
     * DeepSkyBlue color (R:0, G:191, B:255, A:255).
     * 
     * @return DeepSkyBlue color (R:0, G:191, B:255, A:255).
     */
    public static Color DeepSkyBlue()
    {
        return new Color(DeepSkyBlue);
    }

    /**
     * DimGray color (R:105, G:105, B:105, A:255).
     * 
     * @return DimGray color (R:105, G:105, B:105, A:255).
     */
    public static Color DimGray()
    {
        return new Color(DimGray);
    }

    /**
     * DodgerBlue color (R:30, G:144, B:255, A:255).
     * 
     * @return DodgerBlue color (R:30, G:144, B:255, A:255).
     */
    public static Color DodgerBlue()
    {
        return new Color(DodgerBlue);
    }

    /**
     * Firebrick color (R:178, G:34, B:34, A:255).
     * 
     * @return Firebrick color (R:178, G:34, B:34, A:255).
     */
    public static Color Firebrick()
    {
        return new Color(Firebrick);
    }

    /**
     * FloralWhite color (R:255, G:250, B:240, A:255).
     * 
     * @return FloralWhite color (R:255, G:250, B:240, A:255).
     */
    public static Color FloralWhite()
    {
        return new Color(FloralWhite);
    }

    /**
     * ForestGreen color (R:34, G:139, B:34, A:255).
     * 
     * @return ForestGreen color (R:34, G:139, B:34, A:255).
     */
    public static Color ForestGreen()
    {
        return new Color(ForestGreen);
    }

    /**
     * Fuchsia color (R:255, G:0, B:255, A:255).
     * 
     * @return Fuchsia color (R:255, G:0, B:255, A:255).
     */
    public static Color Fuchsia()
    {
        return new Color(Fuchsia);
    }

    /**
     * Gainsboro color (R:220, G:220, B:220, A:255).
     * 
     * @return Gainsboro color (R:220, G:220, B:220, A:255).
     */
    public static Color Gainsboro()
    {
        return new Color(Gainsboro);
    }

    /**
     * GhostWhite color (R:248, G:248, B:255, A:255).
     * 
     * @return GhostWhite color (R:248, G:248, B:255, A:255).
     */
    public static Color GhostWhite()
    {
        return new Color(GhostWhite);
    }

    /**
     * Gold color (R:255, G:215, B:0, A:255).
     * 
     * @return Gold color (R:255, G:215, B:0, A:255).
     */
    public static Color Gold()
    {
        return new Color(Gold);
    }

    /**
     * Goldenrod color (R:218, G:165, B:32, A:255).
     * 
     * @return Goldenrod color (R:218, G:165, B:32, A:255).
     */
    public static Color Goldenrod()
    {
        return new Color(Goldenrod);
    }

    /**
     * Gray color (R:128, G:128, B:128, A:255).
     * 
     * @return Gray color (R:128, G:128, B:128, A:255).
     */
    public static Color Gray()
    {
        return new Color(Gray);
    }

    /**
     * Green color (R:0, G:128, B:0, A:255).
     * 
     * @return Green color (R:0, G:128, B:0, A:255).
     */
    public static Color Green()
    {
        return new Color(Green);
    }

    /**
     * GreenYellow color (R:173, G:255, B:47, A:255).
     * 
     * @return GreenYellow color (R:173, G:255, B:47, A:255).
     */
    public static Color GreenYellow()
    {
        return new Color(GreenYellow);
    }

    /**
     * Honeydew color (R:240, G:255, B:240, A:255).
     * 
     * @return Honeydew color (R:240, G:255, B:240, A:255).
     */
    public static Color Honeydew()
    {
        return new Color(Honeydew);
    }

    /**
     * HotPink color (R:255, G:105, B:180, A:255).
     * 
     * @return HotPink color (R:255, G:105, B:180, A:255).
     */
    public static Color HotPink()
    {
        return new Color(HotPink);
    }

    /**
     * IndianRed color (R:205, G:92, B:92, A:255).
     * 
     * @return IndianRed color (R:205, G:92, B:92, A:255).
     */
    public static Color IndianRed()
    {
        return new Color(IndianRed);
    }

    /**
     * Indigo color (R:75, G:0, B:130, A:255).
     * 
     * @return Indigo color (R:75, G:0, B:130, A:255).
     */
    public static Color Indigo()
    {
        return new Color(Indigo);
    }

    /**
     * Ivory color (R:255, G:255, B:240, A:255).
     * 
     * @return Ivory color (R:255, G:255, B:240, A:255).
     */
    public static Color Ivory()
    {
        return new Color(Ivory);
    }

    /**
     * Khaki color (R:240, G:230, B:140, A:255).
     * 
     * @return Khaki color (R:240, G:230, B:140, A:255).
     */
    public static Color Khaki()
    {
        return new Color(Khaki);
    }

    /**
     * Lavender color (R:230, G:230, B:250, A:255).
     * 
     * @return Lavender color (R:230, G:230, B:250, A:255).
     */
    public static Color Lavender()
    {
        return new Color(Lavender);
    }

    /**
     * LavenderBlush color (R:255, G:240, B:245, A:255).
     * 
     * @return LavenderBlush color (R:255, G:240, B:245, A:255).
     */
    public static Color LavenderBlush()
    {
        return new Color(LavenderBlush);
    }

    /**
     * LawnGreen color (R:124, G:252, B:0, A:255).
     * 
     * @return LawnGreen color (R:124, G:252, B:0, A:255).
     */
    public static Color LawnGreen()
    {
        return new Color(LawnGreen);
    }

    /**
     * LemonChiffon color (R:255, G:250, B:205, A:255).
     * 
     * @return LemonChiffon color (R:255, G:250, B:205, A:255).
     */
    public static Color LemonChiffon()
    {
        return new Color(LemonChiffon);
    }

    /**
     * LightBlue color (R:173, G:216, B:230, A:255).
     * 
     * @return LightBlue color (R:173, G:216, B:230, A:255).
     */
    public static Color LightBlue()
    {
        return new Color(LightBlue);
    }

    /**
     * LightCoral color (R:240, G:128, B:128, A:255).
     * 
     * @return LightCoral color (R:240, G:128, B:128, A:255).
     */
    public static Color LightCoral()
    {
        return new Color(LightCoral);
    }

    /**
     * LightCyan color (R:224, G:255, B:255, A:255).
     * 
     * @return LightCyan color (R:224, G:255, B:255, A:255).
     */
    public static Color LightCyan()
    {
        return new Color(LightCyan);
    }

    /**
     * LightGoldenrodYellow color (R:250, G:250, B:210, A:255).
     * 
     * @return LightGoldenrodYellow color (R:250, G:250, B:210, A:255).
     */
    public static Color LightGoldenrodYellow()
    {
        return new Color(LightGoldenrodYellow);
    }

    /**
     * LightGray color (R:211, G:211, B:211, A:255).
     * 
     * @return LightGray color (R:211, G:211, B:211, A:255).
     */
    public static Color LightGray()
    {
        return new Color(LightGray);
    }

    /**
     * LightGreen color (R:144, G:238, B:144, A:255).
     * 
     * @return LightGreen color (R:144, G:238, B:144, A:255).
     */
    public static Color LightGreen()
    {
        return new Color(LightGreen);
    }

    /**
     * LightPink color (R:255, G:182, B:193, A:255).
     * 
     * @return LightPink color (R:255, G:182, B:193, A:255).
     */
    public static Color LightPink()
    {
        return new Color(LightPink);
    }

    /**
     * LightSalmon color (R:255, G:160, B:122, A:255).
     * 
     * @return LightSalmon color (R:255, G:160, B:122, A:255).
     */
    public static Color LightSalmon()
    {
        return new Color(LightSalmon);
    }

    /**
     * LightSeaGreen color (R:32, G:178, B:170, A:255).
     * 
     * @return LightSeaGreen color (R:32, G:178, B:170, A:255).
     */
    public static Color LightSeaGreen()
    {
        return new Color(LightSeaGreen);
    }

    /**
     * LightSkyBlue color (R:135, G:206, B:250, A:255).
     * 
     * @return LightSkyBlue color (R:135, G:206, B:250, A:255).
     */
    public static Color LightSkyBlue()
    {
        return new Color(LightSkyBlue);
    }

    /**
     * LightSlateGray color (R:119, G:136, B:153, A:255).
     * 
     * @return LightSlateGray color (R:119, G:136, B:153, A:255).
     */
    public static Color LightSlateGray()
    {
        return new Color(LightSlateGray);
    }

    /**
     * LightSteelBlue color (R:176, G:196, B:222, A:255).
     * 
     * @return LightSteelBlue color (R:176, G:196, B:222, A:255).
     */
    public static Color LightSteelBlue()
    {
        return new Color(LightSteelBlue);
    }

    /**
     * LightYellow color (R:255, G:255, B:224, A:255).
     * 
     * @return LightYellow color (R:255, G:255, B:224, A:255).
     */
    public static Color LightYellow()
    {
        return new Color(LightYellow);
    }

    /**
     * Lime color (R:0, G:255, B:0, A:255).
     * 
     * @return Lime color (R:0, G:255, B:0, A:255).
     */
    public static Color Lime()
    {
        return new Color(Lime);
    }

    /**
     * LimeGreen color (R:50, G:205, B:50, A:255).
     * 
     * @return LimeGreen color (R:50, G:205, B:50, A:255).
     */
    public static Color LimeGreen()
    {
        return new Color(LimeGreen);
    }

    /**
     * Linen color (R:250, G:240, B:230, A:255).
     * 
     * @return Linen color (R:250, G:240, B:230, A:255).
     */
    public static Color Linen()
    {
        return new Color(Linen);
    }

    /**
     * Magenta color (R:255, G:0, B:255, A:255).
     * 
     * @return Magenta color (R:255, G:0, B:255, A:255).
     */
    public static Color Magenta()
    {
        return new Color(Magenta);
    }

    /**
     * Maroon color (R:128, G:0, B:0, A:255).
     * 
     * @return Maroon color (R:128, G:0, B:0, A:255).
     */
    public static Color Maroon()
    {
        return new Color(Maroon);
    }

    /**
     * MediumAquamarine color (R:102, G:205, B:170, A:255).
     * 
     * @return MediumAquamarine color (R:102, G:205, B:170, A:255).
     */
    public static Color MediumAquamarine()
    {
        return new Color(MediumAquamarine);
    }

    /**
     * MediumBlue color (R:0, G:0, B:205, A:255).
     * 
     * @return MediumBlue color (R:0, G:0, B:205, A:255).
     */
    public static Color MediumBlue()
    {
        return new Color(MediumBlue);
    }

    /**
     * MediumOrchid color (R:186, G:85, B:211, A:255).
     * 
     * @return MediumOrchid color (R:186, G:85, B:211, A:255).
     */
    public static Color MediumOrchid()
    {
        return new Color(MediumOrchid);
    }

    /**
     * MediumPurple color (R:147, G:112, B:219, A:255).
     * 
     * @return MediumPurple color (R:147, G:112, B:219, A:255).
     */
    public static Color MediumPurple()
    {
        return new Color(MediumPurple);
    }

    /**
     * MediumSeaGreen color (R:60, G:179, B:113, A:255).
     * 
     * @return MediumSeaGreen color (R:60, G:179, B:113, A:255).
     */
    public static Color MediumSeaGreen()
    {
        return new Color(MediumSeaGreen);
    }

    /**
     * MediumSlateBlue color (R:123, G:104, B:238, A:255).
     * 
     * @return MediumSlateBlue color (R:123, G:104, B:238, A:255).
     */
    public static Color MediumSlateBlue()
    {
        return new Color(MediumSlateBlue);
    }

    /**
     * MediumSpringGreen color (R:0, G:250, B:154, A:255).
     * 
     * @return MediumSpringGreen color (R:0, G:250, B:154, A:255).
     */
    public static Color MediumSpringGreen()
    {
        return new Color(MediumSpringGreen);
    }

    /**
     * MediumTurquoise color (R:72, G:209, B:204, A:255).
     * 
     * @return MediumTurquoise color (R:72, G:209, B:204, A:255).
     */
    public static Color MediumTurquoise()
    {
        return new Color(MediumTurquoise);
    }

    /**
     * MediumVioletRed color (R:199, G:21, B:133, A:255).
     * 
     * @return MediumVioletRed color (R:199, G:21, B:133, A:255).
     */
    public static Color MediumVioletRed()
    {
        return new Color(MediumVioletRed);
    }

    /**
     * MidnightBlue color (R:25, G:25, B:112, A:255).
     * 
     * @return MidnightBlue color (R:25, G:25, B:112, A:255).
     */
    public static Color MidnightBlue()
    {
        return new Color(MidnightBlue);
    }

    /**
     * MintCream color (R:245, G:255, B:250, A:255).
     * 
     * @return MintCream color (R:245, G:255, B:250, A:255).
     */
    public static Color MintCream()
    {
        return new Color(MintCream);
    }

    /**
     * MistyRose color (R:255, G:228, B:225, A:255).
     * 
     * @return MistyRose color (R:255, G:228, B:225, A:255).
     */
    public static Color MistyRose()
    {
        return new Color(MistyRose);
    }

    /**
     * Moccasin color (R:255, G:228, B:181, A:255).
     * 
     * @return Moccasin color (R:255, G:228, B:181, A:255).
     */
    public static Color Moccasin()
    {
        return new Color(Moccasin);
    }

    /**
     * MonoGame orange theme color (R:231, G:60, B:0, A:255).
     * 
     * @return MonoGame orange theme color (R:231, G:60, B:0, A:255).
     */
    public static Color MonoGameOrange()
    {
        return new Color(MonoGameOrange);
    }

    /**
     * NavajoWhite color (R:255, G:222, B:173, A:255).
     * 
     * @return NavajoWhite color (R:255, G:222, B:173, A:255).
     */
    public static Color NavajoWhite()
    {
        return new Color(NavajoWhite);
    }

    /**
     * Navy color (R:0, G:0, B:128, A:255).
     * 
     * @return Navy color (R:0, G:0, B:128, A:255).
     */
    public static Color Navy()
    {
        return new Color(Navy);
    }

    /**
     * OldLace color (R:253, G:245, B:230, A:255).
     * 
     * @return OldLace color (R:253, G:245, B:230, A:255).
     */
    public static Color OldLace()
    {
        return new Color(OldLace);
    }

    /**
     * Olive color (R:128, G:128, B:0, A:255).
     * 
     * @return Olive color (R:128, G:128, B:0, A:255).
     */
    public static Color Olive()
    {
        return new Color(Olive);
    }

    /**
     * OliveDrab color (R:107, G:142, B:35, A:255).
     * 
     * @return OliveDrab color (R:107, G:142, B:35, A:255).
     */
    public static Color OliveDrab()
    {
        return new Color(OliveDrab);
    }

    /**
     * Orange color (R:255, G:165, B:0, A:255).
     * 
     * @return Orange color (R:255, G:165, B:0, A:255).
     */
    public static Color Orange()
    {
        return new Color(Orange);
    }

    /**
     * OrangeRed color (R:255, G:69, B:0, A:255).
     * 
     * @return OrangeRed color (R:255, G:69, B:0, A:255).
     */
    public static Color OrangeRed()
    {
        return new Color(OrangeRed);
    }

    /**
     * Orchid color (R:218, G:112, B:214, A:255).
     * 
     * @return Orchid color (R:218, G:112, B:214, A:255).
     */
    public static Color Orchid()
    {
        return new Color(Orchid);
    }

    /**
     * PaleGoldenrod color (R:238, G:232, B:170, A:255).
     * 
     * @return PaleGoldenrod color (R:238, G:232, B:170, A:255).
     */
    public static Color PaleGoldenrod()
    {
        return new Color(PaleGoldenrod);
    }

    /**
     * PaleGreen color (R:152, G:251, B:152, A:255).
     * 
     * @return PaleGreen color (R:152, G:251, B:152, A:255).
     */
    public static Color PaleGreen()
    {
        return new Color(PaleGreen);
    }

    /**
     * PaleTurquoise color (R:175, G:238, B:238, A:255).
     * 
     * @return PaleTurquoise color (R:175, G:238, B:238, A:255).
     */
    public static Color PaleTurquoise()
    {
        return new Color(PaleTurquoise);
    }

    /**
     * PaleVioletRed color (R:219, G:112, B:147, A:255).
     * 
     * @return PaleVioletRed color (R:219, G:112, B:147, A:255).
     */
    public static Color PaleVioletRed()
    {
        return new Color(PaleVioletRed);
    }

    /**
     * PapayaWhip color (R:255, G:239, B:213, A:255).
     * 
     * @return PapayaWhip color (R:255, G:239, B:213, A:255).
     */
    public static Color PapayaWhip()
    {
        return new Color(PapayaWhip);
    }

    /**
     * PeachPuff color (R:255, G:218, B:185, A:255).
     * 
     * @return PeachPuff color (R:255, G:218, B:185, A:255).
     */
    public static Color PeachPuff()
    {
        return new Color(PeachPuff);
    }

    /**
     * Peru color (R:205, G:133, B:63, A:255).
     * 
     * @return Peru color (R:205, G:133, B:63, A:255).
     */
    public static Color Peru()
    {
        return new Color(Peru);
    }

    /**
     * Pink color (R:255, G:192, B:203, A:255).
     * 
     * @return Pink color (R:255, G:192, B:203, A:255).
     */
    public static Color Pink()
    {
        return new Color(Pink);
    }

    /**
     * Plum color (R:221, G:160, B:221, A:255).
     * 
     * @return Plum color (R:221, G:160, B:221, A:255).
     */
    public static Color Plum()
    {
        return new Color(Plum);
    }

    /**
     * PowderBlue color (R:176, G:224, B:230, A:255).
     * 
     * @return PowderBlue color (R:176, G:224, B:230, A:255).
     */
    public static Color PowderBlue()
    {
        return new Color(PowderBlue);
    }

    /**
     * Purple color (R:128, G:0, B:128, A:255).
     * 
     * @return Purple color (R:128, G:0, B:128, A:255).
     */
    public static Color Purple()
    {
        return new Color(Purple);
    }

    /**
     * Red color (R:255, G:0, B:0, A:255).
     * 
     * @return Red color (R:255, G:0, B:0, A:255).
     */
    public static Color Red()
    {
        return new Color(Red);
    }

    /**
     * RosyBrown color (R:188, G:143, B:143, A:255).
     * 
     * @return RosyBrown color (R:188, G:143, B:143, A:255).
     */
    public static Color RosyBrown()
    {
        return new Color(RosyBrown);
    }

    /**
     * RoyalBlue color (R:65, G:105, B:225, A:255).
     * 
     * @return RoyalBlue color (R:65, G:105, B:225, A:255).
     */
    public static Color RoyalBlue()
    {
        return new Color(RoyalBlue);
    }

    /**
     * SaddleBrown color (R:139, G:69, B:19, A:255).
     * 
     * @return SaddleBrown color (R:139, G:69, B:19, A:255).
     */
    public static Color SaddleBrown()
    {
        return new Color(SaddleBrown);
    }

    /**
     * Salmon color (R:250, G:128, B:114, A:255).
     * 
     * @return Salmon color (R:250, G:128, B:114, A:255).
     */
    public static Color Salmon()
    {
        return new Color(Salmon);
    }

    /**
     * SandyBrown color (R:244, G:164, B:96, A:255).
     * 
     * @return SandyBrown color (R:244, G:164, B:96, A:255).
     */
    public static Color SandyBrown()
    {
        return new Color(SandyBrown);
    }

    /**
     * SeaGreen color (R:46, G:139, B:87, A:255).
     * 
     * @return SeaGreen color (R:46, G:139, B:87, A:255).
     */
    public static Color SeaGreen()
    {
        return new Color(SeaGreen);
    }

    /**
     * SeaShell color (R:255, G:245, B:238, A:255).
     * 
     * @return SeaShell color (R:255, G:245, B:238, A:255).
     */
    public static Color SeaShell()
    {
        return new Color(SeaShell);
    }

    /**
     * Sienna color (R:160, G:82, B:45, A:255).
     * 
     * @return Sienna color (R:160, G:82, B:45, A:255).
     */
    public static Color Sienna()
    {
        return new Color(Sienna);
    }

    /**
     * Silver color (R:192, G:192, B:192, A:255).
     * 
     * @return Silver color (R:192, G:192, B:192, A:255).
     */
    public static Color Silver()
    {
        return new Color(Silver);
    }

    /**
     * SkyBlue color (R:135, G:206, B:235, A:255).
     * 
     * @return SkyBlue color (R:135, G:206, B:235, A:255).
     */
    public static Color SkyBlue()
    {
        return new Color(SkyBlue);
    }

    /**
     * SlateBlue color (R:106, G:90, B:205, A:255).
     * 
     * @return SlateBlue color (R:106, G:90, B:205, A:255).
     */
    public static Color SlateBlue()
    {
        return new Color(SlateBlue);
    }

    /**
     * SlateGray color (R:112, G:128, B:144, A:255).
     * 
     * @return SlateGray color (R:112, G:128, B:144, A:255).
     */
    public static Color SlateGray()
    {
        return new Color(SlateGray);
    }

    /**
     * Snow color (R:255, G:250, B:250, A:255).
     * 
     * @return Snow color (R:255, G:250, B:250, A:255).
     */
    public static Color Snow()
    {
        return new Color(Snow);
    }

    /**
     * SpringGreen color (R:0, G:255, B:127, A:255).
     * 
     * @return SpringGreen color (R:0, G:255, B:127, A:255).
     */
    public static Color SpringGreen()
    {
        return new Color(SpringGreen);
    }

    /**
     * SteelBlue color (R:70, G:130, B:180, A:255).
     * 
     * @return SteelBlue color (R:70, G:130, B:180, A:255).
     */
    public static Color SteelBlue()
    {
        return new Color(SteelBlue);
    }

    /**
     * Tan color (R:210, G:180, B:140, A:255).
     * 
     * @return Tan color (R:210, G:180, B:140, A:255).
     */
    public static Color Tan()
    {
        return new Color(Tan);
    }

    /**
     * Teal color (R:0, G:128, B:128, A:255).
     * 
     * @return Teal color (R:0, G:128, B:128, A:255).
     */
    public static Color Teal()
    {
        return new Color(Teal);
    }

    /**
     * Thistle color (R:216, G:191, B:216, A:255).
     * 
     * @return Thistle color (R:216, G:191, B:216, A:255).
     */
    public static Color Thistle()
    {
        return new Color(Thistle);
    }

    /**
     * Tomato color (R:255, G:99, B:71, A:255).
     * 
     * @return Tomato color (R:255, G:99, B:71, A:255).
     */
    public static Color Tomato()
    {
        return new Color(Tomato);
    }

    /**
     * Turquoise color (R:64, G:224, B:208, A:255).
     * 
     * @return Turquoise color (R:64, G:224, B:208, A:255).
     */
    public static Color Turquoise()
    {
        return new Color(Turquoise);
    }

    /**
     * Violet color (R:238, G:130, B:238, A:255).
     * 
     * @return Violet color (R:238, G:130, B:238, A:255).
     */
    public static Color Violet()
    {
        return new Color(Violet);
    }

    /**
     * Wheat color (R:245, G:222, B:179, A:255).
     * 
     * @return Wheat color (R:245, G:222, B:179, A:255).
     */
    public static Color Wheat()
    {
        return new Color(Wheat);
    }

    /**
     * White color (R:255, G:255, B:255, A:255).
     * 
     * @return White color (R:255, G:255, B:255, A:255).
     */
    public static Color White()
    {
        return new Color(White);
    }

    /**
     * WhiteSmoke color (R:245, G:245, B:245, A:255).
     * 
     * @return WhiteSmoke color (R:245, G:245, B:245, A:255).
     */
    public static Color WhiteSmoke()
    {
        return new Color(WhiteSmoke);
    }

    /**
     * Yellow color (R:255, G:255, B:0, A:255).
     * 
     * @return Yellow color (R:255, G:255, B:0, A:255).
     */
    public static Color Yellow()
    {
        return new Color(Yellow);
    }

    /**
     * YellowGreen color (R:154, G:205, B:50, A:255).
     * 
     * @return YellowGreen color (R:154, G:205, B:50, A:255).
     */
    public static Color YellowGreen()
    {
        return new Color(YellowGreen);
    }

    // #endregion

    /**
     * Performs linear interpolation of {@code Color}.
     * 
     * @param value1
     *        Source {@code Color}.
     * @param value2
     *        Destination {@code Color}.
     * @param amount
     *        Interpolation factor.
     * @return Interpolated {@code Color}.
     */
    public static Color lerp(Color value1, Color value2, float amount)
    {
        amount = MathHelper.clamp(amount, 0.0f, 1.0f);
        return new Color((int) MathHelper.lerp(value1.getRed(), value2.getRed(), amount),
                         (int) MathHelper.lerp(value1.getGreen(), value2.getGreen(), amount),
                         (int) MathHelper.lerp(value1.getBlue(), value2.getBlue(), amount),
                         (int) MathHelper.lerp(value1.getAlpha(), value2.getAlpha(), amount));
    }

    /**
     * Performs linear interpolation of {@code Color}.
     * 
     * @param value1
     *        Source {@code Color}.
     * @param value2
     *        Destination {@code Color}.
     * @param amount
     *        Interpolation factor.
     * @return Interpolated {@link Color}.
     * @deprecated {@link Color#lerp} should be used instead of this function.
     */
    @Deprecated
    public static Color lerpPrecise(Color value1, Color value2, float amount)
    {
        amount = MathHelper.clamp(amount, 0, 1);
        return new Color((int) MathHelper.lerpPrecise(value1.getRed(), value2.getRed(), amount),
                         (int) MathHelper.lerpPrecise(value1.getGreen(), value2.getGreen(), amount),
                         (int) MathHelper.lerpPrecise(value1.getBlue(), value2.getBlue(), amount),
                         (int) MathHelper.lerpPrecise(value1.getAlpha(), value2.getAlpha(), amount));
    }

    /**
     * Multiply {@code Color} by value.
     * 
     * @param value
     *        Source {@code Color}.
     * @param scale
     *        Multiplicator.
     * @return Multiplicated {@code Color}.
     */
    public static Color multiply(Color value, float scale)
    {
        return new Color((int) (value.getRed() * scale),
                         (int) (value.getGreen() * scale),
                         (int) (value.getBlue() * scale),
                         (int) (value.getAlpha() * scale));
    }

    /**
     * Returns a three-component {@link Vector3} representation for this object.
     * 
     * @return A three-component {@link Vector3} representation for this object.
     */
    public Vector3 toVector3()
    {
        return new Vector3(getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f);
    }

    /**
     * Returns a four-component {@link Vector4} representation for this object.
     * 
     * @return A four-component {@link Vector4} representation for this object.
     */
    public Vector4 toVector4()
    {
        return new Vector4(getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f, getAlpha() / 255.0f);
    }

    /**
     * Returns the packed value of this {@code Color} in the ABGR space.
     * 
     * @return The packed value of this {@code Color}.
     */
    public int getPackedValue()
    {
        return _packedValue;
    }

    /**
     * Sets the packed value to the specified value.
     * 
     * @param value
     *        The new packed value for this {@code Color}.
     */
    public void setPackedValue(int value)
    {
        _packedValue = value;
    }

    protected String debugDisplayString()
    {
        return this.getRed() + "  " +
               this.getGreen() + "  " +
               this.getBlue() + "  " +
               this.getAlpha();
    }

    /**
     * Returns a {@link String} representation of this {@link Color} in the format:
     * {R:[red] G:[green] B:[blue] A:[alpha]}
     * 
     * @return {@link String} representation of this {@code Color}.
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(25);
        sb.append("{R:");
        sb.append(getRed());
        sb.append(" G:");
        sb.append(getGreen());
        sb.append(" B:");
        sb.append(getBlue());
        sb.append(" A:");
        sb.append(getAlpha());
        sb.append("}");
        return sb.toString();
    }

    /**
     * Translate a non-premultiplied alpha {@code Color} to a {@code Color} that contains
     * premultiplied alpha.
     * 
     * @param vector
     *        A {@code Vector4} representing the {@code Color}.
     * @returnA {@code Color} which contains premultiplied alpha data.
     */
    public static Color fromNonPremultiplied(Vector4 vector)
    {
        return new Color(vector.x * vector.w, vector.y * vector.w, vector.z * vector.w, vector.w);
    }

    /**
     * Translate a non-premultiplied alpha {@code Color} to a {@code Color} that contains
     * premultiplied alpha.
     * 
     * @param r
     *        Red component value.
     * @param g
     *        Green component value.
     * @param b
     *        Blue component value.
     * @param a
     *        Alpha component value.
     * @return A {@code Color} which contains premultiplied alpha data.
     */
    public static Color fromNonPremultiplied(int r, int g, int b, int a)
    {
        return new Color((int) (r * a / 255), (int) (g * a / 255), (int) (b * a / 255), a);
    }

    // NOTE(Eric): I added these constants since Java doesn't have unsigned values
    private final short uBYTE_MIN_VALUE = 0;
    private final short uBYTE_MAX_VALUE = 255;

    //
    // NOTE(Eric): These methods and constants are from java.awt.Color class
    //
    private static final double FACTOR = 0.7;

    /**
     * Creates a new <code>Color</code> that is a brighter version of this <code>Color</code>.
     * <p>
     * This method applies an arbitrary scale factor to each of the three RGB components of this
     * <code>Color</code> to create a brighter version of this <code>Color</code>. The {@code alpha}
     * value is preserved. Although <code>brighter</code> and <code>darker</code> are inverse
     * operations, the results of a series of invocations of these two methods might be inconsistent
     * because of rounding errors.
     * 
     * @return A new <code>Color</code> object that is a brighter version of this <code>Color</code>
     *         with the same {@code alpha} value.
     * @see java.awt.Color#darker
     * @since JDK1.0
     */
    public Color brighter()
    {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        int alpha = getAlpha();

        /*
         * From 2D group:
         * 1. black.brighter() should return grey
         * 2. applying brighter to blue will always return blue, brighter
         * 3. non pure color (non zero rgb) will eventually return white
         */
        int i = (int) (1.0 / (1.0 - FACTOR));
        if (r == 0 && g == 0 && b == 0)
        {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i)
            r = i;
        if (g > 0 && g < i)
            g = i;
        if (b > 0 && b < i)
            b = i;

        return new Color(Math.min((int) (r / FACTOR), 255), Math.min((int) (g / FACTOR), 255), Math.min(
                                                                                                        (int) (b / FACTOR), 255), alpha);
    }

    /**
     * Creates a new <code>Color</code> that is a darker version of this <code>Color</code>.
     * <p>
     * This method applies an arbitrary scale factor to each of the three RGB components of this
     * <code>Color</code> to create a darker version of this <code>Color</code>. The {@code alpha}
     * value is preserved. Although <code>brighter</code> and <code>darker</code> are inverse
     * operations, the results of a series of invocations of these two methods might be inconsistent
     * because of rounding errors.
     * 
     * @return A new <code>Color</code> object that is a darker version of this <code>Color</code>
     *         with the same {@code alpha} value.
     * @see java.awt.Color#brighter
     * @since JDK1.0
     */
    public Color darker()
    {
        return new Color(Math.max((int) (getRed() * FACTOR), 0), Math.max((int) (getGreen() * FACTOR), 0), Math.max(
                                                                                                                    (int) (getBlue() * FACTOR), 0), getAlpha());
    }

}
