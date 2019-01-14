package jMono_Framework.graphics.effect;

import jMono_Framework.graphics.Texture;
import jMono_Framework.graphics.Texture2D;
import jMono_Framework.graphics.Texture3D;
import jMono_Framework.graphics.TextureCube;
import jMono_Framework.math.Matrix;
import jMono_Framework.math.Quaternion;
import jMono_Framework.math.Vector2;
import jMono_Framework.math.Vector3;
import jMono_Framework.math.Vector4;
import jMono_Framework.utilities.StringHelpers;

import java.lang.reflect.Array;

public class EffectParameter
{
	/**
	 * The next state key used when an effect parameter is updated by any of the 'set' methods.
	 */
	protected static long NextStateKey;
	public static long getNextStateKey() { return NextStateKey; }

	protected EffectParameter(EffectParameterClass class_,
							  EffectParameterType type,
							  String name,
							  int rowCount,
							  int columnCount,
							  String semantic,
							  EffectAnnotationCollection annotations,
							  EffectParameterCollection elements,
							  EffectParameterCollection structMembers,
							  Object data)
	{
		this.parameterClass = class_;
		this.parameterType = type;

		this.name = name;
		this.semantic = semantic;
		this.annotations = annotations;

		this.rowCount = rowCount;
		this.columnCount = columnCount;

		this.elements = elements;
		this.structureMembers = structMembers;

		this.data = data;
		this.stateKey = ++NextStateKey;
	}

	protected EffectParameter(EffectParameter cloneSource)
	{
		// Share all the immutable types.
		parameterClass = cloneSource.parameterClass;
		parameterType = cloneSource.parameterType;
		name = cloneSource.name;
		semantic = cloneSource.semantic;
		annotations = cloneSource.annotations;
		rowCount = cloneSource.rowCount;
		columnCount = cloneSource.columnCount;

		// Clone the mutable types.
		elements = cloneSource.elements.clone();
		structureMembers = cloneSource.structureMembers.clone();

		// The data is mutable, so we have to clone it.
		// TODO: should look into that again (see As.java and Constantbuffer.java)
		// NOTE: Equivalent to as Array in C#
		Object array = null;
		if (cloneSource.data != null)
		{
			switch (cloneSource.parameterType)
			{
				case Boolean:
				case Integer:
				{
					int length = Array.getLength(cloneSource.data);
					int[] buffer = new int[length];
					for (int i = 0; i < length; ++i)
					{
						buffer[i] = (int) Array.get(cloneSource.data, i);
					}
					array = buffer;
					break;
				}

				case Single:
				{
					int length = Array.getLength(cloneSource.data);
					float[] buffer = new float[length];
					for (int i = 0; i < length; ++i)
					{
						buffer[i] = (float) Array.get(cloneSource.data, i);
					}
					array = buffer;
					break;
				}

				case String:
					// TODO: We have not investigated what a string
					// type should do in the parameter list. Till then
					// throw to let the user know.
					throw new UnsupportedOperationException();

				default:
					// NOTE: We skip over all other types
					break;
			}
		}
		if (array != null)
			data = array;
		// Data = Arrays.copyOf(array, array.length);
		stateKey = ++NextStateKey;
	}

	private String name;
	public String getName() { return name; }

	private String semantic;
	public String getSemantic() { return semantic; }

	private EffectParameterClass parameterClass;
	public EffectParameterClass getParameterClass() { return parameterClass; }

	private EffectParameterType parameterType;
	public EffectParameterType getParameterType() { return parameterType; }

	private int rowCount;
	public int getRowCount() { return rowCount; }

	private int columnCount;
	public int getColumnCount() { return columnCount; }

	private EffectParameterCollection elements;
	public EffectParameterCollection getElements() 	{ return elements; }

	private EffectParameterCollection structureMembers;
	public EffectParameterCollection getStructureMembers() { return structureMembers; }

	private EffectAnnotationCollection annotations;
	public EffectAnnotationCollection getAnnotations() { return annotations; }

	// TODO: Using object adds alot of boxing/unboxing overhead
	// and garbage generation. We should consider a templated
	// type implementation to fix this!

