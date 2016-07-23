package blackjack.screens;

import jMono_Framework.Rectangle;
import jMono_Framework.components.DrawableGameComponent;
import jMono_Framework.components.IGameComponent;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;

import java.util.ArrayList;
import java.util.List;

import blackjack.BlackjackGame;
import blackjack.cardsFramework.UI.AnimatedGameComponent;
import blackjack.cardsFramework.UI.GameTable;
import blackjack.game.BlackjackCardGame;
import blackjack.misc.BetGameComponent;
import blackjack.misc.InputHelper;
import blackjack.players.BlackjackAIPlayer;
import blackjack.players.BlackjackPlayer;
import blackjack.screenManager.GameScreen;
import blackjack.screenManager.InputState;

public class GameplayScreen extends GameScreen
{
	BlackjackCardGame blackJackGame;

	InputHelper inputHelper;

	String theme;

	List<DrawableGameComponent> pauseEnabledComponents = new ArrayList<DrawableGameComponent>();
	List<DrawableGameComponent> pauseVisibleComponents = new ArrayList<DrawableGameComponent>();

	Rectangle safeArea;

	static Vector2[] playerCardOffset = new Vector2[] {
			new Vector2(100.0f * BlackjackGame.WIDTH_SCALE, 190.0f * BlackjackGame.HEIGHT_SCALE),
			new Vector2(336.0f * BlackjackGame.WIDTH_SCALE, 210.0f * BlackjackGame.HEIGHT_SCALE),
			new Vector2(570.0f * BlackjackGame.WIDTH_SCALE, 190.0f * BlackjackGame.HEIGHT_SCALE)
	};

	/**
	 * Initializes a new instance of the screen.
	 * 
	 * @param theme
	 * @param screenManager
	 */
	public GameplayScreen(String theme)
	{
		transitionOnTime = TimeSpan.fromSeconds(0.0);
		transitionOffTime = TimeSpan.fromSeconds(0.5);
// #if WINDOWS_PHONE
		// EnabledGestures = GestureType.Tap;
// #endif
		this.theme = theme;
	}

	/**
	 * Load content and initializes the actual game.
	 */
	@Override
	public void loadContent()
	{
		safeArea = screenManager.getSafeArea();

		// TODO: This is only for X-box (could be refactored)
		// Initialize virtual cursor
		inputHelper = new InputHelper(screenManager.getGame());
		inputHelper.setDrawOrder(1000);
		screenManager.getGame().getComponents().add(inputHelper);
		// Ignore the cursor when not run in X-box
// #if !XBOX
		inputHelper.setVisible(false);
		inputHelper.setEnabled(false);
// #endif

		blackJackGame = new BlackjackCardGame(screenManager.getGraphicsDevice().getViewport().getBounds(),
				new Vector2(safeArea.left() + safeArea.width / 2 - 50, safeArea.top() + 20),
				this::getPlayerCardPosition, screenManager, theme);

		initializeGame();

		super.loadContent();
	}

