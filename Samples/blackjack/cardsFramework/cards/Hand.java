package blackjack.cardsFramework.cards;

import jMono_Framework.dotNet.events.Event;

import java.util.Iterator;

import javax.smartcardio.Card;

/**
 * Represents a hand of cards held by a player, dealer or the game table A {@link Hand} is a type of
 * {@link CardPacket} that may also receive {@link Card} items, as well as loose them. Therefore, it
 * may receive {@link Card} items from any {@link CardPacket} or from another {@link Hand}.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class Hand extends CardPacket
{
	/**
	 * An event which triggers when a card is added to the hand.
	 */
	public Event<CardEventArgs> receivedCard = new Event<CardEventArgs>();

	/**
	 * Adds the specified card to the hand.
	 * 
	 * @param card
	 *        The card to add to the hand. The card will be added as the
	 *        last card of the hand.
	 */
	void add(TraditionalCard card)
	{
		cards.add(card);
		if (receivedCard != null)
		{
			receivedCard.handleEvent(this, new CardEventArgs(card));
		}
	}

	/**
	 * Adds the specified cards to the hand.
	 * 
	 * @param cards
	 *        The cards to add to the hand. The cards are added as the last
	 *        cards of the hand.
	 */
	void add(Iterator<TraditionalCard> cards)
	{
		while (cards.hasNext())
		{
			this.cards.add(cards.next());
		}
	}
}
