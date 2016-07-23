package blackjack.game;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.dotNet.As;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import blackjack.BlackjackGame;
import blackjack.UI.BlackJackTable;
import blackjack.UI.BlackjackAnimatedDealerHandComponent;
import blackjack.UI.BlackjackAnimatedPlayerHandComponent;
import blackjack.UI.Button;
import blackjack.cardsFramework.UI.AnimatedCardsGameComponent;
import blackjack.cardsFramework.UI.AnimatedGameComponent;
import blackjack.cardsFramework.UI.AnimatedGameComponentAnimation;
import blackjack.cardsFramework.UI.AnimatedHandGameComponent;
import blackjack.cardsFramework.UI.FlipGameComponentAnimation;
import blackjack.cardsFramework.UI.FramesetGameComponentAnimation;
import blackjack.cardsFramework.UI.GameTable;
import blackjack.cardsFramework.UI.ScaleGameComponentAnimation;
import blackjack.cardsFramework.UI.TransitionGameComponentAnimation;
import blackjack.cardsFramework.cards.Hand;
import blackjack.cardsFramework.cards.TraditionalCard;
import blackjack.cardsFramework.cards.TraditionalCard.CardSuit;
import blackjack.cardsFramework.cards.TraditionalCard.CardValue;
import blackjack.cardsFramework.game.CardsGame;
import blackjack.cardsFramework.players.Player;
import blackjack.cardsFramework.rules.GameRule;
import blackjack.misc.AudioManager;
import blackjack.misc.BetGameComponent;
import blackjack.misc.InputHelper;
import blackjack.players.BlackjackAIPlayer;
import blackjack.players.BlackjackPlayer;
import blackjack.players.BlackjackPlayer.HandTypes;
import blackjack.rules.BlackJackRule;
import blackjack.rules.BlackjackGameEventArgs;
import blackjack.rules.BustRule;
import blackjack.rules.InsuranceRule;
import blackjack.screenManager.GameScreen;
import blackjack.screenManager.ScreenManager;
import blackjack.screens.BackgroundScreen;
import blackjack.screens.MainMenuScreen;

/**
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 *
 */
public class BlackjackCardGame extends CardsGame
{
	/**
	 * The various possible game states.
	 * 
	 * @author Eric Perron (inspired by CardsFramework from Microsoft)
	 * 
	 */
	public enum BlackjackGameState
	{
		Shuffling,	//
		Betting,	//
		Playing,	//
		Dealing,	//
		RoundEnd,	//
		GameOver
	}

	private HashMap<Player, String> playerHandValueTexts = new HashMap<Player, String>();
	private HashMap<Player, String> playerSecondHandValueTexts = new HashMap<Player, String>();
	private Hand deadCards = new Hand(); // stores used cards
	private BlackjackPlayer dealerPlayer;
	private boolean[] turnFinishedByPlayer;
	private TimeSpan dealDuration = TimeSpan.fromMilliseconds(500);

	private AnimatedHandGameComponent[] animatedHands;
	// An additional list for managing hands created when performing a split.
	private AnimatedHandGameComponent[] animatedSecondHands;

	private BetGameComponent betGameComponent;
	private AnimatedHandGameComponent dealerHandComponent;
	private HashMap<String, Button> buttons = new HashMap<String, Button>();
	private Button newGame;
	private boolean showInsurance;

	// An offset used for drawing the second hand which appears after a split in
	// the correct location.
	private Vector2 secondHandOffset = new Vector2(100 * BlackjackGame.WIDTH_SCALE,
												   25 * BlackjackGame.HEIGHT_SCALE);
	private static Vector2 ringOffset = new Vector2(0, 110);

// #if WINDOWS_PHONE
	// Vector2 frameSize = new Vector2(162, 162);
// #else
    private Vector2 frameSize = new Vector2(180, 180);
// #endif

	public BlackjackGameState state;
	private ScreenManager screenManager;

	private static final int MAX_PLAYERS = 3;
	private static final int MIN_PLAYERS = 1;

	/**
	 * Creates a new instance of the {@link BlackjackCardGame} class.
	 * 
	 * @param tableBounds
	 *        The table bounds. These serves as the bounds for the game's
	 *        main area.
	 * @param dealerPosition
	 *        Position for the dealer's deck.
	 * @param placeOrder
	 *        A method that translate a player index into the position of
	 *        his deck on the game table.
	 * @param screenManager
	 *        The games {@link ScreenManager}.
	 * @param theme
	 *        The game's deck theme name.
	 */
	public BlackjackCardGame(Rectangle tableBounds, Vector2 dealerPosition, Function<Integer, Vector2> placeOrder,
			ScreenManager screenManager, String theme)
	{
		super(6, 0, CardSuit.AllSuits, CardValue.NonJokers, MIN_PLAYERS, MAX_PLAYERS, new BlackJackTable(ringOffset,
				tableBounds, dealerPosition, MAX_PLAYERS, placeOrder, theme, screenManager.getGame()), theme,
				screenManager.getGame());
		dealerPlayer = new BlackjackPlayer("Dealer", this);
		turnFinishedByPlayer = new boolean[maximumPlayers];
		this.screenManager = screenManager;

		if (animatedHands == null)
		{
			animatedHands = new AnimatedHandGameComponent[MAX_PLAYERS];
		}
		if (animatedSecondHands == null)
		{
			animatedSecondHands = new AnimatedHandGameComponent[MAX_PLAYERS];
		}
	}

	/**
	 * Performs necessary initializations.
	 */
	public void initialize()
	{
		super.loadContent();
		// Initialize a new bet component
		betGameComponent = new BetGameComponent(players, screenManager.input, theme, this);
		game.getComponents().add(betGameComponent);

		// Initialize the game buttons
		String[] buttonsText = { "Hit", "Stand", "Double", "Split", "Insurance" };
		for (int buttonIndex = 0; buttonIndex < buttonsText.length; ++buttonIndex)
		{
			Button button = new Button("ButtonRegular", "ButtonPressed", screenManager.input, this);
			button.text = buttonsText[buttonIndex];
			button.bounds = new Rectangle(screenManager.getSafeArea().left() + 10 + buttonIndex * 110,
										  screenManager.getSafeArea().bottom() - 60,
										  100, 50);
			button.font = this.font;
			button.setVisible(false);
			button.setEnabled(false);
			buttons.put(buttonsText[buttonIndex], button);
			game.getComponents().add(button);
		}

		newGame = new Button("ButtonRegular", "ButtonPressed", screenManager.input, this);
		newGame.text = "New Hand";
		newGame.bounds = new Rectangle(screenManager.getSafeArea().left() + 10,
									   screenManager.getSafeArea().bottom() - 60,
									   200, 50);
		newGame.font = this.font;
		newGame.setVisible(false);
		newGame.setEnabled(false);

		// Alter the insurance button's bounds as it is considerably larger than
		// the other buttons
		Rectangle insuranceBounds = buttons.get("Insurance").getBounds();
		insuranceBounds.width = 200;
		buttons.get("Insurance").bounds = new Rectangle(insuranceBounds);

		newGame.click.add(this::newGame_Click);
		game.getComponents().add(newGame);

		// Register to click event
		buttons.get("Hit").click.add(this::hit_Click);
		buttons.get("Stand").click.add(this::stand_Click);
		buttons.get("Double").click.add(this::double_Click);
		buttons.get("Split").click.add(this::split_Click);
		buttons.get("Insurance").click.add(this::insurance_Click);
	}

