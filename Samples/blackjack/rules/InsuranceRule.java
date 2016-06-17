package blackjack.rules;

import jMono_Framework.dotNet.events.EventArgs;
import blackjack.cardsFramework.cards.Hand;
import blackjack.cardsFramework.cards.TraditionalCard.CardValue;
import blackjack.cardsFramework.rules.GameRule;

/**
 * Represents a rule which checks if the human player can use insurance
 * 
 * @author Eric Perron
 *
 */
public class InsuranceRule extends GameRule
{
	Hand dealerHand;
	boolean done = false;

	/**
	 * Creates a new instance of the {@code nsuranceRule} class.
	 * 
	 * @param dealerHand
	 *        The dealer's hand.
	 */
	public InsuranceRule(Hand dealerHand)
	{
		this.dealerHand = dealerHand;
	}

	/**
	 * Checks whether or not the dealer's revealed card is an ace.
	 */
	@Override
	public void check()
	{
		if (!done)
		{
			if (dealerHand.getCount() > 0)
			{
				if (dealerHand.getTraditionalCard(0).getValue().equals(CardValue.Ace))
				{
					fireRuleMatch(EventArgs.Empty);
				}
				done = true;
			}
		}
	}
}
