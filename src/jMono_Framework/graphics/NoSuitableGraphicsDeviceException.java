package jMono_Framework.graphics;

@SuppressWarnings("serial")
public final class NoSuitableGraphicsDeviceException extends RuntimeException
{
    public NoSuitableGraphicsDeviceException()
    {
        super();
    }

    public NoSuitableGraphicsDeviceException(String message)
    {
        super(message);
    }

    public NoSuitableGraphicsDeviceException(String message, Exception inner)
    {
        super(message, inner);
    }
}
