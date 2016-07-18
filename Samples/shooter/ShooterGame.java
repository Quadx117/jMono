package shooter;

import jMono_Framework.Color;
import jMono_Framework.Game;
import jMono_Framework.GraphicsDeviceManager;
import jMono_Framework.Rectangle;
import jMono_Framework.audio.SoundEffect;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.SpriteEffects;
import jMono_Framework.graphics.SpriteFont;
import jMono_Framework.graphics.SpriteSortMode;
import jMono_Framework.graphics.SurfaceFormat;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.graphics.states.BlendState;
import jMono_Framework.input.Keyboard;
import jMono_Framework.input.KeyboardState;
import jMono_Framework.input.Keys;
import jMono_Framework.math.MathHelper;
import jMono_Framework.math.Vector2;
import jMono_Framework.media.MediaPlayer;
import jMono_Framework.media.Song;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShooterGame extends Game
{
	/** Default serial version UID */
	private static final long serialVersionUID = 1L;

	GraphicsDeviceManager graphics;
	SpriteBatch spriteBatch;

	// Bounding Rectangles
	final boolean DEBUG_DRAW_BOUNDING_RECTANGLE = false;
	Texture2D pixel;
	Rectangle boundingRectPlayer = new Rectangle();
	List<Rectangle> boundingRectEnemies;
	List<Rectangle> boundingRectLaser;

	private FPS_Counter fpsCounter;

	// Represents the Player
	Player player;

	// Keyboard states used to determine key presses
	KeyboardState currentKeyboardState = new KeyboardState();
	KeyboardState previousKeyboardState = new KeyboardState();

	// Gamepad states used to determine button presses
	// GamePadState currentGamePadState;
	// GamePadState previousGamePadState;

	// A movement speed for the Player
	float playerMoveSpeed;

	// Image used to display the static background
	Texture2D mainBackground;

	// Parallaxing Layers
	ParallaxingBackground bgLayer1;
	ParallaxingBackground bgLayer2;

	// Enemies
	Texture2D enemyTexture;
	List<Enemy> enemies;

	// The rate at which the enemies appear
	TimeSpan enemySpawnTime;
	TimeSpan previousSpawnTime;

	// A random number generator
	Random random;

	// Projectiles
	Texture2D projectileTexture;
	List<Projectiles> projectiles;

	// The rate of fire of the Player's Laser
	TimeSpan fireTime;
	TimeSpan previousFireTime = new TimeSpan();

	// Explosion
	Texture2D explosionTexture;
	List<Animation> explosions;

	// The sound played when a laser is fired
	SoundEffect laserSound;

	// The sound used for explosions
	SoundEffect explosionSound;

	// Low Beep (Used in menu)
	SoundEffect lowBeep;

	// The music played during Gameplay and Menus
	Song gameplayMusic;
	Song menuMusic;

	// Variables to check which song is playing
	boolean isPlayingMenuMusic;
	boolean isPlayingGameMusic;

	// Number that holds the player score
	int score;

	// Background textures for the various screens in the game
	Texture2D mainMenuScreenBackground;
	Texture2D endMenuScreenBackground;

	// The enumeration of the various screen states available in the game
	enum ScreenState
	{
		MainGame,	//
		MainMenu,	//
		EndMenu
	}

	// The current screen state
	ScreenState currentScreen;

	// Font resources
	Vector2 fontPos;
	Vector2 fontOrigin;
	SpriteFont gameFont;	// The font used to display UI elements
	SpriteFont menuFont;	// The font used in menus
	Color playColor;
	Color quitColor;
	int elapsedTimeColor;	// The time elapsed since the last fontColor change
	int menuIndex;			// if 0 means we are on Play; if 1 means we are on Quit
	float fontAlphaBlend;	// Value for transparency. 0 = transparent ; 0.5 = 50% ; 1 = opaque;
							// BlendState.AlphaBlend must be set in spritebatch.begin

	// The elapsed time since the player died
	int elapsedTimeDead;

	public ShooterGame()
	{
		graphics = new GraphicsDeviceManager(this);
		getContent().setRootDirectory("Content");

		fpsCounter = new FPS_Counter(this);
	}

	protected void initialize()
	{
		// Initialize the Player class
		player = new Player();

		// Set a constant Player move speed
		playerMoveSpeed = 8.0f;

		// Initialize the enemies list
		enemies = new ArrayList<Enemy>();

		// Set the time keepers to 0
		previousSpawnTime = new TimeSpan(TimeSpan.ZERO);

		// Used to determine how fast enemy re-spawns
		enemySpawnTime = TimeSpan.fromSeconds(1.0f);

		// Initialize our random number generator
		random = new Random();

		// Initalize the ParallaxingBackgrounds
		bgLayer1 = new ParallaxingBackground();
		bgLayer2 = new ParallaxingBackground();

		// Initialize the Projectiles list
		projectiles = new ArrayList<Projectiles>();

		// Set the Laser to fire every quarter second
		fireTime = TimeSpan.fromSeconds(.15f);

		// Initialze the explosions list
		explosions = new ArrayList<Animation>();

		// Set player's score to 0
		score = 0;

		// Initalize the boundingRectangles list
		if (DEBUG_DRAW_BOUNDING_RECTANGLE)
		{
			boundingRectEnemies = new ArrayList<Rectangle>();
			boundingRectLaser = new ArrayList<Rectangle>();
		}
		
		// Initialize the current screen state to the screen we want to display first
		currentScreen = ScreenState.MainMenu;

		// Initialize the Font position to be in the center of the screen
		fontPos = new Vector2(getGraphicsDevice().getViewport().getWidth() / 2,
							  getGraphicsDevice().getViewport().getHeight() / 2);

		// Initialize ou bool so that MenuMusic will play first
		isPlayingGameMusic = false;
		isPlayingMenuMusic = true;

		// Set the elapsedTimeColor and elapsedTimeDead to 0
		elapsedTimeColor = 0;
		elapsedTimeDead = 0;

		// Set the default color for the buttons in the main Menu
		playColor = Color.OrangeRed;
		quitColor = Color.White;
		menuIndex = 0;

		super.initialize();
	}

	protected void loadContent()
	{
		// Create a new SpriteBatch, which can be used to draw textures.
		spriteBatch = new SpriteBatch(getGraphicsDevice());

		// TODO: use this.getContent() to load your game content here

		// Used to draw the Bounding Rectangles. Can be used to draw any primitives
		if (DEBUG_DRAW_BOUNDING_RECTANGLE)
		{
			pixel = new Texture2D(getGraphicsDevice(), 1, 1, false, SurfaceFormat.Color);
			pixel.setData(new Color[] { Color.White }); // So we can draw whatever color we want
		}
		
		// Load the Player resources
		Animation playerAnimation = new Animation();
		Texture2D playerTexture = getContent().load("shipAnimation", Texture2D.class);
		playerAnimation.initialize(playerTexture, Vector2.ZERO, 115, 69, 8, 60, Color.White, 1.0f, true);

		Vector2 playerPosition = new Vector2(getGraphicsDevice().getViewport().getTitleSafeArea().x,
											 getGraphicsDevice().getViewport().getTitleSafeArea().y + getGraphicsDevice().getViewport().getTitleSafeArea().height / 2);
		player.initialize(playerAnimation, playerPosition);

		mainBackground = getContent().load("mainbackground", Texture2D.class);
		bgLayer1.initialize(getContent(), "bgLayer1", getGraphicsDevice().getViewport().getWidth(), -1);
		bgLayer2.initialize(getContent(), "bgLayer2", getGraphicsDevice().getViewport().getWidth(), -2);
		mainMenuScreenBackground = getContent().load("mainMenu", Texture2D.class);
		endMenuScreenBackground = getContent().load("endMenu", Texture2D.class);

		// Load the enemy resources
		enemyTexture = getContent().load("mineAnimation", Texture2D.class);

		// Load the projectiles resources
		projectileTexture = getContent().load("laser", Texture2D.class);

		// Load the explosions resources
		explosionTexture = getContent().load("explosion", Texture2D.class);

		// Load the Laser, Explosion and LowBeep sound effect
		laserSound = getContent().load("sounds/laserFire", SoundEffect.class);
		explosionSound = getContent().load("sounds/explosion", SoundEffect.class);
		lowBeep = getContent().load("sounds/LowBeep", SoundEffect.class);

		// Load the music
		gameplayMusic = getContent().load("sounds/gameMusic", Song.class);
		menuMusic = getContent().load("sounds/menuMusic", Song.class);

		// Start the music right away
		playMusic(menuMusic);

		// Load the UI font
		gameFont = getContent().load("fonts/gameFont", SpriteFont.class);
		menuFont = getContent().load("fonts/menuFont", SpriteFont.class);
	}

	protected void unloadContent()
	{
		// TODO: Unload any non ContentManager content here
	}

	private void startNewGame()
	{
		// Reset everything we need to start a new game
		player = new Player();
		enemies = new ArrayList<Enemy>();
		projectiles = new ArrayList<Projectiles>();
		Animation playerAnimation = new Animation();

		Texture2D playerTexture = getContent().load("shipAnimation", Texture2D.class);

		playerAnimation.initialize(playerTexture, Vector2.ZERO, 115, 69, 8, 60, Color.White, 1f, true);

		Vector2 playerPosition = new Vector2(getGraphicsDevice().getViewport().getTitleSafeArea().x,
											 getGraphicsDevice().getViewport().getTitleSafeArea().y + getGraphicsDevice().getViewport().getTitleSafeArea().height / 2);
		player.initialize(playerAnimation, playerPosition);

		score = 0;

		// Set the time keepers to 0
		previousSpawnTime.setTimeSpan(TimeSpan.ZERO);

		// Set the elapsedTimeColor and elapsedTimeDead to 0
		elapsedTimeColor = 0;
		elapsedTimeDead = 0;
	}

	protected void update(GameTime gameTime)
	{
		fpsCounter.update(gameTime);

		// Save the previous state of the Keyboard and GamePad so we can determine single key/button presses
		previousKeyboardState = currentKeyboardState;

		// Read the current state of the Keyboard and GamePad and store it
		// currentGamePadState = GamePad.GetState(PlayerIndex.One);
		currentKeyboardState = Keyboard.getState();

		// Update method associated with the current screen
		switch (currentScreen)
		{
			case MainMenu:
			{
				// Update the elapsedTimeColor
				elapsedTimeColor += (int) gameTime.getElapsedGameTime().getTotalMilliseconds();

				if (elapsedTimeColor >= 500f)
				{
					elapsedTimeColor = 0;

					if (menuIndex == 0)
					{
						if (playColor == Color.White)
						{
							playColor = Color.OrangeRed;
						}
						else
						{
							playColor = Color.White;
						}
					}
					else if (menuIndex == 1)
					{
						if (quitColor == Color.White)
						{
							quitColor = Color.OrangeRed;
						}
						else
						{
							quitColor = Color.White;
						}
					}
				}

				updateMainMenu();

				if (!isPlayingMenuMusic)
				{
					isPlayingMenuMusic = true;
					isPlayingGameMusic = false;
					playMusic(menuMusic);
				}
				break;
			}
			case EndMenu:
			{
				elapsedTimeColor = 0;
				updateEndMenu();
				if (!isPlayingMenuMusic)
				{
					isPlayingMenuMusic = true;
					isPlayingGameMusic = false;
					playMusic(menuMusic);
				}
				break;
			}
			case MainGame:
			{
				elapsedTimeColor = 0;
				updateMainGame(gameTime);
				if (!isPlayingGameMusic)
				{
					isPlayingMenuMusic = false;
					isPlayingGameMusic = true;
					playMusic(gameplayMusic);
				}
				break;
			}
		}

		super.update(gameTime);
	}

	private void updateMainMenu()
	{
		// Allows the game to exit
		// if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed ||
		if (previousKeyboardState.notEquals(currentKeyboardState) & currentKeyboardState.isKeyDown(Keys.Escape))
			this.exit();

		if (previousKeyboardState.notEquals(currentKeyboardState) & currentKeyboardState.isKeyDown(Keys.Down) & menuIndex != 1)
		{
			menuIndex = 1;
			elapsedTimeColor = 0;
			playColor = Color.White;
			quitColor = Color.OrangeRed;
		
			// Play the LowBeep sound effect
			lowBeep.play(0.9f, 0.0f, 0.0f);
		}

		if (previousKeyboardState.notEquals(currentKeyboardState) & currentKeyboardState.isKeyDown(Keys.Up) & menuIndex != 0)
		{
			menuIndex = 0;
			elapsedTimeColor = 0;
			playColor = Color.OrangeRed;
			quitColor = Color.White;
		
			// Play the LowBeep sound effect
			lowBeep.play(0.9f, 0.0f, 0.0f);
		}

		if (previousKeyboardState.notEquals(currentKeyboardState) & currentKeyboardState.isKeyDown(Keys.Enter) & menuIndex == 0)
		{
			currentScreen = ScreenState.MainGame;
		}
		else if (previousKeyboardState.notEquals(currentKeyboardState) & currentKeyboardState.isKeyDown(Keys.Enter) & menuIndex == 1)
		{
			this.exit();
		}
	}

	private void updateMainGame(GameTime gameTime)
	{
		// Allow the game to return to the main menu
		if (previousKeyboardState.notEquals(currentKeyboardState) & currentKeyboardState.isKeyDown(Keys.Escape))
		{
			currentScreen = ScreenState.MainMenu;
			return;
		}

		// Update the Player
		updatePlayer(gameTime);

		// Update the Enemies
		updateEnemies(gameTime);

		// Update the parallaxing background
		bgLayer1.update();
		bgLayer2.update();

		// Update Collision only if player is alive
		if (player.isActive)
			updateCollision();

		// Update Projectiles
		updateProjectiles();

		// Update Explosions
		updateExplosions(gameTime);

	}

	private void updateEndMenu()
	{
		// Test if any key is pressed. If a key is pressed .Length = 1,
		// if two keys are pressed simultaneously .Length = 2, etc.
		if (previousKeyboardState.notEquals(currentKeyboardState) & currentKeyboardState.getPressedKeys().length > 0)
		{
			startNewGame();
			currentScreen = ScreenState.MainMenu;
		}
	}

	private void updatePlayer(GameTime gameTime)
	{
		player.update(gameTime);

		// Get Thumbstick Controls
		// player.Position.X += currentGamePadState.ThumbSticks.Left.X * playerMoveSpeed;
		// player.Position.Y -= currentGamePadState.ThumbSticks.Left.Y * playerMoveSpeed;

		// Use the Keyboard / Dpad
		if (currentKeyboardState.isKeyDown(Keys.Left)) // || currentGamePadState.DPad.Left == ButtonState.Pressed)
		{
			player.position.x -= playerMoveSpeed;
		}
		if (currentKeyboardState.isKeyDown(Keys.Right)) // || currentGamePadState.DPad.Right == ButtonState.Pressed)
		{
			player.position.x += playerMoveSpeed;
		}
		if (currentKeyboardState.isKeyDown(Keys.Up)) // || currentGamePadState.DPad.Up == ButtonState.Pressed)
		{
			player.position.y -= playerMoveSpeed;
		}
		if (currentKeyboardState.isKeyDown(Keys.Down)) // || currentGamePadState.DPad.Down == ButtonState.Pressed)
		{
			player.position.y += playerMoveSpeed;
		}

		// Make sure the player does not go out of bounds. Because we've put the center of the image
		// of our ship over the top left corner (0, 0) of the destinationRect, we have to set our
		// clamping min. and max. value accordingly.
		player.position.x = MathHelper.clamp(player.position.x, (player.playerAnimation.frameWidth / 2),
											 getGraphicsDevice().getViewport().getWidth() - (player.playerAnimation.frameWidth / 2));
		player.position.y = MathHelper.clamp(player.position.y, (player.playerAnimation.frameHeight / 2),
											 getGraphicsDevice().getViewport().getHeight() - (player.playerAnimation.frameHeight / 2));

		// Fire if we press spacebar and only every interval we set as the fireTime
		if (currentKeyboardState.isKeyDown(Keys.Space))
		{
			if ((gameTime.getTotalGameTime().getTicks() - previousFireTime.getTicks() > fireTime.getTicks()) & (player.isActive))
			{
				// Reset our current time
				previousFireTime.setTimeSpan(gameTime.getTotalGameTime());

				// Add the projectile, but add it to the front and center of the player. Because
				// playerPosition is in the center of our ship, we have to set our projectile position accordingly
				addProjectile(Vector2.add(player.position, new Vector2(player.playerAnimation.frameWidth / 2, 0)));

				// Play the laser sound effect
				laserSound.play();
			}
		}

		// Display the Game Over screen after 3 seconds if the player is dead
		if ((!player.isActive) & (elapsedTimeDead == 0))
		{
			// Add the explosion
			addExplosion(player.position);

			// Play the explosion sound effect
			explosionSound.play(0.85f, 0.0f, 0.0f);

			elapsedTimeDead += (int) gameTime.getElapsedGameTime().getTotalMilliseconds();
		}
		else if ((!player.isActive) & (elapsedTimeDead <= 3000))
		{
			elapsedTimeDead += (int) gameTime.getElapsedGameTime().getTotalMilliseconds();
		}
		else if ((!player.isActive) & (elapsedTimeDead > 3000))
		{
			currentScreen = ScreenState.EndMenu;
		}
	}

	private void addEnemy()
	{
		// Create the animation object
		Animation enemyAnimation = new Animation();

		// Initialize the animation with the correct animation information
		enemyAnimation.initialize(enemyTexture, Vector2.ZERO, 47, 61, 8, 60, Color.White, 1f, true);

		// Randomly generate the position of the enemy
		Vector2 position = new Vector2(getGraphicsDevice().getViewport().getWidth() + enemyTexture.getWidth() / 2,
									   random.nextInt(getGraphicsDevice().getViewport().getHeight() - 200) + 100);

		// Create an enemy
		Enemy enemy = new Enemy();

		// Initialize the enemy
		enemy.initialize(enemyAnimation, position);

		// Add the enemy to the active enemies list
		enemies.add(enemy);
		
		if (DEBUG_DRAW_BOUNDING_RECTANGLE)
		{
			boundingRectEnemies.add(new Rectangle());
		}
	}

	private void updateEnemies(GameTime gameTime)
	{
		// Spawn a new enemy every 1.0 seconds
		if (gameTime.getTotalGameTime().getTicks() - previousSpawnTime.getTicks() > enemySpawnTime.getTicks())
		{
			previousSpawnTime.setTimeSpan(gameTime.getTotalGameTime());
			addEnemy();
		}

		// Update the enemies
		for (int i = enemies.size() - 1; i >= 0; --i)
		{
			enemies.get(i).update(gameTime);
			if (enemies.get(i).isActive == false)
			{
				// If not Active and Health <= 0 (so we don't get an explosion when the enemy goes out of the screen)
				if (enemies.get(i).health <= 0)
				{
					// Add the explosion
					addExplosion(enemies.get(i).position);

					// Play the explosion sound effect
					explosionSound.play(0.85f, 0.0f, 0.0f);

					// Add to the Player's score
					score += enemies.get(i).value;
				}
				enemies.remove(i);
				
				if (DEBUG_DRAW_BOUNDING_RECTANGLE)
				{
					boundingRectEnemies.remove(i);
				}
			}
		}
	}

	private void addProjectile(Vector2 position)
	{
		// Create the projectile
		Projectiles projectile = new Projectiles();

		// Initialize the projectile
		projectile.initialize(getGraphicsDevice().getViewport(), projectileTexture, position);

		// Add the projectile to the active enemies list
		projectiles.add(projectile);
		
		if (DEBUG_DRAW_BOUNDING_RECTANGLE)
		{
			boundingRectLaser.add(new Rectangle());
		}
	}

	private void updateProjectiles()
	{
		// Update the projectiles
		for (int i = projectiles.size() - 1; i >= 0; --i)
		{
			projectiles.get(i).update();

			if (projectiles.get(i).isActive == false)
			{
				projectiles.remove(i);
				
				if (DEBUG_DRAW_BOUNDING_RECTANGLE)
				{
					boundingRectLaser.remove(i);
				}
			}
		}
	}

	private void updateCollision()
	{
		// Use the Rectangle's built-in intersect function to determine if 2 objects are overlapping
		Rectangle rectangle1;
		Rectangle rectangle2;

		// Only create the rectangle once for the player
		rectangle1 = new Rectangle((int) player.position.x - (player.getWidth() / 2),
								   (int) player.position.y - (player.getHeight() / 2),
								   player.getWidth(),
								   player.getHeight());
		
		if (DEBUG_DRAW_BOUNDING_RECTANGLE)
		{
			boundingRectPlayer = new Rectangle((int) player.position.x - (player.getWidth() / 2),
											   (int) player.position.y - (player.getHeight() / 2),
											   player.getWidth(),
											   player.getHeight());
		}
		
		// Check collision between Player and Enemies
		for (int i = 0; i < enemies.size(); ++i)
		{
			rectangle2 = new Rectangle((int) enemies.get(i).position.x - (enemies.get(i).getWidth() / 2),
									   (int) enemies.get(i).position.y - (enemies.get(i).getHeight() / 2),
									   enemies.get(i).getWidth(),
									   enemies.get(i).getHeight());
			
			if (DEBUG_DRAW_BOUNDING_RECTANGLE)
			{
				boundingRectEnemies.set(i, new Rectangle((int) enemies.get(i).position.x - (enemies.get(i).getWidth() / 2),
														 (int) enemies.get(i).position.y - (enemies.get(i).getHeight() / 2),
														 enemies.get(i).getWidth(),
														 enemies.get(i).getHeight()));
			}
			
			// Determine if the two objects collided with each other
			if (rectangle1.intersects(rectangle2))
			{
				// Subtract Health from the player based on the enemy damage
				player.health -= enemies.get(i).damage;

				// Since the Enemy collided with the Player destroy it
				enemies.get(i).health = 0;

				// If the Player Health is less or equal to 0 we died
				if (player.health <= 0)
					player.isActive = false;
			}
		}

		// Projectiles VS Enemies Collisions
		for (int i = 0; i < projectiles.size(); ++i)
		{
			for (int j = 0; j < enemies.size(); ++j)
			{
				// Create the rectangles we need to determine if lasers collided with enemies
				rectangle1 = new Rectangle((int) projectiles.get(i).position.x - (projectiles.get(i).getWidth() / 2),
										   (int) projectiles.get(i).position.y - (projectiles.get(i).getHeight() / 2),
										   projectiles.get(i).getWidth(),
										   projectiles.get(i).getHeight());

				rectangle2 = new Rectangle((int) enemies.get(j).position.x - (enemies.get(j).getWidth() / 2),
										   (int) enemies.get(j).position.y - (enemies.get(j).getHeight() / 2),
										   enemies.get(j).getWidth(),
										   enemies.get(j).getHeight());

				// Using visible bounding rectangles
				if (DEBUG_DRAW_BOUNDING_RECTANGLE)
				{
					 boundingRectLaser.set(i, new Rectangle((int) projectiles.get(i).position.x - (projectiles.get(i).getWidth() / 2),
							 								(int) projectiles.get(i).position.y - (projectiles.get(i).getHeight() / 2),
							 								projectiles.get(i).getWidth(),
							 								projectiles.get(i).getHeight()));
					 boundingRectEnemies.set(j, new Rectangle((int) enemies.get(j).position.x - (enemies.get(j).getWidth() / 2),
							 								  (int) enemies.get(j).position.y - (enemies.get(j).getHeight() / 2),
							 								  enemies.get(j).getWidth(),
							 								  enemies.get(j).getHeight()));
				}
				
				// Determine if the two objects collided with each other
				if (rectangle1.intersects(rectangle2))
				{
					// Subtract enemies Health by the damage of the projectile and deactivate projectile
					enemies.get(j).health -= projectiles.get(i).damage;
					projectiles.get(i).isActive = false;
				}
			}
		}

		// Player VS HUD collision
		// Check if the Player is under the Heath and Score text. If so, the Health and Score text
		// will be transparent so we can see the player. We will check which line is longer and use
		// that to test if the player is under the text. We have use the offset of our player
		// position since it is in the middle of the ship.
		if (gameFont.measureString("Health : " + player.health).x >= gameFont.measureString("Score : " + score).x)
		{
			// + 25 is the Height we are using to set the position of the second Line (Health)
			fontOrigin = new Vector2(gameFont.measureString("Health : " + player.health).x,
									 gameFont.measureString("Score : " + score).y + 25);
		}
		else
		{
			// + 25 is the Height we are using to set the position of the second Line (Health)
			fontOrigin = new Vector2(gameFont.measureString("Score : " + score).x,
									 gameFont.measureString("Score : " + score).y + 25);
		}

		if ((player.position.x - player.getWidth() / 2 <= fontOrigin.x) &&
			(player.position.y - player.getHeight() / 2 <= fontOrigin.y))
		{
			fontAlphaBlend = 0.25f;
		}
		else
		{
			fontAlphaBlend = 1f;
		}
	}

	private void addExplosion(Vector2 position)
	{
		Animation explosion = new Animation();
		explosion.initialize(explosionTexture, position, 134, 134, 12, 45, Color.White, 1f, false);
		explosions.add(explosion);
	}

	private void updateExplosions(GameTime gameTime)
	{
		for (int i = explosions.size() - 1; i >= 0; --i)
		{
			explosions.get(i).update(gameTime);
			if (explosions.get(i).isActive == false)
			{
				explosions.remove(i);
			}
		}
	}

	private void playMusic(Song song)
	{
		// Due to the way the MediaPlayer plays music, we have to catch the exception.
		// Music will play when the game is not tethered
//		try
//		{
			// Play the music
			MediaPlayer.play(song);

			// Loop the currently playing song
			MediaPlayer.setIsRepeating(true);
//		}
//		catch
//		{
//			
//		}
	}

	protected void draw(GameTime gameTime)
	{
		getGraphicsDevice().clear(Color.CornflowerBlue);

		fpsCounter.draw(gameTime);

		// Start drawing
		spriteBatch.begin(SpriteSortMode.Deferred, BlendState.AlphaBlend);

		// Call the Draw method associated with the current screen
		switch (currentScreen)
		{
			case MainMenu:
			{
				drawMainMenu();
				break;
			}
			case EndMenu:
			{
				drawEndMenu();
				break;
			}
			case MainGame:
			{
				drawMainGame();
				break;
			}
		}

		// Stop drawing
		spriteBatch.end();

		super.draw(gameTime);
	}

	private void drawMainMenu()
	{
		// Draw all the elements that are part of the Main Menu
		spriteBatch.draw(mainMenuScreenBackground, Vector2.ZERO, Color.White);

		fontOrigin = menuFont.measureString("Play").divide(2f);
		spriteBatch.drawString(menuFont, "Play", new Vector2(fontPos.x, fontPos.y + 10),
							   playColor, 0, fontOrigin, 1.0f, SpriteEffects.None, 0.5f);
		fontOrigin = menuFont.measureString("Quit").divide(2f);
		spriteBatch.drawString(menuFont, "Quit", new Vector2(fontPos.x, fontPos.y + 10 + fontOrigin.y * 2),
							   quitColor, 0, fontOrigin, 1.0f, SpriteEffects.None, 0.5f);
	}

	private void drawMainGame()
	{
		// Draw the static background
		spriteBatch.draw(mainBackground, Vector2.ZERO, Color.White);

		// Draw the moving backgrounds
		bgLayer1.draw(spriteBatch);
		bgLayer2.draw(spriteBatch);

		// Draw the Player if player's health is above 0
		if (player.health > 0)
			player.draw(spriteBatch);

		// Draw the Enemies
		for (int i = 0; i < enemies.size(); ++i)
		{
			enemies.get(i).draw(spriteBatch);
		}

		// Draw the Projectiles
		for (int i = 0; i < projectiles.size(); ++i)
		{
			projectiles.get(i).draw(spriteBatch);
		}

		// Draw the Explosions
		for (int i = 0; i < explosions.size(); ++i)
		{
			explosions.get(i).draw(spriteBatch);
		}

		// Draw the score
		spriteBatch.drawString(gameFont, "Score : " + score,
							   new Vector2(getGraphicsDevice().getViewport().getTitleSafeArea().x,
									   	   getGraphicsDevice().getViewport().getTitleSafeArea().y),
							   Color.multiply(Color.White, fontAlphaBlend));

		// Draw the player's health
		spriteBatch.drawString(gameFont, "Health : " + player.health,
							   new Vector2 (getGraphicsDevice().getViewport().getTitleSafeArea().x,
									   		getGraphicsDevice().getViewport().getTitleSafeArea().y + 25),
								Color.multiply(Color.White, fontAlphaBlend));

		// Draw Bounding Rectangles
		if (DEBUG_DRAW_BOUNDING_RECTANGLE)
		{
			drawBorder(boundingRectPlayer, 2, Color.BlueViolet);
	
			for (int i = 0; i < boundingRectEnemies.size(); i++)
			{
				drawBorder(boundingRectEnemies.get(i), 2, Color.SpringGreen);
			}
	
			for (int i = 0; i < boundingRectLaser.size(); i++)
			{
				drawBorder(boundingRectLaser.get(i), 2, Color.Black);
			}
		}
	}

	private void drawEndMenu()
	{
		// Draw all the elements that are part of the End Menu
		// Draw the EndMenu Background
		spriteBatch.draw(endMenuScreenBackground, Vector2.ZERO, Color.White);

		// Draw the score
		fontOrigin = menuFont.measureString("Score : " + score).divide(2);
		spriteBatch.drawString(menuFont, "Score : " + score, fontPos, Color.White, 0, fontOrigin, 1.0f, SpriteEffects.None, 0.5f);
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
