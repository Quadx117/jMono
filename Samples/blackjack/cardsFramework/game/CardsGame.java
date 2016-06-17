package blackjack.cardsFramework.game;

import jMono_Framework.Game;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.SpriteFont;
import jMono_Framework.graphics.Texture2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import blackjack.cardsFramework.UI.GameTable;
import blackjack.cardsFramework.cards.CardPacket;
import blackjack.cardsFramework.cards.TraditionalCard;
import blackjack.cardsFramework.cards.TraditionalCard.CardSuit;
import blackjack.cardsFramework.cards.TraditionalCard.CardValue;
import blackjack.cardsFramework.players.Player;
import blackjack.cardsFramework.rules.GameRule;
import blackjack.cardsFramework.util.UIUtility;

/**
 * A cards-game handler. Use a singleton of a class that derives from class to
 * empower a cards-game, while making sure to call the various methods in order
 * to allow the implementing instance to run the game.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public abstract class CardsGame
{
	protected List<GameRule> rules;
	protected List<Player> players;
	protected CardPacket dealer;

	protected int minimumPlayers;
	public int getMinimumPlayers() { return minimumPlayers; }
	
	protected int maximumPlayers;
	public int getMaximumPlayers() { return maximumPlayers; }

	protected String theme;
	public String getTheme() { return theme; }
	
	protected HashMap<String, Texture2D> cardsAssets;
	
	protected GameTable gameTable;
	public GameTable getGameTable() { return gameTable; }

	public SpriteFont font;
	public SpriteBatch spriteBatch;
	public Game game;

	/**
	 * Initializes a new instance of the {@link CardsGame} class.
	 * 
	 * @param decks
	 *        The amount of decks in the game.
	 * @param jokersInDeck
	 *        The amount of jokers in each deck.
	 * @param suits
	 *        The suits which will appear in each deck. Multiple values can
	 *        be supplied using flags.
	 * @param cardValues
	 *        The card values which will appear in each deck. Multiple
	 *        values can be supplied using flags.
	 * @param minimumPlayers
	 *        The minimal amount of players for the game.
	 * @param maximumPlayers
	 *        The maximal amount of players for the game.
	 * @param gameTable
	 *        The UI representation of the table where the game is played.
	 * @param theme
	 *        The name of the theme to use for the game's assets.
	 * @param game
	 *        The associated game object.
	 */
	public CardsGame(int decks, int jokersInDeck, CardSuit suits, CardValue cardValues, int minimumPlayers,
			int maximumPlayers, GameTable gameTable, String theme, Game game)
	{
		rules = new ArrayList<GameRule>();
		players = new ArrayList<Player>();
		dealer = new CardPacket(decks, jokersInDeck, suits.getValue(), cardValues.getValue());

		this.game = game;
		this.minimumPlayers = minimumPlayers;
		this.maximumPlayers = maximumPlayers;

		this.theme = theme;
		cardsAssets = new HashMap<String, Texture2D>();
		this.gameTable = gameTable;
		gameTable.setDrawOrder(-10000);
		game.getComponents().add(gameTable);
	}

	/**
	 * Load the basic contents for a card game (the card assets)
	 */
	public void loadContent()
	{
		spriteBatch = new SpriteBatch(this.game.getGraphicsDevice());

		// Initialize a full deck
		CardPacket fullDeck = new CardPacket(1, 2, CardSuit.AllSuits.getValue(),
				CardValue.NonJokers.getValue() | CardValue.Jokers.getValue());
		String assetName;

		// Load all card assets
		for (int cardIndex = 0; cardIndex < 54; ++cardIndex)
		{
			assetName = UIUtility.getCardAssetName(fullDeck.getTraditionalCard(cardIndex));
			loadUITexture("cards", assetName);
		}
		// Load card back picture
		loadUITexture("Cards", "CardBack_" + theme);

		// Load the game's font
		// font = game.getContent().load(String.Format(@"Fonts\Regular"), SpriteFont.class);
		font = game.getContent().load("fonts/Regular", SpriteFont.class);

		gameTable.initialize();
	}

	/**
	 * Loads the UI textures for the game, taking the theme into account.
	 * 
	 * @param folder
	 *        The asset's folder under the theme folder. For example, for an
	 *        asset belonging to the "Fish" theme and which sits in
	 *        "Images\Fish\Beverages\Soda" folder under the content project,
	 *        use "Fish\Beverages\Soda" as this argument's value.
	 * @param assetName
	 *        The name of the asset.
	 */
	public void loadUITexture(String folder, String assetName)
	{
		cardsAssets.put(assetName, game.getContent().load(String.format("Images\\%s\\%s", folder, assetName), Texture2D.class));
	}

	/**
	 * Checks which of the game's rules need to be fired.
	 */
	public void checkRules()
	{
		for (int ruleIndex = 0; ruleIndex < rules.size(); ++ruleIndex)
		{
			rules.get(ruleIndex).check();
		}
	}

	/**
	 * Returns a card's value in the scope of the game.
	 * 
	 * @param card
	 *        The card to calculate the value for.
	 * @return The card's value.
	 */
	public int cardValue(TraditionalCard card) throws IllegalArgumentException
	{
		switch (card.getValue())
		{
			case Ace:
				return 1;
			case Two:
				return 2;
			case Three:
				return 3;
			case Four:
				return 4;
			case Five:
				return 5;
			case Six:
				return 6;
			case Seven:
				return 7;
			case Eight:
				return 8;
			case Nine:
				return 9;
			case Ten:
				return 10;
			case Jack:
				return 11;
			case Queen:
				return 12;
			case King:
				return 13;
			default:
				throw new IllegalArgumentException("Ambigous card value");
		}
	}

	/**
	 * Adds a player to the game.
	 * 
	 * @param player
	 *        The player to add to the game.
	 */
	public abstract void addPlayer(Player player);

	/**
	 * Gets the player who is currently taking his turn.
	 * 
	 * @return The currently active player.
	 */
	public abstract Player getCurrentPlayer();

	/**
	 * Deals cards to the participating players.
	 */
	public abstract void deal();

	/**
	 * Initializes the game lets the players start playing.
	 */
	public abstract void startPlaying();

	// ++++++++++ GETTERS ++++++++++ //

	public HashMap<String, Texture2D> getCardsAssets()
	{
		return cardsAssets;
	}

	// ++++++++++ SETTERS ++++++++++ //

	protected void setMinimumPlayers(int value)
	{
		minimumPlayers = value;
	}

	protected void setMaximumPlayers(int value)
	{
		maximumPlayers = value;
	}

	protected void setTheme(String theme)
	{
		this.theme = theme;
	}

	protected void setGameTable(GameTable gameTable)
	{
		this.gameTable = gameTable;
	}

}
