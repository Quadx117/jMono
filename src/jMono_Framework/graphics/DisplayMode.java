package jMono_Framework.graphics;

import jMono_Framework.Rectangle;

public class DisplayMode
{
    // #region Fields

    private SurfaceFormat format;
    private int height;
    private int width;

    // #endregion Fields

    // #region Properties

    public float getAspectRatio()
    {
        return (float) width / (float) height;
    }

    public SurfaceFormat getFormat()
    {
        return format;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getWidth()
    {
        return this.width;
    }

    public Rectangle getTitleSafeArea()
    {
        return GraphicsDevice.getTitleSafeArea(0, 0, width, height);
    }

    // #endregion Properties

    // #region Constructors

    protected DisplayMode(int width, int height, SurfaceFormat format)
    {
        this.width = width;
        this.height = height;
        this.format = format;
    }

    // #endregion Constructors

    // #region Public Methods

    /**
     * Compares whether two {@link DisplayMode} instances are equal.
     * 
     * @param obj
     *        The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
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

    /**
     * Compares whether this instance is equal to the specified {@link DisplayMode}.
     * 
     * @param other
     *        The other {@link DisplayMode} object with which to compare.
     * @return {@code true} if this instance is the same as the specified argument; {@code false}
     *         otherwise.
     */
    public boolean equals(DisplayMode other)
    {
        return (this.format == other.format) &&
               (this.height == other.height) &&
               (this.width == other.width);
    }

    /**
     * Indicates whether some other object is "not equal to" this one.
     * 
     * @param obj
     *        the reference object with which to compare.
     * @return {@code false} if this object is the same as the obj argument; {@code true} otherwise.
     * @see #equals(Object)
     */
    public boolean notEquals(Object obj)
    {
        return !this.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return (this.width ^ this.height ^ this.format.hashCode());
    }

    @Override
    public String toString()
    {
        return "{Width:" + this.width + " Height:" + this.height + " Format:" + this.format + " AspectRatio:" + this.getAspectRatio() + "}";
    }

    // #endregion Public Methods
}
