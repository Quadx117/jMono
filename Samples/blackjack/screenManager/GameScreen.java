package blackjack.screenManager;

import jMono_Framework.math.MathHelper;
import jMono_Framework.time.GameTime;
import jMono_Framework.time.TimeSpan;

import java.util.stream.Stream;

/**
 * A screen is a single layer that has update and draw logic, and which
 * can be combined with other layers to build up a complex menu system.
 * For instance the main menu, the options menu, the "are you sure you
 * want to quit" message box, and the main game itself are all implemented
 * as screens.
 * 
 * @author Eric Perron (inspired by CardsFramework from Microsoft)
 * 
 */
public abstract class GameScreen
{
	/**
	 * Enumeration describing the screen transition state.
	 * 
	 * @author Eric Perron (inspired by CardsFramework from Microsoft)
	 *
	 */
	public enum ScreenState
	{
		TRANSITION_ON,	//
		ACTIVE,			//
		TRANSITION_OFF,	//
		HIDDEN,			//
	}

	/**
	 * Normally when one screen is brought up over the top of another,
	 * the first screen will transition off to make room for the new
	 * one. This property indicates whether the screen is only a small
	 * popup, in which case screens underneath it do not need to bother
	 * transitioning off.
	 */
	protected boolean isPopup = false;

	/**
	 * Returns whether or not this screen is a small popup window.
	 * 
	 * @return {@code true} if this screen is a popup, {@code false} otherwise.
	 */
	public boolean isPopup() { return isPopup; }

	/**
	 * Set {@code isPopup} to the specified value.
	 * 
	 * @param value
	 *        The new value to be assigned to {@code isPopup}
	 */
	protected void setIsPopup(boolean value) { isPopup = value; }

	/**
	 * Indicates how long the screen takes to transition on when it is
	 * activated.
	 */
	protected TimeSpan transitionOnTime = new TimeSpan(TimeSpan.ZERO);

	/**
	 * Returns how long the screen takes to transition on when it is
	 * activated.
	 * 
	 * @return A {@link TimeSpan} representing how long the screen takes
	 *         to transition on when it is activated.
	 */
	public TimeSpan getTransitionOnTime() { return transitionOnTime; }

	/**
	 * Sets how long the screen takes to transition on when it is activated.
	 * 
	 * @param value
	 *        The new transition on {@link TimeSpan} value to be assigned to
	 *        this screen.
	 */
	protected void setTransitionOnTime(TimeSpan value) { transitionOnTime.setTimeSpan(value); }

	/**
	 * Indicates how long the screen takes to transition off when it is
	 * deactivated.
	 */
	protected TimeSpan transitionOffTime = new TimeSpan(TimeSpan.ZERO);

	/**
	 * Returns how long the screen takes to transition off when it is
	 * activated.
	 * 
	 * @return A {@link TimeSpan} representing how long the screen takes
	 *         to transition on when it is activated.
	 */
	public TimeSpan getTransitionOffTime() { return transitionOffTime; }

	/**
	 * Sets how long the screen takes to transition off when it is activated.
	 * 
	 * @param value
	 *        The new transition off {@link TimeSpan} value to be assigned to
	 *        this screen.
	 */
	protected void setTransitionOffTime(TimeSpan value) { transitionOffTime.setTimeSpan(value); }

	/**
	 * Indicates the current position of the screen transition, ranging
	 * from zero (fully active, no transition) to one (transitioned
	 * fully off to nothing).
	 */
	protected float transitionPosition = 1f;

	/**
	 * Returns the current position of the screen transition, ranging
	 * from zero (fully active, no transition) to one (transitioned
	 * fully off to nothing).
	 * 
	 * @return A {@code float} value representing the current position
	 *         of the screen transition, ranging from zero to one.
	 */
	public float getTransitionPosition() { return transitionPosition; }

	/**
	 * Sets the current position of the screen transition.
	 * 
	 * <p>
	 * The value must be between zero (fully active, no transition)
	 * to one (transitioned fully off to nothing).
	 * @param value
	 */
	protected void setTransitionPosition(float value) { transitionPosition = value; }

	/**
	 * Returns the current alpha of the screen transition, ranging from 1
	 * (fully active, no transition) to 0 (transitioned fully off to nothing).
	 * 
	 * @return The current alpha of the screen transition.
	 */
	public float getTransitionAlpha() { return 1f - transitionPosition; }

	/**
	 * The screenState of this GameScreen. The default value is set to
	 * ScreenState.TRANSITION_ON.
	 */
	protected ScreenState screenState = ScreenState.TRANSITION_ON;

	/**
	 * Returns the current screen transition state.
	 * 
	 * @return The current screen transition state.
	 */
	public ScreenState getScreenState() { return screenState; }

