package gameCore.graphics.vertices;

/**
 * Helper class which ensures we only lookup a vertex declaration for a particular type once.
 * 
 * @author Eric
 *
 * @param <T>
 *        A vertex structure which implements IVertexType.
 */
public class VertexDeclarationCache<T extends IVertexType>
{
	static private VertexDeclaration _cached;

	static public VertexDeclaration getVertexDeclaration()
	{
		if (_cached == null)
			_cached = VertexDeclaration.fromType(typeof(T));

		return _cached;
	}
}
