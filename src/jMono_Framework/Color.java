package jMono_Framework;

import jMono_Framework.math.MathHelper;
import jMono_Framework.math.Vector3;
import jMono_Framework.math.Vector4;

// C# struct
/**
 * Describes a 32-bit packed color.
 * 
 * @author Eric
 *
 */
public class Color
{

	/**
	 * The color TransparentBlack. In the ABGR space.
	 * A:0, B:0, G:0, R:0 
	 * 
	 */
	public final static Color TransparentBlack = new Color(0);

	/**
	 * The color Transparent. In the ABGR space.
	 * A:0, B:0, G:0, R:0
	 * 
	 */
	public final static Color Transparent = new Color(0);

	/**
	 * The color AliceBlue. In the ABGR space.
	 * A:255, B:255, G:248, R:240
	 * 
	 */
	public final static Color AliceBlue = new Color(0xfffff8f0);

	/**
	 * The color AntiqueWhite. In the ABGR space.
	 * A:255, B:215, G:235, R:250
	 * 
	 */
	public final static Color AntiqueWhite = new Color(0xffd7ebfa);

	/**
	 * The color Aqua. In the ABGR space.
	 * A:255, B:255, G:255, R:0
	 * 
	 */
	public final static Color Aqua = new Color(0xffffff00);

	/**
	 * The color Aquamarine. In the ABGR space.
	 * A:255, B:212, G:255, R:127
	 * 
	 */
	public final static Color Aquamarine = new Color(0xffd4ff7f);

	/**
	 * The color Azure. In the ABGR space.
	 * A:255, B:255, G:255, R:240
	 * 
	 */
	public final static Color Azure = new Color(0xfffffff0);

	/**
	 * The color Beige. In the ABGR space.
	 * A:255, B:220, G:245, R:245
	 * 
	 */
	public final static Color Beige = new Color(0xffdcf5f5);

	/**
	 * The color Bisque. In the ABGR space.
	 * A:255, B:196 ,G:228, R:255
	 * 
	 */
	public final static Color Bisque = new Color(0xffc4e4ff);

	/**
	 * The color Black. In the ABGR space.
	 * A:255, B:0, G:0, R:0
	 * 
	 */
	public final static Color Black = new Color(0xff000000);

	/**
	 * The color BlanchedAlmond. In the ABGR space.
	 * A:255, B:205, G:235, R:255
	 * 
	 */
	public final static Color BlanchedAlmond = new Color(0xffcdebff);

	/**
	 * The color Blue. In the ABGR space.
	 * A:255, B:255 ,G:0, R:0
	 * 
	 */
	public final static Color Blue = new Color(0xffff0000);

	/**
	 * The color BlueViolet. In the ABGR space.
	 * A:255, B:226, G:43, R:138
	 * 
	 */
	public final static Color BlueViolet = new Color(0xffe22b8a);

	/**
	 * The color Brown. In the ABGR space.
	 * A:255, B:42, G:42, R:165
	 * 
	 */
	public final static Color Brown = new Color(0xff2a2aa5);

	/**
	 * The color BurlyWood. In the ABGR space.
	 * A:255, B:135, G:184, R:222
	 * 
	 */
	public final static Color BurlyWood = new Color(0xff87b8de);

	/**
	 * The color CadetBlue. In the ABGR space.
	 * A:255, B:160, G:158, R:95
	 * 
	 */
	public final static Color CadetBlue = new Color(0xffa09e5f);

	/**
	 * The color Chartreuse. In the ABGR space.
	 * A:255, B:0, G:255, R:127
	 * 
	 */
	public final static Color Chartreuse = new Color(0xff00ff7f);

	/**
	 * The color Chocolate. In the ABGR space.
	 * A:255, B:30, G:105, R:210
	 * 
	 */
	public final static Color Chocolate = new Color(0xff1e69d2);

	/**
	 * The color Coral. In the ABGR space.
	 * A:255, B:80, G:127, R:255
	 * 
	 */
	public final static Color Coral = new Color(0xff507fff);

	/**
	 * The color CornflowerBlue. In the ABGR space.
	 * A:255, B:237, G:149, R:100
	 * 
	 */
	public final static Color CornflowerBlue = new Color(0xffed9564);

	/**
	 * The color Cornsilk. In the ABGR space.
	 * A:255, B:220, G:248, R:255
	 * 
	 */
	public final static Color Cornsilk = new Color(0xffdcf8ff);

	/**
	 * The color Crimson. In the ABGR space.
	 * A:255, B:60, G:20, R:220
	 * 
	 */
	public final static Color Crimson = new Color(0xff3c14dc);

	/**
	 * The color Cyan. In the ABGR space.
	 * A:255, B:255, G:255, R:0
	 * 
	 */
	public final static Color Cyan = new Color(0xffffff00);

