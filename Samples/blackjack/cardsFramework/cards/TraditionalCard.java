package blackjack.cardsFramework.cards;

/**
 * Traditional-western card. Each card has a defined {@link CardSuit suit} and
 * {@link CardValue value} as well as the {@link CardPacket} in which it is
 * being held. A card may not be held in more than one {@link CardPacket}. This
 * is achieved by enforcing any card transfer operation between {@link CarkPacket}s
 * and {@link Hand}s to be performed only from within the card's {@link #moveToHand(Hand)}
 * method only. This method accesses the {@link Hand#add(TraditionalCard)} method and
 * {@link CardPacket#remove(TraditionalCard)} method accordingly to complete the
 * card transfer operation.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class TraditionalCard
{
	/**
	 * Enumeration defining the various types of cards for a traditional-western
	 * card-set.
	 * 
	 * @author Eric Perron (inspired by CardsFramework from Microsoft)
	 * 
	 */
	public enum CardSuit
	{
		Heart(0x01),	//
		Diamond(0x02),	//
		Club(0x04),		//
		Spade(0x08),	//
		// Sets:
		AllSuits(Heart.getValue() | Diamond.getValue() | Club.getValue() | Spade.getValue());
		
		// NOTE: This is for flags
		private final int value;

		private CardSuit(int value)
		{
			this.value = value;
		}

		public int getValue()
		{
			return value;
		}
	}

	/**
	 * Enumeration defining the various types of card values for a
	 * traditional-western card-set.
	 * 
	 * @author Eric Perron (inspired by CardsFramework from Microsoft)
	 * 
	 */
	public enum CardValue
	{
		Ace(0x01),			//
		Two(0x02),			//
		Three(0x04),		//
		Four(0x08),			//
		Five(0x10),			//
		Six(0x20),			//
		Seven(0x40),		//
		Eight(0x80),		//
		Nine(0x100),		//
		Ten(0x200),			//
		Jack(0x400),		//
		Queen(0x800),		//
		King(0x1000),		//
		FirstJoker(0x2000),	//
		SecondJoker(0x4000),
		// Sets:
		AllNumbers(0x3FF),	//
		NonJokers(0x1FFF),	//
		Jokers(FirstJoker.getValue() | SecondJoker.getValue()),
		AlFigures(Jack.getValue() | Queen.getValue() | King.getValue());
		
		// NOTE: This is for flags
		private final int value;

		private CardValue(int value)
		{
			this.value = value;
		}

		public int getValue()
		{
			return value;
		}
	}

	public CardSuit suit;
	public CardValue value;
	public CardPacket holdingCardCollection;

	/**
	 * Initializes a new instance of the {@link TraditionalCard} class.
	 * 
	 * @param suit
	 *        The card suit. Supports only a single value.
	 * @param value
	 *        The card's value. Supports only a single value.
	 * @param holdingCardCollection
	 *        The holding card collection.
	 */
	public TraditionalCard(CardSuit suit, CardValue value, CardPacket holdingCardCollection)
			throws IllegalArgumentException
	{
		// Check for single suit
		switch (suit)
		{
			case Club:
			case Diamond:
			case Heart:
			case Spade:
				break;
			default:
			{
				throw new IllegalArgumentException("suit must be single value : " + suit);
			}
		}

		// Check for single value
		switch (value)
		{
			case Ace:
			case Two:
			case Three:
			case Four:
			case Five:
			case Six:
			case Seven:
			case Eight:
			case Nine:
			case Ten:
			case Jack:
			case Queen:
			case King:
			case FirstJoker:
			case SecondJoker:
				break;
			default:
			{
				throw new IllegalArgumentException("value must be single value : " + value);
			}
		}

		this.suit = suit;
		this.value = value;
		this.holdingCardCollection = holdingCardCollection;
	}

	/**
	 * Moves the card from its current {@link CardPacket} to the specified
	 * <paramref name="hand}. This method of operation prevents any one card
	 * instance from being held by more than one {@link CardPacket} at the
	 * same time.
	 * 
	 * @param hand
	 *        The receiving hand.
	 */
	public void moveToHand(Hand hand)
	{
		holdingCardCollection.remove(this);
		holdingCardCollection = hand;
		hand.add(this);
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * Gets this cards suit.
	 * 
	 * @return The suit of the card
	 */
	public CardSuit getSuit()
	{
		return suit;
	}

	/**
	 * Gets this cards value.
	 * 
	 * @return The value of the card.
	 */
	public CardValue getValue()
	{
		return value;
	}

	// ++++++++++ SETTERS ++++++++++ //

	/**
	 * Sets this cards suit to a new value.
	 * 
	 * @param suit
	 *        This cards new suit value.
	 */
	public void setSuit(CardSuit suit)
	{
		this.suit = suit;
	}

	/**
	 * Sets this cards value.
	 * 
	 * @param value
	 *        The new card value.
	 */
	public void setValue(CardValue value)
	{
		this.value = value;
	}
}
