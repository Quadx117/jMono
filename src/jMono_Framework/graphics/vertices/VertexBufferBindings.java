package jMono_Framework.graphics.vertices;

import java.util.Arrays;

/**
 * Stores the vertex buffers to be bound to the input assembler stage.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class VertexBufferBindings extends VertexInputLayout
{
    private final VertexBuffer[] _vertexBuffers;
    private final int[] _vertexOffsets;

    /**
     * Initializes a new instance of the {@code VertexBufferBindings} class.
     * 
     * @param maxVertexBufferSlots
     *        The maximum number of vertex buffer slots.
     */
    public VertexBufferBindings(int maxVertexBufferSlots)
    {
        super(new VertexDeclaration[maxVertexBufferSlots], new int[maxVertexBufferSlots], 0);
        _vertexBuffers = new VertexBuffer[maxVertexBufferSlots];
        _vertexOffsets = new int[maxVertexBufferSlots];
    }

    /**
     * Clears the vertex buffer slots.
     * 
     * @return {@code true} if the input layout was changed; {@code false} otherwise.
     */
    public boolean clear()
    {
        if (getCount() == 0)
            return false;

        Arrays.fill(getVertexDeclarations(), 0, getCount(), null);
        Arrays.fill(getInstanceFrequencies(), 0, getCount(), 0);
        Arrays.fill(_vertexBuffers, 0, getCount(), null);
        Arrays.fill(_vertexOffsets, 0, getCount(), 0);
        setCount(0);
        return true;
    }

    /**
     * Binds the specified vertex buffer to the first input slot.
     * 
     * @param vertexBuffer
     *        The vertex buffer.
     * @param vertexOffset
     *        The offset (in vertices) from the beginning of the vertex buffer to the first vertex
     *        to use.
     * @return {@code true} if the input layout was changed; {@code false} otherwise.
     */
    public boolean set(VertexBuffer vertexBuffer, int vertexOffset)
    {
        assert (vertexBuffer != null);
        assert (0 <= vertexOffset && vertexOffset < vertexBuffer.getVertexCount());

        if (getCount() == 1 &&
            getInstanceFrequencies()[0] == 0 &&
            _vertexBuffers[0] == vertexBuffer &&
            _vertexOffsets[0] == vertexOffset)
        {
            return false;
        }

        getVertexDeclarations()[0] = vertexBuffer.getVertexDeclaration();
        getInstanceFrequencies()[0] = 0;
        _vertexBuffers[0] = vertexBuffer;
        _vertexOffsets[0] = vertexOffset;
        if (getCount() > 1)
        {
            Arrays.fill(getVertexDeclarations(), 1, getCount() - 1, null);
            Arrays.fill(getInstanceFrequencies(), 1, getCount() - 1, 0);
            Arrays.fill(_vertexBuffers, 1, getCount() - 1, null);
            Arrays.fill(_vertexOffsets, 1, getCount() - 1, 0);
        }

        setCount(1);
        return true;
    }

    /**
     * Binds the the specified vertex buffers to the input slots.
     * 
     * @param vertexBufferBindings
     *        The vertex buffer bindings.
     * @return {@code true} if the input layout was changed; {@code false} otherwise.
     */
    public boolean set(VertexBufferBinding... vertexBufferBindings)
    {
        assert (vertexBufferBindings != null);
        assert (vertexBufferBindings.length > 0);
        assert (vertexBufferBindings.length <= _vertexBuffers.length);

        boolean isDirty = false;
        for (int i = 0; i < vertexBufferBindings.length; ++i)
        {
            assert (vertexBufferBindings[i].getVertexBuffer() != null);

            if (getInstanceFrequencies()[i] == vertexBufferBindings[i].getInstanceFrequency() &&
                _vertexBuffers[i] == vertexBufferBindings[i].getVertexBuffer() &&
                _vertexOffsets[i] == vertexBufferBindings[i].getVertexOffset())
            {
                continue;
            }

            getVertexDeclarations()[i] = vertexBufferBindings[i].getVertexBuffer().getVertexDeclaration();
            getInstanceFrequencies()[i] = vertexBufferBindings[i].getInstanceFrequency();
            _vertexBuffers[i] = vertexBufferBindings[i].getVertexBuffer();
            _vertexOffsets[i] = vertexBufferBindings[i].getVertexOffset();
            isDirty = true;
        }

        if (getCount() > vertexBufferBindings.length)
        {
            int startIndex = vertexBufferBindings.length;
            int length = getCount() - startIndex;
            Arrays.fill(getVertexDeclarations(), startIndex, length, null);
            Arrays.fill(getInstanceFrequencies(), startIndex, length, 0);
            Arrays.fill(_vertexBuffers, startIndex, length, null);
            Arrays.fill(_vertexOffsets, startIndex, length, 0);
            isDirty = true;
        }

        setCount(vertexBufferBindings.length);
        return isDirty;
    }

    /**
     * Returns vertex buffer bound to the specified input slots.
     * 
     * @param slot
     * @return The vertex buffer binding.
     */
    public VertexBufferBinding get(int slot)
    {
        assert (0 <= slot && slot < getCount());

        return new VertexBufferBinding(_vertexBuffers[slot],
                                       _vertexOffsets[slot],
                                       getInstanceFrequencies()[slot]);
    }

    /**
     * Returns vertex buffers bound to the input slots.
     * 
     * @return The vertex buffer bindings.
     */
    public VertexBufferBinding[] get()
    {
        VertexBufferBinding[] bindings = new VertexBufferBinding[getCount()];
        for (int i = 0; i < bindings.length; ++i)
            bindings[i] = new VertexBufferBinding(_vertexBuffers[i],
                                                  _vertexOffsets[i],
                                                  getInstanceFrequencies()[i]);

        return bindings;
    }
}
