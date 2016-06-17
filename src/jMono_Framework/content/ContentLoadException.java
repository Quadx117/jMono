package jMono_Framework.content;
// TODO: Should this be a RuntimeException or not ?
public class ContentLoadException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContentLoadException()
	{
		super();
	}

	public ContentLoadException(String message)
	{
		super(message);
	}

	public ContentLoadException(String message, Exception innerException)
	{
		super(message, innerException);
	}
}
