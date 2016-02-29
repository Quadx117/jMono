package gameCore.graphics;

import gameCore.Rectangle;

public class DisplayMode
{

	private int width;
	private int height;
	private int refreshRate;
	private SurfaceFormat format;

	public float getAspectRatio()
	{
		return (float) width / (float) height;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public int getRefreshRate()
	{
		return this.refreshRate;
	}

	public SurfaceFormat getFormat()
	{
		return format;
	}

	public Rectangle getTitleSafeArea()
	{
		return new Rectangle(0, 0, width, height);
	}

	protected DisplayMode(int width, int height, int refreshRate, SurfaceFormat format)
	{
		this.width = width;
		this.height = height;
		this.refreshRate = refreshRate;
		this.format = format;
	}

	/**
	 * Compares whether two {@link DisplayMode} instances are equal.
	 * 
	 * @param obj
	 * 		  the reference object with which to compare.
	 * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj.getClass() != this.getClass())
		{
			return false;
		}
		return this.equals((DisplayMode) obj);
	}
	
	// Helper method
	private boolean equals(DisplayMode other)
	{
		return (this.format == other.format) &&	//
			   (this.height == other.height) &&	//
			   (this.refreshRate == other.refreshRate) &&	//
			   (this.width == other.width);
	}

	/**
	 * Indicates whether some other object is "not equal to" this one.
	 * 
	 * @param obj
	 * 		  the reference object with which to compare.
	 * @return {@code false} if this object is the same as the obj argument;
     *         {@code true} otherwise.
     * @see #equals(Object)
	 */
	public boolean notEquals(Object obj)
	{
		return !this.equals(obj);
	}

	@Override
	public int hashCode()
	{
		return (Integer.hashCode(this.width) ^ Integer.hashCode(this.height) ^ Integer.hashCode(this.refreshRate) ^ this.format
				.hashCode());
	}

	@Override
	public String toString()
	{
		return "{{Width:" + this.width + " Height:" + this.height + " Format:" + this.format + " RefreshRate:"
				+ this.refreshRate + "}}";
	}

}
