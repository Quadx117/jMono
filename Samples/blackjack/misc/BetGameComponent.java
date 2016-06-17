package blackjack.misc;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.components.DrawableGameComponent;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.input.ButtonState;
import jMono_Framework.input.Mouse;
import jMono_Framework.input.MouseState;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import blackjack.BlackjackGame;
import blackjack.UI.BlackJackTable;
import blackjack.UI.Button;
import blackjack.cardsFramework.UI.AnimatedGameComponent;
import blackjack.cardsFramework.UI.FlipGameComponentAnimation;
import blackjack.cardsFramework.UI.TransitionGameComponentAnimation;
import blackjack.cardsFramework.game.CardsGame;
import blackjack.cardsFramework.players.Player;
import blackjack.cardsFramework.screenManager.InputState;
import blackjack.game.BlackjackCardGame;
import blackjack.game.BlackjackCardGame.BlackjackGameState;
import blackjack.players.BlackjackAIPlayer;
import blackjack.players.BlackjackPlayer;
import blackjack.players.BlackjackPlayer.HandTypes;

public class BetGameComponent extends DrawableGameComponent
{
	private List<Player> players;
	private String theme;
	private int[] assetNames = { 5, 25, 100, 500 };
	private HashMap<Integer, Texture2D> chipsAssets;
	private Texture2D blankChip;
	private Vector2[] positions;
	private CardsGame cardGame;
	private SpriteBatch spriteBatch;

	private boolean isKeyDown = false;

	private Button bet;
	private Button clear;

	public Vector2 chipOffset = new Vector2();
	private static float insuranceYPosition = 120 * BlackjackGame.HEIGHT_SCALE;
	private static Vector2 secondHandOffset = new Vector2(25 * BlackjackGame.WIDTH_SCALE,
			30 * BlackjackGame.HEIGHT_SCALE);

	private List<AnimatedGameComponent> currentChipComponent = new ArrayList<AnimatedGameComponent>();
	private int currentBet = 0;
	private InputState input;
	private InputHelper inputHelper;