	/**
	 * Sets the current screen transition state.
	 * 
	 * @param value
	 *        The new {@link ScreenState} to be assigned to this screen.
	 */
	protected void setScreenState(ScreenState value) { screenState = value; }

	/**
	 * There are two possible reasons why a screen might be transitioning
	 * off. It could be temporarily going away to make room for another
	 * screen that is on top of it, or it could be going away for good.
	 * This property indicates whether the screen is exiting for real:
	 * if set, the screen will automatically remove itself as soon as the
	 * transition finishes.
	 */
	protected boolean isExiting = false;

	/**
	 * Returns whether or not this screen is exiting, that is this screen
	 * will automatically remove itself as soon as the transition finishes.
	 * 
	 * @return {@code true} is this screen is exiting, {@code false} otherwise.
	 */
	public boolean isExiting() { return isExiting; }

	/**
	 * Sets whether or not this screen is exiting, that is this screen
	 * will automatically remove itself as soon as the transition finishes.
	 * 
	 * @param value
	 *        Wheter or not this screen should be exiting.
	 */
	protected void setIsExiting(boolean value) { isExiting = value; }

	/**
	 * Checks whether this screen is active and can respond to user input.
	 * 
	 * @return {@code true} if the screen is active, {@code false} otherwise.
	 */
	public boolean isActive()
	{
		return !otherScreenHasFocus && (screenState == ScreenState.TRANSITION_ON || screenState == ScreenState.ACTIVE);
	}

	/**
	 * Checks whether this screen is active and can respond to user input.
	 */
	protected boolean otherScreenHasFocus;

	/**
	 * A reference to the ScreenManager object which is the parent of every
	 * GameScreen object.
	 */
	protected ScreenManager screenManager;

	/**
	 * Gets the manager that this screen belongs to.
	 * 
	 * @return The manager that this screen belongs to.
	 */
	public ScreenManager getScreenManager() { return screenManager; }

	/**
	 * Sets the manager that this screen belongs to.
	 * 
	 * @param value
	 *        The new {@link ScreenManager} that this screen belongs to.
	 */
	public void setScreenManager(ScreenManager value) { screenManager = value; }

	// / <summary>
	// / Gets the index of the player who is currently controlling this screen,
	// / or null if it is accepting input from any player. This is used to lock
	// / the game to a specific player profile. The main menu responds to input
	// / from any connected gamepad, but whichever player makes a selection from
	// / this menu is given control over all subsequent screens, so other gamepads
	// / are inactive until the controlling player returns to the main menu.
	// / </summary>
	// public PlayerIndex? ControllingPlayer
	// {
	// get { return controllingPlayer; }
	// internal set { controllingPlayer = value; }
	// }

	// PlayerIndex? controllingPlayer;

// #if WINDOWS_PHONE
	// / <summary>
	// / Gets the gestures the screen is interested in. Screens should be as specific
	// / as possible with gestures to increase the accuracy of the gesture engine.
	// / For example, most menus only need Tap or perhaps Tap and VerticalDrag to operate.
	// / These gestures are handled by the ScreenManager when screens change and
	// / all gestures are placed in the InputState passed to the HandleInput method.
	// / </summary>
	// public GestureType EnabledGestures
	// {
	// get { return enabledGestures; }
	// protected set
	// {
	// enabledGestures = value;

	// the screen manager handles this during screen changes, but
	// if this screen is active and the gesture types are changing,
	// we have to update the TouchPanel ourself.
	// if (ScreenState == ScreenState.Active)
	// {
	// TouchPanel.EnabledGestures = value;
	// }
	// }
	// }

	// GestureType enabledGestures = GestureType.None;
// #endif

	/**
	 * Whether or not this screen is serializable.
	 * 
	 * <p>
	 * By default, all screens are assumed to be serializable.
	 * 
	 * <p>
	 * If this is true, the screen will be recorded into the screen manager's state and its Serialize and Deserialize
	 * methods will be called as appropriate. If this is false, the screen will be ignored during serialization.
	 * 
	 */
	protected boolean isSerializable = true;

	/**
	 * Gets whether or not this screen is serializable.
	 * 
	 * <p>
	 * If this is true, the screen will be recorded into the screen manager's state and its Serialize and Deserialize
	 * methods will be called as appropriate. If this is false, the screen will be ignored during serialization.
	 * 
	 * @return Whether or not this screen is serializable.
	 */
	public boolean isSerializable()
	{
		return isSerializable;
	}

	/**
	 * Sets whether or not this screen is serializable.
	 * 
	 * <P>
	 * If this is true, the screen will be recorded into the screen manager's state and its Serialize and Deserialize
	 * methods will be called as appropriate. If this is false, the screen will be ignored during serialization.
	 * 
	 * @param value
	 *        The new value for this attribute.
	 */
	protected void setIsSerializable(boolean value)
	{
		isSerializable = value;
	}