	/**
	 * Perform the game's update logic.
	 * 
	 * @param gameTime
	 *        Time elapsed since the last call to this method.
	 */
	public void update(GameTime gameTime)
	{
		switch (state)
		{
			case Shuffling:
			{
				showShuffleAnimation();
			}
				break;
			case Betting:
			{
				enableButtons(false);
			}
				break;
			case Dealing:
			{
				// Deal 2 cards and start playing
				state = BlackjackGameState.Playing;
				deal();
				startPlaying();
			}
				break;
			case Playing:
			{
				// Calculate players' current hand values
				for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
				{
					((BlackjackPlayer) players.get(playerIndex)).calculateValues();
				}
				dealerPlayer.calculateValues();

				// Make sure no animations are running
				if (!checkForRunningAnimations(AnimatedGameComponent.class))
				{
					BlackjackPlayer player = (BlackjackPlayer) getCurrentPlayer();
					// If the current player is an AI player, make it play
					if (player instanceof BlackjackAIPlayer)
					{
						((BlackjackAIPlayer) player).AIPlay();
					}

					checkRules();

					// If all players have finished playing, the
					// current round ends
					if (state == BlackjackGameState.Playing && getCurrentPlayer() == null)
					{
						endRound();
					}

					// Update button availability according to player options
					setButtonAvailability();
				}
				else
					enableButtons(false);
			}
				break;
			case RoundEnd:
			{
				if (dealerHandComponent.estimatedTimeForAnimationsCompletion().equals(TimeSpan.ZERO))
				{
					betGameComponent.calculateBalance(dealerPlayer);
					// Check if there is enough money to play
					// then show new game option or tell the player he has lost
					if (((BlackjackPlayer) players.get(0)).getBalance() < 5)
					{
						endGame();
					}
					else
					{
						newGame.setEnabled(true);
						newGame.setVisible(true);
					}
				}
			}
				break;
			case GameOver:
			{

			}
				break;
			default:
				break;
		}
	}

	/**
	 * Shows the card shuffling animation.
	 */
	private void showShuffleAnimation()
	{
		// Add shuffling animation
		AnimatedGameComponent animationComponent = new AnimatedGameComponent(this.game);
		animationComponent.currentPosition = new Vector2(gameTable.getDealerPosition());
		animationComponent.setVisible(false);
		game.getComponents().add(animationComponent);

		FramesetGameComponentAnimation animation =
				new FramesetGameComponentAnimation(cardsAssets.get("shuffle_" + theme),
						32, 11, frameSize);
		animation.duration = TimeSpan.fromSeconds(1.5f);
		animation.performBeforeStart = this::showComponent;
		animation.performBeforSartArgs = animationComponent;
		animation.performWhenDone = this::playShuffleAndRemoveComponent;
		animation.performWhenDoneArgs = animationComponent;
		animationComponent.addAnimation(animation);
		state = BlackjackGameState.Betting;
	}

	/**
	 * Helper method to show component.
	 * 
	 * @param obj
	 */
	private void showComponent(Object obj)
	{
		((AnimatedGameComponent) obj).setVisible(true);
	}

	/**
	 * Helper method to play shuffle sound and remove component.
	 * 
	 * @param obj
	 */
	private void playShuffleAndRemoveComponent(Object obj)
	{
		AudioManager.playSound("Shuffle");
		game.getComponents().remove((AnimatedGameComponent) obj);
	}

	/**
	 * Renders the visual elements for which the game itself is responsible.
	 * 
	 * @param gameTime
	 *        Time passed since the last call to this method.
	 */
	public void draw(GameTime gameTime)
	{
		spriteBatch.begin();

		switch (state)
		{
			case Playing:
			{
				showPlayerValues();
			}
				break;
			case GameOver:
			{
			}
				break;
			case RoundEnd:
			{
				if (dealerHandComponent.estimatedTimeForAnimationsCompletion().equals(TimeSpan.ZERO))
				{
					showDealerValue();
				}
				showPlayerValues();
			}
				break;
			default:
				break;
		}

		spriteBatch.end();
	}

	/**
	 * Draws the dealer's hand value on the screen.
	 */
	private void showDealerValue()
	{
		// Calculate the value to display
		String dealerValue = String.valueOf(dealerPlayer.getFirstValue());
		if (dealerPlayer.getFirstValueConsiderAce())
		{
			if (dealerPlayer.getFirstValue() + 10 == 21)
			{
				dealerValue = "21";
			}
			else
			{
				dealerValue += "\\" + Integer.toString((dealerPlayer.getFirstValue() + 10));
			}
		}

		// Draw the value
		Vector2 measure = font.measureString(dealerValue);
		Vector2 position = Vector2.subtract(gameTable.getDealerPosition(), new Vector2(measure.x + 20, 0));

		spriteBatch.draw(screenManager.getBlankTexture(),
						new Rectangle((int) position.x - 4, (int) position.y,
								(int) measure.x + 8, (int) measure.y),
						Color.Black);

        spriteBatch.drawString(font, dealerValue, position, Color.White);
	}