	/**
	 * The color DarkBlue. In the ABGR space.
	 * A:255, B:139, G:0, R:0
	 * 
	 */
	public final static Color DarkBlue = new Color(0xff8b0000);

	/**
	 * The color DarkCyan. In the ABGR space.
	 * A:255, B:139, G:139, R:0
	 * 
	 */
	public final static Color DarkCyan = new Color(0xff8b8b00);

	/**
	 * The color DarkGoldenrod. In the ABGR space.
	 * A:255, B:11, G:134, R:184
	 * 
	 */
	public final static Color DarkGoldenrod = new Color(0xff0b86b8);

	/**
	 * The color DarkGray. In the ABGR space.
	 * A:255, B:169, G:169, R:169
	 * 
	 */
	public final static Color DarkGray = new Color(0xffa9a9a9);

	/**
	 * The color DarkGreen. In the ABGR space.
	 * A:255, B:0, G:100, R:0
	 * 
	 */
	public final static Color DarkGreen = new Color(0xff006400);

	/**
	 * The color DarkKhaki. In the ABGR space.
	 * A:255, B:107, G:183, R:189
	 * 
	 */
	public final static Color DarkKhaki = new Color(0xff6bb7bd);

	/**
	 * The color DarkMagenta. In the ABGR space.
	 * A:255, B:139, G:0, R:139
	 * 
	 */
	public final static Color DarkMagenta = new Color(0xff8b008b);

	/**
	 * The color DarkOliveGreen. In the ABGR space.
	 * A:255, B:47, G:107, R:85
	 * 
	 */
	public final static Color DarkOliveGreen = new Color(0xff2f6b55);

	/**
	 * The color DarkOrange. In the ABGR space.
	 * A:255, B:0, G:140, R:255
	 * 
	 */
	public final static Color DarkOrange = new Color(0xff008cff);

	/**
	 * The color DarkOrchid. In the ABGR space.
	 * A:255, B:204, G:50, R:153
	 * 
	 */
	public final static Color DarkOrchid = new Color(0xffcc3299);

	/**
	 * The color DarkRed. In the ABGR space.
	 * A:255, B:0, G:0,R:139
	 * 
	 */
	public final static Color DarkRed = new Color(0xff00008b);

	/**
	 * The color DarkSalmon. In the ABGR space.
	 * A:255, B:122, G:150, R:233
	 * 
	 */
	public final static Color DarkSalmon = new Color(0xff7a96e9);

	/**
	 * The color DarkSeaGreen. In the ABGR space.
	 * A:255, B:139, G:188, R:143
	 * 
	 */
	public final static Color DarkSeaGreen = new Color(0xff8bbc8f);

	/**
	 * The color DarkSlateBlue. In the ABGR space.
	 * A:255, B:139, G:61, R:72
	 * 
	 */
	public final static Color DarkSlateBlue = new Color(0xff8b3d48);

	/**
	 * The color DarkSlateGray. In the ABGR space.
	 * A:255, B:79, G:79, R:47
	 * 
	 */
	public final static Color DarkSlateGray = new Color(0xff4f4f2f);

	/**
	 * The color DarkTurquoise. In the ABGR space.
	 * A:255, B:209, G:206, R:0
	 * 
	 */
	public final static Color DarkTurquoise = new Color(0xffd1ce00);

	/**
	 * The color DarkViolet. In the ABGR space.
	 * A:255, B:211, G:0, R:148
	 * 
	 */
	public final static Color DarkViolet = new Color(0xffd30094);

	/**
	 * The color DeepPink. In the ABGR space.
	 * A:255, B:147, G:20, R:255
	 * 
	 */
	public final static Color DeepPink = new Color(0xff9314ff);

	/**
	 * The color DeepSkyBlue. In the ABGR space.
	 * A:255, B:255, G:191, R:0
	 * 
	 */
	public final static Color DeepSkyBlue = new Color(0xffffbf00);

	/**
	 * The color DimGray. In the ABGR space.
	 * A:255, B:105, G:105, R:105
	 * 
	 */
	public final static Color DimGray = new Color(0xff696969);

	/**
	 * The color DodgerBlue. In the ABGR space.
	 * A:255, B:255, G:144, R:30
	 * 
	 */
	public final static Color DodgerBlue = new Color(0xffff901e);

	/**
	 * The color Firebrick. In the ABGR space.
	 * A:255, B:34, G:34, R:178
	 * 
	 */
	public final static Color Firebrick = new Color(0xff2222b2);

	/**
	 * The color FloralWhite. In the ABGR space.
	 * A:255, B:240, G:250, R:255
	 * 
	 */
	public final static Color FloralWhite = new Color(0xfff0faff);

	/**
	 * The color ForestGreen. In the ABGR space.
	 * A:255, B:34, G:139,R:34
	 * 
	 */
	public final static Color ForestGreen = new Color(0xff228b22);