	private Object data;
	public Object getData() { return data; }

	private long stateKey;

	/**
	 * The current state key which is used to detect if the parameter value has been changed.
	 * 
	 * @return The current state key.
	 */
	public long getStateKey()
	{
		return stateKey;
	}

	/**
	 * Property referenced by the DebuggerDisplayAttribute.
	 * 
	 * @return A string containing debugging information
	 */
	public String getDebugDisplayString()
	{
		String semanticStr = new String();
		if (!StringHelpers.isNullOrEmpty(semantic))
			semanticStr = " <" + semantic + ">";

		String valueStr;
		if (data == null)
			valueStr = "(null)";
		else
		{
			switch (parameterClass)
			{
			// Object types are stored directly in the Data property.
			// Display Data's string value.
				case Object:
					valueStr = data.toString();
					break;

				// Matrix types are stored in a float[16] which we don't really have room for.
				// Display "...".
				case Matrix:
					valueStr = "...";
					break;

				// Scalar types are stored as a float[1].
				// Display the first (and only) element's string value.
				case Scalar:
					// valueStr = (Data as Array).GetValue(0).ToString();
					valueStr = ((Object[]) data)[0].toString();
					break;

				// Vector types are stored as an Array<Type>.
				// Display the string value of each array element.
				case Vector:
					// var array = Data as Array;
					Object[] array = (Object[]) data;
					String[] arrayStr = new String[array.length];
					int idx = 0;
					for (@SuppressWarnings("unused")
					Object e : array)
					{
						arrayStr[idx] = array[idx].toString();
						++idx;
					}

					valueStr = " " + arrayStr;
					break;

				// Handle additional cases here...
				default:
					valueStr = data.toString();
					break;
			}
		}

		return ("[" + parameterClass + " " + parameterType + "]" + semanticStr + " " + name + " : " + valueStr);
	}

	public boolean getValueBoolean()
	{
		if (parameterClass != EffectParameterClass.Scalar || parameterType != EffectParameterType.Boolean)
			throw new ClassCastException();

// #if DIRECTX
		// return ((int[])Data)[0] != 0;
// #else

		// MojoShader encodes even booleans into a float.
		return ((float[]) data)[0] != 0.0f;
// #endif
	}

	// NOTE: This was already commented in the original
	/*
	 * public boolean[] getValueBooleanArray ()
	 * {
	 * throw new UnsupportedOperationException();
	 * }
	 */

	public int getValueInt()
	{
		if (parameterClass != EffectParameterClass.Scalar || parameterType != EffectParameterType.Integer)
			throw new ClassCastException();

// #if DIRECTX
		return ((int[]) data)[0];
// #else
		// MojoShader encodes integers into a float.
		// return (int) ((float[]) Data)[0];
// #endif
	}

	// NOTE: This was already commented in the original
	/*
	 * public int[] getValueInt32Array ()
	 * {
	 * throw new UnsupportedOperationException();
	 * }
	 */

