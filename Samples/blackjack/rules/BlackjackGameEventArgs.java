package blackjack.rules;

import jMono_Framework.dotNet.events.EventArgs;
import blackjack.cardsFramework.players.Player;
import blackjack.players.BlackjackPlayer.HandTypes;

public class BlackjackGameEventArgs extends EventArgs
{
	public BlackjackGameEventArgs(Player player, HandTypes hand)
	{
		this.player = player;
		this.hand = hand;
	}

	public Player player;
	public HandTypes hand;
}
