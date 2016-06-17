package blackjack.cardsFramework.cards;

import jMono_Framework.dotNet.events.EventArgs;

/**
 * Card related {@link EventArgs} holding event information of a {@link TraditionalCard}
 * 
 * @author Eric Perron
 *
 */
public class CardEventArgs extends EventArgs
{
	public CardEventArgs(TraditionalCard card)
	{
		this.card = card;
	}
	
	public TraditionalCard card;
}
