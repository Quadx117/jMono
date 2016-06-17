package shooter;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.math.Vector2;
import jMono_Framework.time.GameTime;

public class Animation
{
	// The image representing the collection of images used for animation
	Texture2D spriteStrip;

	// The scale used to display the sprite strip
	float scale;

	// The time since we last updated the frame
	int elapsedTime;

	// The time we display a frame until the next one
	int frameTime;

	// The number of frames that the animation contains
	int frameCount;

	// The index of the current frame we are displaying
	int currentFrame;

	// The color of the frame we will be displaying
	Color color = new Color();

	// The area of the image strip we want to display
	Rectangle sourceRect = new Rectangle();

	// The area where we want to display the image from the strip in the game
	Rectangle destinationRect = new Rectangle();

	// Width of a given frame
	public int frameWidth;

	// Height of a given frame
	public int frameHeight;

	// The state of the animation
	public boolean isActive;

	// Determines if the animation will keep playing or deactivate after one run
	public boolean isLooping;

	// Position of a given frame
	public Vector2 position = new Vector2();

	public void initialize(Texture2D texture, Vector2 position, int frameWidth, int frameHeight, int frameCount,
			int frametime, Color color, float scale, boolean looping)
	{
		// Keep a local copy of the values passed in
		this.color.setPackedValue(color.getPackedValue());
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameCount = frameCount;
		this.frameTime = frametime;
		this.scale = scale;

		this.isLooping = looping;
		this.position.set(position);
		this.spriteStrip = texture;

		// Set the time to zero
		this.elapsedTime = 0;
		this.currentFrame = 0;

		// Set the animation to active by default
		this.isActive = true;

	}

	public void update(GameTime gameTime)
	{
		// Do not update the game if we are not active
		if (isActive == false)
			return;

		// Update the elapsed time
		elapsedTime += (int) gameTime.getElapsedGameTime().getTotalMilliseconds();

		// If the elapsed time is larger than the frame time we need to switch frames
		if (elapsedTime > frameTime)
		{
			// Move to the next frame
			++currentFrame;

			// If the currentFrame is equal to frameCount reset currentFrame to zero
			if (currentFrame == frameCount)
			{
				currentFrame = 0;
				// If we are not looping deactivate the animation
				if (isLooping == false)
					isActive = false;
			}

			// Reset the elapsed time to 0
			elapsedTime = 0;
		}

		// Grab the correct frame in the image strip by multiplying the currentFrame index by the
		// FrameWidth
		sourceRect = new Rectangle(currentFrame * frameWidth, 0, frameWidth, frameHeight);

		// Where we want to draw our image from the strip on the screen. Because all the images are
		// not the same size we will put the center of the image on the top left corner of the
		// rectangle. That way if we put an explosion in place of a destroyed ship it will appear at
		// the same place even if the two images are not the same size.
		destinationRect = new Rectangle((int) position.x - (int) (frameWidth * scale) / 2,
				(int) position.y - (int) (frameHeight * scale) / 2, (int) (frameWidth * scale),
				(int) (frameHeight * scale));
	}

	// Draw the animation
	public void draw(SpriteBatch spriteBatch)
	{
		// Only draw the animation when we are active
		if (isActive)
		{
			spriteBatch.draw(spriteStrip, destinationRect, sourceRect, color);
		}
	}
}