	/**
	 * Unload content loaded by the screen.
	 */
	@Override
	public void unloadContent()
	{
		screenManager.getGame().getComponents().remove(inputHelper);

		super.unloadContent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleInput(InputState input)
	{
		if (input.isPauseGame())
		{
			pauseCurrentGame();
		}

		super.handleInput(input);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(GameTime gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
// #if XBOX
//		if (Guide.IsVisible)
//		{
//			pauseCurrentGame();
//		}
// #endif
		if (blackJackGame != null && !coveredByOtherScreen)
		{
			blackJackGame.update(gameTime);
		}

		super.update(gameTime, otherScreenHasFocus, coveredByOtherScreen);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		super.draw(gameTime);

		if (blackJackGame != null)
		{
			blackJackGame.draw(gameTime);
		}

	}

	/**
	 * Initializes the game component.
	 */
	private void initializeGame()
	{
		blackJackGame.initialize();
		// Add human player
		blackJackGame.addPlayer(new BlackjackPlayer("Abe", blackJackGame));

		// Add AI players
		BlackjackAIPlayer player = new BlackjackAIPlayer("Benny", blackJackGame);
		blackJackGame.addPlayer(player);
		player.hit.add(this::player_Hit);
		player.stand.add(this::player_Stand);

		player = new BlackjackAIPlayer("Chuck", blackJackGame);
		blackJackGame.addPlayer(player);
		player.hit.add(this::player_Hit);
		player.stand.add(this::player_Stand);

		// Load UI assets
		String[] assets = { "blackjack", "bust", "lose", "push", "win", "pass", "shuffle_" + theme };

		for (int chipIndex = 0; chipIndex < assets.length; ++chipIndex)
		{
			blackJackGame.loadUITexture("UI", assets[chipIndex]);
		}

		blackJackGame.startRound();
	}

	/**
	 * Gets the player hand positions according to the player index.
	 * 
	 * @param player
	 *        The player's index.
	 * @return The position for the player's hand on the game table.
	 */
	private Vector2 getPlayerCardPosition(int player)
	{
		switch (player)
		{
			case 0:
			case 1:
			case 2:
				return new Vector2(safeArea.x,
						(int) (safeArea.y + 200 * (BlackjackGame.HEIGHT_SCALE - 1)))
						.add(playerCardOffset[player]);
			default:
				throw new IndexOutOfBoundsException("Player index should be between 0 and 2");
		}
	}

	/**
	 * Pause the game.
	 */
	private void pauseCurrentGame()
	{
		// Move to the pause screen
		screenManager.addScreen(new BackgroundScreen());
		screenManager.addScreen(new PauseScreen());

		// Hide and disable all components which are related to the gameplay screen
		pauseEnabledComponents.clear();
		pauseVisibleComponents.clear();
		for (IGameComponent component : screenManager.getGame().getComponents())
		{
			if (component instanceof BetGameComponent ||
				component instanceof AnimatedGameComponent ||
				component instanceof GameTable ||
				component instanceof InputHelper)
			{
				DrawableGameComponent pauseComponent = (DrawableGameComponent) component;
				if (pauseComponent.isEnabled())
				{
					pauseEnabledComponents.add(pauseComponent);
					pauseComponent.setEnabled(false);
				}
				if (pauseComponent.isVisible())
				{
					pauseVisibleComponents.add(pauseComponent);
					pauseComponent.setVisible(false);
				}
			}
		}
	}

	/**
	 * Returns from pause.
	 */
	public void returnFromPause()
	{
		// Reveal and enable all previously hidden components
		for (DrawableGameComponent component : pauseEnabledComponents)
		{
			component.setEnabled(true);
		}
		for (DrawableGameComponent component : pauseVisibleComponents)
		{
			component.setVisible(true);
		}
	}

	/**
	 * Responds to the event sent when AI player's choose to "Stand".
	 * 
	 * @param sender
	 *        The source of the event.
	 * @param e
	 *        The {@link EventArgs} instance containing the event data.
	 */
	void player_Stand(Object sender, EventArgs e)
	{
		blackJackGame.stand();
	}

	/**
	 * Responds to the event sent when AI player's choose to "Split".
	 * 
	 * @param senderThe
	 *        source of the event.
	 * @param e
	 *        The {@link EventArgs} instance containing the event data.
	 */
	void player_Split(Object sender, EventArgs e)
	{
		blackJackGame.split();
	}

	/**
	 * Responds to the event sent when AI player's choose to "Hit".
	 * 
	 * @param sender
	 *        The source of the event.
	 * @param e
	 *        The {@link EventArgs} instance containing the event data.
	 */
	void player_Hit(Object sender, EventArgs e)
	{
		blackJackGame.hit();
	}

	/**
	 * Responds to the event sent when AI player's choose to "Double".
	 * 
	 * @param sender
	 *        The source of the event.
	 * @param e
	 *        The {@link EventArgs} instance containing the event data.
	 */
	void player_Double(Object sender, EventArgs e)
	{
		blackJackGame.Double();
	}
}
