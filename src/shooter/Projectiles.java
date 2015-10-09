package shooter;

import gameCore.Color;
import gameCore.graphics.SpriteBatch;
import gameCore.graphics.SpriteEffects;
import gameCore.graphics.Texture2D;
import gameCore.graphics.Viewport;
import gameCore.math.Vector2;

public class Projectiles
{
	// Image representing the Projectiles
	public Texture2D texture;

	// Position of the Projectiles relative to the upper left side of the screen
	public Vector2 position;

	// State of the Projectiles
	public boolean isActive;

	// The amount of Damage the projectiles infilcts to an enemy
	public int damage;

	// Represents the viewable boundary of the game
	Viewport viewport;

	// Get the width of the Projectiles
	public int getWidth()
	{
		return texture.getWidth();
	}

	// Get the height of the Projectiles
	public int getHeight()
	{
		return texture.getHeight();
	}

	// Determine how fast the projectiles moves
	float projectileMoveSpeed;

	public void initialize(Viewport viewport, Texture2D texture, Vector2 position)
	{
		this.texture = texture;
		this.position.set(position);
		this.viewport = new Viewport(viewport);

		isActive = true;

		damage = 2;

		projectileMoveSpeed = 20f;
	}

	public void update()
	{
		// Projectiles always move to the right
		position.x += projectileMoveSpeed;

		// Deactivate the bullet if it goes out of screen
		if (position.x + texture.getWidth() / 2 > viewport.getWidth())
			isActive = false;
	}

	public void draw(SpriteBatch spriteBatch)
	{
		spriteBatch.draw(texture, position, null, Color.White, 0f, new Vector2(getWidth() / 2, getHeight() / 2), 1f,
				SpriteEffects.None, 0f);
	}
}
