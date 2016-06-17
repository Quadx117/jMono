package blackjack.UI;

import jMono_Framework.Color;
import jMono_Framework.Game;
import jMono_Framework.Rectangle;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;

import java.util.function.Function;

import blackjack.cardsFramework.UI.GameTable;

public class BlackJackTable extends GameTable
{
	private Texture2D ringTexture;
	private Vector2 ringOffset;

	public BlackJackTable(Vector2 ringOffset, Rectangle tableBounds, Vector2 dealerPosition, int places,
			Function<Integer, Vector2> placeOrder, String theme, Game game)
	{
		super(tableBounds, dealerPosition, places, placeOrder, theme, game);
		this.ringOffset = ringOffset;
	}

	/**
	 * Load the component assets.
	 */
	@Override
	protected void loadContent()
	{
		ringTexture = getGame().getContent().load("images/UI/ring", Texture2D.class);

		super.loadContent();
	}

	/**
	 * Draw the rings of the chip on the table
	 * 
	 * @param gameTime
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		super.draw(gameTime);

		spriteBatch.begin();

		for (int placeIndex = 0; placeIndex < getPlaces(); ++placeIndex)
		{
			spriteBatch.draw(ringTexture, getPlaceOrder().apply(placeIndex).add(ringOffset), Color.White);
		}

		spriteBatch.end();
	}

	// ++++++++++ GETTERS ++++++++++ //

	public Texture2D getRingTexture()
	{
		return ringTexture;
	}

	public Vector2 getRingOffset()
	{
		return ringOffset;
	}

}
