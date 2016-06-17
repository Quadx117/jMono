package blackjack.cardsFramework.UI;

import jMono_Framework.components.GameComponentCollectionEventArgs;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import blackjack.cardsFramework.cards.CardEventArgs;
import blackjack.cardsFramework.cards.Hand;
import blackjack.cardsFramework.cards.TraditionalCard;
import blackjack.cardsFramework.game.CardsGame;

public class AnimatedHandGameComponent extends AnimatedGameComponent
{
	private int place;
	public int getPlace() { return place; }
	
	public Hand hand;

	private List<AnimatedCardsGameComponent> heldAnimatedCards = new ArrayList<AnimatedCardsGameComponent>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAnimating()
	{
		for (int animationIndex = 0; animationIndex < heldAnimatedCards.size(); ++animationIndex)
		{
			if (heldAnimatedCards.get(animationIndex).isAnimating())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the animated cards contained in the hand.
	 * 
	 * @return The animated cards contained in the hand.
	 */
	public Iterator<AnimatedCardsGameComponent> getAnimatedCards()
	{
		return heldAnimatedCards.iterator();
	}
	
	/**
	 * Initializes a new instance of the animated hand component. This means
	 * setting the hand's position and initializing all animated cards and their
	 * respective positions. Also, registrations are performed to the associated
	 * <paramref name="hand} events to update the animated hand as cards are
	 * added or removed.
	 * 
	 * @param place
	 *        The player's place index (-1 for the dealer).
	 * @param hand
	 *        The hand represented by this instance.
	 * @param cardGame
	 *        The associated card game.
	 */
	public AnimatedHandGameComponent(int place, Hand hand, CardsGame cardGame)
	{
		super(cardGame, null);
		this.place = place;
		this.hand = hand;

		hand.receivedCard.add(this::hand_ReceivedCard);
		hand.lostCard.add(this::hand_LostCard);

		// Set the component's position
		if (place == -1)
		{
			currentPosition = new Vector2(cardGame.getGameTable().getDealerPosition());
		}
		else
		{
			currentPosition = cardGame.getGameTable().getPlayerPosition(place);
		}

		// Create and initialize animated cards according to the cards in the
		// associated hand
		for (int cardIndex = 0; cardIndex < hand.getCount(); ++cardIndex)
		{
			AnimatedCardsGameComponent animatedCardGameComponent =
					new AnimatedCardsGameComponent(hand.getTraditionalCard(cardIndex), cardGame);
			animatedCardGameComponent.currentPosition = new Vector2(30 * cardIndex, 0).add(currentPosition);

			heldAnimatedCards.add(animatedCardGameComponent);
			game.getComponents().add(animatedCardGameComponent);
		}

		game.getComponents().componentRemoved.add(this::components_ComponentRemoved);
	}

	/**
	 * Updates the component.
	 */
	@Override
	public void update(GameTime gameTime)
	{
		// Arrange the hand's animated cards' positions
		for (int animationIndex = 0; animationIndex < heldAnimatedCards.size(); ++animationIndex)
		{
			if (!heldAnimatedCards.get(animationIndex).isAnimating())
			{
				heldAnimatedCards.get(animationIndex).currentPosition =
						Vector2.add(getCardRelativePosition(animationIndex), currentPosition);
			}
		}
		super.update(gameTime);
	}

	/**
	 * Gets the card's offset from the hand position according to its index in
	 * the hand.
	 * 
	 * @param cardLocationInHand
	 *        The card index in the hand.
	 * @return
	 */
	public Vector2 getCardRelativePosition(int cardLocationInHand)
	{
		return null;
	}

	/**
	 * Finds the index of a specified card in the hand.
	 * 
	 * @param card
	 *        The card to locate.
	 * @return The card's index inside the hand, or -1 if it cannot be found.
	 */
	public int getCardLocationInHand(TraditionalCard card)
	{
		for (int animationIndex = 0; animationIndex < heldAnimatedCards.size(); ++animationIndex)
		{
			if (heldAnimatedCards.get(animationIndex).getTraditionalCard().equals(card))
			{
				return animationIndex;
			}
		}
		return -1;
	}

	/**
	 * Gets the animated game component associated with a specified card.
	 * 
	 * @param card
	 *        The card for which to get the animation component.
	 * @return The card's animation component, or null if such a card cannot be
	 *         found in the hand.
	 */
	public AnimatedCardsGameComponent getCardGameComponent(TraditionalCard card)
	{
		int location = getCardLocationInHand(card);
		if (location == -1)
			return null;

		return heldAnimatedCards.get(location);
	}

	/**
	 * Gets the animated game component associated with a specified card.
	 * 
	 * @param location
	 *        The location where the desired card is in the hand.
	 * @return The card's animation component.
	 */
	public AnimatedCardsGameComponent getCardGameComponent(int location)
	{
		if (location == -1 || location >= heldAnimatedCards.size())
			return null;

		return heldAnimatedCards.get(location);
	}

	// <summary>
	// Handles the ComponentRemoved event of the Components control.
	// </summary>
	// <param name="sender">The source of the event.</param>
	// <param name="e">The
	// {@link Microsoft.Xna.Framework.GameComponentCollectionEventArgs}
	// instance containing the event data.</param>
	void components_ComponentRemoved(Object sender, GameComponentCollectionEventArgs e)
	{
		if (e.getGameComponent() == this)
		{
			close();
		}
	}
	
	// <summary>
	// Handles the hand's LostCard event be removing the corresponding
	// animated
	// card.
	// </summary>
	// <param name="sender">The source of the event.</param>
	// <param name="e">The
	// {@link CardsFramework.CardEventArgs}
	// instance containing the event data.</param>
	void hand_LostCard(Object sender, CardEventArgs e)
	{
		// Remove the card from screen
		for (int animationIndex = 0; animationIndex < heldAnimatedCards.size(); ++animationIndex)
		{
			if (heldAnimatedCards.get(animationIndex).getTraditionalCard() == e.card)
			{
				getGame().getComponents().remove(heldAnimatedCards.get(animationIndex));
				heldAnimatedCards.remove(animationIndex);
				return;
			}
		}
	}

	// <summary>
	// Handles the hand's ReceivedCard event be adding a corresponding
	// animated card.
	// </summary>
	// <param name="sender">The source of the event.</param>
	// <param name="e">The
	// {@link CardsFramework.CardEventArgs}
	// instance containing the event data.</param>
	void hand_ReceivedCard(Object sender, CardEventArgs e)
	{
		// Add the card to the screen
		AnimatedCardsGameComponent animatedCardGameComponent = new AnimatedCardsGameComponent(e.card, cardGame);
		animatedCardGameComponent.setVisible(false);
	
		heldAnimatedCards.add(animatedCardGameComponent);
		getGame().getComponents().add(animatedCardGameComponent);
	}

	/**
	 * Calculate the estimated time at which the longest lasting animation
	 * currently managed will complete.
	 */
	@Override
	public TimeSpan estimatedTimeForAnimationsCompletion()
	{
		TimeSpan result = new TimeSpan(TimeSpan.ZERO);

		if (isAnimating())
		{
			for (int animationIndex = 0; animationIndex < heldAnimatedCards.size(); ++animationIndex)
			{
				if (heldAnimatedCards.get(animationIndex).estimatedTimeForAnimationsCompletion().getTicks() > result.getTicks())
				{
					result = heldAnimatedCards.get(animationIndex).estimatedTimeForAnimationsCompletion();
				}
			}
		}
		return result;
	}

	// <summary>
	// Properly disposes of the component when it is removed.
	// </summary>
	// <param name="disposing"></param>
	@Override
	protected void dispose(boolean disposing)
	{
		// Remove the registrations to the event to make this
		// instance collectable by gc
		hand.receivedCard.remove(this::hand_ReceivedCard);
		hand.lostCard.remove(this::hand_LostCard);
	
		super.dispose(disposing);
	}
}
