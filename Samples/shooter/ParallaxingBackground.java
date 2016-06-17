package shooter;

import jMono_Framework.Color;
import jMono_Framework.content.ContentManager;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.math.Vector2;

public class ParallaxingBackground
{
	// The image representing the parallaxing background
    Texture2D texture;


    // An array of positions of the parallaxing background
    Vector2[] positions;

    // The speed which the background is moving
    int speed;

    public void initialize(ContentManager content, String texturePath, int screenWidth, int speed)
    {
        // Load the background texture we will be using
        texture = content.load(texturePath, Texture2D.class);

        // Set the speed of the background
        this.speed = speed;

        // If we divide the screen with the texture width then we can determine the number of tiles need.
        // We add 1 to it so that we won't have a gap in the tiling
        positions = new Vector2[screenWidth / texture.getWidth() + 1];

        // Set the initial positions of the parallaxing background
        for (int i = 0; i < positions.length; ++i)
        {
            // We need the tiles to be side by side to create a tiling effect
            positions[i] = new Vector2(i * texture.getWidth(), 0);
        }
    }

    public void update()
    {
        // Update the positions of the background
        for (int i = 0; i < positions.length; ++i)
        {
            // Update the position of the screen by adding the speed
            positions[i].x += speed;
            // If the speed has the background moving to the left
            if (speed <= 0)
            {
                // Check the texture is out of view then put that texture at the end of the screen
                if (positions[i].x <= -texture.getWidth())
                {
                    positions[i].x = texture.getWidth() * (positions.length - 1);
                }
            }


            // If the speed has the background moving to the right
            else
            {
                // Check if the texture is out of view then position it to the start of the screen
                if (positions[i].x >= texture.getWidth() * (positions.length - 1))
                {
                    positions[i].x = -texture.getWidth();
                }
            }
        }
    }

    public void draw(SpriteBatch spriteBatch)
    {
        for (int i = 0; i < positions.length; ++i)
        {
            spriteBatch.draw(texture, positions[i], Color.White);
        }
    }
}
