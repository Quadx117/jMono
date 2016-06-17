package blackjack.cardsFramework.util;

import blackjack.cardsFramework.cards.TraditionalCard;
import blackjack.cardsFramework.cards.TraditionalCard.CardValue;

public class UIUtility
{
	/**
	 * Gets the name of a card asset.
	 * 
	 * @param card
	 *        The card type for which to get the asset name.
	 * @return The card's asset name.
	 */
	public static String getCardAssetName(TraditionalCard card)
	{
		return String.format("%s%s",
				((card.getValue().getValue() | CardValue.FirstJoker.getValue()) == CardValue.FirstJoker.getValue() ||
				(card.getValue().getValue() | CardValue.SecondJoker.getValue()) == CardValue.SecondJoker.getValue()) ?
				"" : card.getSuit(), card.getValue());
	}
}
