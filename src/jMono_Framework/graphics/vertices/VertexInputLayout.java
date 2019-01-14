package jMono_Framework.graphics.vertices;

import jMono_Framework.dotNet.As;

/**
 * Stores the vertex layout (input elements) for the input assembler stage.
 * <p>
 * In the DirectX version the input layouts are cached in a dictionary. The
 * {@code VertexInputLayout} is used as the key in the dictionary and therefore needs to override
 * {@link Object#equals(Object)}. Two {@code VertexInputLayout} instance are considered equal if the
 * vertex layouts are structurally identical.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public abstract class VertexInputLayout // implements IEquatable<VertexInputLayout>
{
    private VertexDeclaration[] _vertexDeclarations;

    protected VertexDeclaration[] getVertexDeclarations()
    {
        return _vertexDeclarations;
    }

    private int[] _instanceFrequencies;

    protected int[] getInstanceFrequencies()
    {
        return _instanceFrequencies;
    }

    // <summary>
    // Gets or sets the number of used input slots.
    // </summary>
    // <value>The number of used input slots.</value>
    private int _count;

    public int getCount()
    {
        return _count;
    }

    protected void setCount(int value)
    {
        _count = value;
    }

    /**
     * Initializes a new instance of the {@code VertexInputLayout} class.
     * 
     * @param maxVertexBufferSlots
     *        The maximum number of vertex buffer slots.
     */
    protected VertexInputLayout(int maxVertexBufferSlots)

    {
        this(new VertexDeclaration[maxVertexBufferSlots], new int[maxVertexBufferSlots], 0);
    }

    /**
     * Initializes a new instance of the {@code VertexInputLayout} class.
     * 
     * @param vertexDeclarations
     *        The array for storing vertex declarations.
     * @param instanceFrequencies
     *        The array for storing instance frequencies.
     * @param count
     *        The number of used slots.
     */
    protected VertexInputLayout(VertexDeclaration[] vertexDeclarations, int[] instanceFrequencies, int count)
    {
        assert (vertexDeclarations != null);
        assert (instanceFrequencies != null);
        assert (count >= 0);
        assert (vertexDeclarations.length >= count);
        assert (vertexDeclarations.length == instanceFrequencies.length);

        _count = count;
        _vertexDeclarations = vertexDeclarations;
        _instanceFrequencies = instanceFrequencies;
    }

    /**
     * Determines whether the specified {@link Object} is equal to this instance.
     * 
     * @param obj
     *        The object to compare with the current object.
     * @return {@code true} if the specified {@link Object} is equal to this instance; {@code false}
     *         otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        return equals(As.as(obj, VertexInputLayout.class));
    }

    /**
     * Determines whether the specified {@code VertexInputLayout} is equal to this instance.
     * 
     * @param other
     *        The object to compare with the current object.
     * @return {@code true} if the specified {@code VertexInputLayout} is equal to this instance;
     *         {@code false} otherwise.
     */
    public boolean equals(VertexInputLayout other)
    {
        if (other == null)
            return false;

        int count = getCount();
        if (count != other.getCount())
            return false;

        for (int i = 0; i < count; ++i)
        {
            assert (getVertexDeclarations()[i] != null);
            if (!getVertexDeclarations()[i].equals(other.getVertexDeclarations()[i]))
                return false;
        }

        for (int i = 0; i < count; ++i)
        {
            if (getInstanceFrequencies()[i] != other.getInstanceFrequencies()[i])
                return false;
        }

        return true;
    }

    /**
     * Returns a hash code for this instance.
     * 
     * @return A hash code for this instance, suitable for use in hashing algorithms and data
     *         structures like a hash table.
     */
    @Override
    public int hashCode()
    {
        // ReSharper disable NonReadonlyMemberInGetHashCode
        int hashCode = 0;
        int count = getCount();
        if (count > 0)
        {
            hashCode = getVertexDeclarations()[0].hashCode();
            hashCode = (hashCode * 397) ^ getInstanceFrequencies()[0];
            for (int i = 1; i < count; ++i)
            {
                hashCode = (hashCode * 397) ^ getVertexDeclarations()[i].hashCode();
                hashCode = (hashCode * 397) ^ getInstanceFrequencies()[i];
            }
        }
        return hashCode;
        // ReSharper restore NonReadonlyMemberInGetHashCode
    }

    /**
     * Determines whether the specified {@code VertexInputLayout} is not equal to this instance.
     * 
     * @param other
     *        The object to compare with the current object.
     * @return {@code false} if the specified {@code VertexInputLayout} is equal to this instance;
     *         {@code true} otherwise.
     */
    public boolean notEquals(VertexInputLayout other)
    {
        return !equals(other);
    }
}
