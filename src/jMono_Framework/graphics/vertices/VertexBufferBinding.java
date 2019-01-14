package jMono_Framework.graphics.vertices;

/**
 * Defines how a vertex buffer is bound to the graphics device for rendering.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class VertexBufferBinding
{
    private final VertexBuffer _vertexBuffer;
    private final int _vertexOffset;
    private final int _instanceFrequency;

    /**
     * Returns the vertex buffer.
     * 
     * @return The vertex buffer.
     */
    public VertexBuffer getVertexBuffer()
    {
        return _vertexBuffer;
    }

    /**
     * Returns the index of the first vertex in the vertex buffer to use.
     * 
     * @return The index of the first vertex in the vertex buffer to use.
     */
    public int getVertexOffset()
    {
        return _vertexOffset;
    }

    /**
     * Returns the number of instances to draw using the same per-instance data before advancing in
     * the buffer by one element.
     * 
     * @return The number of instances to draw using the same per-instance data before advancing in
     *         the buffer by one element. This value must be 0 for an element that contains
     *         per-vertex data and greater than 0 for per-instance data.
     */
    public int getInstanceFrequency()
    {
        return _instanceFrequency;
    }

    /**
     * Creates an instance of {@code VertexBufferBinding}.
     * 
     * @param vertexBuffer
     *        The vertex buffer to bind.
     */
    public VertexBufferBinding(VertexBuffer vertexBuffer)
    {
        this(vertexBuffer, 0, 0);
    }

    /**
     * Creates an instance of {@code VertexBufferBinding}.
     * 
     * @param vertexBuffer
     *        The vertex buffer to bind.
     * @param vertexOffset
     *        The index of the first vertex in the vertex buffer to use.
     */
    public VertexBufferBinding(VertexBuffer vertexBuffer, int vertexOffset)
    {
        this(vertexBuffer, vertexOffset, 0);
    }

    /**
     * Creates an instance of VertexBufferBinding.
     * 
     * @param vertexBuffer
     *        The vertex buffer to bind.
     * @param vertexOffset
     *        The index of the first vertex in the vertex buffer to use.
     * @param instanceFrequency
     *        The number of instances to draw using the same per-instance data before advancing in
     *        the buffer by one element. This value must be 0 for an element that contains
     *        per-vertex data and greater than 0 for per-instance data.
     * @throws NullPointerException
     *         {@code vertexBuffer} is {@code null}.
     * @throws IllegalArgumentException
     *         {@code vertexOffset} or {@code instanceFrequency} is invalid.
     */
    public VertexBufferBinding(VertexBuffer vertexBuffer, int vertexOffset, int instanceFrequency) throws NullPointerException, IllegalArgumentException
    {
        if (vertexBuffer == null)
            throw new NullPointerException("vertexBuffer");
        if (vertexOffset < 0 || vertexOffset >= vertexBuffer.getVertexCount())
            throw new IllegalArgumentException("vertexOffset");
        if (instanceFrequency < 0)
            throw new IllegalArgumentException("instanceFrequency");

        _vertexBuffer = vertexBuffer;
        _vertexOffset = vertexOffset;
        _instanceFrequency = instanceFrequency;
    }
}
