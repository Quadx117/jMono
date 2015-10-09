package shooter;

import gameCore.graphics.SpriteBatch;
import gameCore.math.Vector2;
import gameCore.time.GameTime;

public class Player
{
	// Image representing the player
    // public Texture2D PlayerTexture;

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
    // get { return PlayerTexture.Width; }  Ancien code pour une seule image

    // Get height of the Player ship
    public int getHeight() { return playerAnimation.frameHeight; }
    // get { return PlayerTexture.Height; } Ancien code pour une seule image

    // Ancien code pour une seule image
    // public void Initialize(Texture2D texture, Vector2 position)
    // {
    //     PlayerTexture = texture;

           // Set the starting position of the Player around the middle of the screen and to the back
    //     Position = position;

           // Set the Player to be Active
    //     Active = true;

           // Set the Player Health
    //     Health = 100;
    // }

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
    	// Ancien code pour une seul image
        // spriteBatch.Draw(PlayerTexture, Position, null, Color.White, 0f, Vector2.Zero, 1f, SpriteEffects.None, 0f);
        playerAnimation.draw(spriteBatch);
    }
}
