package jMono_Framework.graphics;

import jMono_Framework.Rectangle;
import jMono_Framework.math.Matrix;
import jMono_Framework.math.Vector3;

// C# struct
/**
 * Describes the view bounds for render-target surface.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class Viewport
{
    private int x;
    private int y;
    private int width;
    private int height;
    private float minDepth;
    private float maxDepth;

    // #region Properties

    /**
     * Returns the height of the bounds in pixels.
     * 
     * @return The height of the bounds in pixels.
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Sets the height of the bounds in pixels to the specified value.
     * 
     * @param value
     *        The new height of the bounds in pixels.
     */
    public void setHeight(int value)
    {
        height = value;
    }

    /**
     * Returns the upper limit of depth of this viewport.
     * 
     * @return The upper limit of depth of this viewport.
     */
    public float getMaxDepth()
    {
        return this.maxDepth;
    }

    /**
     * Sets the upper limit of depth of this viewport to the specified value.
     * 
     * @param value
     *        The new upper limit of depth of this viewport.
     */
    public void setMaxDepth(float value)
    {
        maxDepth = value;
    }

    /**
     * Returns the lower limit of depth of this viewport.
     * 
     * @return The lower limit of depth of this viewport.
     */
    public float getMinDepth()
    {
        return this.minDepth;
    }

    /**
     * Sets the lower limit of depth of this viewport to the specified value.
     * 
     * @param value
     *        The new lower limit of depth of this viewport.
     */
    public void setMinDepth(float value)
    {
        minDepth = value;
    }

    /**
     * Returns the width of the bounds in pixels.
     * 
     * @return The width of the bounds in pixels.
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Sets the width of the bounds in pixels to the specified value.
     * 
     * @param value
     *        The new width of the bounds in pixels.
     */
    public void setWidth(int value)
    {
        width = value;
    }

    /**
     * Returns the y coordinate of the beginning of this viewport.
     * 
     * @return The y coordinate of the beginning of this viewport.
     */
    public int getY()
    {
        return this.y;
    }

    /**
     * Sets the y coordinate of the beginning of this viewport to the specified value.
     * 
     * @param value
     *        The new y coordinate of the beginning of this viewport.
     */
    public void setY(int value)
    {
        y = value;
    }

    /**
     * Returns the x coordinate of the beginning of this viewport.
     * 
     * @return The x coordinate of the beginning of this viewport.
     */
    public int getX()
    {
        return x;
    }

    /**
     * Sets the x coordinate of the beginning of this viewport.
     * 
     * @param value
     *        The new x coordinate of the beginning of this viewport.
     */
    public void setX(int value)
    {
        x = value;
    }

    // #endregion

    /**
     * Returns the aspect ratio of this {@code Viewport}, which is width / height.
     * 
     * @return The aspect ratio of this {@code Viewport}
     */
    public float getAspectRatio()
    {
        if ((height != 0) && (width != 0))
        {
            return (((float) width) / ((float) height));
        }
        return 0f;
    }

    /**
     * Returns a boundary of this {@code Viewport}.
     * 
     * @return A boundary of this {@code Viewport}.
     */
    public Rectangle getBounds()
    {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Sets the boundary of this {@code Viewport} to the specified {@link Rectangle} value.
     * 
     * @param value
     *        The new bounds of this {@code Viewport}
     */
    public void setBounds(Rectangle value)
    {
        x = value.x;
        y = value.y;
        width = value.width;
        height = value.height;
    }

    /**
     * Returns the subset of the viewport that is guaranteed to be visible on a lower quality
     * display.
     * 
     * @return Returns the subset of the viewport that is guaranteed to be visible on a lower
     *         quality display.
     */
    public Rectangle getTitleSafeArea()
    {
        return GraphicsDevice.getTitleSafeArea(x, y, width, height);
    }

    // NOTE(Eric): Added this since it is provided by default for struct in C#
    /**
     * Constructs a viewport with all its components set to 0.
     */
    public Viewport()
    {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.minDepth = 0.0f;
        this.maxDepth = 0.0f;
    }

    /**
     * Constructs a viewport from the given values.
     * The {@link #minDepth} will be 0.0 and {@link #maxDepth} will be 1.0.
     * 
     * @param x
     *        The x coordinate of the upper-left corner of the view bounds in pixels.
     * @param y
     *        The y coordinate of the upper-left corner of the view bounds in pixels.
     * @param width
     *        The width of the view bounds in pixels.
     * @param height
     *        The height of the view bounds in pixels.
     */
    public Viewport(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.minDepth = 0.0f;
        this.maxDepth = 1.0f;
    }

    /**
     * Constructs a viewport from the given values.
     * 
     * @param x
     *        The x coordinate of the upper-left corner of the view bounds in pixels.
     * @param y
     *        The y coordinate of the upper-left corner of the view bounds in pixels.
     * @param width
     *        The width of the view bounds in pixels.
     * @param height
     *        The height of the view bounds in pixels.
     * @param minDepth
     *        The lower limit of depth.
     * @param maxDepth
     *        The upper limit of depth.
     */
    public Viewport(int x, int y, int width, int height, float minDepth, float maxDepth)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
    }

    /**
     * Constructs a viewport from the specified {@link Rectangle} value.
     * 
     * @param bounds
     *        A {@code Rectangle} that defines the location and size of the {@code Viewport} in a
     *        render target.
     */
    public Viewport(Rectangle bounds)
    {
        this(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    // NOTE(Eric): added this utility constructor since struct behaves differently than class in C#
    /**
     * Constructs a viewport from the specified {@code Viewport} value.
     * 
     * @param viewport
     *        A {@code Viewport} that defines the location and size of the {@code Viewport} in a
     *        render target.
     */
    public Viewport(Viewport viewport)
    {
        this.x = viewport.x;
        this.y = viewport.y;
        this.width = viewport.width;
        this.height = viewport.height;
        this.minDepth = viewport.minDepth;
        this.maxDepth = viewport.maxDepth;
    }

    /**
     * Projects a {@link Vector3} from world space into screen space.
     * 
     * @param source
     *        The {@code Vector3} to project.
     * @param projection
     *        The projection {@link Matrix}.
     * @param view
     *        The view {@code Matrix}.
     * @param world
     *        The world {@code Matrix}.
     * @return The {@code Vector3} projected into screen space.
     */
    public Vector3 project(Vector3 source, Matrix projection, Matrix view, Matrix world)
    {
        Matrix matrix = Matrix.multiply(Matrix.multiply(world, view), projection);
        Vector3 vector = Vector3.transform(source, matrix);
        float a = (((source.x * matrix.m14) + (source.y * matrix.m24)) + (source.z * matrix.m34)) + matrix.m44;
        if (!withinEpsilon(a, 1f))
        {
            vector.x = vector.x / a;
            vector.y = vector.y / a;
            vector.z = vector.z / a;
        }
        vector.x = (((vector.x + 1f) * 0.5f) * this.width) + this.x;
        vector.y = (((-vector.y + 1f) * 0.5f) * this.height) + this.y;
        vector.z = (vector.z * (this.maxDepth - this.minDepth)) + this.minDepth;
        return vector;
    }

    /**
     * Unprojects a {@link Vector3} from screen space into world space.
     * 
     * @param source
     *        The {@code Vector3} to unproject.
     * @param projection
     *        The projection {@link Matrix}.
     * @param view
     *        The view {@code Matrix}.
     * @param world
     *        The world {@code Matrix}.
     * @return The {@code Vector3} unprojected into world space.
     */
    public Vector3 unproject(Vector3 source, Matrix projection, Matrix view, Matrix world)
    {
        Matrix matrix = Matrix.invert(Matrix.multiply(Matrix.multiply(world, view), projection));
        source.x = (((source.x - this.x) / ((float) this.width)) * 2f) - 1f;
        source.y = -((((source.y - this.y) / ((float) this.height)) * 2f) - 1f);
        source.z = (source.z - this.minDepth) / (this.maxDepth - this.minDepth);
        Vector3 vector = Vector3.transform(source, matrix);
        float a = (((source.x * matrix.m14) + (source.y * matrix.m24)) + (source.z * matrix.m34)) + matrix.m44;
        if (!withinEpsilon(a, 1f))
        {
            vector.x = vector.x / a;
            vector.y = vector.y / a;
            vector.z = vector.z / a;
        }
        return vector;
    }

    private static boolean withinEpsilon(float a, float b)
    {
        float num = a - b;
        return ((-1.401298E-45f <= num) && (num <= Float.MIN_VALUE));
    }

    /**
     * Returns a {@link String} representation of this {@code Viewport} in the format:<br/>
     * {X:[{@link #x}] Y:[{@link #y}] Width:[{@link #width}] Height:[{@link #height}] MinDepth:[
     * {@link #minDepth}] MaxDepth:[{@link #maxDepth}]}
     * 
     * @return A {@code String} representation of this {@code Viewport}.
     */
    @Override
    public String toString()
    {
        return "{X:" + x + " Y:" + y + " Width:" + width + " Height:" + height + " MinDepth:" + minDepth + " MaxDepth:" + maxDepth + "}";
    }
}