	/**
	 * Draws the players' hand value on the screen.
	 */
	private void showPlayerValues()
	{
		Color color = Color.Black;
		Player currentPlayer = getCurrentPlayer();

		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			BlackjackPlayer player = (BlackjackPlayer) players.get(playerIndex);
			// The current player's hand value will be red to serve as a visual
			// prompt for who the active player is
			if (player == currentPlayer)
			{
				color = Color.Red;
			}
			else
			{
				color = Color.White;
			}

			// Calculate the values to draw
			String playerHandValueText;
			String playerSecondHandValueText = null;
			if (!animatedHands[playerIndex].isAnimating())
			{
				if (player.getFirstValue() > 0)
				{
					playerHandValueText = Integer.toString(player.getFirstValue());
					// Take the fact that an ace is wither 1 or 11 into
					// consideration when calculating the value to display
					// Since the ace already counts as 1, we add 10 to get
					// the alternate value
					if (player.getFirstValueConsiderAce())
					{
						if (player.getFirstValue() + 10 == 21)
						{
							playerHandValueText = "21";
						}
						else
						{
							playerHandValueText += "\\" + Integer.toString((player.getFirstValue() + 10));
						}
					}
					playerHandValueTexts.put(player, playerHandValueText);
				}
				else
				{
					playerHandValueText = null;
				}

				if (player.isSplit())
				{
					// If the player has performed a split, he has an additional
					// hand with its own value
					if (player.getSecondValue() > 0)
					{
						playerSecondHandValueText = Integer.toString(player.getSecondValue());
						if (player.getSecondValueConsiderAce())
						{
							if (player.getSecondValue() + 10 == 21)
							{
								playerSecondHandValueText = "21";
							}
							else
							{
								playerSecondHandValueText += "\\" + Integer.toString((player.getSecondValue() + 10));
							}
						}
						playerSecondHandValueTexts.put(player, playerSecondHandValueText);
					}
					else
					{
						playerSecondHandValueText = null;
					}
				}
			}
			else
			{
				playerHandValueText = playerHandValueTexts.get(player);
				playerSecondHandValueText = playerSecondHandValueTexts.get(player);
			}

			if (player.isSplit())
			{
				// If the player has performed a split, mark the active hand
				// alone with a red value
				color = player.getCurrentHandType() == HandTypes.First && player == currentPlayer ?
						Color.Red : Color.White;

				if (playerHandValueText != null)
				{
					drawValue(animatedHands[playerIndex], playerIndex, playerHandValueText, color);
				}

				color = player.getCurrentHandType() == HandTypes.Second && player == currentPlayer ?
						Color.Red : Color.White;

				if (playerSecondHandValueText != null)
				{
					drawValue(animatedSecondHands[playerIndex], playerIndex, playerSecondHandValueText, color);
				}
			}
			else
			{
				// If there is a value to draw, draw it
				if (playerHandValueText != null)
				{
					drawValue(animatedHands[playerIndex], playerIndex, playerHandValueText, color);
				}
			}
		}
	}

	/**
	 * Draws the value of a player's hand above his top card. The value will be
	 * drawn over a black background.
	 * 
	 * @param animatedHand
	 *        The player's hand.
	 * @param place
	 *        A number representing the player's position on the game table.
	 * @param value
	 *        The value to draw.
	 * @param valueColor
	 *        The color in which to draw the value.
	 */
	private void drawValue(AnimatedHandGameComponent animatedHand, int place, String value, Color valueColor)
	{
		Hand hand = animatedHand.hand;

		Vector2 position = Vector2.add(gameTable.getPlaceOrder().apply(place),
				animatedHand.getCardRelativePosition(hand.getCount() - 1));

		Vector2 measure = font.measureString(value);

		position.x += (cardsAssets.get("CardBack_" + theme).getBounds().width - measure.x) / 2;
		position.y -= measure.y + 5;

		spriteBatch.draw(screenManager.getBlankTexture(),
                		new Rectangle((int) position.x - 4, (int) position.y,
                				(int) measure.x + 8, (int) measure.y),
                		Color.Black);
		spriteBatch.drawString(font, value, position, valueColor);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPlayer(Player player)
	{
		if (player instanceof BlackjackPlayer && players.size() < maximumPlayers)
		{
			players.add(player);
		}
	}

	/**
	 * Gets the active player.
	 * 
	 * @return The first player who has placed a bet and has not finish playing.
	 */
	@Override
	public Player getCurrentPlayer()
	{
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			if (((BlackjackPlayer) players.get(playerIndex)).madeBet() && turnFinishedByPlayer[playerIndex] == false)
			{
				return players.get(playerIndex);
			}
		}
		return null;
	}

	/**
	 * Calculate the value of a blackjack card. An ace's value will be 1. Game
	 * logic will treat it as 11 where appropriate.
	 * 
	 * @return The card's value. All card values are equal to their face number,
	 *         except for jack/queen/king which value at 10
	 */
	@Override
	public int cardValue(TraditionalCard card)
	{
		return Math.min(super.cardValue(card), 10);
	}

	/**
	 * Deals 2 cards to each player including the dealer and adds the
	 * appropriate animations.
	 */
	@Override
	public void deal()
	{
		if (state == BlackjackGameState.Playing)
		{
			TraditionalCard card;
			for (int dealIndex = 0; dealIndex < 2; ++dealIndex)
			{
				for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
				{
					if (((BlackjackPlayer) players.get(playerIndex)).madeBet())
					{
						// Deal a card to one of the players
						card = dealer.dealCardToHand(players.get(playerIndex).hand);

						try
						{
							addDealAnimation(
									card,
									animatedHands[playerIndex],
									true,
									dealDuration,
									TimeSpan.add(
											TimeSpan.now(),
											TimeSpan.fromSeconds(dealDuration.getTotalSeconds()
													* (dealIndex * players.size() + playerIndex))));
						}
						catch (IllegalArgumentException e)
						{
							e.printStackTrace();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				// Deal a card to the dealer
				card = dealer.dealCardToHand(dealerPlayer.hand);
				addDealAnimation(card, dealerHandComponent, dealIndex == 0, dealDuration, TimeSpan.now());
			}
		}
	}

	/**
	 * Performs necessary initializations needed after dealing the cards in
	 * order to start playing.
	 */
	@Override
	public void startPlaying()
	{
		// Check that there are enough players to start playing
		if ((minimumPlayers <= players.size() && players.size() <= maximumPlayers))
		{
			// Set up and register to gameplay events

			GameRule gameRule = new BustRule(players);
			rules.add(gameRule);
			gameRule.ruleMatch.add(this::bustGameRule);

			gameRule = new BlackJackRule(players);
			rules.add(gameRule);
			gameRule.ruleMatch.add(this::blackJackGameRule);

			gameRule = new InsuranceRule(dealerPlayer.hand);
			rules.add(gameRule);
			gameRule.ruleMatch.add(this::insuranceGameRule);

			// Display the hands participating in the game
			for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
			{
				if (((BlackjackPlayer) players.get(playerIndex)).madeBet())
				{
					animatedHands[playerIndex].setVisible(false);
				}
				else
				{
					animatedHands[playerIndex].setVisible(true);
				}
			}
		}
	}

	/**
	 * Display an animation when a card is dealt.
	 * 
	 * @param card
	 *        The card being dealt.
	 * @param animatedHand
	 *        The animated hand into which the card is dealt.
	 * @param flipCard
	 *        Should the card be flipped after dealing it.
	 * @param duration
	 *        The animations desired duration.
	 * @param startTime
	 *        The time at which the animation should start.
	 */
	public void addDealAnimation(TraditionalCard card, AnimatedHandGameComponent animatedHand, boolean flipCard,
			TimeSpan duration, TimeSpan startTime)
	{
		// Get the card location and card component
		int cardLocationInHand = animatedHand.getCardLocationInHand(card);
		AnimatedCardsGameComponent cardComponent = animatedHand.getCardGameComponent(cardLocationInHand);

		// Add the transition animation
		TransitionGameComponentAnimation transitionAnimation =
				new TransitionGameComponentAnimation(gameTable.getDealerPosition(), Vector2.add(
						animatedHand.currentPosition, animatedHand.getCardRelativePosition(cardLocationInHand)));
		transitionAnimation.startTime = new TimeSpan(startTime);
		transitionAnimation.performBeforeStart = this::showComponent;
		transitionAnimation.performBeforSartArgs = cardComponent;
		transitionAnimation.performWhenDone = this::playDealSound;
		cardComponent.addAnimation(transitionAnimation);

		if (flipCard)
		{
			// Add the flip animation
			FlipGameComponentAnimation flipAnimation = new FlipGameComponentAnimation();
			flipAnimation.duration = new TimeSpan(duration);
			flipAnimation.startTime = new TimeSpan(startTime);
			flipAnimation.performWhenDone = this::playFlipSound;
			cardComponent.addAnimation(flipAnimation);
		}
	}

	/**
	 * Helper method to play deal sound.
	 * 
	 * @param obj
	 */
	private void playDealSound(Object obj)
	{
		AudioManager.playSound("Deal");
	}

	/**
	 * Helper method to play flip sound.
	 * 
	 * @param obj
	 */
	private void playFlipSound(Object obj)
	{
		AudioManager.playSound("Flip");
	}

	/**
	 * Adds an animation which displays an asset over a player's hand. The asset
	 * will appear above the hand and appear to "fall" on top of it.
	 * 
	 * @param player
	 *        The player over the hand of which to place the animation.
	 * @param assetName
	 *        Name of the asset to display above the hand.
	 * @param animationHand
	 *        Which hand to put cue over.
	 * @param waitForHand
	 *        Start the cue animation when the animation of this hand over
	 *        null of the animation of the currentHand
	 * @throws IllegalArgumentException
	 */
	private void cueOverPlayerHand(BlackjackPlayer player, String assetName, HandTypes animationHand,
			AnimatedHandGameComponent waitForHand) throws IllegalArgumentException
	{
		// Get the position of the relevant hand
		int playerIndex = players.indexOf(player);
		AnimatedHandGameComponent currentAnimatedHand;
		Vector2 currentPosition;
		if (playerIndex >= 0)
		{
			switch (animationHand)
			{
				case First:
					currentAnimatedHand = animatedHands[playerIndex];
					currentPosition = new Vector2(currentAnimatedHand.currentPosition);
					break;
				case Second:
					currentAnimatedHand = animatedSecondHands[playerIndex];
					currentPosition = Vector2.add(currentAnimatedHand.currentPosition, secondHandOffset);
					break;
				default:
					throw new IllegalArgumentException("Player has an unsupported hand type.");
			}
		}
		else
		{
			currentAnimatedHand = dealerHandComponent;
			currentPosition = new Vector2(currentAnimatedHand.currentPosition);
		}
		
		// Add the animation component
		AnimatedGameComponent animationComponent = new AnimatedGameComponent(this, cardsAssets.get(assetName));
		animationComponent.currentPosition = new Vector2(currentPosition);
		animationComponent.setVisible(false);
		game.getComponents().add(animationComponent);

		// Calculate when to start the animation. The animation will only begin
		// after all hand cards finish animating
		TimeSpan estimatedTimeToCompleteAnimations;
		if (waitForHand != null)
		{
			estimatedTimeToCompleteAnimations = waitForHand.estimatedTimeForAnimationsCompletion();
		}
		else
		{
			estimatedTimeToCompleteAnimations = currentAnimatedHand.estimatedTimeForAnimationsCompletion();
		}

		// Add a scale effect animation
		ScaleGameComponentAnimation scaleAnimation = new ScaleGameComponentAnimation(2.0f, 1.0f);
		try
		{
			scaleAnimation.startTime = TimeSpan.add(TimeSpan.now(), estimatedTimeToCompleteAnimations);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		scaleAnimation.duration = TimeSpan.fromSeconds(1f);
		scaleAnimation.performBeforeStart = this::showComponent;
		scaleAnimation.performBeforSartArgs = animationComponent;
		animationComponent.addAnimation(scaleAnimation);
	}

	/**
	 * Ends the current round.
	 */
	private void endRound()
	{
		revealDealerFirstCard();
		dealerAI();
		showResults();
		state = BlackjackGameState.RoundEnd;
	}

	/**
	 * Causes the dealer's hand to be displayed.
	 */
	private void showDealerHand()
	{
		dealerHandComponent = new BlackjackAnimatedDealerHandComponent(-1, dealerPlayer.hand, this);
		game.getComponents().add(dealerHandComponent);
	}

	/**
	 * Reveal's the dealer's hidden card.
	 */
	private void revealDealerFirstCard()
	{
		// Iterate over all dealer cards expect for the last
		AnimatedCardsGameComponent cardComponent = dealerHandComponent.getCardGameComponent(1);
		FlipGameComponentAnimation flipAnimation = new FlipGameComponentAnimation();
		flipAnimation.duration = TimeSpan.fromSeconds(0.5);
		flipAnimation.startTime = TimeSpan.now();
		cardComponent.addAnimation(flipAnimation);
	}

	/**
	 * Present visual indication as to how the players fared in the current
	 * round.
	 */
	private void showResults()
	{
		// Calculate the dealer's hand value
		int dealerValue = dealerPlayer.getFirstValue();

		if (dealerPlayer.getFirstValueConsiderAce())
		{
			dealerValue += 10;
		}

		// Show each player's result
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			showResultForPlayer((BlackjackPlayer) players.get(playerIndex), dealerValue, HandTypes.First);
			if (((BlackjackPlayer) players.get(playerIndex)).isSplit())
			{
				showResultForPlayer((BlackjackPlayer) players.get(playerIndex), dealerValue, HandTypes.Second);
			}
		}
	}

	/**
	 * Display's a player's status after the turn has ended.
	 * 
	 * @param player
	 *        The player for which to display the status.
	 * @param dealerValue
	 *        The dealer's hand value.
	 * @param currentHandType
	 *        The player's hand to take into account.
	 * @throws IllegalArgumentException
	 */
	private void showResultForPlayer(BlackjackPlayer player, int dealerValue, HandTypes currentHandType)
			throws IllegalArgumentException
	{
		// Calculate the player's hand value and check his state (blackjack/bust)
		boolean blackjack, bust;
		int playerValue;
		switch (currentHandType)
		{
			case First:
				blackjack = player.isBlackJack();
				bust = player.isBust();

				playerValue = player.getFirstValue();

				if (player.getFirstValueConsiderAce())
				{
					playerValue += 10;
				}
				break;
			case Second:
				blackjack = player.isSecondBlackJack();
				bust = player.isSecondBust();

				playerValue = player.getSecondValue();

				if (player.getSecondValueConsiderAce())
				{
					playerValue += 10;
				}
				break;
			default:
				throw new IllegalArgumentException("Player has an unsupported hand type.");
		}
		// The bust or blackjack state are animated independently of this method,
		// so only trigger different outcome indications
		if (player.madeBet() && (!blackjack || (dealerPlayer.isBlackJack() && blackjack)) && !bust)
		{
			String assetName = getResultAsset(player, dealerValue, playerValue);

			cueOverPlayerHand(player, assetName, currentHandType, dealerHandComponent);
		}
	}

	/**
	 * Return the asset name according to the result.
	 * 
	 * @param player
	 *        The player for which to return the asset name.
	 * @param dealerValue
	 *        The dealer's hand value.
	 * @param playerValue
	 *        The player's hand value.
	 * @return The asset name
	 */
	private String getResultAsset(BlackjackPlayer player, int dealerValue, int playerValue)
	{
		String assetName;
		if (dealerPlayer.isBust())
		{
			assetName = "win";
		}
		else if (dealerPlayer.isBlackJack())
		{
			if (player.isBlackJack())
			{
				assetName = "push";
			}
			else
			{
				assetName = "lose";
			}
		}
		else if (playerValue < dealerValue)
		{
			assetName = "lose";
		}
		else if (playerValue > dealerValue)
		{
			assetName = "win";
		}
		else
		{
			assetName = "push";
		}
		return assetName;
	}

	/**
	 * Have the dealer play. The dealer hits until reaching 17+ and then stands.
	 */
	private void dealerAI()
	{
		// The dealer may have not need to draw additional cards after his first
		// two. Check if this is the case and if so end the dealer's play.
		dealerPlayer.calculateValues();
		int dealerValue = dealerPlayer.getFirstValue();

		if (dealerPlayer.getFirstValueConsiderAce())
		{
			dealerValue += 10;
		}

		if (dealerValue > 21)
		{
			dealerPlayer.setIsBust(true);
			cueOverPlayerHand(dealerPlayer, "bust", HandTypes.First, dealerHandComponent);
		}
		else if (dealerValue == 21)
		{
			dealerPlayer.setIsBlackJack(true);
			cueOverPlayerHand(dealerPlayer, "blackjack", HandTypes.First, dealerHandComponent);
		}

		if (dealerPlayer.isBlackJack() || dealerPlayer.isBust())
		{
			return;
		}

		// Draw cards until 17 is reached, or the dealer gets a blackjack or busts
		int cardsDealed = 0;
		while (dealerValue <= 17)
		{
			TraditionalCard card = dealer.dealCardToHand(dealerPlayer.hand);
			try
			{
				addDealAnimation(card, dealerHandComponent, true, dealDuration,
						TimeSpan.add(TimeSpan.now(), TimeSpan.fromMilliseconds(1000 * (cardsDealed + 1))));
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			++cardsDealed;
			dealerPlayer.calculateValues();
			dealerValue = dealerPlayer.getFirstValue();

			if (dealerPlayer.getFirstValueConsiderAce())
			{
				dealerValue += 10;
			}

			if (dealerValue > 21)
			{
				dealerPlayer.setIsBust(true);
				cueOverPlayerHand(dealerPlayer, "bust", HandTypes.First, dealerHandComponent);
			}
		}
	}

	/**
	 * Displays the hands currently in play.
	 */
	private void displayPlayingHands()
	{
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			AnimatedHandGameComponent animatedHandGameComponent = new BlackjackAnimatedPlayerHandComponent(playerIndex,
					players.get(playerIndex).hand, this);
			game.getComponents().add(animatedHandGameComponent);
			animatedHands[playerIndex] = animatedHandGameComponent;
		}

		showDealerHand();
	}

	/**
	 * Starts a new game round.
	 */
	public void startRound()
	{
		playerHandValueTexts.clear();
		AudioManager.playSound("Shuffle");
		dealer.shuffle();
		displayPlayingHands();
		state = BlackjackGameState.Shuffling;
	}

	/**
	 * Sets the button availability according to the options available to the
	 * current player.
	 */
	private void setButtonAvailability()
	{
		BlackjackPlayer player = (BlackjackPlayer) getCurrentPlayer();
		// Hide all buttons if no player is in play or the player is an AI player
		if (player == null || player instanceof BlackjackAIPlayer)
		{
			enableButtons(false);
			changeButtonsVisiblility(false);
			return;
		}

		// Show all buttons
		enableButtons(true);
		changeButtonsVisiblility(true);

		// Set insurance button availability
		buttons.get("Insurance").setVisible(showInsurance);
		buttons.get("Insurance").setEnabled(showInsurance);

		if (player.isSplit() == false)
		{
			// Remember that the bet amount was already reduced from the balance,
			// so we only need to check if the player has more money than the
			// current bet when trying to double/split

			// Set double button availability
			if (player.getBetAmount() > player.getBalance() || player.hand.getCount() != 2)
			{
				buttons.get("Double").setVisible(false);
				buttons.get("Double").setEnabled(false);
			}

			if (player.hand.getCount() != 2
					||
					player.hand.getTraditionalCard(0).getValue() != player.hand.getTraditionalCard(1)
							.getValue() ||
					player.getBetAmount() > player.getBalance())
			{
				buttons.get("Split").setVisible(false);
				buttons.get("Split").setEnabled(false);
			}
		}
		else
		{
			// We've performed a split. Get the initial bet amount to check whether
			// or not we can double the current bet.
			float initialBet = player.getBetAmount()
					/ ((player.isDouble() ? 2f : 1f) + (player.isSecondDouble() ? 2f : 1f));

			// Set double button availability.
			if (initialBet > player.getBalance() || player.getCurrentHand().getCount() != 2)
			{
				buttons.get("Double").setVisible(false);
				buttons.get("Double").setEnabled(false);
			}

			// Once you've split, you can't split again
			buttons.get("Split").setVisible(false);
			buttons.get("Split").setEnabled(false);
		}
	}

	/**
	 * Checks for running animations.
	 * 
	 * @return {@code true} if a running animation of the desired type is found and
	 *         {@code false} otherwise.
	 */
	public <T extends AnimatedGameComponent> boolean checkForRunningAnimations(Class<T> type)
	{
		T animationComponent;
		for (int componentIndex = 0; componentIndex < game.getComponents().size(); ++componentIndex)
		{
			animationComponent = As.as(game.getComponents().get(componentIndex), type);
			if (animationComponent != null)
			{
				if (animationComponent.isAnimating())
					return true;
			}
		}
		return false;
	}

	/**
	 * Ends the game.
	 */
	private void endGame()
	{
		// Calculate the estimated time for all playing animations to end
		long estimatedTime = 0;
		AnimatedGameComponent animationComponent;
		for (int componentIndex = 0; componentIndex < game.getComponents().size(); ++componentIndex)
		{
			animationComponent = As.as(game.getComponents().get(componentIndex), AnimatedGameComponent.class);
			if (animationComponent != null)
			{
				estimatedTime = Math.max(estimatedTime, animationComponent.estimatedTimeForAnimationsCompletion()
						.getTicks());
			}
		}

		// Add a component for an empty stalling animation. This actually acts
		// as a timer.
		Texture2D texture = this.game.getContent().load("images/youlose", Texture2D.class);
		animationComponent = new AnimatedGameComponent(this, texture);
		animationComponent.currentPosition = new Vector2(
				this.game.getGraphicsDevice().getViewport().getBounds().getCenter().x - texture.getWidth() / 2,
                this.game.getGraphicsDevice().getViewport().getBounds().getCenter().y - texture.getHeight() / 2);
		animationComponent.setVisible(false);
		this.game.getComponents().add(animationComponent);

		// Add a button to return to the main menu
		Rectangle bounds = this.game.getGraphicsDevice().getViewport().getBounds();
		Vector2 center = new Vector2(bounds.getCenter().x, bounds.getCenter().y);
		Button backButton = new Button("ButtonRegular", "ButtonPressed", screenManager.input, this);
		backButton.bounds = new Rectangle((int) center.x - 100, (int) center.y + 80, 200, 50);
		backButton.font = this.font;
		backButton.text = "Main Menu";
		backButton.setVisible(false);
		backButton.setEnabled(true);

		backButton.click.add(this::backButton_Click);

		// Add stalling animation
		AnimatedGameComponentAnimation animatedGameAnimation = new AnimatedGameComponentAnimation();
		try
		{
			animatedGameAnimation.duration = TimeSpan.add(TimeSpan.fromTicks(estimatedTime), TimeSpan.fromSeconds(1));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		animatedGameAnimation.performWhenDone = this::resetGame;
		animatedGameAnimation.performWhenDoneArgs = new Object[] { animationComponent, backButton };
		animationComponent.addAnimation(animatedGameAnimation);
		game.getComponents().add(backButton);
	}

	/**
	 * Helper method to reset the game.
	 * 
	 * @param obj
	 */
	private void resetGame(Object obj)
	{
		Object[] arr = (Object[]) obj;
		state = BlackjackGameState.GameOver;
		((AnimatedGameComponent) arr[0]).setVisible(true);
		((Button) arr[1]).setVisible(true);

		// Remove all unnecessary game components
		for (int componentIndex = 0; componentIndex < game.getComponents().size();)
		{
			if ((game.getComponents().get(componentIndex) != ((AnimatedGameComponent) arr[0]) &&
					game.getComponents().get(componentIndex) != ((Button) arr[1])) &&
					(game.getComponents().get(componentIndex) instanceof BetGameComponent ||
							game.getComponents().get(componentIndex) instanceof AnimatedGameComponent ||
					game.getComponents().get(componentIndex) instanceof Button))
			{
				game.getComponents().remove(componentIndex);
			}
			else
				++componentIndex;
		}
	}

	/**
	 * Finishes the current turn.
	 */
	private void finishTurn()
	{
		// Remove all unnecessary components
		for (int componentIndex = 0; componentIndex < game.getComponents().size(); ++componentIndex)
		{
			if (!(game.getComponents().get(componentIndex) instanceof GameTable ||
				  game.getComponents().get(componentIndex) instanceof BlackjackCardGame ||
				  game.getComponents().get(componentIndex) instanceof BetGameComponent ||
				  game.getComponents().get(componentIndex) instanceof Button ||
				  game.getComponents().get(componentIndex) instanceof ScreenManager ||
				  game.getComponents().get(componentIndex) instanceof InputHelper))
			{
				if (game.getComponents().get(componentIndex) instanceof AnimatedCardsGameComponent)
				{
					AnimatedCardsGameComponent animatedCard = (AnimatedCardsGameComponent) game.getComponents().get(
							componentIndex);
					TransitionGameComponentAnimation transitionGameAnimation =
							new TransitionGameComponentAnimation(
									animatedCard.currentPosition,
									new Vector2(animatedCard.currentPosition.x, this.game.getGraphicsDevice().getViewport().getHeight()));
					transitionGameAnimation.duration = TimeSpan.fromSeconds(0.40);
					transitionGameAnimation.performWhenDone = this::removeComponent;
					transitionGameAnimation.performWhenDoneArgs = animatedCard;
					animatedCard.addAnimation(transitionGameAnimation);
				}
				else
				{
					game.getComponents().remove(componentIndex);
					--componentIndex;
				}
			}
		}

		// Reset player values
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			((BlackjackPlayer) players.get(playerIndex)).resetValues();
			players.get(playerIndex).hand
					.dealCardsToHand(deadCards, players.get(playerIndex).hand.getCount());
			turnFinishedByPlayer[playerIndex] = false;
			animatedHands[playerIndex] = null;
			animatedSecondHands[playerIndex] = null;
		}

		// Reset the bet component
		betGameComponent.reset();
		betGameComponent.setEnabled(true);

		// Reset dealer
		dealerPlayer.hand.dealCardsToHand(deadCards, dealerPlayer.hand.getCount());
		dealerPlayer.resetValues();

		// Reset rules
		rules.clear();
	}

	/**
	 * Helper method to remove component.
	 * 
	 * @param obj
	 */
	private void removeComponent(Object obj)
	{
		game.getComponents().remove((AnimatedGameComponent) obj);
	}

	/**
	 * Performs the "Stand" move for the current player.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void stand() throws IllegalArgumentException
	{
		BlackjackPlayer player = (BlackjackPlayer) getCurrentPlayer();
		if (player == null)
			return;

		// If the player only has one hand, his turn ends. Otherwise, he now plays
		// using his next hand
		if (player.isSplit() == false)
		{
			turnFinishedByPlayer[players.indexOf(player)] = true;
		}
		else
		{
			switch (player.getCurrentHandType())
			{
				case First:
					if (player.isSecondBlackJack())
					{
						turnFinishedByPlayer[players.indexOf(player)] = true;
					}
					else
					{
						player.currentHandType = HandTypes.Second;
					}
					break;
				case Second:
					turnFinishedByPlayer[players.indexOf(player)] = true;
					break;
				default:
					throw new IllegalArgumentException("Player has an unsupported hand type.");
			}
		}
	}

	/**
	 * Performs the "Split" move for the current player. This includes adding
	 * the animations which shows the first hand splitting into two.
	 */
	public void split()
	{
		BlackjackPlayer player = (BlackjackPlayer) getCurrentPlayer();

		int playerIndex = players.indexOf(player);

		player.initializeSecondHand();

		Vector2 sourcePosition = animatedHands[playerIndex].getCardGameComponent(1).currentPosition;
		Vector2 targetPosition = Vector2.add(animatedHands[playerIndex].getCardGameComponent(0).currentPosition,
											 secondHandOffset);
		// Create an animation moving the top card to the second hand location
		AnimatedGameComponentAnimation animation = new TransitionGameComponentAnimation(sourcePosition, targetPosition);
		animation.startTime = TimeSpan.now();
		animation.duration = TimeSpan.fromSeconds(0.5);

		// Actually perform the split
		player.splitHand();

		// Add additional chip stack for the second hand
		betGameComponent.addChips(playerIndex, player.getBetAmount(), false, true);

		// Initialize visual representation of the second hand
		animatedSecondHands[playerIndex] =
				new BlackjackAnimatedPlayerHandComponent(playerIndex, secondHandOffset, player.getSecondHand(), this);
		game.getComponents().add(animatedSecondHands[playerIndex]);

		AnimatedCardsGameComponent animatedGameComponet = animatedSecondHands[playerIndex].getCardGameComponent(0);
		animatedGameComponet.isFaceDown = false;
		animatedGameComponet.addAnimation(animation);

		// Deal an additional cards to each of the new hands
		TraditionalCard card = dealer.dealCardToHand(player.hand);
		addDealAnimation(card, animatedHands[playerIndex], true, dealDuration,
				new TimeSpan(TimeSpan.now().getTicks() + animation.getEstimatedTimeForAnimationCompletion().getTicks()));
		card = dealer.dealCardToHand(player.getSecondHand());
		addDealAnimation(card, animatedSecondHands[playerIndex], true, dealDuration,
				new TimeSpan(TimeSpan.now().getTicks() + animation.getEstimatedTimeForAnimationCompletion().getTicks() +
						dealDuration.getTicks()));
	}

	/**
	 * Performs the "Double" move for the current player.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void Double() throws IllegalArgumentException
	{
		BlackjackPlayer player = (BlackjackPlayer) getCurrentPlayer();

		int playerIndex = players.indexOf(player);

		switch (player.getCurrentHandType())
		{
			case First:
				player.setIsDouble(true);
				float betAmount = player.getBetAmount();

				if (player.isSplit())
				{
					betAmount /= 2f;
				}

				betGameComponent.addChips(playerIndex, betAmount, false, false);
				break;
			case Second:
				player.setIsSecondDouble(true);
				if (player.isDouble() == false)
				{
					// The bet is evenly spread between both hands, add one half
					betGameComponent.addChips(playerIndex, player.getBetAmount() / 2f, false, true);
				}
				else
				{
					// The first hand's bet is double, add one third of the total
					betGameComponent.addChips(playerIndex, player.getBetAmount() / 3f, false, true);
				}
				break;
			default:
				throw new IllegalArgumentException("Player has an unsupported hand type.");
		}
		hit();
		stand();
	}

	/**
	 * Performs the "Hit" move for the current player.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void hit() throws IllegalArgumentException
	{
		BlackjackPlayer player = (BlackjackPlayer) getCurrentPlayer();
		if (player == null)
			return;

		int playerIndex = players.indexOf(player);

		// Draw a card to the appropriate hand
		switch (player.getCurrentHandType())
		{
			case First:
				TraditionalCard card = dealer.dealCardToHand(player.hand);
				addDealAnimation(card, animatedHands[playerIndex], true, dealDuration, TimeSpan.now());
				break;
			case Second:
				card = dealer.dealCardToHand(player.getSecondHand());
				addDealAnimation(card, animatedSecondHands[playerIndex], true, dealDuration, TimeSpan.now());
				break;
			default:
				throw new IllegalArgumentException("Player has an unsupported hand type.");
		}
	}

	/**
	 * Changes the visibility of most game buttons.
	 * 
	 * @param visible
	 *        True to make the buttons visible, false to make them
	 *        invisible.
	 */
	void changeButtonsVisiblility(boolean visible)
	{
		buttons.get("Hit").setVisible(visible);
		buttons.get("Stand").setVisible(visible);
		buttons.get("Double").setVisible(visible);
		buttons.get("Split").setVisible(visible);
		buttons.get("Insurance").setVisible(visible);
	}

	/**
	 * Enables or disable most game buttons.
	 * 
	 * @param enabled
	 *        True to enable the buttons , false to disable them.
	 */
	void enableButtons(boolean enabled)
	{
		buttons.get("Hit").setEnabled(enabled);
		buttons.get("Stand").setEnabled(enabled);
		buttons.get("Double").setEnabled(enabled);
		buttons.get("Split").setEnabled(enabled);
		buttons.get("Insurance").setEnabled(enabled);
	}

	/**
	 * Add an indication that the player has passed on the current round.
	 * 
	 * @param indexPlayer
	 *        The player's index.
	 */
	public void showPlayerPass(int indexPlayer)
	{
		// Add animation component
		AnimatedGameComponent passComponent = new AnimatedGameComponent(this, cardsAssets.get("pass"));
		passComponent.currentPosition = gameTable.getPlaceOrder().apply(indexPlayer);
		passComponent.setVisible(false);
		game.getComponents().add(passComponent);

		// Hide insurance button only when the first payer passes
		Consumer<Object> performWhenDone = null;
		if (indexPlayer == 0) {
			performWhenDone = this::hideInshurance;
		}

		// Add scale animation for the pass "card"
		ScaleGameComponentAnimation scaleAnimation = new ScaleGameComponentAnimation(2.0f, 1.0f);
		scaleAnimation.setAnimationCycles(1);
		scaleAnimation.startTime = TimeSpan.now();
		scaleAnimation.duration = TimeSpan.fromSeconds(1);
		scaleAnimation.performBeforeStart = this::showComponent;
		scaleAnimation.performBeforSartArgs = passComponent;
		scaleAnimation.performWhenDone = performWhenDone;
		passComponent.addAnimation(scaleAnimation);
	}

	/**
	 * Helper method to hide insurance
	 * 
	 * @param obj
	 */
	void hideInshurance(Object obj) {
		showInsurance = false;
	}

	/**
	 * Shows the insurance button if the first player can afford insurance.
	 * 
	 * @param sender
	 *        The sender.
	 * @param e
	 *        The {@link System.EventArgs} instance containing the event data.
	 */
	void insuranceGameRule(Object sender, EventArgs e)
	{
		BlackjackPlayer player = (BlackjackPlayer)players.get(0);
		if (player.getBalance() >= player.getBetAmount() / 2)
		{
			showInsurance = true;
		}
	}

	/**
	 * Shows the bust visual cue after the bust rule has been matched.
	 * 
	 * @param sender
	 *        The sender.
	 * @param e
	 *        The {@link System.EventArgs} instance containing the event data.
	 */
	void bustGameRule(Object sender, EventArgs e)
	{
		showInsurance = false;
		BlackjackGameEventArgs args = As.as(e, BlackjackGameEventArgs.class);
		BlackjackPlayer player = (BlackjackPlayer)args.player;
	
		cueOverPlayerHand(player, "bust", args.hand, null);
	
		switch (args.hand)
		{
			case First:
				player.isBust = true;
	
				if (player.isSplit && !player.isSecondBlackJack)
				{
					player.currentHandType = HandTypes.Second;
				}
				else
				{
					turnFinishedByPlayer[players.indexOf(player)] = true;
				}
				break;
			case Second:
				player.isSecondBust = true;
				turnFinishedByPlayer[players.indexOf(player)] = true;
				break;
			default:
				throw new RuntimeException("Player has an unsupported hand type.");
		}
	}

	/**
	 * Shows the blackjack visual cue after the blackjack rule has been matched.
	 * 
	 * @param sender
	 *        The sender.
	 * @param e
	 *        The {@link System.EventArgs} instance containing the event data.
	 */
	void blackJackGameRule(Object sender, EventArgs e)
	{
		showInsurance = false;
		BlackjackGameEventArgs args = As.as(e, BlackjackGameEventArgs.class);
		BlackjackPlayer player = (BlackjackPlayer)args.player;
	
		cueOverPlayerHand(player, "blackjack", args.hand, null);
	
		switch (args.hand)
		{
			case First:
				player.isBlackJack = true;
	
				if (player.isSplit)
				{
					player.currentHandType = HandTypes.Second;
				}
				else
				{
					turnFinishedByPlayer[players.indexOf(player)] = true;
				}
				break;
			case Second:
				player.isSecondBlackJack = true;
				if (player.currentHandType == HandTypes.Second)
				{
					turnFinishedByPlayer[players.indexOf(player)] = true;
				}
				break;
			default:
				throw new RuntimeException("Player has an unsupported hand type.");
		}
	}
	
	/**
	 * Handles the Click event of the insurance button.
	 * 
	 * @param sender
	 *        The source of the event.
	 * @param e
	 *        The {@link System.EventArgs} instance containing the event data.
	 */
	void insurance_Click(Object sender, EventArgs e)
	{
		BlackjackPlayer player = (BlackjackPlayer) getCurrentPlayer();
		if (player == null)
			return;
		player.isInsurance = true;
		player.balance -= player.getBetAmount() / 2f;
		betGameComponent.addChips(players.indexOf(player),
								  player.getBetAmount() / 2,
								  true, false);
		showInsurance = false;
	}

	/**
	 * Handles the Click event of the new game button.
	 * 
	 * @param sender
	 *        The source of the event.
	 * @param e
	 *        The {@link System.EventArgs} instance containing the event data.
	 */
	void newGame_Click(Object sender, EventArgs e)
	{
		finishTurn();
		startRound();
		newGame.setEnabled(false);
		newGame.setVisible(false);
	}

	/**
	 * Handles the Click event of the hit button.
	 * 
	 * @param sender
	 *        The source of the event.
	 * @param e
	 *        The {@link System.EventArgs} instance containing the event data.
	 */
	void hit_Click(Object sender, EventArgs e)
	{
		hit();
		showInsurance = false;
	}

	/**
	 * Handles the Click event of the stand button.
	 * 
	 * @param sender
	 *        The source of the event.
	 * @param e
	 *        The {@link System.EventArgs} instance containing the event data.
	 */
	void stand_Click(Object sender, EventArgs e)
	{
		stand();
		showInsurance = false;
	}

	/**
	 * Handles the Click event of the double button.
	 * 
	 * @param sender
	 *        The source of the event.
	 * @param e
	 *        The {@link System.EventArgs} instance containing the event data.
	 */
	void double_Click(Object sender, EventArgs e)
	{
		Double();
		showInsurance = false;
	}

	/**
	 * Handles the Click event of the split button.
	 * 
	 * @param sender
	 *        The source of the event.
	 * @param e
	 *        The {@link System.EventArgs} instance containing the event data.
	 */
	void split_Click(Object sender, EventArgs e)
	{
		split();
		showInsurance = false;
	}

	/**
	 * Handles the Click event of the back button.
	 * 
	 * @param sender
	 *        The source of the event.
	 * @param e
	 *        The {@link System.EventArgs} instance containing the event data.
	 */
	void backButton_Click(Object sender, EventArgs e)
	{
		// Remove all unnecessary components
		for (int componentIndex = 0; componentIndex < game.getComponents().size(); ++componentIndex)
		{
			if (!(game.getComponents().get(componentIndex) instanceof ScreenManager))
			{
				game.getComponents().remove(componentIndex);
				--componentIndex;
			}
	}
	
	for (GameScreen screen : screenManager.getScreens())
		screen.exitScreen();
	
	screenManager.addScreen(new BackgroundScreen());
	screenManager.addScreen(new MainMenuScreen());
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * Returns the current state of the game.
	 * 
	 * @return The current state of the game.
	 */
	public BlackjackGameState getState()
	{
		return state;
	}

	// ++++++++++ SETTERS ++++++++++ //

	/**
	 * Set the state of the game to the specified value.
	 * 
	 * @param state
	 *        the new state for the game.
	 */
	public void setState(BlackjackGameState state)
	{
		this.state = state;
	}

}