	/**
	 * The color Fuchsia. In the ABGR space.
	 * A:255, B:255, G:0, R:255
	 * 
	 */
	public final static Color Fuchsia = new Color(0xffff00ff);

	/**
	 * The color Gainsboro. In the ABGR space.
	 * A:255, B:220, G:220, R:220
	 * 
	 */
	public final static Color Gainsboro = new Color(0xffdcdcdc);

	/**
	 * The color GhostWhite. In the ABGR space.
	 * A:255, B:255, G:248, R:248
	 * 
	 */
	public final static Color GhostWhite = new Color(0xfffff8f8);

	/**
	 * The color Goldenrod. In the ABGR space.
	 * A:255, B:0, G:215, R:255
	 * 
	 */
	public final static Color Gold = new Color(0xff00d7ff);

	/**
	 * The color Goldenrod. In the ABGR space.
	 * A:255, B:32, G:165, R:218
	 * 
	 */
	public final static Color Goldenrod = new Color(0xff20a5da);

	/**
	 * The color Gray. In the ABGR space.
	 * A:255, B:128, G:128, R:128
	 * 
	 */
	public final static Color Gray = new Color(0xff808080);

	/**
	 * The color Green. In the ABGR space.
	 * A:255, B:0, G:128, R:0
	 * 
	 */
	public final static Color Green = new Color(0xff008000);

	/**
	 * The color GreenYellow. In the ABGR space.
	 * A:255, B:47, G:255, R:173
	 * 
	 */
	public final static Color GreenYellow = new Color(0xff2fffad);

	/**
	 * The color Honeydew. In the ABGR space.
	 * A:255, B:240, G:255, R:240
	 * 
	 */
	public final static Color Honeydew = new Color(0xfff0fff0);

	/**
	 * The color HotPink. In the ABGR space.
	 * A:255, B:180, G:105, R:255
	 * 
	 */
	public final static Color HotPink = new Color(0xffb469ff);

	/**
	 * The color IndianRed. In the ABGR space.
	 * A:255, B:92, G:92, R:205
	 * 
	 */
	public final static Color IndianRed = new Color(0xff5c5ccd);

	/**
	 * The color Indigo. In the ABGR space.
	 * A:255, B:130, G:0, R:75
	 * 
	 */
	public final static Color Indigo = new Color(0xff82004b);

	/**
	 * The color Ivory. In the ABGR space.
	 * A:255, B:240, G:255, R:255
	 * 
	 */
	public final static Color Ivory = new Color(0xfff0ffff);

	/**
	 * The color Khaki. In the ABGR space.
	 * A:255, B:140, G:230, R:240
	 * 
	 */
	public final static Color Khaki = new Color(0xff8ce6f0);

	/**
	 * The color Lavender. In the ABGR space.
	 * A:255, B:250, G:230, R:230
	 * 
	 */
	public final static Color Lavender = new Color(0xfffae6e6);

	/**
	 * The color LavenderBlush. In the ABGR space.
	 * A:255, B:245, G:240, R:255
	 * 
	 */
	public final static Color LavenderBlush = new Color(0xfff5f0ff);

	/**
	 * The color LawnGreen. In the ABGR space.
	 * A:255, B:0, G:252, R:124
	 * 
	 */
	public final static Color LawnGreen = new Color(0xff00fc7c);

	/**
	 * The color LemonChiffon. In the ABGR space.
	 * A:255, B:205, G:250, R:255
	 * 
	 */
	public final static Color LemonChiffon = new Color(0xffcdfaff);

	/**
	 * The color LightBlue. In the ABGR space.
	 * A:255, B:230, G:216, R:173
	 * 
	 */
	public final static Color LightBlue = new Color(0xffe6d8ad);

	/**
	 * The color LightCoral. In the ABGR space.
	 * A:255, B:128, G:128, R:240
	 * 
	 */
	public final static Color LightCoral = new Color(0xff8080f0);

	/**
	 * The color LightCyan. In the ABGR space.
	 * A:255, B:255, G:255, R:224
	 * 
	 */
	public final static Color LightCyan = new Color(0xffffffe0);

	/**
	 * The color LightGoldenrodYellow. In the ABGR space.
	 * A:255, B:210, G:250, R:250
	 * 
	 */
	public final static Color LightGoldenrodYellow = new Color(0xffd2fafa);

	/**
	 * The color LightGray. In the ABGR space.
	 * A:255, B:211, G:211, R:211
	 * 
	 */
	public final static Color LightGray = new Color(0xffd3d3d3);

	/**
	 * The color LightGreen. In the ABGR space.
	 * A:255, B:144, G:238, R:144
	 * 
	 */
	public final static Color LightGreen = new Color(0xff90ee90);

