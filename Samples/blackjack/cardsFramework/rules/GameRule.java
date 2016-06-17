package blackjack.cardsFramework.rules;

import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;

/**
 * Represents a rule in a card game. Inherit from this class and write your
 * code.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public abstract class GameRule
{
	// An event which triggers when the rule conditions are matched.
	public Event<EventArgs> ruleMatch = new Event<EventArgs>();

	/**
	 * Checks whether the rule conditions are met. Should call {@link #fireRuleMatch()}
	 */
	public abstract void check();

	/**
	 * Fires the rule's event.
	 * 
	 * @param e
	 *        Event arguments.
	 */
	protected void fireRuleMatch(EventArgs e)
	{
		if (ruleMatch != null)
		{
			ruleMatch.handleEvent(this, e);
		}
	}
}