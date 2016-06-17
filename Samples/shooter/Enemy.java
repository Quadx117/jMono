package shooter;

import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;

public class Enemy
{
	// Animation representing the enemy
	public Animation enemyAnimation;

	// The position of the enemy ship relative to the Top Left corner fo the screen
	public Vector2 position;

	// The state of the enemy ship
	public boolean isActive;

	// The hit points of the enemy, if it goes to zero the enemy dies
	public int health;

	// The amount of damage the enemy inflicts on the player ship
	public int damage;

	// The amount of score the enemy will give to the player
	public int value;

	// Get the Width of the enemy ship
	public int getWidth()
	{
		return enemyAnimation.frameWidth;
	}

	// Get the Height of the enemy ship
	public int getHeight()
	{
		return enemyAnimation.frameHeight;
	}

	// The speed at which the enemy moves
	float enemyMoveSpeed;

	public void initialize(Animation animation, Vector2 position)
	{
		// Load the enemy ship texture
		enemyAnimation = animation;

		// Set the position of the enemy
		this.position = new Vector2(position);

		// We initialize the enemy to be active so it will update in the game
		isActive = true;

		// Set the health of the enemy
		health = 10;

		// Set the amount of damage the enemy does
		damage = 10;

		// Set how fast the enemy moves
		enemyMoveSpeed = 6f;

		// Set the score value of the enemy
		value = 100;

	}

	public void update(GameTime gameTime)
	{
		// The enemy always moves to the left, so decrement it's X position
		position.x -= enemyMoveSpeed;

		// Update the position of the Animation
		enemyAnimation.position.set(position);

		// Update Animation
		enemyAnimation.update(gameTime);

		// If enemy is past the screen or its health reaches 0 then deactivate it
		if (position.x < -getWidth() || health <= 0)
		{
			// By setting the Active flag to false, the game will remove this object from the active
			// game list
			isActive = false;
		}
	}

	public void draw(SpriteBatch spriteBatch)
	{
		// Draw the animation
		enemyAnimation.draw(spriteBatch);
	}
}