	/**
	 * The color LightPink. In the ABGR space.
	 * A:255, B:193, G:182, R:255
	 * 
	 */
	public final static Color LightPink = new Color(0xffc1b6ff);

	/**
	 * The color LightSalmon. In the ABGR space.
	 * A:255, B:122, G:160, R:255
	 * 
	 */
	public final static Color LightSalmon = new Color(0xff7aa0ff);

	/**
	 * The color LightSeaGreen. In the ABGR space.
	 * A:255, B:170, G:178, R:32
	 * 
	 */
	public final static Color LightSeaGreen = new Color(0xffaab220);

	/**
	 * The color LightSkyBlue. In the ABGR space.
	 * A:255, B:250, G:206, R:135
	 * 
	 */
	public final static Color LightSkyBlue = new Color(0xffface87);

	/**
	 * The color LightSlateGray. In the ABGR space.
	 * A:255, B:153, G:136, R:119
	 * 
	 */
	public final static Color LightSlateGray = new Color(0xff998877);

	/**
	 * The color LightSteelBlue. In the ABGR space.
	 * A:255, B:222, G:196, R:176
	 * 
	 */
	public final static Color LightSteelBlue = new Color(0xffdec4b0);

	/**
	 * The color LightYellow. In the ABGR space.
	 * A:255, B:224, G:255, R:255
	 * 
	 */
	public final static Color LightYellow = new Color(0xffe0ffff);

	/**
	 * The color Lime. In the ABGR space.
	 * A:255, B:0, G:255, R:0
	 * 
	 */
	public final static Color Lime = new Color(0xff00ff00);

	/**
	 * The color LimeGreen. In the ABGR space.
	 * A:255, B:50, G:205, R:50
	 * 
	 */
	public final static Color LimeGreen = new Color(0xff32cd32);

	/**
	 * The color Linen. In the ABGR space.
	 * A:255, B:230, G:240, R:250
	 * 
	 */
	public final static Color Linen = new Color(0xffe6f0fa);

	/**
	 * The color Magenta. In the ABGR space.
	 * A:255, B:255, G:0, R:255
	 * 
	 */
	public final static Color Magenta = new Color(0xffff00ff);

	/**
	 * The color Maroon. In the ABGR space.
	 * A:255, B:0, G:0, R:128
	 * 
	 */
	public final static Color Maroon = new Color(0xff000080);

	/**
	 * The color MediumAquamarine. In the ABGR space.
	 * A:255, B:170, G:205, R:102
	 * 
	 */
	public final static Color MediumAquamarine = new Color(0xffaacd66);

	/**
	 * The color MediumBlue. In the ABGR space.
	 * A:255, B:205, G:0, R:0
	 * 
	 */
	public final static Color MediumBlue = new Color(0xffcd0000);

	/**
	 * The color MediumOrchid. In the ABGR space.
	 * A:255, B:211, G:85, R:186
	 * 
	 */
	public final static Color MediumOrchid = new Color(0xffd355ba);

	/**
	 * The color MediumPurple. In the ABGR space.
	 * A:255, B:219, G:112, R:147
	 * 
	 */
	public final static Color MediumPurple = new Color(0xffdb7093);

	/**
	 * The color MediumSeaGreen. In the ABGR space.
	 * A:255, B:113, G:179, R:60
	 * 
	 */
	public final static Color MediumSeaGreen = new Color(0xff71b33c);

	/**
	 * The color MediumSlateBlue. In the ABGR space.
	 * A:255, B:238, G:104, R:123
	 * 
	 */
	public final static Color MediumSlateBlue = new Color(0xffee687b);

	/**
	 * The color MediumSpringGreen. In the ABGR space.
	 * A:255, B:154, G:250, R:0
	 * 
	 */
	public final static Color MediumSpringGreen = new Color(0xff9afa00);

	/**
	 * The color MediumTurquoise. In the ABGR space.
	 * A:255, B:204, G:209, R:72
	 * 
	 */
	public final static Color MediumTurquoise = new Color(0xffccd148);

	/**
	 * The color MediumVioletRed. In the ABGR space.
	 * A:255, B:133, G:21, R:199
	 * 
	 */
	public final static Color MediumVioletRed = new Color(0xff8515c7);

	/**
	 * The color MidnightBlue. In the ABGR space.
	 * A:255, B:112, G:25, R:25
	 * 
	 */
	public final static Color MidnightBlue = new Color(0xff701919);

	/**
	 * The color MintCream. In the ABGR space.
	 * A:255, B:250, G:255, R:245
	 * 
	 */
	public final static Color MintCream = new Color(0xfffafff5);

	/**
	 * The color MistyRose. In the ABGR space.
	 * A:255, B:225, G:228, R:255
	 * 
	 */
	public final static Color MistyRose = new Color(0xffe1e4ff);

