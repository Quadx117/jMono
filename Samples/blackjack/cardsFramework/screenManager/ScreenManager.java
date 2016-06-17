package blackjack.cardsFramework.screenManager;

import jMono_Framework.Color;
import jMono_Framework.Game;
import jMono_Framework.Rectangle;
import jMono_Framework.components.DrawableGameComponent;
import jMono_Framework.content.ContentManager;
import jMono_Framework.graphics.SpriteBatch;
import jMono_Framework.graphics.SpriteFont;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.graphics.Viewport;
import jMono_Framework.time.GameTime;

import java.util.ArrayList;
import java.util.List;

import blackjack.cardsFramework.screenManager.GameScreen.ScreenState;

/**
 * The screen manager is a component which manages one or more GameScreen
 * instances. It maintains a stack of screens, calls their Update and Draw
 * methods at the appropriate times, and automatically routes input to the
 * topmost active screen.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public class ScreenManager extends DrawableGameComponent
{
	/**
	 * The list of screens maintained by this screen manager.
	 */
	List<GameScreen> screens = new ArrayList<GameScreen>();

	/**
	 * The list of screens that needs to be updated by this screen manager.
	 */
	List<GameScreen> screensToUpdate = new ArrayList<GameScreen>();

	public InputState input = new InputState();

	/**
	 * A default SpriteBatch shared by all the screens. This saves
	 * each screen having to bother creating their own local instance.
	 */
	private SpriteBatch spriteBatch;

	/**
	 * A default font shared by all the screens. This saves
	 * each screen having to bother loading their own local copy.
	 */
	private SpriteFont font;

	/**
	 * 
	 */
	private Texture2D blankTexture;

	/**
	 * A sprite representing the background of a button.
	 */
	private Texture2D buttonBackground;

	private boolean isInitialized;

	/**
	 * If true, the manager prints out a list of all the screens
	 * each time it is updated. This can be useful for making sure
	 * everything is being added and removed at the right times.
	 */
	private boolean traceEnabled;

	public SpriteBatch getSpriteBatch() { return spriteBatch; }

	public Texture2D getButtonBackground() { return buttonBackground; }

	public Texture2D getBlankTexture() { return blankTexture; }

	public SpriteFont getFont() { return font; }	
	
	/**
	 * Returns {@code true} if the screen manager should print out a list of all
	 * the screens each time it is updated.
	 * 
	 * @return {@code true} if trace is enabled.
	 */
	public boolean isTraceEnabled() { return traceEnabled; }
	public void setTraceEnabled(boolean value) { traceEnabled = value; }	

	/**
	 * Returns the portion of the screen where drawing is safely allowed.
	 * 
	 * @return The portion of the screen where drawing is safely allowed.
	 */
	public Rectangle getSafeArea()
	{
		return getGame().getGraphicsDevice().getViewport().getTitleSafeArea();
	}
	
	/**
	 * Constructs a new screen manager component.
	 * 
	 * @param game
	 * 			The game associated with this {@link ScreenManager}
	 */
	public ScreenManager(Game game)
	{
		super(game);
		// we must set EnabledGestures before we can query for them, but
        // we don't assume the game wants to read them.
// #if WINDOWS_PHONE
		// TouchPanel.EnabledGestures = GestureType.None;
// #endif
	}

	/**
	 * Initializes the screen manager component.
	 */
	@Override
	public void initialize()
	{
		super.initialize();

		isInitialized = true;
	}

	/**
	 * Load your graphics content.
	 */
	@Override
	protected void loadContent()
	{
		// Load content belonging to the screen manager.
        ContentManager content = game.getContent();

        spriteBatch = new SpriteBatch(getGraphicsDevice());
        font = content.load("fonts/menuFont_2", SpriteFont.class);
        blankTexture = content.load("images/blank", Texture2D.class);
        buttonBackground = content.load("images/buttonRegular", Texture2D.class);

        // Tell each of the screens to load their content.
        for (GameScreen screen : screens)
        {
            screen.loadContent();
        }
	}

	/**
	 * Unload your graphics content.
	 */
	@Override
	protected void unloadContent()
	{
		// Tell each of the screens to unload their content.
		for (GameScreen screen : screens)
		{
			screen.unloadContent();
		}
	}

	/**
	 * Allows each screen to run logic.
	 */
	@Override
	public void update(GameTime gameTime)
	{
		// Read the keyboard and gamepad.
		input.update();

		// Make a copy of the master screen list, to avoid confusion if
		// the process of updating one screen adds or removes others.
		screensToUpdate.clear();

		for (GameScreen screen : screens)
		{
			screensToUpdate.add(screen);
		}

		boolean otherScreenHasFocus = !game.isActive();
		boolean coveredByOtherScreen = false;

		// Loop as long as there are screens waiting to be updated.
		while (screensToUpdate.size() > 0)
		{
			// Pop the topmost screen off the waiting list.
			GameScreen screen = screensToUpdate.get(screensToUpdate.size() - 1);

			screensToUpdate.remove(screensToUpdate.size() - 1);

			// Update the screen.
			screen.update(gameTime, otherScreenHasFocus, coveredByOtherScreen);

			if (screen.getScreenState() == ScreenState.TRANSITION_ON || screen.getScreenState() == ScreenState.ACTIVE)
			{
				// If this is the first active screen we came across, give it a
				// chance to handle input.
				if (!otherScreenHasFocus)
				{
					screen.handleInput(input);

					otherScreenHasFocus = true;
				}

				// If this is an active non-popup, inform any subsequent
				// screens that they are covered by it.
				if (!screen.isPopup())
					coveredByOtherScreen = true;
			}
		}

		// Print debug trace ?
		if (traceEnabled)
			traceScreens();
	}

	/**
	 * Prints a list of all the screens, for debugging.
	 */
	private void traceScreens()
	{
		List<String> screenNames = new ArrayList<String>();

		for (GameScreen screen : screens)
			screenNames.add(screen.getClass().getName());

		System.out.println(screenNames);
	}

	/**
	 * Tells each screen to draw itself.
	 */
	@Override
	public void draw(GameTime gameTime)
	{
		for (GameScreen screen : screens)
		{
			if (screen.getScreenState() == ScreenState.HIDDEN)
				continue;

			screen.draw(gameTime);
		}
	}

	/**
	 * Adds a new screen to the screen manager.
	 * 
	 * @param screen
	 *        The new screen to add.
	 */
	public void addScreen(GameScreen screen)
	{
		// screen.ControllingPlayer = controllingPlayer;
		screen.screenManager = this;
		screen.isExiting = false;

		// If we have a graphics device, tell the screen to load content.
		if (isInitialized)
		{
			screen.loadContent();
		}

		screens.add(screen);
		
        // update the TouchPanel to respond to gestures this screen is interested in
// #if WINDOWS_PHONE
		// TouchPanel.EnabledGestures = screen.EnabledGestures;
// #endif
	}

	/**
	 * Removes a screen from the screen manager. You should normally
	 * use GameScreen.ExitScreen instead of calling this directly, so
	 * the screen can gradually transition off rather than just being
	 * instantly removed.
	 * 
	 * @param screen
	 * 			The screen to be removed.
	 */
	public void removeScreen(GameScreen screen)
	{
		// If we have a graphics device, tell the screen to unload content.
		if (isInitialized)
		{
			screen.unloadContent();
		}

		screens.remove(screen);
		screensToUpdate.remove(screen);
		
// #if WINDOWS_PHONE
		// if there is a screen still in the manager, update TouchPanel
		// to respond to gestures that screen is interested in.
		// if (screens.Count > 0)
		// {
		// TouchPanel.EnabledGestures = screens[screens.Count - 1].EnabledGestures;
		// }
// #endif
	}

	/**
	 * Expose an array holding all the screens. We return a copy rather
	 * than the real master list, because screens should only ever be added
	 * or removed using the addScreen and removeScreen methods.
	 * 
	 * @return A copy of the array of all the screens.
	 */
	public GameScreen[] getScreens()
	{
		return screens.toArray(new GameScreen[screens.size()]);
	}

	/**
	 * Helper draws a translucent black full screen sprite, used for fading
	 * screens in and out, and for darkening the background behind popups.
	 * 
	 * @param alpha
	 */
	public void fadeBackBufferToBlack(float alpha)
	{
		Viewport viewport = getGraphicsDevice().getViewport();
		
		spriteBatch.begin();

		spriteBatch.draw(blankTexture, new Rectangle(0, 0, viewport.getWidth(), viewport.getHeight()),
						 Color.multiply(Color.Black, alpha));

		spriteBatch.end();
	}

	// TODO: Serialization
