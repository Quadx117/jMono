package jMono_Framework;

/**
 * Defines how <see cref="Game"/> should be runned.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 *
 */
public enum GameRunBehavior
{
	/**
	 * The game loop will be runned asynchronous.
	 */
	Asynchronous,

	/**
	 * The game loop will be runned synchronous.
	 */
	Synchronous
}