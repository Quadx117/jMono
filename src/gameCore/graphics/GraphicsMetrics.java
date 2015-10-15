package gameCore.graphics;

// C# struct
/**
 * A snapshot of rendering statistics from <see cref="GraphicsDevice.Metrics"/> to be used for
 * runtime debugging and profiling.
 * 
 * @author Eric
 *
 */
public class GraphicsMetrics
{
	// TODO: These are ulong in C#, test if this could be a problem
	protected long _spriteCount;
	protected long _drawCount;
	protected long _primitiveCount;

	// / <summary>
	// / The count of sprites and text characters rendered via <see cref="SpriteBatch"/>.
	// / </summary>
	public long getSpriteCount() { return _spriteCount; }

	// / <summary>
	// / The count of draw calls.
	// / </summary>
	public long getDrawCount() { return _drawCount; }

	// / <summary>
	// / The count of rendered primitives.
	// / </summary>
	public long getPrimitiveCount() { return _primitiveCount; }

	// / <summary>
	// / Returns the difference between two sets of metrics.
	// / </summary>
	// / <param name="value1">Source <see cref="GraphicsMetrics"/> on the left of the sub
	// sign.</param>
	// / <param name="value2">Source <see cref="GraphicsMetrics"/> on the right of the sub
	// sign.</param>
	// / <returns>Difference between two sets of metrics.</returns>
	public static GraphicsMetrics subtract(GraphicsMetrics value1, GraphicsMetrics value2)
	{
		long spriteCount = value1._spriteCount - value2._spriteCount;
		long drawCount = value1._drawCount - value2._drawCount;
		long primitiveCount = value1._primitiveCount - value2._primitiveCount;
		return new GraphicsMetrics(spriteCount, drawCount, primitiveCount);
	}

	// / <summary>
	// / Returns the combination of two sets of metrics.
	// / </summary>
	// / <param name="value1">Source <see cref="GraphicsMetrics"/> on the left of the add
	// sign.</param>
	// / <param name="value2">Source <see cref="GraphicsMetrics"/> on the right of the add
	// sign.</param>
	// / <returns>Combination of two sets of metrics.</returns>
	public static GraphicsMetrics add(GraphicsMetrics value1, GraphicsMetrics value2)
	{
		long spriteCount = value1._spriteCount + value2._spriteCount;
		long drawCount = value1._drawCount + value2._drawCount;
		long primitiveCount = value1._primitiveCount + value2._primitiveCount;
		return new GraphicsMetrics(spriteCount, drawCount, primitiveCount);
	}

	// Note: Added this since it is provided by default for struct in C#
	public GraphicsMetrics()
	{
		this._spriteCount = 0L;
		this._drawCount = 0L;
		this._primitiveCount = 0L;
	}
	
	// NOTE: Added this utility constructor used in add() and subtract
	private GraphicsMetrics(long spriteCount, long drawCount, long primitiveCount)
	{
		this._spriteCount = spriteCount;
		this._drawCount = drawCount;
		this._primitiveCount = primitiveCount;
	}
}
