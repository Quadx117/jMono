package gameCore.graphics.vertices;

import java.nio.charset.Charset;

import gameCore.graphics.GraphicsResource;

public class VertexDeclaration extends GraphicsResource {

	private VertexElement[] _elements;
	private int _vertexStride;

	// / <summary>
	// / A hash value which can be used to compare declarations.
	// / </summary>
	private int _hashKey;

	protected int getHashKey() {
		return _hashKey;
	}

	// TODO: Do I need an Ellipsis (...) here
	public VertexDeclaration(VertexElement[] elements) {
		this(getVertexStride(elements), elements);
	}

	// TODO: Do I need an Ellipsis (...) here
	public VertexDeclaration(int vertexStride, VertexElement[] elements) {
		if ((elements == null) || (elements.length == 0))
			throw new NullPointerException("elements: Elements cannot be empty");

		VertexElement[] elementArray = (VertexElement[]) elements.clone();
		_elements = elementArray;
		_vertexStride = vertexStride;

		// TODO: Is there a faster/better way to generate a
		// unique hashkey for the same vertex layouts?
		{
			StringBuilder signature = new StringBuilder();
			for (VertexElement element : _elements)
				signature.append(element);

			byte bytes[] = signature.toString().getBytes(Charset.forName("UTF-8"));
			_hashKey = gameCore.utilities.Hash.computeHash(bytes);
		}
	}

	private static int getVertexStride(VertexElement[] elements) {
		int max = 0;
		for (int i = 0; i < elements.length; ++i) {
			int start = elements[i].getOffset() + getSize(elements[i].getVertexElementFormat()); // elements[i].getVertexElementFormat().getSize();
			if (max < start)
				max = start;
		}

		return max;
	}

	// TODO: Is it usefull in Java ?  Should look into that at some point in time.
	public static int getSize(VertexElementFormat elementFormat)
    {
        switch (elementFormat)
        {
            case Single:
                return 4;

            case Vector2:
                return 8;

            case Vector3:
                return 12;

            case Vector4:
                return 16;

            case Color:
                return 4;

            case Byte4:
                return 4;

            case Short2:
                return 4;

            case Short4:
                return 8;

            case NormalizedShort2:
                return 4;

            case NormalizedShort4:
                return 8;

            case HalfVector2:
                return 4;

            case HalfVector4:
                return 8;
        }
        return 0;
    }
	
	// / <summary>
	// / Returns the VertexDeclaration for Type.
	// / </summary>
	// / <param name="vertexType">A value type which implements the IVertexType interface.</param>
	// / <returns>The VertexDeclaration.</returns>
	// / <remarks>
	// / Prefer to use VertexDeclarationCache when the declaration lookup
	// / can be performed with a templated type.
	// / </remarks>
	protected static VertexDeclaration fromType(Class<?> vertexType) {
		if (vertexType == null)
			throw new NullPointerException("vertexType: Cannot be null");

		if (!gameCore.utilities.ReflectionHelpers.isValueType(vertexType)) {
			throw new IllegalArgumentException("vertexType: Must be value type");
		}

		IVertexType type = null;
		try {
			type = (IVertexType) Class.forName(vertexType.getName()).newInstance();
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}// Activator.CreateInstance(vertexType);
		if (type == null) {
			throw new IllegalArgumentException("vertexData does not inherit IVertexType");
		}

		VertexDeclaration vertexDeclaration = type.getVertexDeclaration();
		if (vertexDeclaration == null) {
			throw new NullPointerException("VertexDeclaration cannot be null");
		}

		return vertexDeclaration;
	}

	public VertexElement[] getVertexElements() {
		return (VertexElement[]) _elements.clone();
	}

	public int getVertexStride() {
		return _vertexStride;
	}
}