	/**
	 * Load graphics content for the screen.
	 */
	public void loadContent() {}

	/**
	 * Unload content for the screen.
	 */
	public void unloadContent() {}

	/**
	 * Allows the screen to run logic, such as updating the transition position.
	 * Unlike HandleInput, this method is called regardless of whether the
	 * screen is active, hidden, or in the middle of a transition.
	 * 
	 * @param gameTime
	 *        The time which has elapsed since the last call to this method.
	 * @param otherScreenHasFocus
	 *        Whether or not another screen has the focus.
	 * @param coveredByOtherScreen
	 *        Whether or not another screen covers this one.
	 */
	protected void update(GameTime gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		this.otherScreenHasFocus = otherScreenHasFocus;

		if (isExiting)
		{
			// If the screen is going away to die, it should transition off.
			screenState = ScreenState.TRANSITION_OFF;

			if (!updateTransition(gameTime, transitionOffTime, 1))
			{
				// When the transition finishes, remove the screen.
				screenManager.removeScreen(this);
			}
		}
		else if (coveredByOtherScreen)
		{
			// If the screen is covered by another, it should transition off.
			if (updateTransition(gameTime, transitionOffTime, 1))
			{
				// Still busy transitioning.
				screenState = ScreenState.TRANSITION_OFF;
			}
			else
			{
				// Transition finished!
				screenState = ScreenState.HIDDEN;
			}
		}
		else
		{
			// Otherwise the screen should transition on and become active.
			if (updateTransition(gameTime, transitionOnTime, -1))
			{
				// Still busy transitioning.
				screenState = ScreenState.TRANSITION_ON;
			}
			else
			{
				// Transition finished!
				screenState = ScreenState.ACTIVE;
			}
		}
	}

	/**
	 * Helper for updating the screen transition position.
	 * 
	 * @param gameTime
	 *        The time which has elapsed since the last call to this method.
	 * @param time
	 *        The length in TimeSpan of the animation.
	 * @param direction
	 * 
	 * @return Whether or not the transition is finished.
	 */
	private boolean updateTransition(GameTime gameTime, TimeSpan time, int direction)
	{
		// How much should we move by?
		float transitionDelta;

		// if (time.getTicks() == TimeSpan.ZERO.getTicks())
		if (time.equals(TimeSpan.ZERO))
			transitionDelta = 1;
		else
			transitionDelta = (float) (gameTime.getElapsedGameTime().getTotalMilliseconds() / time
					.getTotalMilliseconds());

		// Update the transition position.
		transitionPosition += transitionDelta * direction;

		// Did we reach the end of the transition?
		if (((direction < 0) && (transitionPosition <= 0)) || ((direction > 0) && (transitionPosition >= 1)))
		{
			transitionPosition = MathHelper.clamp(transitionPosition, 0, 1);
			return false;
		}

		// Otherwise we are still busy transitioning.
		return true;
	}

	/**
	 * Allows the screen to handle user input. Unlike Update, this method is
	 * only called when the screen is active, and not when some other screen has
	 * taken the focus.
	 * 
	 * @param input
	 *        User input information.
	 */
	protected void handleInput(InputState input) {}

	/**
	 * This is called when the screen should draw itself.
	 * 
	 * @param gameTime
	 *        The time which has elapsed since the last call to this method.
	 */
	protected void draw(GameTime gameTime) {}

	// TODO: Serialization and check type for Stream<> (create my own in dotNet)
	/**
	 * Tells the screen to serialize its state into the given stream.
	 * 
	 * @param stream
	 *        The {@code Stream} used for serialization
	 */
	public void serialize(Stream<?> stream) {}

	/**
	 * Tells the screen to deserialize its state from the given stream.
	 * 
	 * @param stream
	 *        The {@code Stream} used for deserialization
	 */
	public void deserialize(Stream<?> stream) {}

	/**
	 * Tells the screen to go away. Unlike ScreenManager.removeScreen, which
	 * instantly kills the screen, this method respects the transition timings
	 * and will give the screen a chance to gradually transition off.
	 */
	public void exitScreen()
	{
		// This works too
		// if (transitionOffTime.getTicks() == TimeSpan.ZERO.getTicks()) {
		if (transitionOffTime.equals(TimeSpan.ZERO))
		{
			// If the screen has a zero transition time, remove it immediately.
			screenManager.removeScreen(this);
		}
		else
		{
			// Otherwise flag that it should transition off and then exit.
			isExiting = true;
		}
	}

	/**
	 * A helper method which loads assets using the screen manager's associated game content loader.
	 * 
	 * @param assetName
	 *        Asset name, relative to the loader root directory,
	 *        and not including the .xnb extension.
	 * @param type
	 *        Type of asset.
	 * @return
	 */
	public <T> T load(String assetName, Class<?> type)
	{
		return screenManager.getGame().getContent().load(assetName, type);
	}
}
