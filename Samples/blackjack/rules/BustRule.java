package blackjack.rules;

import java.util.ArrayList;
import java.util.List;

import blackjack.cardsFramework.players.Player;
import blackjack.cardsFramework.rules.GameRule;
import blackjack.players.BlackjackPlayer;
import blackjack.players.BlackjackPlayer.HandTypes;

public class BustRule extends GameRule
{
	List<BlackjackPlayer> players;

	/**
	 * Creates a new instance of the {@link BustRule} class.
	 * 
	 * @param players
	 *        A list of players participating in the game.
	 */
	public BustRule(List<Player> players)
	{
		this.players = new ArrayList<BlackjackPlayer>();
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			this.players.add((BlackjackPlayer) players.get(playerIndex));
		}
	}

	/**
	 * Check if any of the players has exceeded 21 in any of their hands.
	 */
	@Override
	public void check()
	{
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			players.get(playerIndex).calculateValues();

			if (!players.get(playerIndex).isBust())
			{
				if (!players.get(playerIndex).getFirstValueConsiderAce() &&
					 players.get(playerIndex).getFirstValue() > 21)
				{
					fireRuleMatch(new BlackjackGameEventArgs(players.get(playerIndex), HandTypes.First));
				}
			}
			if (!players.get(playerIndex).isSecondBust())
			{
				if ((players.get(playerIndex).isSplit() &&
					 !players.get(playerIndex).getSecondValueConsiderAce() && players.get(playerIndex).getSecondValue() > 21))
				{
					fireRuleMatch(new BlackjackGameEventArgs(players.get(playerIndex), HandTypes.Second));
				}
			}
		}
	}
}
