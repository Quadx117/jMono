package jMono_Framework.graphics;

// C# struct
/**
 * A snapshot of rendering statistics from {@link GraphicsDevice#getMetrics()} to be used for
 * runtime debugging and profiling.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class GraphicsMetrics
{
    protected long _clearCount;
    protected long _drawCount;
    protected long _pixelShaderCount;
    protected long _primitiveCount;
    protected long _spriteCount;
    protected long _targetCount;
    protected long _textureCount;
    protected long _vertexShaderCount;

    /**
     * Returns the number of times clear was called.
     * 
     * @return The number of times clear was called.
     */
    public long getClearCount()
    {
        return _clearCount;
    }

    /**
     * Returns the number of times draw was called.
     * 
     * @return The number of times draw was called.
     */
    public long getDrawCount()
    {
        return _drawCount;
    }

    /**
     * Returns the number of times the pixel shader was changed on the GPU.
     * 
     * @return The number of times the pixel shader was changed on the GPU.
     */
    public long getPixelShaderCount()
    {
        return _pixelShaderCount;
    }

    /**
     * Returns the number of rendered primitives.
     * 
     * @return The number of rendered primitives.
     */
    public long getPrimitiveCount()
    {
        return _primitiveCount;
    }

    /**
     * Returns the number of sprites and text characters rendered via {@link SpriteBatch}.
     * 
     * @return The number of sprites and text characters rendered via {@link SpriteBatch}.
     */
    public long getSpriteCount()
    {
        return _spriteCount;
    }

    /**
     * Returns the number of times a target was changed on the GPU.
     * 
     * @return The number of times a target was changed on the GPU.
     */
    public long getTargetCount()
    {
        return _targetCount;
    }

    /**
     * Returns the number of times a texture was changed on the GPU.
     * 
     * @return The number of times a texture was changed on the GPU.
     */
    public long getTextureCount()
    {
        return _textureCount;
    }

    /**
     * Returns the number of times the vertex shader was changed on the GPU.
     * 
     * @return The number of times the vertex shader was changed on the GPU.
     */
    public long getVertexShaderCount()
    {
        return _vertexShaderCount;
    }

    /**
     * Returns the difference between two sets of metrics.
     * 
     * @param value1
     *        Source {@link GraphicsMetrics} on the left of the sub sign.
     * @param value2
     *        Source {@link GraphicsMetrics} on the right of the sub sign.
     * @return Difference between two sets of metrics.
     */
    public static GraphicsMetrics subtract(GraphicsMetrics value1, GraphicsMetrics value2)
    {
        GraphicsMetrics result = new GraphicsMetrics();
        result._clearCount = value1._clearCount - value2._clearCount;
        result._drawCount = value1._drawCount - value2._drawCount;
        result._pixelShaderCount = value1._pixelShaderCount - value2._pixelShaderCount;
        result._primitiveCount = value1._primitiveCount - value2._primitiveCount;
        result._spriteCount = value1._spriteCount - value2._spriteCount;
        result._targetCount = value1._targetCount - value2._targetCount;
        result._textureCount = value1._textureCount - value2._textureCount;
        result._vertexShaderCount = value1._vertexShaderCount - value2._vertexShaderCount;
        return result;
    }

    /**
     * Returns the combination of two sets of metrics.
     * 
     * @param value1
     *        Source {@link GraphicsMetrics} on the left of the add sign.
     * @param value2
     *        Source {@link GraphicsMetrics} on the right of the add sign.
     * @return Combination of two sets of metrics.
     */
    public static GraphicsMetrics add(GraphicsMetrics value1, GraphicsMetrics value2)
    {
        GraphicsMetrics result = new GraphicsMetrics();
        result._clearCount = value1._clearCount + value2._clearCount;
        result._drawCount = value1._drawCount + value2._drawCount;
        result._pixelShaderCount = value1._pixelShaderCount + value2._pixelShaderCount;
        result._primitiveCount = value1._primitiveCount + value2._primitiveCount;
        result._spriteCount = value1._spriteCount + value2._spriteCount;
        result._targetCount = value1._targetCount + value2._targetCount;
        result._textureCount = value1._textureCount + value2._textureCount;
        result._vertexShaderCount = value1._vertexShaderCount + value2._vertexShaderCount;
        return result;
    }

    // NOTE(Eric): Added this since it is provided by default for struct in C#
    public GraphicsMetrics()
    {
        _clearCount = 0L;
        _drawCount = 0L;
        _pixelShaderCount = 0L;
        _primitiveCount = 0L;
        _spriteCount = 0L;
        _targetCount = 0L;
        _textureCount = 0L;
        _vertexShaderCount = 0L;
    }

}