	/**
	 * The color Moccasin. In the ABGR space.
	 * A:255, B:181, G:228, R:255
	 * 
	 */
	public final static Color Moccasin = new Color(0xffb5e4ff);

	/**
	 * The color Moccasin. In the ABGR space.
	 * A:255, B:181, G:228, R:255
	 * 
	 */
	public final static Color MonoGameOrange = new Color(0xff003ce7);

	/**
	 * The color NavajoWhite. In the ABGR space.
	 * A:255, B:173, G:222, R:255
	 * 
	 */
	public final static Color NavajoWhite = new Color(0xffaddeff);

	/**
	 * The color Navy. In the ABGR space.
	 * A:255, B:128, G:0, R:0
	 * 
	 */
	public final static Color Navy = new Color(0xff800000);

	/**
	 * The color OldLace. In the ABGR space.
	 * A:255, B:230, G:245,R:253
	 * 
	 */
	public final static Color OldLace = new Color(0xffe6f5fd);

	/**
	 * The color Olive. In the ABGR space.
	 * A:255, B:0, G:128, R:128
	 * 
	 */
	public final static Color Olive = new Color(0xff008080);

	/**
	 * The color OliveDrab. In the ABGR space.
	 * A:255, B:35, G:142, R:107
	 * 
	 */
	public final static Color OliveDrab = new Color(0xff238e6b);

	/**
	 * The color Orange. In the ABGR space.
	 * A:255, B:0, G:165, R:255
	 * 
	 */
	public final static Color Orange = new Color(0xff00a5ff);

	/**
	 * The color OrangeRed. In the ABGR space.
	 * A:255, B:0, G:69, R:255
	 * 
	 */
	public final static Color OrangeRed = new Color(0xff0045ff);

	/**
	 * The color Orchid. In the ABGR space.
	 * A:255, B:214, G:112, R:218
	 * 
	 */
	public final static Color Orchid = new Color(0xffd670da);

	/**
	 * The color PaleGoldenrod. In the ABGR space.
	 * A:255, B:170, G:232, R:238
	 * 
	 */
	public final static Color PaleGoldenrod = new Color(0xffaae8ee);

	/**
	 * The color PaleGreen. In the ABGR space.
	 * A:255, B:152, G:251, R:152
	 * 
	 */
	public final static Color PaleGreen = new Color(0xff98fb98);

	/**
	 * The color PaleTurquoise. In the ABGR space.
	 * A:255, B:238, G:238, R:175
	 * 
	 */
	public final static Color PaleTurquoise = new Color(0xffeeeeaf);

	/**
	 * The color PaleVioletRed. In the ABGR space.
	 * A:255, B:147, G:112, R:219
	 * 
	 */
	public final static Color PaleVioletRed = new Color(0xff9370db);

	/**
	 * The color PapayaWhip. In the ABGR space.
	 * A:255, B:213, G:239, R:255
	 * 
	 */
	public final static Color PapayaWhip = new Color(0xffd5efff);

	/**
	 * The color PeachPuff. In the ABGR space.
	 * A:255, B:185, G:218, R:255
	 * 
	 */
	public final static Color PeachPuff = new Color(0xffb9daff);

	/**
	 * The color Peru. In the ABGR space.
	 * A:255, B:63, G:133, R:205
	 * 
	 */
	public final static Color Peru = new Color(0xff3f85cd);

	/**
	 * The color Pink. In the ABGR space.
	 * A:255, B:203, G:192, R:255
	 * 
	 */
	public final static Color Pink = new Color(0xffcbc0ff);

	/**
	 * The color Plum. In the ABGR space.
	 * A:255, B:221, G:160, R:221
	 * 
	 */
	public final static Color Plum = new Color(0xffdda0dd);

	/**
	 * The color PowderBlue. In the ABGR space.
	 * A:255, B:230, G:224, R:176
	 * 
	 */
	public final static Color PowderBlue = new Color(0xffe6e0b0);

	/**
	 * The color Purple. In the ABGR space.
	 * A:255, B:128, G:0, R:128
	 * 
	 */
	public final static Color Purple = new Color(0xff800080);

	/**
	 * The color Red. In the ABGR space.
	 * A:255, B:0, G:0, R:255
	 * 
	 */
	public final static Color Red = new Color(0xff0000ff);

	/**
	 * The color RosyBrown. In the ABGR space.
	 * A:255, B:143, G:143, R:188
	 * 
	 */
	public final static Color RosyBrown = new Color(0xff8f8fbc);

	/**
	 * The color RoyalBlue. In the ABGR space.
	 * A:255, B:225, G:105, R:65
	 * 
	 */
	public final static Color RoyalBlue = new Color(0xffe16941);

	/**
	 * The color SaddleBrown. In the ABGR space.
	 * A:255, B:19, G:69, R:139
	 * 
	 */
	public final static Color SaddleBrown = new Color(0xff13458b);

