package gameCore;

import gameCore.math.MathHelper;
import gameCore.math.Vector3;
import gameCore.math.Vector4;

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
	 * The color TransparentBlack. In the default sRGB space.
	 * R:0, G:0, B:0, A:0
	 * 
	 */
	public final static Color TransparentBlack = new Color(0);

	/**
	 * The color Transparent. In the default sRGB space.
	 * R:0,G:0,B:0,A:0
	 * 
	 */
	public final static Color Transparent = new Color(0);

	/**
	 * The color AliceBlue. In the default sRGB space.
	 * R:240,G:248,B:255,A:255
	 * 
	 */
	public final static Color AliceBlue = new Color(0xfff0f8ff);

	/**
	 * The color AntiqueWhite. In the default sRGB space.
	 * R:250,G:235,B:215,A:255
	 * 
	 */
	public final static Color AntiqueWhite = new Color(0xfffaebd7);

	/**
	 * The color Aqua. In the default sRGB space.
	 * R:0,G:255,B:255,A:255
	 * 
	 */
	public final static Color Aqua = new Color(0xff00ffff);

	/**
	 * The color Aquamarine. In the default sRGB space.
	 * R:127,G:255,B:212,A:255
	 * 
	 */
	public final static Color Aquamarine = new Color(0xff7fffd4);

	/**
	 * The color Azure. In the default sRGB space.
	 * R:240,G:255,B:255,A:255
	 * 
	 */
	public final static Color Azure = new Color(0xfff0ffff);

	/**
	 * The color Beige. In the default sRGB space.
	 * R:245,G:245,B:220,A:255
	 * 
	 */
	public final static Color Beige = new Color(0xfff5f5dc);

	/**
	 * The color Bisque. In the default sRGB space.
	 * R:255,G:228,B:196,A:255
	 * 
	 */
	public final static Color Bisque = new Color(0xffffe4c4);

	/**
	 * The color Black. In the default sRGB space.
	 * R:0,G:0,B:0,A:255
	 * 
	 */
	public final static Color Black = new Color(0xff000000);

	/**
	 * The color BlanchedAlmond. In the default sRGB space.
	 * R:255,G:235,B:205,A:255
	 * 
	 */
	public final static Color BlanchedAlmond = new Color(0xffffebcd);

	/**
	 * The color Blue. In the default sRGB space.
	 * R:0,G:0,B:255,A:255
	 * 
	 */
	public final static Color Blue = new Color(0xff0000ff);

	/**
	 * The color BlueViolet. In the default sRGB space.
	 * R:138,G:43,B:226,A:255
	 * 
	 */
	public final static Color BlueViolet = new Color(0xff8a2be2);

	/**
	 * The color Brown. In the default sRGB space.
	 * R:165,G:42,B:42,A:255
	 * 
	 */
	public final static Color Brown = new Color(0xffa52a2a);

	/**
	 * The color BurlyWood. In the default sRGB space.
	 * R:222,G:184,B:135,A:255
	 * 
	 */
	public final static Color BurlyWood = new Color(0xffdeb887);

	/**
	 * The color CadetBlue. In the default sRGB space.
	 * R:95,G:158,B:160,A:255
	 * 
	 */
	public final static Color CadetBlue = new Color(0xff5f9ea0);

	/**
	 * The color Chartreuse. In the default sRGB space.
	 * R:127,G:255,B:0,A:255
	 * 
	 */
	public final static Color Chartreuse = new Color(0xff7fff00);

	/**
	 * The color Chocolate. In the default sRGB space.
	 * R:210,G:105,B:30,A:255
	 * 
	 */
	public final static Color Chocolate = new Color(0xffd2691e);

	/**
	 * The color Coral. In the default sRGB space.
	 * R:255,G:127,B:80,A:255
	 * 
	 */
	public final static Color Coral = new Color(0xffff7f50);

	/**
	 * The color CornflowerBlue. In the default sRGB space.
	 * R:100,G:149,B:237,A:255
	 * 
	 */
	public final static Color CornflowerBlue = new Color(0xff6495ed);

	/**
	 * The color Cornsilk. In the default sRGB space.
	 * R:255,G:248,B:220,A:255
	 * 
	 */
	public final static Color Cornsilk = new Color(0xfffff8dc);

	/**
	 * The color Crimson. In the default sRGB space.
	 * R:220,G:20,B:60,A:255
	 * 
	 */
	public final static Color Crimson = new Color(0xffdc143c);

	/**
	 * The color Cyan. In the default sRGB space.
	 * R:0,G:255,B:255,A:255
	 * 
	 */
	public final static Color Cyan = new Color(0xff00ffff);

	/**
	 * The color DarkBlue. In the default sRGB space.
	 * R:0,G:0,B:139,A:255
	 * 
	 */
	public final static Color DarkBlue = new Color(0xff00008b);

	/**
	 * The color DarkCyan. In the default sRGB space.
	 * R:0,G:139,B:139,A:255
	 * 
	 */
	public final static Color DarkCyan = new Color(0xff008b8b);

	/**
	 * The color DarkGoldenrod. In the default sRGB space.
	 * R:184,G:134,B:11,A:255
	 * 
	 */
	public final static Color DarkGoldenrod = new Color(0xffb8860b);

	/**
	 * The color DarkGray. In the default sRGB space.
	 * R:169,G:169,B:169,A:255
	 * 
	 */
	public final static Color DarkGray = new Color(0xffa9a9a9);

	/**
	 * The color DarkGreen. In the default sRGB space.
	 * R:0,G:100,B:0,A:255
	 * 
	 */
	public final static Color DarkGreen = new Color(0xff006400);

	/**
	 * The color DarkKhaki. In the default sRGB space.
	 * R:189,G:183,B:107,A:255
	 * 
	 */
	public final static Color DarkKhaki = new Color(0xffbdb76b);

	/**
	 * The color DarkMagenta. In the default sRGB space.
	 * R:139,G:0,B:139,A:255
	 * 
	 */
	public final static Color DarkMagenta = new Color(0xff8b008b);

	/**
	 * The color DarkOliveGreen. In the default sRGB space.
	 * R:85,G:107,B:47,A:255
	 * 
	 */
	public final static Color DarkOliveGreen = new Color(0xff556b2f);

	/**
	 * The color DarkOrange. In the default sRGB space.
	 * R:255,G:140,B:0,A:255
	 * 
	 */
	public final static Color DarkOrange = new Color(0xffff8c00);

	/**
	 * The color DarkOrchid. In the default sRGB space.
	 * R:153,G:50,B:204,A:255
	 * 
	 */
	public final static Color DarkOrchid = new Color(0xff9932cc);

	/**
	 * The color DarkRed. In the default sRGB space.
	 * R:139,G:0,B:0,A:255
	 * 
	 */
	public final static Color DarkRed = new Color(0xff8b0000);

	/**
	 * The color DarkSalmon. In the default sRGB space.
	 * R:233,G:150,B:122,A:255
	 * 
	 */
	public final static Color DarkSalmon = new Color(0xffe9967a);

	/**
	 * The color DarkSeaGreen. In the default sRGB space.
	 * R:143,G:188,B:139,A:255
	 * 
	 */
	public final static Color DarkSeaGreen = new Color(0xff8fbc8b);

	/**
	 * The color DarkSlateBlue. In the default sRGB space.
	 * R:72,G:61,B:139,A:255
	 * 
	 */
	public final static Color DarkSlateBlue = new Color(0xff483d8b);

	/**
	 * The color DarkSlateGray. In the default sRGB space.
	 * R:47,G:79,B:79,A:255
	 * 
	 */
	public final static Color DarkSlateGray = new Color(0xff2f4f4f);

	/**
	 * The color DarkTurquoise. In the default sRGB space.
	 * R:0,G:206,B:209,A:255
	 * 
	 */
	public final static Color DarkTurquoise = new Color(0xff00ced1);

	/**
	 * The color DarkViolet. In the default sRGB space.
	 * R:148,G:0,B:211,A:255
	 * 
	 */
	public final static Color DarkViolet = new Color(0xff9400d3);

	/**
	 * The color DeepPink. In the default sRGB space.
	 * R:255,G:20,B:147,A:255
	 * 
	 */
	public final static Color DeepPink = new Color(0xffff1493);

	/**
	 * The color DeepSkyBlue. In the default sRGB space.
	 * R:0,G:191,B:255,A:255
	 * 
	 */
	public final static Color DeepSkyBlue = new Color(0xff00bfff);

	/**
	 * The color DimGray. In the default sRGB space.
	 * R:105,G:105,B:105,A:255
	 * 
	 */
	public final static Color DimGray = new Color(0xff696969);

	/**
	 * The color DodgerBlue. In the default sRGB space.
	 * R:30,G:144,B:255,A:255
	 * 
	 */
	public final static Color DodgerBlue = new Color(0xff1e90ff);

	/**
	 * The color Firebrick. In the default sRGB space.
	 * R:178,G:34,B:34,A:255
	 * 
	 */
	public final static Color Firebrick = new Color(0xffb22222);

	/**
	 * The color FloralWhite. In the default sRGB space.
	 * R:255,G:250,B:240,A:255
	 * 
	 */
	public final static Color FloralWhite = new Color(0xfffffaf0);

	/**
	 * The color ForestGreen. In the default sRGB space.
	 * R:34,G:139,B:34,A:255
	 * 
	 */
	public final static Color ForestGreen = new Color(0xff228b22);

	/**
	 * The color Fuchsia. In the default sRGB space.
	 * R:255,G:0,B:255,A:255
	 * 
	 */
	public final static Color Fuchsia = new Color(0xffff00ff);

	/**
	 * The color Gainsboro. In the default sRGB space.
	 * R:220,G:220,B:220,A:255
	 * 
	 */
	public final static Color Gainsboro = new Color(0xffdcdcdc);

	/**
	 * The color GhostWhite. In the default sRGB space.
	 * R:248,G:248,B:255,A:255
	 * 
	 */
	public final static Color GhostWhite = new Color(0xfff8f8ff);

	/**
	 * The color Goldenrod. In the default sRGB space.
	 * R:255,G:215,B:0,A:255
	 * 
	 */
	public final static Color Gold = new Color(0xffffd700);

	/**
	 * The color Goldenrod. In the default sRGB space.
	 * R:218,G:165,B:32,A:255
	 * 
	 */
	public final static Color Goldenrod = new Color(0xffdaa520);

	/**
	 * The color Gray. In the default sRGB space.
	 * R:128,G:128,B:128,A:255
	 * 
	 */
	public final static Color Gray = new Color(0xff808080);

	/**
	 * The color Green. In the default sRGB space.
	 * R:0,G:128,B:0,A:255
	 * 
	 */
	public final static Color Green = new Color(0xff008000);

	/**
	 * The color GreenYellow. In the default sRGB space.
	 * R:173,G:255,B:47,A:255
	 * 
	 */
	public final static Color GreenYellow = new Color(0xffadff2f);

	/**
	 * The color Honeydew. In the default sRGB space.
	 * R:240,G:255,B:240,A:255
	 * 
	 */
	public final static Color Honeydew = new Color(0xfff0fff0);

	/**
	 * The color HotPink. In the default sRGB space.
	 * R:255,G:105,B:180,A:255
	 * 
	 */
	public final static Color HotPink = new Color(0xffff69b4);

	/**
	 * The color IndianRed. In the default sRGB space.
	 * R:205,G:92,B:92,A:255
	 * 
	 */
	public final static Color IndianRed = new Color(0xffcd5c5c);

	/**
	 * The color Indigo. In the default sRGB space.
	 * R:75,G:0,B:130,A:255
	 * 
	 */
	public final static Color Indigo = new Color(0xff4b0082);

	/**
	 * The color Ivory. In the default sRGB space.
	 * R:255,G:255,B:240,A:255
	 * 
	 */
	public final static Color Ivory = new Color(0xfffffff0);

	/**
	 * The color Khaki. In the default sRGB space.
	 * R:240,G:230,B:140,A:255
	 * 
	 */
	public final static Color Khaki = new Color(0xfff0e68c);

	/**
	 * The color Lavender. In the default sRGB space.
	 * R:230,G:230,B:250,A:255
	 * 
	 */
	public final static Color Lavender = new Color(0xffe6e6fa);

	/**
	 * The color LavenderBlush. In the default sRGB space.
	 * R:255,G:240,B:245,A:255
	 * 
	 */
	public final static Color LavenderBlush = new Color(0xfffff0f5);

	/**
	 * The color LawnGreen. In the default sRGB space.
	 * R:124,G:252,B:0,A:255
	 * 
	 */
	public final static Color LawnGreen = new Color(0xff7cfc00);

	/**
	 * The color LemonChiffon. In the default sRGB space.
	 * R:255,G:250,B:205,A:255
	 * 
	 */
	public final static Color LemonChiffon = new Color(0xfffffacd);

	/**
	 * The color LightBlue. In the default sRGB space.
	 * R:173,G:216,B:230,A:255
	 * 
	 */
	public final static Color LightBlue = new Color(0xffadd8e6);

	/**
	 * The color LightCoral. In the default sRGB space.
	 * R:240,G:128,B:128,A:255
	 * 
	 */
	public final static Color LightCoral = new Color(0xfff08080);

	/**
	 * The color LightCyan. In the default sRGB space.
	 * R:224,G:255,B:255,A:255
	 * 
	 */
	public final static Color LightCyan = new Color(0xffe0ffff);

	/**
	 * The color LightGoldenrodYellow. In the default sRGB space.
	 * R:250,G:250,B:210,A:255
	 * 
	 */
	public final static Color LightGoldenrodYellow = new Color(0xfffafad2);

	/**
	 * The color LightGray. In the default sRGB space.
	 * R:211,G:211,B:211,A:255
	 * 
	 */
	public final static Color LightGray = new Color(0xffd3d3d3);

	/**
	 * The color LightGreen. In the default sRGB space.
	 * R:144,G:238,B:144,A:255
	 * 
	 */
	public final static Color LightGreen = new Color(0xff90ee90);

	/**
	 * The color LightPink. In the default sRGB space.
	 * R:255,G:182,B:193,A:255
	 * 
	 */
	public final static Color LightPink = new Color(0xffffb6c1);

	/**
	 * The color LightSalmon. In the default sRGB space.
	 * R:255,G:160,B:122,A:255
	 * 
	 */
	public final static Color LightSalmon = new Color(0xffffa07a);

	/**
	 * The color LightSeaGreen. In the default sRGB space.
	 * R:32,G:178,B:170,A:255
	 * 
	 */
	public final static Color LightSeaGreen = new Color(0xff20b2aa);

	/**
	 * The color LightSkyBlue. In the default sRGB space.
	 * R:135,G:206,B:250,A:255
	 * 
	 */
	public final static Color LightSkyBlue = new Color(0xff87cefa);

	/**
	 * The color LightSlateGray. In the default sRGB space.
	 * R:119,G:136,B:153,A:255
	 * 
	 */
	public final static Color LightSlateGray = new Color(0xff778899);

	/**
	 * The color LightSteelBlue. In the default sRGB space.
	 * R:176,G:196,B:222,A:255
	 * 
	 */
	public final static Color LightSteelBlue = new Color(0xffb0c4de);

	/**
	 * The color LightYellow. In the default sRGB space.
	 * R:255,G:255,B:224,A:255
	 * 
	 */
	public final static Color LightYellow = new Color(0xffffffe0);

	/**
	 * The color Lime. In the default sRGB space.
	 * R:0,G:255,B:0,A:255
	 * 
	 */
	public final static Color Lime = new Color(0xff00ff00);

	/**
	 * The color LimeGreen. In the default sRGB space.
	 * R:50,G:205,B:50,A:255
	 * 
	 */
	public final static Color LimeGreen = new Color(0xff32cd32);

	/**
	 * The color Linen. In the default sRGB space.
	 * R:250,G:240,B:230,A:255
	 * 
	 */
	public final static Color Linen = new Color(0xfffaf0e6);

	/**
	 * The color Magenta. In the default sRGB space.
	 * R:255,G:0,B:255,A:255
	 * 
	 */
	public final static Color Magenta = new Color(0xffff00ff);

	/**
	 * The color Maroon. In the default sRGB space.
	 * R:128,G:0,B:0,A:255
	 * 
	 */
	public final static Color Maroon = new Color(0xff800000);

	/**
	 * The color MediumAquamarine. In the default sRGB space.
	 * R:102,G:205,B:170,A:255
	 * 
	 */
	public final static Color MediumAquamarine = new Color(0xff66cdaa);

	/**
	 * The color MediumBlue. In the default sRGB space.
	 * R:0,G:0,B:205,A:255
	 * 
	 */
	public final static Color MediumBlue = new Color(0xff0000cd);

	/**
	 * The color MediumOrchid. In the default sRGB space.
	 * R:186,G:85,B:211,A:255
	 * 
	 */
	public final static Color MediumOrchid = new Color(0xffba55d3);

	/**
	 * The color MediumPurple. In the default sRGB space.
	 * R:147,G:112,B:219,A:255
	 * 
	 */
	public final static Color MediumPurple = new Color(0xff9370db);

	/**
	 * The color MediumSeaGreen. In the default sRGB space.
	 * R:60,G:179,B:113,A:255
	 * 
	 */
	public final static Color MediumSeaGreen = new Color(0xff3cb371);

	/**
	 * The color MediumSlateBlue. In the default sRGB space.
	 * R:123,G:104,B:238,A:255
	 * 
	 */
	public final static Color MediumSlateBlue = new Color(0xff7b68ee);

	/**
	 * The color MediumSpringGreen. In the default sRGB space.
	 * R:0,G:250,B:154,A:255
	 * 
	 */
	public final static Color MediumSpringGreen = new Color(0xff00fa9a);

	/**
	 * The color MediumTurquoise. In the default sRGB space.
	 * R:72,G:209,B:204,A:255
	 * 
	 */
	public final static Color MediumTurquoise = new Color(0xff48d1cc);

	/**
	 * The color MediumVioletRed. In the default sRGB space.
	 * R:199,G:21,B:133,A:255
	 * 
	 */
	public final static Color MediumVioletRed = new Color(0xffc71585);

	/**
	 * The color MidnightBlue. In the default sRGB space.
	 * R:25,G:25,B:112,A:255
	 * 
	 */
	public final static Color MidnightBlue = new Color(0xff191970);

	/**
	 * The color MintCream. In the default sRGB space.
	 * R:245,G:255,B:250,A:255
	 * 
	 */
	public final static Color MintCream = new Color(0xfff5fffa);

	/**
	 * The color MistyRose. In the default sRGB space.
	 * R:255,G:228,B:225,A:255
	 * 
	 */
	public final static Color MistyRose = new Color(0xffffe4e1);

	/**
	 * The color Moccasin. In the default sRGB space.
	 * R:255,G:228,B:181,A:255
	 * 
	 */
	public final static Color Moccasin = new Color(0xffffe4b5);

	/**
	 * The color Moccasin. In the default sRGB space.
	 * R:255,G:228,B:181,A:255
	 * 
	 */
	public final static Color MonoGameOrange = new Color(0xffe73c00);

	/**
	 * The color NavajoWhite. In the default sRGB space.
	 * R:255,G:222,B:173,A:255
	 * 
	 */
	public final static Color NavajoWhite = new Color(0xffffdead);

	/**
	 * The color Navy. In the default sRGB space.
	 * R:0,G:0,B:128,A:255
	 * 
	 */
	public final static Color Navy = new Color(0xff000080);

	/**
	 * The color OldLace. In the default sRGB space.
	 * R:253,G:245,B:230,A:255
	 * 
	 */
	public final static Color OldLace = new Color(0xfffdf5e6);

	/**
	 * The color Olive. In the default sRGB space.
	 * R:128,G:128,B:0,A:255
	 * 
	 */
	public final static Color Olive = new Color(0xff808000);

	/**
	 * The color OliveDrab. In the default sRGB space.
	 * R:107,G:142,B:35,A:255
	 * 
	 */
	public final static Color OliveDrab = new Color(0xff6b8e23);

	/**
	 * The color Orange. In the default sRGB space.
	 * R:255,G:165,B:0,A:255
	 * 
	 */
	public final static Color Orange = new Color(0xffffa500);

	/**
	 * The color OrangeRed. In the default sRGB space.
	 * R:255,G:69,B:0,A:255
	 * 
	 */
	public final static Color OrangeRed = new Color(0xffff4500);

	/**
	 * The color Orchid. In the default sRGB space.
	 * R:218,G:112,B:214,A:255
	 * 
	 */
	public final static Color Orchid = new Color(0xffda70d6);

	/**
	 * The color PaleGoldenrod. In the default sRGB space.
	 * R:238,G:232,B:170,A:255
	 * 
	 */
	public final static Color PaleGoldenrod = new Color(0xffeee8aa);

	/**
	 * The color PaleGreen. In the default sRGB space.
	 * R:152,G:251,B:152,A:255
	 * 
	 */
	public final static Color PaleGreen = new Color(0xff98fb98);

	/**
	 * The color PaleTurquoise. In the default sRGB space.
	 * R:175,G:238,B:238,A:255
	 * 
	 */
	public final static Color PaleTurquoise = new Color(0xffafeeee);

	/**
	 * The color PaleVioletRed. In the default sRGB space.
	 * R:219,G:112,B:147,A:255
	 * 
	 */
	public final static Color PaleVioletRed = new Color(0xffdb7093);

	/**
	 * The color PapayaWhip. In the default sRGB space.
	 * R:255,G:239,B:213,A:255
	 * 
	 */
	public final static Color PapayaWhip = new Color(0xffffefd5);

	/**
	 * The color PeachPuff. In the default sRGB space.
	 * R:255,G:218,B:185,A:255
	 * 
	 */
	public final static Color PeachPuff = new Color(0xffffdab9);

	/**
	 * The color Peru. In the default sRGB space.
	 * R:205,G:133,B:63,A:255
	 * 
	 */
	public final static Color Peru = new Color(0xffcd853f);

	/**
	 * The color Pink. In the default sRGB space.
	 * R:255,G:192,B:203,A:255
	 * 
	 */
	public final static Color Pink = new Color(0xffffc0cb);

	/**
	 * The color Plum. In the default sRGB space.
	 * R:221,G:160,B:221,A:255
	 * 
	 */
	public final static Color Plum = new Color(0xffdda0dd);

	/**
	 * The color PowderBlue. In the default sRGB space.
	 * R:176,G:224,B:230,A:255
	 * 
	 */
	public final static Color PowderBlue = new Color(0xffb0e0e6);

	/**
	 * The color Purple. In the default sRGB space.
	 * R:128,G:0,B:128,A:255
	 * 
	 */
	public final static Color Purple = new Color(0xff800080);

	/**
	 * The color Red. In the default sRGB space.
	 * R:255,G:0,B:0,A:255
	 * 
	 */
	public final static Color Red = new Color(0xffff0000);

	/**
	 * The color RosyBrown. In the default sRGB space.
	 * R:188,G:143,B:143,A:255
	 * 
	 */
	public final static Color RosyBrown = new Color(0xffbc8f8f);

	/**
	 * The color RoyalBlue. In the default sRGB space.
	 * R:65,G:105,B:225,A:255
	 * 
	 */
	public final static Color RoyalBlue = new Color(0xff4169e1);

	/**
	 * The color SaddleBrown. In the default sRGB space.
	 * R:139,G:69,B:19,A:255
	 * 
	 */
	public final static Color SaddleBrown = new Color(0xff8b4513);

	/**
	 * The color Salmon. In the default sRGB space.
	 * R:250,G:128,B:114,A:255
	 * 
	 */
	public final static Color Salmon = new Color(0xfffa8072);

	/**
	 * The color SandyBrown. In the default sRGB space.
	 * R:244,G:164,B:96,A:255
	 * 
	 */
	public final static Color SandyBrown = new Color(0xfff4a460);

	/**
	 * The color SeaGreen. In the default sRGB space.
	 * R:46,G:139,B:87,A:255
	 * 
	 */
	public final static Color SeaGreen = new Color(0xff2e8b57);

	/**
	 * The color SeaShell. In the default sRGB space.
	 * R:255,G:245,B:238,A:255
	 * 
	 */
	public final static Color SeaShell = new Color(0xfffff5ee);

	/**
	 * The color Sienna. In the default sRGB space.
	 * R:160,G:82,B:45,A:255
	 * 
	 */
	public final static Color Sienna = new Color(0xffa0522d);

	/**
	 * The color Silver. In the default sRGB space.
	 * R:192,G:192,B:192,A:255
	 * 
	 */
	public final static Color Silver = new Color(0xffc0c0c0);

	/**
	 * The color SkyBlue. In the default sRGB space.
	 * R:135,G:206,B:235,A:255
	 * 
	 */
	public final static Color SkyBlue = new Color(0xff87ceeb);

	/**
	 * The color SlateBlue. In the default sRGB space.
	 * R:106,G:90,B:205,A:255
	 * 
	 */
	public final static Color SlateBlue = new Color(0xff6a5acd);

	/**
	 * The color SlateGray. In the default sRGB space.
	 * R:112,G:128,B:144,A:255
	 * 
	 */
	public final static Color SlateGray = new Color(0xff708090);

	/**
	 * The color Snow. In the default sRGB space.
	 * R:255,G:250,B:250,A:255
	 * 
	 */
	public final static Color Snow = new Color(0xfffffafa);

	/**
	 * The color SpringGreen. In the default sRGB space.
	 * R:0,G:255,B:127,A:255
	 * 
	 */
	public final static Color SpringGreen = new Color(0xff00ff7f);

	/**
	 * The color SteelBlue. In the default sRGB space.
	 * R:70,G:130,B:180,A:255
	 * 
	 */
	public final static Color SteelBlue = new Color(0xff4682b4);

	/**
	 * The color Tan. In the default sRGB space.
	 * R:210,G:180,B:140,A:255
	 * 
	 */
	public final static Color Tan = new Color(0xffd2b48c);

	/**
	 * The color Teal. In the default sRGB space.
	 * R:0,G:128,B:128,A:255
	 * 
	 */
	public final static Color Teal = new Color(0xff008080);

	/**
	 * The color Thistle. In the default sRGB space.
	 * R:216,G:191,B:216,A:255
	 * 
	 */
	public final static Color Thistle = new Color(0xffd8bfd8);

	/**
	 * The color Tomato. In the default sRGB space.
	 * R:255,G:99,B:71,A:255
	 * 
	 */
	public final static Color Tomato = new Color(0xffff6347);

	/**
	 * The color Turquoise. In the default sRGB space.
	 * R:64,G:224,B:208,A:255
	 * 
	 */
	public final static Color Turquoise = new Color(0xff40e0d0);

	/**
	 * The color Violet. In the default sRGB space.
	 * R:238,G:130,B:238,A:255
	 * 
	 */
	public final static Color Violet = new Color(0xffee82ee);

	/**
	 * The color Wheat. In the default sRGB space.
	 * R:245,G:222,B:179,A:255
	 * 
	 */
	public final static Color Wheat = new Color(0xfff5deb3);

	/**
	 * The color White. In the default sRGB space.
	 * R:255,G:255,B:255,A:255
	 * 
	 */
	public final static Color White = new Color(0xffffffff);

	/**
	 * The color WhiteSmoke. In the default sRGB space.
	 * R:245,G:245,B:245,A:255
	 * 
	 */
	public final static Color WhiteSmoke = new Color(0xfff5f5f5);

	/**
	 * The color Yellow. In the default sRGB space.
	 * R:255,G:255,B:0,A:255
	 * 
	 */
	public final static Color Yellow = new Color(0xffffff00);

	/**
	 * The color YellowGreen. In the default sRGB space.
	 * R:154,G:205,B:50,A:255
	 * 
	 */
	public final static Color YellowGreen = new Color(0xff9acd32);

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
	 * Creates an sRGB color with the specified combined RGBA value consisting
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
	 * Creates an sRGB color with the specified combined RGBA value consisting
	 * of the alpha component in bits 24-31, the red component in bits 16-23,
	 * the green component in bits 8-15, and the blue component in bits 0-7.
	 * If the <code>hasalpha</code> argument is <code>false</code>, alpha
	 * is defaulted to 255.
	 *
	 * @param rgba
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
	public Color(int rgba, boolean hasalpha)
	{
		if (hasalpha)
		{
			_packedValue = rgba;
		}
		else
		{
			_packedValue = 0xff000000 | rgba;
		}
	}

	// / <summary>
	// / Creates a new instance of {@code Color} struct.
	// / </summary>
	// / <param name="color">A <see cref="Vector4"/> representing color.</param>
	/**
	 * 
	 * @param color
	 */
	public Color(Vector4 color)
	{
		_packedValue = 0;

		setRed((short) MathHelper.clamp(color.x * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setGreen((short) MathHelper.clamp(color.y * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setBlue((short) MathHelper.clamp(color.z * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setAlpha((short) MathHelper.clamp(color.w * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
	}

	// / <summary>
	// / Creates a new instance of {@code Color} struct.
	// / </summary>
	// / <param name="color">A <see cref="Vector3"/> representing color.</param>
	/**
	 * 
	 * @param color
	 */
	public Color(Vector3 color)
	{
		_packedValue = 0;

		setRed((short) MathHelper.clamp(color.x * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setGreen((short) MathHelper.clamp(color.y * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setBlue((short) MathHelper.clamp(color.z * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
		setAlpha((short) 255);
	}

	// / <summary>
	// / Creates a new instance of {@code Color} struct.
	// / </summary>
	// / <param name="color">A {@code Color} for RGB values of new {@code Color} instance.</param>
	// / <param name="alpha">Alpha component value.</param>
	/**
	 * 
	 * @param color
	 * @param alpha
	 */
	public Color(Color color, int alpha)
	{
		setRed(color.getRed());
		setGreen(color.getGreen());
		setBlue(color.getBlue());
		setAlpha((short) MathHelper.clamp(alpha, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
	}

	// / <summary>
	// / Creates a new instance of {@code Color} struct.
	// / </summary>
	// / <param name="color">A {@code Color} for RGB values of new {@code Color} instance.</param>
	// / <param name="alpha">Alpha component value.</param>
	/**
	 * 
	 * @param color
	 * @param alpha
	 */
	public Color(Color color, float alpha)
	{
		setRed(color.getRed());
		setGreen(color.getGreen());
		setBlue(color.getBlue());
		setAlpha((short) MathHelper.clamp(alpha * 255, uBYTE_MIN_VALUE, uBYTE_MAX_VALUE));
	}

	/**
	 * Creates an opaque sRGB color with the specified red, green, and blue
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
	 * Creates an opaque sRGB color with the specified red, green, blue, and alpha values in the
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
	 * Creates an opaque sRGB color with the specified red, green, and blue values in the range (0 -
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
	 * Creates an opaque sRGB color with the specified red, green, and blue values in the range (0 -
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
	 * Returns the packed value of this {@code Color} in the default sRGB space.
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
	 * Returns the blue component in the range 0-255 in the default sRGB
	 * space.
	 * 
	 * @return the blue component.
	 * @see #getPackedValue()
	 */
	public short getBlue()
	{
		return (short) (this._packedValue & 0xff);
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
	 * Returns the green component in the range 0-255 in the default sRGB
	 * space.
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
	 * Returns the red component in the range 0-255 in the default sRGB
	 * space.
	 * 
	 * @return the red component.
	 * @see #getPackedValue()
	 */
	public short getRed()
	{
		return (short) (this._packedValue >> 16 & 0xff);
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
	 * Determines whether another object is equal to this <code>Color</code>.
	 * <p>
	 * The result is <code>true</code> if and only if the argument is not <code>null</code> and is a
	 * <code>Color</code> object that has the same red, green, blue, and alpha values as this
	 * object.
	 * 
	 * @param obj
	 *        the object to test for equality with this <code>Color</code>
	 * @return <code>true</code> if the objects are the same; <code>false</code> otherwise.
	 * @since JDK1.0
	 */
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Color && ((Color) obj).getPackedValue() == this.getPackedValue();
	}

	/**
	 * Compares whether two {@code Color} instances are equal.
	 * 
	 * @param a
	 *        {@code Color} instance on the left of the equal sign.
	 * @param b
	 *        {@code Color} instance on the right of the equal sign.
	 * @return {@code true} if the instances are equal; {@code false} otherwise.
	 */
	public static boolean equals(Color a, Color b)
	{
		return a.getPackedValue() == b.getPackedValue();
	}

	/**
	 * Compares whether two {@code Color} instances are not equal.
	 * 
	 * @param a
	 *        {@code Color} instance on the left of the equal sign.
	 * @param b
	 *        {@code Color} instance on the right of the equal sign.
	 * @return {@code true} if the instances are equal; {@code false} otherwise.
	 */
	public static boolean notEquals(Color a, Color b)
	{
		return !equals(a, b);
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
	 * Gets a three-component {@link Vector3} representation for this object.
	 * 
	 * @return A three-component {@link Vector3} representation for this object.
	 */
	public Vector3 toVector3()
	{
		return new Vector3(getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f);
	}

	// / <summary>
	// / Gets a four-component <see cref="Vector4"/> representation for this object.
	// / </summary>
	// / <returns>A four-component <see cref="Vector4"/> representation for this object.</returns>
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

	// / <summary>
	// / Returns a <see cref="String"/> representation of this <see cref="Color"/> in the format:
	// / {R:[red] G:[green] B:[blue] A:[alpha]}
	// / </summary>
	// / <returns><see cref="String"/> representation of this <see cref="Color"/>.</returns>
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
