package jMono_Framework.graphics;

/**
 * Defines sprite sort rendering options.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public enum SpriteSortMode
{
    /**
     * All sprites are drawn when {@link SpriteBatch#end()} invokes, in order of draw call sequence.
     * Depth is ignored.
     */
    Deferred,

    /**
     * Each sprite is drawn at individual draw call, instead of {@link SpriteBatch#end()}.
     * Depth is ignored.
     */
    Immediate,

    /**
     * Same as {@link SpriteSortMode#Deferred} except sprites are sorted by texture prior to
     * drawing. Depth is ignored.
     */
    Texture,

    /**
     * Same as {@link SpriteSortMode#Deferred}, except sprites are sorted by depth in back-to-front
     * order prior to drawing.
     */
    BackToFront,

    /**
     * Same as {@link SpriteSortMode#Deferred}, except sprites are sorted by depth in front-to-back
     * order prior to drawing.
     */
    FrontToBack,
}
