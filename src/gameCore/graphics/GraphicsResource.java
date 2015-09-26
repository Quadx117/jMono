package gameCore.graphics;

import gameCore.events.EventArgs;
import gameCore.events.EventHandler;

import java.lang.ref.WeakReference;

public abstract class GraphicsResource implements AutoCloseable
{
	boolean disposed;

	// The GraphicsDevice property should only be accessed in Dispose(bool) if the disposing
	// parameter is true. If disposing is false, the GraphicsDevice may or may not be
	// disposed yet.
	protected GraphicsDevice graphicsDevice;

	private WeakReference<?> _selfReference;

	protected GraphicsResource()
	{

	}

	@Override
	public void finalize()
	{
		// Pass false so the managed objects are not released
		dispose(false);
	}

	/**
	 * Called before the device is reset. Allows graphics resources to invalidate their state
	 * so they can be recreated after the device reset.
	 * 
	 * <p>
	 * Warning: This may be called after a call to Dispose() up until the resource is garbage
	 * collected.
	 */
	protected void graphicsDeviceResetting() {}

	@Override
	public void close()
	{
		// Dispose of managed objects as well
		dispose(true);
		// Since we have been manually disposed, do not call the finalizer on this object
		// GC.SuppressFinalize(this);
	}

	// / <summary>
	// / The method that derived classes should override to implement disposing of managed and
	// native resources.
	// / </summary>
	// / <param name="disposing">True if managed objects should be disposed.</param>
	// / <remarks>Native resources should always be released regardless of the value of the
	// disposing parameter.</remarks>
	protected void dispose(boolean disposing)
	{
		if (!disposed)
		{
			if (disposing)
			{
				// Release managed objects
				// ...
			}

			// Release native objects
			// ...

			// Do not trigger the event if called from the finalizer
			if (disposing && Disposing != null)
				Disposing.accept(this, EventArgs.Empty);

			// Remove from the global list of graphics resources
			if (graphicsDevice != null)
				graphicsDevice.removeResourceReference(_selfReference);

			_selfReference = null;
			graphicsDevice = null;
			disposed = true;
		}
	}

	public EventHandler<EventArgs> Disposing;

	public GraphicsDevice getGraphicsDevice()
	{
		return graphicsDevice;
	}

	public void setGraphicsDevice(GraphicsDevice value)
	{
		assert(value != null);

		if (graphicsDevice == value)
			return;

		// VertexDeclaration objects can be bound to multiple GraphicsDevice objects
		// during their lifetime. But only one GraphicsDevice should retain ownership.
		if (graphicsDevice != null)
		{
			graphicsDevice.removeResourceReference(_selfReference);
			_selfReference = null;
		}

		graphicsDevice = value;

		_selfReference = new WeakReference<Object>(this);
		graphicsDevice.addResourceReference(_selfReference);
	}

	public boolean isDisposed()
	{
		return disposed;
	}

	public String name;

	public Object tag;

	@Override
	public String toString()
	{
		return stringIsNullOrEmpty(name) ? super.toString() : name;
	}

	// TODO: This should go in a wrapper class so id don't have to copy it around.
	// Helper method
	private boolean stringIsNullOrEmpty(String s)
	{
		return s == null || s.isEmpty();
	}

}