/*
		/// <summary>
        /// Informs the screen manager to serialize its state to disk.
        /// </summary>
        public void SerializeState()
        {
            // open up isolated storage
            using (IsolatedStorageFile storage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                // if our screen manager directory already exists, delete the contents
                if (storage.DirectoryExists("ScreenManager"))
                {
                    DeleteState(storage);
                }

                // otherwise just create the directory
                else
                {
                    storage.CreateDirectory("ScreenManager");
                }

                // create a file we'll use to store the list of screens in the stack
                using (IsolatedStorageFileStream stream = storage.CreateFile("ScreenManager\\ScreenList.dat"))
                {
                    using (BinaryWriter writer = new BinaryWriter(stream))
                    {
                        // write out the full name of all the types in our stack so we can
                        // recreate them if needed.
                        foreach (GameScreen screen in screens)
                        {
                            if (screen.IsSerializable)
                            {
                                writer.Write(screen.GetType().AssemblyQualifiedName);
                            }
                        }
                    }
                }

                // now we create a new file stream for each screen so it can save its state
                // if it needs to. we name each file "ScreenX.dat" where X is the index of
                // the screen in the stack, to ensure the files are uniquely named
                int screenIndex = 0;
                foreach (GameScreen screen in screens)
                {
                    if (screen.IsSerializable)
                    {
                        string fileName = string.Format("ScreenManager\\Screen{0}.dat", screenIndex);

                        // open up the stream and let the screen serialize whatever state it wants
                        using (IsolatedStorageFileStream stream = storage.CreateFile(fileName))
                        {
                            screen.Serialize(stream);
                        }

                        screenIndex++;
                    }
                }
            }
        }

        public bool DeserializeState()
        {
            // open up isolated storage
            using (IsolatedStorageFile storage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                // see if our saved state directory exists
                if (storage.DirectoryExists("ScreenManager"))
                {
                    try
                    {
                        // see if we have a screen list
                        if (storage.FileExists("ScreenManager\\ScreenList.dat"))
                        {
                            // load the list of screen types
                            using (IsolatedStorageFileStream stream =
                                storage.OpenFile("ScreenManager\\ScreenList.dat", FileMode.Open,
                                FileAccess.Read))
                            {
                                using (BinaryReader reader = new BinaryReader(stream))
                                {
                                    while (reader.BaseStream.Position < reader.BaseStream.Length)
                                    {
                                        // read a line from our file
                                        string line = reader.ReadString();

                                        // if it isn't blank, we can create a screen from it
                                        if (!string.IsNullOrEmpty(line))
                                        {
                                            Type screenType = Type.GetType(line);
                                            GameScreen screen = Activator.CreateInstance(screenType) as GameScreen;
                                            AddScreen(screen, PlayerIndex.One);
                                        }
                                    }
                                }
                            }
                        }

                        // next we give each screen a chance to deserialize from the disk
                        for (int i = 0; i < screens.Count; i++)
                        {
                            string filename = string.Format("ScreenManager\\Screen{0}.dat", i);
                            using (IsolatedStorageFileStream stream = storage.OpenFile(filename,
                                FileMode.Open, FileAccess.Read))
                            {
                                screens[i].Deserialize(stream);
                            }
                        }

                        return true;
                    }
                    catch (Exception)
                    {
                        // if an exception was thrown while reading, odds are we cannot recover
                        // from the saved state, so we will delete it so the game can correctly
                        // launch.
                        DeleteState(storage);
                    }
                }
            }

            return false;
        }

        /// <summary>
        /// Deletes the saved state files from isolated storage.
        /// </summary>
        private void DeleteState(IsolatedStorageFile storage)
        {
            // get all of the files in the directory and delete them
            string[] files = storage.GetFileNames("ScreenManager\\*");
            foreach (string file in files)
            {
                storage.DeleteFile(Path.Combine("ScreenManager", file));
            }
        }
 */

}
