package jMono_Framework;

/**
 * Defines how {@link Game} should be run.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 *
 */
public enum GameRunBehavior
{
    /**
     * The game loop will be run asynchronous.
     */
    Asynchronous,

    /**
     * The game loop will be run synchronous.
     */
    Synchronous
}
