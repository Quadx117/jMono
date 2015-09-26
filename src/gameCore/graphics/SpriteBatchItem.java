package gameCore.graphics;

import gameCore.Color;
import gameCore.graphics.vertices.VertexPositionColorTexture;
import gameCore.math.Vector2;

public class SpriteBatchItem
{
	public Texture2D texture;
	public float depth;

	public VertexPositionColorTexture vertexTL;
	public VertexPositionColorTexture vertexTR;
	public VertexPositionColorTexture vertexBL;
	public VertexPositionColorTexture vertexBR;

	public SpriteBatchItem()
	{
		vertexTL = new VertexPositionColorTexture();
		vertexTR = new VertexPositionColorTexture();
		vertexBL = new VertexPositionColorTexture();
		vertexBR = new VertexPositionColorTexture();
	}

	public void set(float x, float y, float w, float h, Color color, Vector2 texCoordTL, Vector2 texCoordBR)
	{
		vertexTL.position.x = x;
		vertexTL.position.y = y;
		vertexTL.position.z = depth;
		vertexTL.color = color;
		vertexTL.textureCoordinate.x = texCoordTL.x;
		vertexTL.textureCoordinate.y = texCoordTL.y;

		vertexTR.position.x = x + w;
		vertexTR.position.y = y;
		vertexTR.position.z = depth;
		vertexTR.color = color;
		vertexTR.textureCoordinate.x = texCoordBR.x;
		vertexTR.textureCoordinate.y = texCoordTL.y;

		vertexBL.position.x = x;
		vertexBL.position.y = y + h;
		vertexBL.position.z = depth;
		vertexBL.color = color;
		vertexBL.textureCoordinate.x = texCoordTL.x;
		vertexBL.textureCoordinate.y = texCoordBR.y;

		vertexBR.position.x = x + w;
		vertexBR.position.y = y + h;
		vertexBR.position.z = depth;
		vertexBR.color = color;
		vertexBR.textureCoordinate.x = texCoordBR.x;
		vertexBR.textureCoordinate.y = texCoordBR.y;
	}

	public void set(float x, float y, float dx, float dy, float w, float h, float sin, float cos, Color color,
			Vector2 texCoordTL, Vector2 texCoordBR)
	{
		// TODO, Should we be just assigning the Depth Value to Z?
		// According to
		// http://blogs.msdn.com/b/shawnhar/archive/2011/01/12/spritebatch-billboards-in-a-3d-world.aspx
		// We do.
		vertexTL.position.x = x + dx * cos - dy * sin;
		vertexTL.position.y = y + dx * sin + dy * cos;
		vertexTL.position.z = depth;
		vertexTL.color = color;
		vertexTL.textureCoordinate.x = texCoordTL.x;
		vertexTL.textureCoordinate.y = texCoordTL.y;

		vertexTR.position.x = x + (dx + w) * cos - dy * sin;
		vertexTR.position.y = y + (dx + w) * sin + dy * cos;
		vertexTR.position.z = depth;
		vertexTR.color = color;
		vertexTR.textureCoordinate.x = texCoordBR.x;
		vertexTR.textureCoordinate.y = texCoordTL.y;

		vertexBL.position.x = x + dx * cos - (dy + h) * sin;
		vertexBL.position.y = y + dx * sin + (dy + h) * cos;
		vertexBL.position.z = depth;
		vertexBL.color = color;
		vertexBL.textureCoordinate.x = texCoordTL.x;
		vertexBL.textureCoordinate.y = texCoordBR.y;

		vertexBR.position.x = x + (dx + w) * cos - (dy + h) * sin;
		vertexBR.position.y = y + (dx + w) * sin + (dy + h) * cos;
		vertexBR.position.z = depth;
		vertexBR.color = color;
		vertexBR.textureCoordinate.x = texCoordBR.x;
		vertexBR.textureCoordinate.y = texCoordBR.y;
	}
}
