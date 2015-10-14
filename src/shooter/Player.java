package shooter;

import gameCore.graphics.SpriteBatch;
import gameCore.math.Vector2;
import gameCore.time.GameTime;

public class Player
{
    // Animation representing the player
    public Animation playerAnimation;

    // Position of the Player relative to the upper left side of the screen
    public Vector2 position = new Vector2();

    // State of the Player
    public boolean isActive;

    // Amount of hit points (HP) that the Player has
    public int health;

    // Get the width of the Player ship
    public int getWidth() { return playerAnimation.frameWidth; }

    // Get height of the Player ship
    public int getHeight() { return playerAnimation.frameHeight; }

    public void initialize(Animation animation, Vector2 position)
    {
        playerAnimation = animation;

        // Set the starting position of the Player around the middle of the screen and to the back
        this.position.set(position);

        // Set the Player to be Active
        this.isActive = true;

        // Set the Player Health
        this.health = 100;

    }

    // Update the player animation
    public void update(GameTime gameTime)
    {
        playerAnimation.position.set(position);
        playerAnimation.update(gameTime);
    }

    // Draw the Player
    public void draw(SpriteBatch spriteBatch)
    {
        playerAnimation.draw(spriteBatch);
    }
}
