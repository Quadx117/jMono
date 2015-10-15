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

	public static boolean notEquals(DisplayMode left, DisplayMode right)
	{
		return !(equals(left, right));
	}

	public static boolean equals(DisplayMode left, DisplayMode right)
	{
		if (left == right)	// Same object or both are null
			return true;

		if (left == null || right == null)
			return false;

		return (left.format == right.format) &&	//
			   (left.height == right.height) &&	//
			   (left.refreshRate == right.refreshRate) &&	//
			   (left.width == right.width);
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof DisplayMode))
			return false;

		DisplayMode right = (DisplayMode) other;

		return equals(this, right);
	}

	public boolean notEquals(Object other)
	{
		return !(this.equals(other));
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
