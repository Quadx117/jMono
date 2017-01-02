package blackjack.UI;

import jMono_Framework.math.Vector2;
import blackjack.cardsFramework.UI.AnimatedHandGameComponent;
import blackjack.cardsFramework.cards.Hand;
import blackjack.cardsFramework.game.CardsGame;

public class BlackjackAnimatedPlayerHandComponent extends AnimatedHandGameComponent
{
	Vector2 offset;

	/**
	 * Creates a new instance of the {@code BlackjackAnimatedPlayerHandComponent} class.
	 * 
	 * @param place
	 *        A number indicating the hand's position on the game table.
	 * @param hand
	 *        The player's hand.
	 * @param cardGame
	 *        The associated game.
	 */
	public BlackjackAnimatedPlayerHandComponent(int place, Hand hand, CardsGame cardGame)
	{
		super(place, hand, cardGame);
		this.offset = new Vector2(Vector2.Zero());
	}

	/**
	 * Creates a new instance of the {@code BlackjackAnimatedPlayerHandComponent} class.
	 * 
	 * @param place
	 *        A number indicating the hand's position on the game table.
	 * @param offset
	 *        An offset which will be added to all card locations returned
	 *        by this component.
	 * @param hand
	 *        The player's hand.
	 * @param cardGame
	 *        The associated game.
	 */
	public BlackjackAnimatedPlayerHandComponent(int place, Vector2 offset, Hand hand, CardsGame cardGame)
	{
		super(place, hand, cardGame);
		this.offset = offset;
	}

	/**
	 * Gets the position relative to the hand position at which a specific card
	 * contained in the hand should be rendered.
	 * 
	 * @param cardLocationInHand
	 *        The card's location in the hand (0 is the first card in the
	 *        hand).
	 * @return An offset from the hand's location where the card should be
	 *         rendered.
	 */
	@Override
	public Vector2 getCardRelativePosition(int cardLocationInHand)
	{
		return new Vector2(25 * cardLocationInHand, -30 * cardLocationInHand).add(offset);
	}
}
