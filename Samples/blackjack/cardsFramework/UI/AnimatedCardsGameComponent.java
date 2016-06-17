package blackjack.cardsFramework.UI;

import jMono_Framework.Color;
import jMono_Framework.time.GameTime;
import blackjack.cardsFramework.cards.TraditionalCard;
import blackjack.cardsFramework.game.CardsGame;
import blackjack.cardsFramework.util.UIUtility;

/**
 * An {@link AnimatedGameComponent} implemented for a card game.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class AnimatedCardsGameComponent extends AnimatedGameComponent
{
	private TraditionalCard card;
	public TraditionalCard getTraditionalCard() { return card; }

	/**
	 * Initializes a new instance of the class.
	 * 
	 * @param card
	 *        The card associated with the animation component.
	 * @param cardGame
	 *        The associated game.
	 */
	public AnimatedCardsGameComponent(TraditionalCard card, CardsGame cardGame)
	{
		super(cardGame, null);
		this.card = card;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(GameTime gameTime)
	{
		super.update(gameTime);

		currentFrame = isFaceDown ?
					   cardGame.getCardsAssets().get("CardBack_" + cardGame.getTheme()) :
					   cardGame.getCardsAssets().get(UIUtility.getCardAssetName(card));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		super.draw(gameTime);

		cardGame.spriteBatch.begin();

		// Draw the currentFrame at the designated destination, or at the
		// initial position if a destination has not been set
		if (currentFrame != null)
		{
			if (currentDestination != null)
			{
				cardGame.spriteBatch.draw(currentFrame, currentDestination, Color.White);
			}
			else
			{
				cardGame.spriteBatch.draw(currentFrame, currentPosition, Color.White);
			}
		}

		cardGame.spriteBatch.end();
	}
}