	/**
	 * The color Salmon. In the ABGR space.
	 * A:255, B:114, G:128, R:250
	 * 
	 */
	public final static Color Salmon = new Color(0xff7280fa);

	/**
	 * The color SandyBrown. In the ABGR space.
	 * A:255, B:96, G:164, R:244
	 * 
	 */
	public final static Color SandyBrown = new Color(0xff60a4f4);

	/**
	 * The color SeaGreen. In the ABGR space.
	 * A:255, B:87, G:139, R:46
	 * 
	 */
	public final static Color SeaGreen = new Color(0xff578b2e);

	/**
	 * The color SeaShell. In the ABGR space.
	 * A:255, B:238, G:245, R:255
	 * 
	 */
	public final static Color SeaShell = new Color(0xffeef5ff);

	/**
	 * The color Sienna. In the ABGR space.
	 * A:255, B:45, G:82, R:160
	 * 
	 */
	public final static Color Sienna = new Color(0xff2d52a0);

	/**
	 * The color Silver. In the ABGR space.
	 * A:255, B:192, G:192, R:192
	 * 
	 */
	public final static Color Silver = new Color(0xffc0c0c0);

	/**
	 * The color SkyBlue. In the ABGR space.
	 * A:255, B:235, G:206, R:135
	 * 
	 */
	public final static Color SkyBlue = new Color(0xffebce87);

	/**
	 * The color SlateBlue. In the ABGR space.
	 * A:255, B:205, G:90, R:106
	 * 
	 */
	public final static Color SlateBlue = new Color(0xffcd5a6a);

	/**
	 * The color SlateGray. In the ABGR space.
	 * A:255, B:144, G:128, R:112
	 * 
	 */
	public final static Color SlateGray = new Color(0xff908070);

	/**
	 * The color Snow. In the ABGR space.
	 * A:255, B:250, G:250, R:255
	 * 
	 */
	public final static Color Snow = new Color(0xfffafaff);

	/**
	 * The color SpringGreen. In the ABGR space.
	 * A:255, B:127, G:255, R:0
	 * 
	 */
	public final static Color SpringGreen = new Color(0xff7fff00);

	/**
	 * The color SteelBlue. In the ABGR space.
	 * A:255, B:180, G:130, R:70
	 * 
	 */
	public final static Color SteelBlue = new Color(0xffb48246);

	/**
	 * The color Tan. In the ABGR space.
	 * A:255, B:140, G:180, R:210
	 * 
	 */
	public final static Color Tan = new Color(0xff8cb4d2);

	/**
	 * The color Teal. In the ABGR space.
	 * A:255, B:128, G:128, R:0
	 * 
	 */
	public final static Color Teal = new Color(0xff808000);

	/**
	 * The color Thistle. In the ABGR space.
	 * A:255, B:216, G:191, R:216
	 * 
	 */
	public final static Color Thistle = new Color(0xffd8bfd8);

	/**
	 * The color Tomato. In the ABGR space.
	 * A:255, B:71, G:99, R:255
	 * 
	 */
	public final static Color Tomato = new Color(0xff4763ff);

	/**
	 * The color Turquoise. In the ABGR space.
	 * A:255, B:208, G:224, R:64
	 * 
	 */
	public final static Color Turquoise = new Color(0xffd0e040);

	/**
	 * The color Violet. In the ABGR space.
	 * A:255, B:238, G:130, R:238
	 * 
	 */
	public final static Color Violet = new Color(0xffee82ee);

	/**
	 * The color Wheat. In the ABGR space.
	 * A:255, B:179, G:222, R:245
	 * 
	 */
	public final static Color Wheat = new Color(0xffb3def5);

	/**
	 * The color White. In the ABGR space.
	 * A:255, B:255, G:255, R:255
	 * 
	 */
	public final static Color White = new Color(0xffffffff);

	/**
	 * The color WhiteSmoke. In the ABGR space.
	 * A:255, B:245, G:245, R:245
	 * 
	 */
	public final static Color WhiteSmoke = new Color(0xfff5f5f5);

	/**
	 * The color Yellow. In the ABGR space.
	 * A:255, B:0, G:255, R:255
	 * 
	 */
	public final static Color Yellow = new Color(0xff00ffff);

	/**
	 * The color YellowGreen. In the ABGR space.
	 * A:255, B:50, G:205, R:154
	 * 
	 */
	public final static Color YellowGreen = new Color(0xff32cd9a);

	/**
	 * The color value in ARGB format.
	 * 
	 * @serial
	 * @see #getPackedValue()
	 */
	private int _packedValue;
	// TODO: _packedValue is a uint, test to see if it matters
	
	// Note: Added this since it is provided by default for struct in C#
	public Color()
	{
		_packedValue = 0;
	}
	
