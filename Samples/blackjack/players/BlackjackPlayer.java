package blackjack.players;

import blackjack.cardsFramework.cards.Hand;
import blackjack.cardsFramework.cards.TraditionalCard.CardValue;
import blackjack.cardsFramework.game.CardsGame;
import blackjack.cardsFramework.players.Player;

public class BlackjackPlayer extends Player
{
	/**
	 * Depicts hands the player can interact with.
	 * 
	 * @author Eric Perron (inspired by CardsFramework from Microsoft)
	 * 
	 */
	public enum HandTypes
	{
		First,	//
		Second	//
	}

	// Various fields which depict the state of the players two hands
	private int firstValue;
	protected boolean firstValueConsiderAce;

	private int secondValue;
	private boolean secondValueConsiderAce;

	public boolean isBust;
	public boolean isSecondBust;
	public boolean isBlackJack;
	public boolean isSecondBlackJack;
	public boolean isDouble;
	public boolean isSecondDouble;
	
	public boolean isSplit;
	private Hand secondHand;

	/**
	 * The type of hand that the player is currently interacting with.
	 */
	public HandTypes currentHandType;
	
	/**
	 * Returns the hand that the player is currently interacting with.
	 * 
	 * @return The hand that the player is currently interacting with.
	 * @throws IllegalStateException
	 *         If we try to access a hand that doesn't exist.
	 */
	public Hand getCurrentHand() throws IllegalStateException
	{
		switch (currentHandType)
		{
			case First:
				return hand;
			case Second:
				return secondHand;
			default:
				throw new IllegalStateException("No hand to return");
		}
	}
	
	public int getFirstValue() { return firstValue; }

	public boolean getFirstValueConsiderAce() { return firstValueConsiderAce; }

	public int getSecondValue() { return secondValue; }

	public boolean getSecondValueConsiderAce() { return secondValueConsiderAce; }

	public boolean madeBet() { return betAmount > 0; }
	public boolean isDoneBetting;
	public float balance;
	private float betAmount;
	public float getBetAmount() { return betAmount; }
	public boolean isInsurance;

	/**
	 * Creates a new blackjack player instance.
	 * 
	 * @param name
	 *        The player's name.
	 * @param game
	 *        The game associated with the player.
	 */
	public BlackjackPlayer(String name, CardsGame game)
	{
		super(name, game);
		balance = 500;
		currentHandType = HandTypes.First;
	}

	/**
	 * Calculates the value represented by a specified hand. The results are
	 * stored in the appropriate fields (firstValue, secondValue,
	 * firstValueConsiderAce and secondValueConsiderAce). If the hand has two
	 * possible values due to it containing an ace, firstValue and secondValue
	 * will be the lower value.
	 * 
	 * @param hand
	 *        The hand for which to calculate the value.
	 * @param game
	 *        The associated game.
	 * @param handType
	 *        Determines if this is the first or second hand so we can set
	 *        the appropriate fields
	 */
	private void calulateValue(Hand hand, CardsGame game, HandTypes handType)
	{
		int value = 0;
		boolean considerAce = false;

		for (int cardIndex = 0; cardIndex < hand.getCount(); ++cardIndex)
		{
			value += game.cardValue(hand.getTraditionalCard(cardIndex));

			if (hand.getTraditionalCard(cardIndex).getValue() == CardValue.Ace)
			{
				considerAce = true;
			}
		}

		if (considerAce && value + 10 > 21)
		{
			considerAce = false;
		}

		if (handType == HandTypes.First)
		{
			firstValue = value;
			firstValueConsiderAce = considerAce;
		}
		else
		{
			secondValue = value;
			secondValueConsiderAce = considerAce;
		}
	}
	
	/**
	 * Bets a specified amount of money, if the player's balance permits it. The
	 * player's bet amount and balance are only updated if this method returns
	 * true.
	 * 
	 * @param amount
	 *        The amount to bet.
	 * @return True if the player has enough money to perform the bet, false
	 *         otherwise.
	 */
	public boolean bet(float amount)
	{
		if (amount > balance)
		{
			return false;
		}
		betAmount += amount;
		balance -= amount;
		return true;
	}

	/**
	 * Resets the player's bet to 0, returning the current bet amount to the
	 * player's balance.
	 */
	public void clearBet()
	{
		balance += betAmount;
		betAmount = 0;
	}

	/**
	 * Calculates the values of the player's two hands.
	 */
	public void calculateValues()
	{
		calulateValue(hand, game, HandTypes.First);

		if (secondHand != null)
		{
			calulateValue(secondHand, game, HandTypes.Second);
		}
	}
	
	/**
	 * Reset's the player's various state fields.
	 */
	public void resetValues()
	{
		isBlackJack = false;
		isSecondBlackJack = false;
		isBust = false;
		isSecondBust = false;
		isDouble = false;
		isSecondDouble = false;
		firstValue = 0;
		firstValueConsiderAce = false;
		isSplit = false;
		secondValue = 0;
		secondValueConsiderAce = false;
		betAmount = 0;
		isDoneBetting = false;
		isInsurance = false;
		currentHandType = HandTypes.First;
	}

	/**
	 * Initializes the player's second hand.
	 */
	public void initializeSecondHand()
	{
		secondHand = new Hand();
	}

	/**
	 * Splits the player's current hand into two hands as per the blackjack
	 * rules.
	 * 
	 * @throws IllegalStateException
	 *         If performing a split is not legal for the current player
	 *         status (already split or the hand doesn't have 2 cards or
	 *         both cards are not the same value).
	 * @throws NullPointerException
	 *         If second hand is null.
	 */
	public void splitHand() throws IllegalStateException, NullPointerException
	{
		if (secondHand == null)
		{
			throw new NullPointerException("Second hand is not initialized.");
		}

		if (isSplit == true)
		{
			throw new IllegalStateException("A hand cannot be split more than once.");
		}

		if (hand.getCount() != 2)
		{
			throw new IllegalStateException("You must have two cards to perform a split.");
		}

		if (hand.getTraditionalCard(0).getValue() != hand.getTraditionalCard(1).getValue())
		{
			throw new IllegalStateException("You can only split when both cards are of identical value.");
		}

		isSplit = true;

		// Move the top card in the first hand to the second hand
		hand.getTraditionalCard(1).moveToHand(secondHand);
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * Returns the type of hand that the player is currently interacting with.
	 * 
	 * @return The type of hand that the player is currently interacting with.
	 */
	public HandTypes getCurrentHandType() { return currentHandType; }
	
	public boolean isDoneBetting() { return isDoneBetting; }
	
	public float getBalance() { return balance; }
	
	public boolean isInsurance() { return isInsurance; }
	
	public boolean isBust() { return isBust; }

	public boolean isSecondBust() { return isSecondBust; }

	public boolean isBlackJack() { return isBlackJack; }

	public boolean isSecondBlackJack() { return isSecondBlackJack; }

	public boolean isDouble() { return isDouble; }

	public boolean isSecondDouble() { return isSecondDouble; }

	public boolean isSplit() { return isSplit; }

	public Hand getSecondHand() { return secondHand; }

	// ++++++++++ SETTERS ++++++++++ //

	public void setIsBust(boolean value) { isBust = value; }

	public void setIsBlackJack(boolean value) { isBlackJack = value; }

	public void setIsDouble(boolean value) { isDouble = value; }

	public void setIsSecondDouble(boolean value) { isSecondDouble = value; }
}
