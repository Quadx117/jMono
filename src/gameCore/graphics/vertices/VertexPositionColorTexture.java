package gameCore.graphics.vertices;

import gameCore.Color;
import gameCore.math.Vector2;
import gameCore.math.Vector3;

public class VertexPositionColorTexture implements IVertexType {

	public Vector3 position;
	public Color color;
	public Vector2 textureCoordinate;
	public static VertexDeclaration vertexDeclaration;

	public VertexPositionColorTexture(Vector3 position, Color color, Vector2 textureCoordinate) {
		this.position = position;
		this.color = color;
		this.textureCoordinate = textureCoordinate;
	}

	// TODO: I added this conveniance constructor (see SpriteBatchItem)
	public VertexPositionColorTexture() {
		this(new Vector3(), new Color(Color.White.getPackedValue()), new Vector2());
	}
	
	public VertexDeclaration getVertexDeclaration() {
		return vertexDeclaration;
	}

	@Override
	public int hashCode() {
		// TODO: Fix gethashcode
		return 0;
	}

	@Override
	public String toString() {
		return "{{Position:" + this.position + " Color:" + this.color + " TextureCoordinate:" + this.textureCoordinate
				+ "}}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj.getClass() != this.getClass())
			return false;

		VertexPositionColorTexture other = (VertexPositionColorTexture) obj;
		return ((((this.position.equals(other.position)) && (this.color.equals(other.color))) && (this.textureCoordinate
				.equals(other.textureCoordinate))));
	}

	public boolean notEquals(Object obj) {
		return !equals(obj);
	}

	static {
		VertexElement[] elements = new VertexElement[] {
				new VertexElement(0, VertexElementFormat.Vector3, VertexElementUsage.Position, 0),
				new VertexElement(12, VertexElementFormat.Color, VertexElementUsage.Color, 0),
				new VertexElement(16, VertexElementFormat.Vector2, VertexElementUsage.TextureCoordinate, 0) };
		vertexDeclaration = new VertexDeclaration(elements);
	}
}