	/**
	 * Creates an ABGR color with the specified combined RGBA value consisting
	 * of the alpha component in bits 24-31, the red component in bits 16-23,
	 * the green component in bits 8-15, and the blue component in bits 0-7.
	 *
	 * @param rgba
	 *        the combined RGBA components
	 * @see java.awt.image.ColorModel#getPackedValue()default
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 * @see #getAlpha()
	 * @see #getPackedValue()
	 */
	public Color(int packedValue)
	{
		this(packedValue, true);
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
	 * @see java.awt.image.ColorModel#getPackedValue()default
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 * @see #getAlpha()
	 * @see #getPackedValue()
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
	 * Creates a new instance of {@code Color} struct.
	 * 
	 * @param color
	 *        A {@link Vector4} representing the color.
	 */
	public Color(Vector4 color)
	{
		_packedValue = 0;

		setRed((short) MathHelper.clamp(color.x * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setGreen((short) MathHelper.clamp(color.y * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setBlue((short) MathHelper.clamp(color.z * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setAlpha((short) MathHelper.clamp(color.w * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
	}

	/**
	 * Creates a new instance of the {@code Color} class.
	 * 
	 * @param color
	 *        A {@link Vector3} representing the color.
	 */
	public Color(Vector3 color)
	{
		_packedValue = 0;

		setRed((short) MathHelper.clamp(color.x * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setGreen((short) MathHelper.clamp(color.y * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setBlue((short) MathHelper.clamp(color.z * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setAlpha((short) 255);
	}

	/**
	 * Creates a new instance of the {@code Color} class.
	 * 
	 * @param color
	 *        A {@code Color} for the RGB values of new the {@code Color} instance.
	 * @param alpha
	 *        Alpha component value.
	 */
	public Color(Color color, int alpha)
	{
		setRed(color.getRed());
		setGreen(color.getGreen());
		setBlue(color.getBlue());
		setAlpha((short) MathHelper.clamp(alpha, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
	}

	/**
	 * Creates a new instance of the {@code Color} class.
	 * 
	 * @param color
	 *        A {@code Color} for the RGB values of the new {@code Color} instance.
	 * @param alpha
	 *        Alpha component value.
	 */
	public Color(Color color, float alpha)
	{
		setRed(color.getRed());
		setGreen(color.getGreen());
		setBlue(color.getBlue());
		setAlpha((short) MathHelper.clamp(alpha * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
	}

	/**
	 * Creates an opaque ABGR color with the specified red, green, and blue
	 * values in the range (0.0 - 1.0). Alpha is defaulted to 1.0. The
	 * actual color used in rendering depends on finding the best
	 * match given the color space available for a particular output
	 * device.
	 *
	 * @param r
	 *        the red component
	 * @param g
	 *        the green component
	 * @param b
	 *        the blue component
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 * @see #getPackedValue()
	 */
	public Color(float r, float g, float b)
	{
		this(r, g, b, 1.0f);
	}

	/**
	 * Creates an opaque ABGR color with the specified red, green, blue, and alpha values in the
	 * range (0.0 - 1.0). The actual color used in rendering depends on finding the best match given
	 * the color space available for a particular output device.
	 *
	 * @param r
	 *        the red component
	 * @param g
	 *        the green component
	 * @param b
	 *        the blue component
	 * @param a
	 *        the alpha component
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 * @see #getAlpha()
	 * @see #getPackedValue()
	 */
	public Color(float r, float g, float b, float a)
	{
		_packedValue = 0;

		setRed((short) MathHelper.clamp(r * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setGreen((short) MathHelper.clamp(g * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setBlue((short) MathHelper.clamp(b * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setAlpha((short) MathHelper.clamp(a * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
	}

	/**
	 * Creates an opaque ABGR color with the specified red, green, and blue values in the range (0 -
	 * 255). Alpha is defaulted to 255. The actual color used in rendering depends on finding the
	 * best match given the color space available for a given output device.
	 *
	 * @param r
	 *        the red component
	 * @param g
	 *        the green component
	 * @param b
	 *        the blue component
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 * @see #getPackedValue()
	 */
	public Color(int r, int g, int b)
	{
		this(r, g, b, 255);
	}

	/**
	 * Creates an opaque ABGR color with the specified red, green, and blue values in the range (0 -
	 * 255). The actual color used in rendering depends on finding the best match given the color
	 * space available for a given output device.
	 *
	 * @param r
	 *        the red component
	 * @param g
	 *        the green component
	 * @param b
	 *        the blue component
	 * @param a
	 *        the alpha component
	 * @see #getRed()
	 * @see #getGreen()
	 * @see #getBlue()
	 * @see #getPackedValue()
	 */
	public Color(int r, int g, int b, int a)
	{
		_packedValue = 0;

		setRed((short) MathHelper.clamp(r, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setGreen((short) MathHelper.clamp(g, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setBlue((short) MathHelper.clamp(b, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setAlpha((short) MathHelper.clamp(a, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
	}

	/**
	 * Returns the packed value of this {@code Color} in the ABGR space.
	 * 
	 * @return The packed value of this {@code Color}.
	 * @see #getPackedValue()
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

	/**
	 * Returns the blue component in the range 0-255 in the ABGR space.
	 * 
	 * @return the blue component.
	 * @see #getPackedValue()
	 */
	public short getBlue()
	{
		return (short) (this._packedValue >> 16 & 0xff);
	}

	/**
	 * Sets the blue channel to the specified value.
	 * 
	 * @param value
	 *        The new value for the blue channel
	 */
	public void setBlue(short value)
	{
		this._packedValue = (this._packedValue & 0xff00ffff) | ((int) value << 16);
	}

	/**
	 * Returns the green component in the range 0-255 in the ABGR space.
	 * 
	 * @return the green component.
	 * @see #getPackedValue()
	 */
	public short getGreen()
	{
		return (short) (this._packedValue >> 8 & 0xff);
	}

	/**
	 * Sets the green channel to the specified value.
	 * 
	 * @param value
	 *        The new value for the green channel
	 */
	public void setGreen(short value)
	{
		this._packedValue = (this._packedValue & 0xffff00ff) | ((int) value << 8);
	}

	/**
	 * Returns the red component in the range 0-255 in the ABGR space.
	 * 
	 * @return the red component.
	 * @see #getPackedValue()
	 */
	public short getRed()
	{
		return (short) (this._packedValue & 0xff);
	}

	/**
	 * Sets the red channel to the specified value.
	 * 
	 * @param value
	 *        The new value for the red channel
	 */
	public void setRed(short value)
	{
		this._packedValue = (this._packedValue & 0xffffff00) | value;
	}

	/**
	 * Returns the alpha component in the range 0-255.
	 * 
	 * @return the alpha component.
	 * @see #getPackedValue()
	 */
	public short getAlpha()
	{
		return (short) (this._packedValue >> 24 & 0xff);
	}

	/**
	 * Sets the alpha channel to the specified value.
	 * 
	 * @param value
	 *        The new value for the alpha channel
	 */
	public void setAlpha(short value)
	{
		this._packedValue = (this._packedValue & 0x00ffffff) | ((int) value << 24);
	}

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
	 * @return a new <code>Color</code> object that is
	 *         a brighter version of this <code>Color</code> with the same {@code alpha} value.
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
	 * @return a new <code>Color</code> object that is
	 *         a darker version of this <code>Color</code> with the same {@code alpha} value.
	 * @see java.awt.Color#brighter
	 * @since JDK1.0
	 */
	public Color darker()
	{
		return new Color(Math.max((int) (getRed() * FACTOR), 0), Math.max((int) (getGreen() * FACTOR), 0), Math.max(
				(int) (getBlue() * FACTOR), 0), getAlpha());
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
	
	// Helper method
	private boolean equals(Color other)
	{
		return this.getPackedValue() == other.getPackedValue();
	}

	/**
	 * Indicates whether some other object is "not equal to" this one.
	 * 
	 * @param obj
	 * 		  the reference object with which to compare.
	 * @return {@code false} if this object is the same as the obj argument;
     *         {@code true} otherwise.
     * @see #equals(Object)
	 */
	public boolean notEquals(Object obj)
	{
		return !this.equals(obj);
	}
	
	/**
	 * Computes the hash code for this <{@code Color}.
	 * 
	 * @return a hash code value for this {@code Color}.
	 */
	@Override
	public int hashCode()
	{
		return Integer.hashCode(_packedValue);
	}

	/**
	 * Performs linear interpolation of {@code Color}.
	 * 
	 * @param value1
	 *        Source {@code Color}..
	 * @param value2
	 *        Destination {@code Color}.
	 * @param amount
	 *        Interpolation factor.
	 * @return Interpolated {@code Color}.
	 */
	public static Color lerp(Color value1, Color value2, float amount)
	{
		amount = MathHelper.clamp(amount, 0.0f, 1.0f);
		return new Color((int) MathHelper.lerp(value1.getRed(), value2.getRed(), amount), (int) MathHelper.lerp(
				value1.getGreen(), value2.getGreen(), amount), (int) MathHelper.lerp(value1.getBlue(),
				value2.getBlue(), amount), (int) MathHelper.lerp(value1.getAlpha(), value2.getAlpha(), amount));
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
		return new Color((int) (value.getRed() * scale), (int) (value.getGreen() * scale),
				(int) (value.getBlue() * scale), (int) (value.getAlpha() * scale));
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
	
	// NOTE: I added these constants since Java doesn't have unsigned values
	private final short uBYTE_MIN_VALUE = 0;
	private final short uBYTE_MAX_VALUE = 255;
}