	public Matrix getValueMatrix()
	{
		if (parameterClass != EffectParameterClass.Matrix || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		if (rowCount != 4 || columnCount != 4)
			throw new ClassCastException();

		float[] floatData = (float[]) data;

		return new Matrix(floatData[0], floatData[4], floatData[8], floatData[12],
						  floatData[1], floatData[5], floatData[9], floatData[13],
						  floatData[2], floatData[6], floatData[10], floatData[14],
						  floatData[3],	floatData[7], floatData[11], floatData[15]);
	}

	public Matrix[] getValueMatrixArray(int count)
	{
		if (parameterClass != EffectParameterClass.Matrix || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		Matrix[] ret = new Matrix[count];
		for (int i = 0; i < count; ++i)
			ret[i] = elements.getEffectParameter(i).getValueMatrix();

		return ret;
	}

	public Quaternion getValueQuaternion()
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		float[] vecInfo = (float[]) data;
		return new Quaternion(vecInfo[0], vecInfo[1], vecInfo[2], vecInfo[3]);
	}

	// NOTE: This was already commented in the original
	/*
	 * public Quaternion[] getValueQuaternionArray ()
	 * {
	 * throw new UnsupportedOperationException();
	 * }
	 */

	public float getValueFloat()
	{
		// TODO: Should this fetch int and boolean as a float?
		if (parameterClass != EffectParameterClass.Scalar || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		return ((float[]) data)[0];
	}

	public float[] getValueFloatArray()
	{
		if (elements != null && elements.getCount() > 0)
		{
			float[] ret = new float[rowCount * columnCount * elements.getCount()];
			for (int i = 0; i < elements.getCount(); ++i)
			{
				float[] elmArray = elements.getEffectParameter(i).getValueFloatArray();
				for (int j = 0; j < elmArray.length; ++j)
					ret[rowCount * columnCount * i + j] = elmArray[j];
			}
			return ret;
		}

		switch (parameterClass)
		{
			case Scalar:
				return new float[] { getValueFloat() };
			case Vector:
			case Matrix:
				if (data instanceof Matrix)
					return Matrix.toFloatArray((Matrix) data);
				else
					return (float[]) data;
			default:
				throw new UnsupportedOperationException();
		}
	}

	public String getValueString()
	{
		if (parameterClass != EffectParameterClass.Object || parameterType != EffectParameterType.String)
			throw new ClassCastException();

		return ((String[]) data)[0];
	}

	public Texture2D getValueTexture2D()
	{
		if (parameterClass != EffectParameterClass.Object || parameterType != EffectParameterType.Texture2D)
			throw new ClassCastException();

		return (Texture2D) data;
	}

// #if !GLES
	public Texture3D getValueTexture3D()
	{
		if (parameterClass != EffectParameterClass.Object || parameterType != EffectParameterType.Texture3D)
			throw new ClassCastException();

		return (Texture3D) data;
	}
// #endif

	public TextureCube getValueTextureCube()
	{
		if (parameterClass != EffectParameterClass.Object || parameterType != EffectParameterType.TextureCube)
			throw new ClassCastException();

		return (TextureCube) data;
	}

	public Vector2 getValueVector2()
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		float[] vecInfo = (float[]) data;
		return new Vector2(vecInfo[0], vecInfo[1]);
	}

	public Vector2[] getValueVector2Array()
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();
		if (elements != null && elements.getCount() > 0)
		{
			Vector2[] result = new Vector2[elements.getCount()];
			for (int i = 0; i < elements.getCount(); ++i)
			{
				float[] v = elements.getEffectParameter(i).getValueFloatArray();
				result[i] = new Vector2(v[0], v[1]);
			}
			return result;
		}

		return null;
	}

