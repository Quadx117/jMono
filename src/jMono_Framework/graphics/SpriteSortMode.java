package jMono_Framework.graphics;

/**
 * Defines sprite sort rendering options.
 * 
 * @author Eric
 *
 */
public enum SpriteSortMode
{
	/**
	 * All sprites are drawn when <see cref="SpriteBatch.End"/> invokes, in order of draw call
	 * sequence. Depth is ignored.
	 */
	Deferred,

	/**
	 * Each sprite is drawn at individual draw call, instead of <see cref="SpriteBatch.End"/>.
	 * Depth is ignored.
	 */
	Immediate,

	/**
	 * Same as <see cref="SpriteSortMode.Deferred"/>, except sprites are sorted by texture prior to
	 * drawing. Depth is ignored.
	 */
	Texture,

	/**
	 * Same as <see cref="SpriteSortMode.Deferred"/>, except sprites are sorted by depth in
	 * back-to-front order prior to drawing.
	 */
	BackToFront,

	/**
	 * Same as <see cref="SpriteSortMode.Deferred"/>, except sprites are sorted by depth in
	 * front-to-back order prior to drawing.
	 */
	FrontToBack,
}
