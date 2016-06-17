package blackjack.players;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;

import java.util.Random;

import blackjack.cardsFramework.game.CardsGame;

public class BlackjackAIPlayer extends BlackjackPlayer
{
	private Random random = new Random();

	public Event<EventArgs> hit = new Event<EventArgs>();
	public Event<EventArgs> stand = new Event<EventArgs>();

	/**
	 * Creates a new instance of the {@link BlackjackAIPlayer} class.
	 * 
	 * @param name
	 *        The name of the player.
	 * @param game
	 *        The game instance.
	 */
	public BlackjackAIPlayer(String name, CardsGame game)
	{
		super(name, game);
	}

	/**
	 * Performs a move during a round.
	 */
	public void AIPlay()
	{
		int value = getFirstValue();
		if (firstValueConsiderAce && value + 10 <= 21)
		{
			value += 10;
		}

		if (value < 17 && hit != null)
		{
			hit.handleEvent(this, EventArgs.Empty);
		}
		else if (stand != null)
		{
			stand.handleEvent(this, EventArgs.Empty);
		}
	}

	/**
	 * Returns the amount which the AI player decides to bet.
	 * 
	 * @return The AI player's bet.
	 */
	public int AIBet()
	{
		int[] chips = { 0, 5, 25, 100, 500 };
		int bet = chips[random.nextInt(chips.length)];

		if (bet < balance)
		{
			return bet;
		}
		return 0;
	}
}
