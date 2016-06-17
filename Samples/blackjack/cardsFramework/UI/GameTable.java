package blackjack.cardsFramework.UI;

import jMono_Framework.Color;
import jMono_Framework.Game;
import jMono_Framework.Rectangle;
import jMono_Framework.components.DrawableGameComponent;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;

import java.util.function.Function;

/**
 * The UI representation of the table where the game is played.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class GameTable extends DrawableGameComponent
{
	private String theme;
	public String getTheme() { return theme; }

	private Texture2D tableTexture;
	public Texture2D getTableTexture() { return tableTexture; }

	private Vector2 dealerPosition;
	public Vector2 getDealerPosition() { return dealerPosition; }

	protected SpriteBatch spriteBatch;
	public SpriteBatch getSpriteBatch() { return spriteBatch; }

	private Function<Integer, Vector2> placeOrder;
	public Function<Integer, Vector2> getPlaceOrder() { return placeOrder; }

	private Rectangle tableBounds;
	public Rectangle getTableBounds() { return tableBounds; }

	private int places;
	public int getPlaces() { return places; }

	/**
	 * Returns the player position on the table according to the player index.
	 * The location's are relative to the entire game area, even if the table
	 * only occupies part of it.
	 * 
	 * @param index
	 *        Player's index.
	 * @return The position of the player corresponding to the supplied index.
	 */
	public Vector2 getPlayerPosition(int index)
	{
		Vector2 temp = placeOrder.apply(index);
		return new Vector2(tableBounds.x + temp.x, tableBounds.y + temp.y);
	}
	
	/**
	 * Initializes a new instance of the class.
	 * 
	 * @param tableBounds
	 *        The table bounds.
	 * @param dealerPosition
	 *        The dealer's position.
	 * @param places
	 *        Amount of places on the table
	 * @param placeOrder
	 *        A method to convert player indices to their respective
	 *        location on the table.
	 * @param theme
	 *        The theme used to display UI elements.
	 * @param game
	 *        The associated game object.
	 */
	public GameTable(Rectangle tableBounds, Vector2 dealerPosition, int places,
			Function<Integer, Vector2> placeOrder, String theme, Game game)
	{
		super(game);
		this.tableBounds = new Rectangle(tableBounds);
		this.dealerPosition = Vector2.add(dealerPosition, new Vector2(tableBounds.x, tableBounds.y));
		this.places = places;
		this.placeOrder = placeOrder;
		this.theme = theme;
		spriteBatch = new SpriteBatch(getGame().getGraphicsDevice());
	}

	/**
	 * Load the table texture.
	 */
	@Override
	protected void loadContent()
	{
		// String assetName = String.format(@"Images\UI\table");
        tableTexture = getGame().getContent().load("/images/UI/table", Texture2D.class);
        
		super.loadContent();
	}

	/**
	 * Render the table.
	 * 
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		spriteBatch.begin();
		
		// Draw the table texture
		spriteBatch.draw(tableTexture, tableBounds, Color.White);
		
		spriteBatch.end();

		super.draw(gameTime);
	}
}
