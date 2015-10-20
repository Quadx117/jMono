package gameCore.input;

import java.util.HashMap;
import java.util.Map;

// TODO: Do I want to refactor these to the same as KeyEvent.class
// (see also switch statement in KeyboardInput.java

/**
 * Defines the keys on a keyboard.
 * 
 * @author Eric Perron
 *
 */
public enum Keys
{
	/**
	 * Reserved.
	 */
	None(0),
	/**
	 * BACKSPACE key.
	 */
	Back(8),
	/**
	 * TAB key.
	 */
	Tab(9),					// Doesn't seem to be processed in Java 
	/**
	 * ENTER key.
	 */
	Enter(13),				// Java 10,
	/**
	 * CAPS LOCK key.
	 */
	CapsLock(20),
	/**
	 * ESC key.
	 */
	Escape(27),
	/**
	 * SPACEBAR key.
	 */
	Space(32),
	/**
	 * PAGE UP key.
	 */
	PageUp(33),
	/**
	 * PAGE DOWN key.
	 */
	PageDown(34),
	/**
	 * END key.
	 */
	End(35),
	/**
	 * HOME key.
	 */
	Home(36),
	/**
	 * LEFT ARROW key.
	 */
	Left(37),
	/**
	 * UP ARROW key.
	 */
	Up(38),
	/**
	 * RIGHT ARROW key.
	 */
	Right(39),
	/**
	 * DOWN ARROW key.
	 */
	Down(40),
	/**
	 * SELECT key.
	 */
	Select(41),				// Don't have it on my keyboard
	/**
	 * PRINT key.
	 */
	Print(42),
	/**
	 * EXECUTE key.
	 */
	Execute(43),			// Don't have it on my keyboard
	/**
	 * PRINT SCREEN key.
	 */
	PrintScreen(44),
	/**
	 * INS key.
	 */
	Insert(45),				// Java 155,
	/**
	 * DEL key.
	 */
	Delete(46),				// Java 127
	/**
	 * HELP key.
	 */
	Help(47),				// Don't have it on my keyboard
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	D0(48),
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	D1(49),
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	D2(50),
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	D3(51),
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	D4(52),
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	D5(53),
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	D6(54),
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	D7(55),
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	D8(56),
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	D9(57),
	/**
	 * A key.
	 */
	A(65),
	/**
	 * B key.
	 */
	B(66),
	/**
	 * C key.
	 */
	C(67),
	/**
	 * D key.
	 */
	D(68),
	/**
	 * E key.
	 */
	E(69),
	/**
	 * F key.
	 */
	F(70),
	/**
	 * G key.
	 */
	G(71),
	/**
	 * H key.
	 */
	H(72),
	/**
	 * 
	 */
	I(73),
	/**
	 * J key.
	 */
	J(74),
	/**
	 * K key.
	 */
	K(75),
	/**
	 * L key.
	 */
	L(76),
	/**
	 * M key.
	 */
	M(77),
	/**
	 * N key.
	 */
	N(78),
	/**
	 * O key.
	 */
	O(79),
	/**
	 * P key.
	 */
	P(80),
	/**
	 * Q key.
	 */
	Q(81),
	/**
	 * R key.
	 */
	R(82),
	/**
	 * S key.
	 */
	S(83),
	/**
	 * T key.
	 */
	T(84),
	/**
	 * U key.
	 */
	U(85),
	/**
	 * V key.
	 */
	V(86),
	/**
	 * W key.
	 */
	W(87),
	/**
	 * X key.
	 */
	X(88),
	/**
	 * Y key.
	 */
	Y(89),
	/**
	 * Z key.
	 */
	Z(90),
	/**
	 * Left Windows key.
	 */
	LeftWindows(91),		// Java 524
	/**
	 * Right Windows key.
	 */
	RightWindows(92),		// Java 524
	/**
	 * Applications key.
	 */
	Apps(93),				// Java 525
	/**
	 * Computer Sleep key.
	 */
	Sleep(95),				// Don't have it on my keyboard
	/**
	 * Numeric keypad 0 key.
	 */
	NumPad0(96),
	/**
	 * Numeric keypad 1 key.
	 */
	NumPad1(97),
	/**
	 * Numeric keypad 2 key.
	 */
	NumPad2(98),
	/**
	 * Numeric keypad 3 key.
	 */
	NumPad3(99),
	/**
	 * Numeric keypad 4 key.
	 */
	NumPad4(100),
	/**
	 * Numeric keypad 5 key.
	 */
	NumPad5(101),
	/**
	 * Numeric keypad 6 key.
	 */
	NumPad6(102),
	/**
	 * Numeric keypad 7 key.
	 */
	NumPad7(103),
	/**
	 * Numeric keypad 8 key.
	 */
	NumPad8(104),
	/**
	 * Numeric keypad 9 key.
	 */
	NumPad9(105),
	/**
	 * Multiply key.
	 */
	Multiply(106),
	/**
	 * Add key.
	 */
	Add(107),
	/**
	 * Separator key.
	 */
	Separator(108),			// Don't have it on my keyboard
	/**
	 * Subtract key.
	 */
	Subtract(109),
	/**
	 * Decimal key.
	 */
	Decimal(110),
	/**
	 * Divide key.
	 */
	Divide(111),
	/**
	 * F1 key.
	 */
	F1(112),
	/**
	 * F2 key.
	 */
	F2(113),
	/**
	 * F3 key.
	 */
	F3(114),
	/**
	 * F4 key.
	 */
	F4(115),
	/**
	 * F5 key.
	 */
	F5(116),
	/**
	 * F6 key.
	 */
	F6(117),
	/**
	 * F7 key.
	 */
	F7(118),
	/**
	 * F8 key.
	 */
	F8(119),
	/**
	 * F9 key.
	 */
	F9(120),
	/**
	 * F10 key.
	 */
	F10(121),
	/**
	 * F11 key.
	 */
	F11(122),
	/**
	 * F12 key.
	 */
	F12(123),
	/**
	 * F13 key.
	 */
	F13(124),				// Don't have it on my keyboard
	/**
	 * F14 key.
	 */
	F14(125),				// Don't have it on my keyboard
	/**
	 * F15 key.
	 */
	F15(126),				// Don't have it on my keyboard
	/**
	 * F16 key.
	 */
	F16(127),				// Don't have it on my keyboard
	/**
	 * F17 key.
	 */
	F17(128),				// Don't have it on my keyboard
	/**
	 * F18 key.
	 */
	F18(129),				// Don't have it on my keyboard
	/**
	 * F19 key.
	 */
	F19(130),				// Don't have it on my keyboard
	/**
	 * F20 key.
	 */
	F20(131),				// Don't have it on my keyboard// Not tested in Java
	/**
	 * F21 key.
	 */
	F21(132),				// Don't have it on my keyboard
	/**
	 * F22 key.
	 */
	F22(133),				// Don't have it on my keyboard
	/**
	 * F23 key.
	 */
	F23(134),				// Don't have it on my keyboard
	/**
	 * F24 key.
	 */
	F24(135),				// Don't have it on my keyboard
	/**
	 * NUM LOCK key.
	 */
	NumLock(144),
	/**
	 * SCROLL LOCK key.
	 */
	Scroll(145),
	/**
	 * Left SHIFT key.
	 */
	LeftShift(160),			// Java 16
	/**
	 * Right SHIFT key.
	 */
	RightShift(161),		// Java 16
	/**
	 * Left CONTROL key.
	 */
	LeftControl(162),		// Java 17
	/**
	 * Right CONTROL key.
	 */
	RightControl(163),		// Java 17
	/**
	 * Left ALT key.
	 */
	LeftAlt(164),			// Java 18
	/**
	 * Right ALT key.
	 */
	RightAlt(165),			// Java 17 +18 ?
	/**
	 * Browser Back key.
	 */
	BrowserBack(166),		// Can't find it in Java
	/**
	 * Browser Forward key.
	 */
	BrowserForward(167),	// Can't find it in Java
	/**
	 * Browser Refresh key.
	 */
	BrowserRefresh(168),	// Can't find it in Java
	/**
	 * Browser Stop key.
	 */
	BrowserStop(169),		// Can't find it in Java
	/**
	 * Browser Search key.
	 */
	BrowserSearch(170),		// Don't have it on my keyboard
	/**
	 * Browser Favorites key.
	 */
	BrowserFavorites(171),	// Don't have it on my keyboard
	/**
	 * Browser Start and Home key.
	 */
	BrowserHome(172),		// Can't find it in Java
	/**
	 * Volume Mute key.
	 */
	VolumeMute(173),		// Can't find it in Java
	/**
	 * Volume Down key.
	 */
	VolumeDown(174),		// Can't find it in Java
	/**
	 * Volume Up key.
	 */
	VolumeUp(175),			// Can't find it in Java
	/**
	 * Next Track key.
	 */
	MediaNextTrack(176),	// Can't find it in Java
	/**
	 * Previous Track key.
	 */
	MediaPreviousTrack(177),	// Can't find it in Java
	/**
	 * Stop Media key.
	 */
	MediaStop(178),			// Can't find it in Java
	/**
	 * Play/Pause Media key.
	 */
	MediaPlayPause(179),	// Can't find it in Java
	/**
	 * Start Mail key.
	 */
	LaunchMail(180),		// Can't find it in Java
	/**
	 * Select Media key.
	 */
	SelectMedia(181),		// Don't have it on my keyboard
	/**
	 * Start Application 1 key.
	 */
	LaunchApplication1(182),	// Can't find it in Java
	/**
	 * Start Application 2 key.
	 */
	LaunchApplication2(183),	// Can't find it in Java
	/**
	 * The OEM Semicolon key on a US standard keyboard.
	 */
	OemSemicolon(186),		// Java 59
	/**
	 * For any country/region, the '+' key.
	 */
	OemPlus(187),			// Java 61
	/**
	 * For any country/region, the '),' key.
	 */
	OemComma(188),			// Java 44
	/**
	 * For any country/region, the '-' key.
	 */
	OemMinus(189),			// Java 45
	/**
	 * For any country/region, the '.' key.
	 */
	OemPeriod(190),
	/**
	 * The OEM question mark key on a US standard keyboard.
	 */
	OemQuestion(191),
	/**
	 * The OEM tilde key on a US standard keyboard.
	 */
	OemTilde(192),
	/**
	 * The OEM open bracket key on a US standard keyboard.
	 */
	OemOpenBrackets(219),
	/**
	 * The OEM pipe key on a US standard keyboard.
	 */
	OemPipe(220),
	/**
	 * The OEM close bracket key on a US standard keyboard.
	 */
	OemCloseBrackets(221),
	/**
	 * The OEM singled/double quote key on a US standard keyboard.
	 */
	OemQuotes(222),
	/**
	 * Used for miscellaneous characters; it can vary by keyboard.
	 */
	Oem8(223),
	/**
	 * The OEM angle bracket or backslash key on the RT 102 key keyboard.
	 */
	OemBackslash(226),
	/**
	 * IME PROCESS key.
	 */
	ProcessKey(229),
	/**
	 * Attn key.
	 */
	Attn(246),
	/**
	 * CrSel key.
	 */
	Crsel(247),
	/**
	 * ExSel key.
	 */
	Exsel(248),
	/**
	 * Erase EOF key.
	 */
	EraseEof(249),
	/**
	 * Play key.
	 */
	Play(250),
	/**
	 * Zoom key.
	 */
	Zoom(251),
	/**
	 * PA1 key.
	 */
	Pa1(253),
	/**
	 * CLEAR key.
	 */
	OemClear(254),
	/**
	 * Green ChatPad key.
	 */
	ChatPadGreen(0xCA),
	/*
	 * Orange ChatPad key.
	 */
	ChatPadOrange(0xCB),
	/**
	 * PAUSE key.
	 */
	Pause(0x13),
	/**
	 * IME Convert key.
	 */
	ImeConvert(0x1c),
	/**
	 * IME NoConvert key.
	 */
	ImeNoConvert(0x1d),
	/**
	 * Kana key on Japanese keyboards.
	 */
	Kana(0x15),
	/**
	 * Kanji key on Japanese keyboards.
	 */
	Kanji(0x19),
	/**
	 * OEM Auto key.
	 */
	OemAuto(0xf3),
	/**
	 * OEM Copy key.
	 */
	OemCopy(0xf2),
	/**
	 * OEM Enlarge Window key.
	 */
	OemEnlW(0xf4);

	// NOTE: This is for flags
	private final int value;

	private Keys(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	// NOTE: This is to convert the data into an enum value
	private static Map<Integer, Keys> map = new HashMap<Integer, Keys>();

	static
	{
		for (Keys key : Keys.values())
		{
			map.put(key.getValue(), key);
		}
	}

	public static Keys valueOf(int key)
	{
		return map.get(key);
	}
}