	/**
	 * Creates a new instance of the {@link BetGameComponent} class.
	 * 
	 * @param players
	 *        A list of participating players.
	 * @param input
	 *        An instance of {@link GameStateManagement.InputState} which
	 *        can be used to check user input.
	 * @param theme
	 *        The name of the selected card theme.
	 * @param cardGame
	 *        An instance of {@link CardsGame} which is the current game.
	 */
	public BetGameComponent(List<Player> players, InputState input, String theme, CardsGame cardGame)
	{
		super(cardGame.game);
		this.players = players;
		this.theme = theme;
		this.cardGame = cardGame;
		this.input = input;
		chipsAssets = new HashMap<Integer, Texture2D>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize()
	{
		// #if WINDOWS_PHONE
		// Enable tap gesture
		// TouchPanel.EnabledGestures = GestureType.Tap;
		// #endif
		// Get xbox cursor
		inputHelper = null;
		for (int componentIndex = 0; componentIndex < game.getComponents().size(); ++componentIndex)
		{
			if (game.getComponents().get(componentIndex) instanceof InputHelper)
			{
				inputHelper = (InputHelper) game.getComponents().get(componentIndex);
				break;
			}
		}

		// Show mouse
		game.setMouseVisible(true);

		super.initialize();

		spriteBatch = new SpriteBatch(game.getGraphicsDevice());

		// Calculate chips position for the chip buttons which allow placing the bet
		Rectangle size = chipsAssets.get(assetNames[0]).getBounds();

		Rectangle bounds = spriteBatch.getGraphicsDevice().getViewport().getTitleSafeArea();

		positions[chipsAssets.size() - 1] = new Vector2(bounds.left() + 10, bounds.bottom() - size.height - 80);
		for (int chipIndex = 2; chipIndex <= chipsAssets.size(); ++chipIndex)
		{
			size = chipsAssets.get(assetNames[chipsAssets.size() - chipIndex]).getBounds();
			positions[chipsAssets.size() - chipIndex] =
					Vector2.subtract(positions[chipsAssets.size() - (chipIndex - 1)], new Vector2(0, size.height + 10));
		}

		// Initialize bet button
		bet = new Button("ButtonRegular", "ButtonPressed", input, cardGame);
		bet.bounds = new Rectangle(bounds.left() + 10, bounds.bottom() - 60, 100, 50);
		bet.font = cardGame.font;
		bet.text = "Deal";
		bet.click.add(this::bet_Click);
		game.getComponents().add(bet);

		// Initialize clear button
		clear = new Button("ButtonRegular", "ButtonPressed", input, cardGame);
		clear.bounds = new Rectangle(bounds.left() + 120, bounds.bottom() - 60, 100, 50);
		clear.font = cardGame.font;
		clear.text = "Clear";
		clear.click.add(this::clear_Click);
		game.getComponents().add(clear);

		showAndEnableButtons(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadContent()
	{
		// Load blank chip texture
		blankChip = game.getContent().load("images/Chips/chipWhite", Texture2D.class);

		// Load chip textures
		int[] assetNames = { 5, 25, 100, 500 };
		for (int chipIndex = 0; chipIndex < assetNames.length; ++chipIndex)
		{
			chipsAssets.put(assetNames[chipIndex], game.getContent().load(
					String.format("images/Chips/chip%s", assetNames[chipIndex]), Texture2D.class));
		}
		positions = new Vector2[assetNames.length];

		super.loadContent();
	}

	/**
	 * Perform update logic related to the component.
	 */
	@Override
	public void update(GameTime gameTime)
	{
		if (players.size() > 0)
		{
			// If betting is possible
			if (((BlackjackCardGame) cardGame).getState() == BlackjackGameState.Betting &&
				!((BlackjackPlayer) players.get(players.size() - 1)).isDoneBetting())
			{
				int playerIndex = getCurrentPlayer();

				BlackjackPlayer player = (BlackjackPlayer) players.get(playerIndex);

				// If the player is an AI player, have it bet
				if (player instanceof BlackjackAIPlayer)
				{
					showAndEnableButtons(false);
					int bet = ((BlackjackAIPlayer) player).AIBet();
					if (bet == 0)
					{
						bet_Click(this, EventArgs.Empty);
					}
					else
					{
						addChip(playerIndex, bet, false);
					}
				}
				else
				{
					// Reveal the input buttons for a human player and handle input
					// remember that buttons handle their own input, so we only check
					// for input on the chip buttons
					showAndEnableButtons(true);

					handleInput(Mouse.getState());
				}
			}

			// Once all players are done betting, advance the game to the
			// dealing stage
			if (((BlackjackPlayer) players.get(players.size() - 1)).isDoneBetting())
			{
				BlackjackCardGame blackjackGame = ((BlackjackCardGame) cardGame);

				if (!blackjackGame.checkForRunningAnimations(AnimatedGameComponent.class))
				{
					showAndEnableButtons(false);
					blackjackGame.setState(BlackjackGameState.Dealing);

					setEnabled(false);
				}
			}
		}

		super.update(gameTime);
	}

	/**
	 * Gets the player which is currently betting. This is the first player who
	 * has yet to finish betting.
	 * 
	 * @return The player which is currently betting.
	 */
	private int getCurrentPlayer()
	{
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			if (!((BlackjackPlayer) players.get(playerIndex)).isDoneBetting())
			{
				return playerIndex;
			}
		}
		return -1;
	}

	/**
	 * Handle the input of adding chip on all platform
	 * 
	 * @param mouseState
	 *        Mouse input information.
	 */
	private void handleInput(MouseState mouseState)
	{
		boolean isPressed = false;
		Vector2 position = new Vector2(Vector2.ZERO);

		if (mouseState.getLeftButton() == ButtonState.Pressed)
		{
			isPressed = true;
			position = new Vector2(mouseState.getX(), mouseState.getY());
		}
		else if (inputHelper.isPressed)
		{
			isPressed = true;
			position = inputHelper.getPointPosition();
		}
// #if WINDOWS_PHONE
		// else if ((input.Gestures.Count > 0) && input.Gestures[0].GestureType == GestureType.Tap)
		// {
			// isPressed = true;
			// position = input.Gestures[0].Position;
		// }
// #endif

		if (isPressed)
		{
			if (!isKeyDown)
			{
				int chipValue = getIntersectingChipValue(position);
				if (chipValue != 0)
				{
					addChip(getCurrentPlayer(), chipValue, false);
				}
				isKeyDown = true;
			}
		}
		else
		{
			isKeyDown = false;
		}
	}

	/**
	 * Get which chip intersects with a given position.
	 * 
	 * @param position
	 *        The position to check for intersection.
	 * 
	 * @return The value of the chip intersecting with the specified position
	 *         or 0 if no chips intersect with the position.
	 */
	private int getIntersectingChipValue(Vector2 position)
	{
		Rectangle size;
		// Calculate the bounds of the position
		Rectangle touchTap = new Rectangle((int) position.x - 1, (int) position.y - 1, 2, 2);
		for (int chipIndex = 0; chipIndex < chipsAssets.size(); ++chipIndex)
		{
			// Calculate the bounds of the asset
			size = chipsAssets.get(assetNames[chipIndex]).getBounds();
			size.x = (int) positions[chipIndex].x;
			size.y = (int) positions[chipIndex].y;
			if (size.intersects(touchTap))
			{
				return assetNames[chipIndex];
			}
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		spriteBatch.begin();

		// Draws the chips
		for (int chipIndex = 0; chipIndex < chipsAssets.size(); ++chipIndex)
		{
			spriteBatch.draw(chipsAssets.get(assetNames[chipIndex]), positions[chipIndex], Color.White);
		}

		BlackjackPlayer player;

		// Draws the player balance and bet amount
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			BlackJackTable table = (BlackJackTable) cardGame.getGameTable();
			Vector2 position = Vector2.add(table.getPlaceOrder().apply(playerIndex),
					new Vector2(table.getRingTexture().getBounds().width, 0).add(
							table.getRingOffset()));
			player = (BlackjackPlayer) players.get(playerIndex);
			spriteBatch.drawString(cardGame.font, "$" + String.format("%.0f", player.getBetAmount()),
					position, Color.White);
			spriteBatch.drawString(cardGame.font, "$" + String.format("%.0f", player.balance),
					Vector2.add(position, new Vector2(0, 30)), Color.White);
		}

		spriteBatch.end();

		super.draw(gameTime);
	}

	/**
	 * Adds the chip to one of the player betting zones.
	 * 
	 * @param playerIndex
	 *        Index of the player for whom to add a chip.
	 * @param chipValue
	 *        The value on the chip to add.
	 * @param secondHand
	 *        {@code True} if this chip is added to the chip pile
	 *        belonging to the player's second hand.
	 */
	public void addChip(int playerIndex, int chipValue, boolean secondHand)
	{
		// Only add the chip if the bet is successfully performed
		if (((BlackjackPlayer) players.get(playerIndex)).bet(chipValue))
		{
			currentBet += chipValue;
			// Add chip component
			AnimatedGameComponent chipComponent = new AnimatedGameComponent(cardGame, chipsAssets.get(chipValue));
			chipComponent.setVisible(false);

			game.getComponents().add(chipComponent);

			// Calculate the position for the new chip
			Vector2 position;
			// Get the proper offset according to the platform (pc, phone, xbox)
			Vector2 offset = getChipOffset(playerIndex, secondHand);

			position = Vector2.add(cardGame.getGameTable().getPlayerPosition(playerIndex), offset);
			position.add(new Vector2(-currentChipComponent.size() * 2, currentChipComponent.size() * 1));

			// Find the index of the chip
			int currentChipIndex = 0;
			for (int chipIndex = 0; chipIndex < chipsAssets.size(); ++chipIndex)
			{
				if (assetNames[chipIndex] == chipValue)
				{
					currentChipIndex = chipIndex;
					break;
				}
			}

			// Add transition animation
			TransitionGameComponentAnimation transitionAnimation = new TransitionGameComponentAnimation(
					positions[currentChipIndex], position);
			transitionAnimation.duration = TimeSpan.fromSeconds(1f);
			transitionAnimation.performBeforeStart = this::showComponent;
			transitionAnimation.performBeforSartArgs = chipComponent;
			transitionAnimation.performWhenDone = this::playBetSound;
			chipComponent.addAnimation(transitionAnimation);

			// Add flip animation
			FlipGameComponentAnimation flipAnimation = new FlipGameComponentAnimation();
			flipAnimation.duration = TimeSpan.fromSeconds(1f);
			flipAnimation.setAnimationCycles(3);
			chipComponent.addAnimation(flipAnimation);

			currentChipComponent.add(chipComponent);
		}
	}

	/**
	 * Helper method to show component.
	 * 
	 * @param obj
	 */
	void showComponent(Object obj)
	{
		((AnimatedGameComponent) obj).setVisible(true);
	}

	/**
	 * Helper method to play bet sound.
	 * 
	 * @param obj
	 */
	void playBetSound(Object obj)
	{
		// TODO: Sound
		// AudioManager.PlaySound("Bet");
	}

	/**
	 * Adds chips to a specified player.
	 * 
	 * @param playerIndex
	 *        Index of the player.
	 * @param amount
	 *        The total amount to add.
	 * @param insurance
	 *        If {@code true}, an insurance chip is added instead of regular chips.
	 * @param secondHand
	 *        True if chips are to be added to the player's second hand.
	 */
	public void addChips(int playerIndex, float amount, boolean insurance, boolean secondHand)
	{
		if (insurance)
		{
			addInsuranceChipAnimation(amount);
		}
		else
		{
			addChips(playerIndex, amount, secondHand);
		}
	}

	/**
	 * Resets this instance.
	 */
	public void reset()
	{
		showAndEnableButtons(true);
		currentChipComponent.clear();
	}

	/**
	 * Updates the balance of all players in light of their bets and the dealer's hand.
	 * 
	 * @param dealerPlayer
	 *        Player object representing the dealer.
	 */
	public void calculateBalance(BlackjackPlayer dealerPlayer)
	{
		for (int playerIndex = 0; playerIndex < players.size(); ++playerIndex)
		{
			BlackjackPlayer player = (BlackjackPlayer) players.get(playerIndex);

			// Calculate first factor, which represents the amount of the first
			// hand bet which returns to the player
			float factor = calculateFactorForHand(dealerPlayer, player, HandTypes.First);

			if (player.isSplit())
			{
				// Calculate the return factor for the second hand
				float factor2 = calculateFactorForHand(dealerPlayer, player, HandTypes.Second);
				// Calculate the initial bet performed by the player
				float initialBet = player.getBetAmount()
						/ ((player.isDouble() ? 2f : 1f) + (player.isSecondDouble() ? 2f : 1f));

				float bet1 = initialBet * (player.isDouble() ? 2f : 1f);
				float bet2 = initialBet * (player.isSecondDouble() ? 2f : 1f);

				// Update the balance in light of the bets and results
				player.balance += bet1 * factor + bet2 * factor2;

				if (player.isInsurance() && dealerPlayer.isBlackJack())
				{
					player.balance += initialBet;
				}
			}
			else
			{
				if (player.isInsurance() && dealerPlayer.isBlackJack())
				{
					player.balance += player.getBetAmount();
				}

				// Update the balance in light of the bets and results
				player.balance += player.getBetAmount() * factor;
			}

			player.clearBet();
		}
	}

	/**
	 * Adds chips to a specified player in order to reach a specified bet amount.
	 * 
	 * @param playerIndex
	 *        Index of the player to whom the chips are to be added.
	 * @param amount
	 *        The bet amount to add to the player.
	 * @param secondHand
	 *        True to add the chips to the player's second hand, false to add them to the first
	 *        hand.
	 */
	private void addChips(int playerIndex, float amount, boolean secondHand)
	{
		int[] assetNames = { 5, 25, 100, 500 };

		while (amount > 0)
		{
			if (amount >= 5)
			{
				// Add the chip with the highest possible value
				for (int chipIndex = assetNames.length; chipIndex > 0; chipIndex--)
				{
					while (assetNames[chipIndex - 1] <= amount)
					{
						addChip(playerIndex, assetNames[chipIndex - 1], secondHand);
						amount -= assetNames[chipIndex - 1];
					}
				}
			}
			else
			{
				amount = 0;
			}
		}
	}

	/**
	 * Animates the placement of an insurance chip on the table.
	 * 
	 * @param amount
	 *        The amount which should appear on the chip.
	 */
	private void addInsuranceChipAnimation(float amount)
	{
		// Add chip component
		AnimatedGameComponent chipComponent = new AnimatedGameComponent(cardGame, blankChip);
		chipComponent.textColor = Color.Black;
		chipComponent.setEnabled(true);
		chipComponent.setVisible(false);

		game.getComponents().add(chipComponent);

		// Add transition animation
		TransitionGameComponentAnimation transitionAnimation =
				new TransitionGameComponentAnimation(positions[0],
						new Vector2(getGraphicsDevice().getViewport().getWidth() / 2,
									insuranceYPosition));
		transitionAnimation.duration = TimeSpan.fromSeconds(1);
		transitionAnimation.startTime = TimeSpan.now();
		transitionAnimation.performBeforeStart = this::showComponent;
		transitionAnimation.performBeforSartArgs = chipComponent;
		transitionAnimation.performWhenDone = this::showChipAmountAndPlayBetSound;
		transitionAnimation.performWhenDoneArgs = new Object[] { chipComponent, amount };
		chipComponent.addAnimation(transitionAnimation);

		// Add flip animation
		FlipGameComponentAnimation flipAnimation = new FlipGameComponentAnimation();
		flipAnimation.duration = TimeSpan.fromSeconds(1f);
		flipAnimation.setAnimationCycles(3);
		chipComponent.addAnimation(flipAnimation);
	}

	/**
	 * Helper method to show the amount on the chip and play bet sound
	 * @param obj
	 */
	private void showChipAmountAndPlayBetSound(Object obj)
	{
		Object[] arr = (Object[]) obj;
		((AnimatedGameComponent) arr[0]).text = (arr[1].toString());
		// TODO: Sound
		// AudioManager.PlaySound("Bet");
	}

	/**
	 * Gets the offset at which newly added chips should be placed.
	 * 
	 * @param playerIndex
	 *        Index of the player to whom the chip is added.
	 * @param secondHand
	 *        True if the chip is added to the player's second hand, false otherwise.
	 * 
	 * @return The offset from the player's position where chips should be placed.
	 */
	private Vector2 getChipOffset(int playerIndex, boolean secondHand)
	{
		Vector2 offset = new Vector2(Vector2.ZERO);

		BlackJackTable table = ((BlackJackTable) cardGame.getGameTable());
		offset = Vector2.add(table.getRingOffset(),
						new Vector2((table.getRingTexture().getBounds().width - blankChip.getBounds().width) / 2,
								(table.getRingTexture().getBounds().height - blankChip.getBounds().height) / 2f));

		if (secondHand == true)
		{
			offset.add(secondHandOffset);
		}

		return offset;
	}

	/**
	 * Show and enable, or hide and disable, the bet related buttons.
	 * 
	 * @param visibleEnabled
	 *        True to show and enable the buttons, false to hide and disable them.
	 */
	private void showAndEnableButtons(boolean visibleEnabled)
	{
		bet.setVisible(visibleEnabled);
		bet.setEnabled(visibleEnabled);
		clear.setVisible(visibleEnabled);
		clear.setEnabled(visibleEnabled);
	}

	/**
	 * Returns a factor which determines how much of a bet a player should get back,
	 * according to the outcome of the round.
	 * 
	 * @param dealerPlayer
	 *        The player representing the dealer.
	 * @param player
	 *        The player for whom we calculate the factor.
	 * @param currentHand
	 *        The hand to calculate the factor for.
	 * 
	 * @return A factor which determines how much of a bet a player should get back.
	 * 
	 * @throws IllegalArgumentException
	 */
	private float calculateFactorForHand(BlackjackPlayer dealerPlayer, BlackjackPlayer player, HandTypes currentHand)
			throws IllegalArgumentException
	{
		float factor;

		boolean blackjack, bust, considerAce;
		int playerValue;
		player.calculateValues();

		// Get some player status information according to the desired hand
		switch (currentHand)
		{
			case First:
				blackjack = player.isBlackJack();
				bust = player.isBust();
				playerValue = player.getFirstValue();
				considerAce = player.getFirstValueConsiderAce();
				break;
			case Second:
				blackjack = player.isSecondBlackJack();
				bust = player.isSecondBust();
				playerValue = player.getSecondValue();
				considerAce = player.getSecondValueConsiderAce();
				break;
			default:
				throw new IllegalArgumentException("Player has an unsupported hand type.");
		}

		if (considerAce)
		{
			playerValue += 10;
		}

		if (bust)
		{
			factor = -1; // Bust
		}
		else if (dealerPlayer.isBust())
		{
			if (blackjack)
			{
				factor = 1.5f; // Win BlackJack
			}
			else
			{
				factor = 1; // Win
			}
		}
		else if (dealerPlayer.isBlackJack())
		{
			if (blackjack)
			{
				factor = 0; // Push BlackJack
			}
			else
			{
				factor = -1; // Lose BlackJack
			}
		}
		else if (blackjack)
		{
			factor = 1.5f;
		}
		else
		{
			int dealerValue = dealerPlayer.getFirstValue();

			if (dealerPlayer.getFirstValueConsiderAce())
			{
				dealerValue += 10;
			}

			if (playerValue > dealerValue)
			{
				factor = 1; // Win
			}
			else if (playerValue < dealerValue)
			{
				factor = -1; // Lose
			}
			else
			{
				factor = 0; // Push
			}
		}
		return factor;
	}

	/**
	 * Handles the Click event of the Clear button.
	 * 
	 * @param sender
	 *          The source of the event.
	 * @param e
	 *          The {@link EventArgs} instance containing the event data.
	 */
	void clear_Click(Object sender, EventArgs e)
	{
		// Clear current player chips from screen and resets his bet
		currentBet = 0;
		((BlackjackPlayer) players.get(getCurrentPlayer())).clearBet();
		for (int chipComponentIndex = 0; chipComponentIndex < currentChipComponent.size(); ++chipComponentIndex)
		{
			game.getComponents().remove(currentChipComponent.get(chipComponentIndex));
		}
		currentChipComponent.clear();
	}
	

	/**
	 * Handles the Click event of the Bet button.
	 * 
	 * @param sender
	 *          The source of the event.
	 * @param e
	 *          The {@link EventArgs} instance containing the event data.
	 */
	void bet_Click(Object sender, EventArgs e)
	{
		// Finish the bet
		int playerIndex = getCurrentPlayer();
		// If the player did not bet, show that he has passed on this round
		if (currentBet == 0)
		{
			((BlackjackCardGame) cardGame).showPlayerPass(playerIndex);
		}
		((BlackjackPlayer) players.get(playerIndex)).isDoneBetting = true;
		currentChipComponent.clear();
		currentBet = 0;
	}
	

	// TODO: Do I keep these ?
	// ++++++++++ GETTERS ++++++++++ //

	public Vector2 getChipOffset()
	{
		return chipOffset;
	}

	// ++++++++++ SETTERS ++++++++++ //

	public void setChipOffset(Vector2 offset)
	{
		chipOffset = offset;
	}
}
