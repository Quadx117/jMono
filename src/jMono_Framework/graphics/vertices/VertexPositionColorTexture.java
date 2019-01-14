package jMono_Framework.graphics.vertices;

import jMono_Framework.Color;
import jMono_Framework.math.Vector2;
import jMono_Framework.math.Vector3;

// C# struct
public class VertexPositionColorTexture implements IVertexType
{
	public Vector3 position;
	public Color color;
	public Vector2 textureCoordinate;
	public static VertexDeclaration vertexDeclaration;

	public VertexPositionColorTexture(Vector3 position, Color color, Vector2 textureCoordinate)
	{
		this.position = position;
		this.color = color;
		this.textureCoordinate = textureCoordinate;
	}

	// NOTE(Eric): Added this since it is provided by default for struct in C#
	public VertexPositionColorTexture()
	{
		this(new Vector3(), new Color(), new Vector2());
	}

	public VertexDeclaration getVertexDeclaration()
	{
		return vertexDeclaration;
	}

	@Override
	public int hashCode()
	{
		// TODO: Fix gethashcode
		return 0;
	}

	@Override
	public String toString()
	{
		return "{{Position:" + this.position + " Color:" + this.color + " TextureCoordinate:" + this.textureCoordinate
				+ "}}";
	}

	/**
	 * Compares whether two {@link VertexPositionColorTexture} instances are equal.
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
		return this.equals((VertexPositionColorTexture) obj);
	}
	
	// Helper method
	private boolean equals(VertexPositionColorTexture other)
	{
		return ((((this.position.equals(other.position)) && //
				  (this.color.equals(other.color))) && //
				  (this.textureCoordinate.equals(other.textureCoordinate))));
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

	static
	{
		VertexElement[] elements = new VertexElement[] {
				new VertexElement(0, VertexElementFormat.Vector3, VertexElementUsage.Position, 0),
				new VertexElement(12, VertexElementFormat.Color, VertexElementUsage.Color, 0),
				new VertexElement(16, VertexElementFormat.Vector2, VertexElementUsage.TextureCoordinate, 0) };
		vertexDeclaration = new VertexDeclaration(elements);
	}
}
