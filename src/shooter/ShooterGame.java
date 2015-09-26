package shooter;

import gameCore.Color;
import gameCore.Game;
import gameCore.GraphicsDeviceManager;
import gameCore.graphics.SpriteBatch;
import gameCore.graphics.SpriteSortMode;
import gameCore.graphics.SurfaceFormat;
import gameCore.graphics.Texture2D;
import gameCore.graphics.states.BlendState;
import gameCore.time.GameTime;

import java.awt.Rectangle;

public class ShooterGame extends Game {

	/** Default serial version UID */
	private static final long serialVersionUID = 1L;

	GraphicsDeviceManager graphics;
	SpriteBatch spriteBatch;

	// Bounding Rectangles
    Texture2D pixel;
    Rectangle boundingRectPlayer = new Rectangle(20, 20, 50, 50);
	
	// Image used to display the static background
    Texture2D mainBackground;
    
	public ShooterGame() {
		
		graphics = new GraphicsDeviceManager(this);
		// Content.RootDirectory = "Content";
	}

	protected void initialize() {
		// TODO: Add your initialization logic here

		super.initialize();
	}

	protected void loadContent() {
		// Create a new SpriteBatch, which can be used to draw textures.
		spriteBatch = new SpriteBatch(getGraphicsDevice());

		// TODO: use this.Content to load your game content here
		
		// Used to draw the Bounding Rectangles.  Can be used to draw any primitives
        pixel = new Texture2D(getGraphicsDevice(), 1, 1, false, SurfaceFormat.Color);
        pixel.setData(new Color[] { Color.White }); // So we can draw whatever color we want
		
		// Load the Background images
        // mainBackground = Content.Load<Texture2D>("mainbackground");
	}

	protected void unloadContent() {
		// TODO: Unload any non ContentManager content here
	}

	protected void update(GameTime gameTime) {
		// Allows the game to exit
		// if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
		// this.stop();

		// TODO: Add your update logic here

		super.update(gameTime);
	}

	protected void draw(GameTime gameTime) {
		getGraphicsDevice().clear(Color.CornflowerBlue);

		// TODO: Add your drawing code here

		// Start drawing
        spriteBatch.begin(SpriteSortMode.Deferred, BlendState.AlphaBlend);
        
		// Draw Bounding Rectangles
        drawBorder(boundingRectPlayer, 2, Color.BlueViolet);
		
        // Stop drawing
        spriteBatch.end();
        
		super.draw(gameTime);
	}

	@Override
	protected void drawText(GameTime gameTime) {
		// TODO Auto-generated method stub

	}

	// Used to draw the Bounding Rectangles
    private void drawBorder(Rectangle rectangleToDraw, int borderThickness, Color borderColor)
    {
        // Draw Top line
        spriteBatch.draw(pixel, new Rectangle(rectangleToDraw.x, rectangleToDraw.y,
                        rectangleToDraw.width, borderThickness), borderColor);

        // Draw Left line
        spriteBatch.draw(pixel, new Rectangle(rectangleToDraw.x, rectangleToDraw.y,
                        borderThickness, rectangleToDraw.height), borderColor);

        // Draw Right line
        spriteBatch.draw(pixel, new Rectangle(rectangleToDraw.x + rectangleToDraw.width - borderThickness,
                        rectangleToDraw.y, borderThickness, rectangleToDraw.height), borderColor);

        // Draw Bottom line
        spriteBatch.draw(pixel, new Rectangle(rectangleToDraw.x, rectangleToDraw.y + rectangleToDraw.height - borderThickness,
                        rectangleToDraw.width, borderThickness), borderColor);
    }
	
}
