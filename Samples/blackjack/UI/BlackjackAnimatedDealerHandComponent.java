package blackjack.UI;

import jMono_Framework.math.Vector2;
import blackjack.cardsFramework.UI.AnimatedHandGameComponent;
import blackjack.cardsFramework.cards.Hand;
import blackjack.cardsFramework.game.CardsGame;

public class BlackjackAnimatedDealerHandComponent extends AnimatedHandGameComponent
{
	/**
	 * Creates a new instance of the {@code BlackjackAnimatedDealerHandComponent} class.
	 * 
	 * @param place
	 *        A number indicating the hand's position on the game table.
	 * @param hand
	 *        The dealer's hand.
	 * @param cardGame
	 *        The associated game.
	 */
	public BlackjackAnimatedDealerHandComponent(int place, Hand hand, CardsGame cardGame)
	{
		super(place, hand, cardGame);
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
		return new Vector2(30 * cardLocationInHand, 0);
	}
}
