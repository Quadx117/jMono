package blackjack.rules;

import java.util.ArrayList;
import java.util.List;

import blackjack.cardsFramework.players.Player;
import blackjack.cardsFramework.rules.GameRule;
import blackjack.players.BlackjackPlayer;
import blackjack.players.BlackjackPlayer.HandTypes;

/**
 * Represents a rule which checks if one of the player has achieved "blackjack".
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 *
 */
public class BlackJackRule extends GameRule
{
	List<BlackjackPlayer> players;

	/**
	 * Creates a new instance of the {@link BlackJackRule} class.
	 * 
	 * @param players
	 *        A list of players participating in the game.
	 */
	public BlackJackRule(List<Player> players)
	{
		this.players = new ArrayList<BlackjackPlayer>();
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			this.players.add((BlackjackPlayer) players.get(playerIndex));
		}
	}

	/**
	 * Check if any of the players has a hand value of 21 in any of their hands.
	 */
	@Override
	public void check()
	{
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			players.get(playerIndex).calculateValues();

			if (!players.get(playerIndex).isBlackJack())
			{
				// Check to see if the hand is eligible for a Black Jack
				if (((players.get(playerIndex).getFirstValue() == 21) ||
					 (players.get(playerIndex).getFirstValueConsiderAce() &&
					  players.get(playerIndex).getFirstValue() + 10 == 21)) &&
					  players.get(playerIndex).hand.getCount() == 2)
				{
					fireRuleMatch(new BlackjackGameEventArgs(players.get(playerIndex), HandTypes.First));
				}
			}
			if (!players.get(playerIndex).isSecondBlackJack())
			{
				// Check to see if the hand is eligible for a Black Jack
				// A Black Jack is only eligible with 2 cards in a hand
				if ((players.get(playerIndex).isSplit()) &&
				   ((players.get(playerIndex).getSecondValue() == 21) ||
					(players.get(playerIndex).getSecondValueConsiderAce() &&
					 players.get(playerIndex).getSecondValue() + 10 == 21)) &&
					 players.get(playerIndex).getSecondHand().getCount() == 2)
				{
					fireRuleMatch(new BlackjackGameEventArgs(players.get(playerIndex), HandTypes.Second));
				}
			}
		}
	}
}
