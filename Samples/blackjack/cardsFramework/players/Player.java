package blackjack.cardsFramework.players;

import blackjack.cardsFramework.cards.Hand;
import blackjack.cardsFramework.game.CardsGame;

/**
 * Represents base player to be extended by inheritance for each card game.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class Player
{
	public String name;
	public CardsGame game;
	public Hand hand;

	public Player(String name, CardsGame game)
	{
		this.name = name;
		this.game = game;
		this.hand = new Hand();
	}
}