	public Vector3 getValueVector3()
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		float[] vecInfo = (float[]) data;
		return new Vector3(vecInfo[0], vecInfo[1], vecInfo[2]);
	}

	public Vector3[] getValueVector3Array()
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		if (elements != null && elements.getCount() > 0)
		{
			Vector3[] result = new Vector3[elements.getCount()];
			for (int i = 0; i < elements.getCount(); ++i)
			{
				float[] v = elements.getEffectParameter(i).getValueFloatArray();
				result[i] = new Vector3(v[0], v[1], v[2]);
			}
			return result;
		}
		return null;
	}

	public Vector4 getValueVector4()
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		float[] vecInfo = (float[]) data;
		return new Vector4(vecInfo[0], vecInfo[1], vecInfo[2], vecInfo[3]);
	}

	public Vector4[] getValueVector4Array()
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		if (elements != null && elements.getCount() > 0)
		{
			Vector4[] result = new Vector4[elements.getCount()];
			for (int i = 0; i < elements.getCount(); ++i)
			{
				float[] v = elements.getEffectParameter(i).getValueFloatArray();
				result[i] = new Vector4(v[0], v[1], v[2], v[3]);
			}
			return result;
		}
		return null;
	}

	public void setValue(boolean value)
	{
		if (parameterClass != EffectParameterClass.Scalar || parameterType != EffectParameterType.Boolean)
			throw new ClassCastException();

// #if DIRECTX
		// We store the boolean as an integer as that
		// is what the constant buffers expect.
		((int[]) data)[0] = value ? 1 : 0;
// #else
		// MojoShader encodes even booleans into a float.
		// ((float[]) Data)[0] = value ? 1 : 0;
// #endif
		stateKey = ++NextStateKey;
	}

	// NOTE: This was already commented in the original
	/*
	 * public void setValue (bool[] value)
	 * {
	 * throw new UnsupportedOperationException();
	 * }
	 */

	public void setValue(int value)
	{
		if (parameterClass != EffectParameterClass.Scalar || parameterType != EffectParameterType.Integer)
			throw new ClassCastException();

// #if DIRECTX
		((int[]) data)[0] = value;
// #else
		// MojoShader encodes integers into a float.
		// ((float[]) Data)[0] = value;
// #endif
		stateKey = ++NextStateKey;
	}

	// NOTE: This was already commented in the original
	/*
	 * public void setValue (int[] value)
	 * {
	 * throw new UnsupportedOperationException();
	 * }
	 */

	public void setValue(Matrix value)
	{
		if (parameterClass != EffectParameterClass.Matrix || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		// HLSL expects matrices to be transposed by default.
		// These unrolled loops do the transpose during assignment.
		if (rowCount == 4 && columnCount == 4)
		{
			float[] fData = (float[]) data;

			fData[0] = value.m11;
			fData[1] = value.m21;
			fData[2] = value.m31;
			fData[3] = value.m41;

			fData[4] = value.m12;
			fData[5] = value.m22;
			fData[6] = value.m32;
			fData[7] = value.m42;

			fData[8] = value.m13;
			fData[9] = value.m23;
			fData[10] = value.m33;
			fData[11] = value.m43;

			fData[12] = value.m14;
			fData[13] = value.m24;
			fData[14] = value.m34;
			fData[15] = value.m44;
		}
		else if (rowCount == 4 && columnCount == 3)
		{
			float[] fData = (float[]) data;

			fData[0] = value.m11;
			fData[1] = value.m21;
			fData[2] = value.m31;
			fData[3] = value.m41;

			fData[4] = value.m12;
			fData[5] = value.m22;
			fData[6] = value.m32;
			fData[7] = value.m42;

			fData[8] = value.m13;
			fData[9] = value.m23;
			fData[10] = value.m33;
			fData[11] = value.m43;
		}
		else if (rowCount == 3 && columnCount == 4)
		{
			float[] fData = (float[]) data;

			fData[0] = value.m11;
			fData[1] = value.m21;
			fData[2] = value.m31;

			fData[3] = value.m12;
			fData[4] = value.m22;
			fData[5] = value.m32;

			fData[6] = value.m13;
			fData[7] = value.m23;
			fData[8] = value.m33;

			fData[9] = value.m14;
			fData[10] = value.m24;
			fData[11] = value.m34;
		}
		else if (rowCount == 3 && columnCount == 3)
		{
			float[] fData = (float[]) data;

			fData[0] = value.m11;
			fData[1] = value.m21;
			fData[2] = value.m31;

			fData[3] = value.m12;
			fData[4] = value.m22;
			fData[5] = value.m32;

			fData[6] = value.m13;
			fData[7] = value.m23;
			fData[8] = value.m33;
		}
		else if (rowCount == 3 && columnCount == 2)
		{
			float[] fData = (float[]) data;

			fData[0] = value.m11;
			fData[1] = value.m21;
			fData[2] = value.m31;

			fData[3] = value.m12;
			fData[4] = value.m22;
			fData[5] = value.m32;
		}

		stateKey = ++NextStateKey;
	}

	public void setValueTranspose(Matrix value)
	{
		if (parameterClass != EffectParameterClass.Matrix || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		// HLSL expects matrices to be transposed by default, so copying them straight
		// from the in-memory version effectively transposes them back to row-major.
		if (rowCount == 4 && columnCount == 4)
		{
			float[] fData = (float[]) data;

			fData[0] = value.m11;
			fData[1] = value.m12;
			fData[2] = value.m13;
			fData[3] = value.m14;

			fData[4] = value.m21;
			fData[5] = value.m22;
			fData[6] = value.m23;
			fData[7] = value.m24;

			fData[8] = value.m31;
			fData[9] = value.m32;
			fData[10] = value.m33;
			fData[11] = value.m34;

			fData[12] = value.m41;
			fData[13] = value.m42;
			fData[14] = value.m43;
			fData[15] = value.m44;
		}
		else if (rowCount == 4 && columnCount == 3)
		{
			float[] fData = (float[]) data;

			fData[0] = value.m11;
			fData[1] = value.m12;
			fData[2] = value.m13;

			fData[3] = value.m21;
			fData[4] = value.m22;
			fData[5] = value.m23;

			fData[6] = value.m31;
			fData[7] = value.m32;
			fData[8] = value.m33;

			fData[9] = value.m41;
			fData[10] = value.m42;
			fData[11] = value.m43;
		}
		else if (rowCount == 3 && columnCount == 4)
		{
			float[] fData = (float[]) data;

			fData[0] = value.m11;
			fData[1] = value.m12;
			fData[2] = value.m13;
			fData[3] = value.m14;

			fData[4] = value.m21;
			fData[5] = value.m22;
			fData[6] = value.m23;
			fData[7] = value.m24;

			fData[8] = value.m31;
			fData[9] = value.m32;
			fData[10] = value.m33;
			fData[11] = value.m34;
		}
		else if (rowCount == 3 && columnCount == 3)
		{
			float[] fData = (float[]) data;

			fData[0] = value.m11;
			fData[1] = value.m12;
			fData[2] = value.m13;

			fData[3] = value.m21;
			fData[4] = value.m22;
			fData[5] = value.m23;

			fData[6] = value.m31;
			fData[7] = value.m32;
			fData[8] = value.m33;
		}
		else if (rowCount == 3 && columnCount == 2)
		{
			float[] fData = (float[]) data;

			fData[0] = value.m11;
			fData[1] = value.m12;
			fData[2] = value.m13;

			fData[3] = value.m21;
			fData[4] = value.m22;
			fData[5] = value.m23;
		}

		stateKey = ++NextStateKey;
	}

	public void setValue(Matrix[] value)
	{
		if (parameterClass != EffectParameterClass.Matrix || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		if (rowCount == 4 && columnCount == 4)
		{
			for (int i = 0; i < value.length; ++i)
			{
				float[] fData = (float[]) elements.getEffectParameter(i).data;

				fData[0] = value[i].m11;
				fData[1] = value[i].m21;
				fData[2] = value[i].m31;
				fData[3] = value[i].m41;

				fData[4] = value[i].m12;
				fData[5] = value[i].m22;
				fData[6] = value[i].m32;
				fData[7] = value[i].m42;

				fData[8] = value[i].m13;
				fData[9] = value[i].m23;
				fData[10] = value[i].m33;
				fData[11] = value[i].m43;

				fData[12] = value[i].m14;
				fData[13] = value[i].m24;
				fData[14] = value[i].m34;
				fData[15] = value[i].m44;
			}
		}
		else if (rowCount == 4 && columnCount == 3)
		{
			for (int i = 0; i < value.length; ++i)
			{
				float[] fData = (float[]) elements.getEffectParameter(i).data;

				fData[0] = value[i].m11;
				fData[1] = value[i].m21;
				fData[2] = value[i].m31;
				fData[3] = value[i].m41;

				fData[4] = value[i].m12;
				fData[5] = value[i].m22;
				fData[6] = value[i].m32;
				fData[7] = value[i].m42;

				fData[8] = value[i].m13;
				fData[9] = value[i].m23;
				fData[10] = value[i].m33;
				fData[11] = value[i].m43;
			}
		}
		else if (rowCount == 3 && columnCount == 4)
		{
			for (int i = 0; i < value.length; ++i)
			{
				float[] fData = (float[]) elements.getEffectParameter(i).data;

				fData[0] = value[i].m11;
				fData[1] = value[i].m21;
				fData[2] = value[i].m31;

				fData[3] = value[i].m12;
				fData[4] = value[i].m22;
				fData[5] = value[i].m32;

				fData[6] = value[i].m13;
				fData[7] = value[i].m23;
				fData[8] = value[i].m33;

				fData[9] = value[i].m14;
				fData[10] = value[i].m24;
				fData[11] = value[i].m34;
			}
		}
		else if (rowCount == 3 && columnCount == 3)
		{
			for (int i = 0; i < value.length; ++i)
			{
				float[] fData = (float[]) elements.getEffectParameter(i).data;

				fData[0] = value[i].m11;
				fData[1] = value[i].m21;
				fData[2] = value[i].m31;

				fData[3] = value[i].m12;
				fData[4] = value[i].m22;
				fData[5] = value[i].m32;

				fData[6] = value[i].m13;
				fData[7] = value[i].m23;
				fData[8] = value[i].m33;
			}
		}
		else if (rowCount == 3 && columnCount == 2)
		{
			for (int i = 0; i < value.length; ++i)
			{
				float[] fData = (float[]) elements.getEffectParameter(i).data;

				fData[0] = value[i].m11;
				fData[1] = value[i].m21;
				fData[2] = value[i].m31;

				fData[3] = value[i].m12;
				fData[4] = value[i].m22;
				fData[5] = value[i].m32;
			}
		}

		stateKey = ++NextStateKey;
	}

	public void setValue(Quaternion value)
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		float[] fData = (float[]) data;
		fData[0] = value.x;
		fData[1] = value.y;
		fData[2] = value.z;
		fData[3] = value.w;
		stateKey = ++NextStateKey;
	}

	// NOTE: This was already commented in the original
	/*
	 * public void setValue (Quaternion[] value)
	 * {
	 * throw new UnsupportedOperationException();
	 * }
	 */

	public void setValue(float value)
	{
		if (parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		((float[]) data)[0] = value;
		stateKey = ++NextStateKey;
	}

	public void setValue(float[] value)
	{
		for (int i = 0; i < value.length; ++i)
			elements.getEffectParameter(i).setValue(value[i]);

		stateKey = ++NextStateKey;
	}

	// NOTE: This was already commented in the original
	/*
	 * public void setValue (string value)
	 * {
	 * throw new UnsupportedOperationException();
	 * }
	 */

	public void setValue(Texture value)
	{
		if (this.parameterType != EffectParameterType.Texture &&
				this.parameterType != EffectParameterType.Texture1D &&
				this.parameterType != EffectParameterType.Texture2D &&
				this.parameterType != EffectParameterType.Texture3D &&
				this.parameterType != EffectParameterType.TextureCube)
		{
			throw new ClassCastException();
		}

		data = value;
		stateKey = ++NextStateKey;
	}

	public void setValue(Vector2 value)
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		float[] fData = (float[]) data;
		fData[0] = value.x;
		fData[1] = value.y;
		stateKey = ++NextStateKey;
	}

	public void setValue(Vector2[] value)
	{
		for (int i = 0; i < value.length; ++i)
			elements.getEffectParameter(i).setValue(value[i]);
		stateKey = ++NextStateKey;
	}

	public void setValue(Vector3 value)
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		float[] fData = (float[]) data;
		fData[0] = value.x;
		fData[1] = value.y;
		fData[2] = value.z;
		stateKey = ++NextStateKey;
	}

	public void setValue(Vector3[] value)
	{
		for (int i = 0; i < value.length; ++i)
			elements.getEffectParameter(i).setValue(value[i]);
		stateKey = ++NextStateKey;
	}

	public void setValue(Vector4 value)
	{
		if (parameterClass != EffectParameterClass.Vector || parameterType != EffectParameterType.Single)
			throw new ClassCastException();

		float[] fData = (float[]) data;
		fData[0] = value.x;
		fData[1] = value.y;
		fData[2] = value.z;
		fData[3] = value.w;
		stateKey = ++NextStateKey;
	}

	public void SetValue(Vector4[] value)
	{
		for (int i = 0; i < value.length; ++i)
			elements.getEffectParameter(i).setValue(value[i]);
		stateKey = ++NextStateKey;
	}

}
