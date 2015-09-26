package gameCore.graphics;

import gameCore.math.Matrix;
import gameCore.math.Vector3;

import java.awt.Rectangle;

/**
 * Describes the view bounds for render-target surface.
 * @author Eric
 *
 */
public class Viewport {

	private int x;
	private int y;
	private int width;
	private int height;
	private float minDepth;
	private float maxDepth;

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int value) {
		width = value;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int value) {
		height = value;
	}

	public float getMaxDepth() {
		return this.maxDepth;
	}

	public void setMaxDepth(float value) {
		maxDepth = value;
	}

	public float getMinDepth() {
		return this.minDepth;
	}

	public void setMinDepth(float value) {
		minDepth = value;
	}

	public int getX() {
		return x;
	}

	public void setX(int value) {
		x = value;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int value) {
		y = value;
	}

	/**
	 * Gets the aspect ratio of this {@link Viewport}, which is width / height.
	 * @return The aspect ratio of this {@link Viewport}
	 */
	public float getAspectRatio() {
		if ((height != 0) && (width != 0)) {
			return (((float) width) / ((float) height));
		}
		return 0f;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
		
	}

	public void setBounds(Rectangle value) {
		x = value.x;
		y = value.y;
		width = value.width;
		height = value.height;
	}

	public Rectangle getTitleSafeArea() {
		return new Rectangle(x, y, width, height);
	}

	public Viewport(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.minDepth = 0.0f;
		this.maxDepth = 1.0f;
	}

	public Viewport(Rectangle bounds) {
		this(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public Vector3 project(Vector3 source, Matrix projection, Matrix view, Matrix world) {
		Matrix matrix = Matrix.multiply(Matrix.multiply(world, view), projection);
		Vector3 vector = Vector3.transform(source, matrix);
		float a = (((source.x * matrix.M14) + (source.y * matrix.M24)) + (source.z * matrix.M34)) + matrix.M44;
		if (!withinEpsilon(a, 1f)) {
			vector.x = vector.x / a;
			vector.y = vector.y / a;
			vector.z = vector.z / a;
		}
		vector.x = (((vector.x + 1f) * 0.5f) * this.width) + this.x;
		vector.y = (((-vector.y + 1f) * 0.5f) * this.height) + this.y;
		vector.z = (vector.z * (this.maxDepth - this.minDepth)) + this.minDepth;
		return vector;
	}

	public Vector3 unproject(Vector3 source, Matrix projection, Matrix view, Matrix world) {
		Matrix matrix = Matrix.invert(Matrix.multiply(Matrix.multiply(world, view), projection));
		source.x = (((source.x - this.x) / ((float) this.width)) * 2f) - 1f;
		source.y = -((((source.y - this.y) / ((float) this.height)) * 2f) - 1f);
		source.z = (source.z - this.minDepth) / (this.maxDepth - this.minDepth);
		Vector3 vector = Vector3.transform(source, matrix);
		float a = (((source.x * matrix.M14) + (source.y * matrix.M24)) + (source.z * matrix.M34)) + matrix.M44;
		if (!withinEpsilon(a, 1f)) {
			vector.x = vector.x / a;
			vector.y = vector.y / a;
			vector.z = vector.z / a;
		}
		return vector;

	}

	private static boolean withinEpsilon(float a, float b) {
		float num = a - b;
		return ((-1.401298E-45f <= num) && (num <= Float.MIN_VALUE));
	}

	@Override
	public String toString() {
		return "{X:" + x + " Y:" + y + " Width:" + width + " Height:" + height + " MinDepth:" + minDepth + " MaxDepth:"
				+ maxDepth + "}";
	}
}
