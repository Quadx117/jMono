package jMono_Framework.graphics.shader;

import jMono_Framework.dotNet.As;
import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.GraphicsResource;
import jMono_Framework.graphics.effect.EffectParameter;
import jMono_Framework.graphics.effect.EffectParameterClass;
import jMono_Framework.graphics.effect.EffectParameterCollection;

public class ConstantBuffer extends GraphicsResource
{
	private byte[] _buffer;

	private int[] _parameters;

	private int[] _offsets;

	private String _name;

	private long _stateKey;

	private boolean _dirty;
	private boolean isDirty()
	{
		return _dirty;
	}

	public ConstantBuffer(ConstantBuffer cloneSource)
	{
		graphicsDevice = cloneSource.getGraphicsDevice();

		// Share the immutable types.
		_name = cloneSource._name;
		_parameters = cloneSource._parameters;
		_offsets = cloneSource._offsets;

		// Clone the mutable types.
		_buffer = (byte[]) cloneSource._buffer.clone();
		// TODO: PlatformInitialize()
		// PlatformInitialize();
	}

	public ConstantBuffer(GraphicsDevice device,
						  int sizeInBytes,
						  int[] parameterIndexes,
						  int[] parameterOffsets,
						  String name)
	{
		graphicsDevice = device;

		_buffer = new byte[sizeInBytes];

		_parameters = parameterIndexes;
		_offsets = parameterOffsets;

		_name = name;

		// TODO: PlatformInitialize()
		// PlatformInitialize();
	}

	public void clear()
	{
		// TODO: PlatformClear()
		// PlatformClear();
	}

	private void setData(int offset, int rows, int columns, Object data)
	{
		// Shader registers are always 4 bytes and all the
		// incoming data objects should be 4 bytes per element.
		final int elementSize = 4;
		final int rowSize = elementSize * 4;

		// NOTE: I added this because I need to convert data to the same type as _buffer for
		// System.arraycopy to work. Otherwise, we get an exception.
		byte[] source = As.floatArrayToByteArray(As.as(data, float[].class));

		// Take care of a single element.
		if (rows == 1 && columns == 1)
		{
			// EffectParameter stores all values in arrays by default.
			if (data.getClass().isArray())
				// Buffer.BlockCopy(data as Array, 0, _buffer, offset, elementSize);
				System.arraycopy(source, 0, _buffer, offset, elementSize);
			else
			{
				// TODO: When we eventually expose the protected Shader
				// API then we will need to deal with non-array elements.
				throw new UnsupportedOperationException("Unimplemented for non array types");
			}
		}

		// Take care of the single copy case!
		else if (rows == 1 || (rows == 4 && columns == 4))
			// Buffer.BlockCopy(data as Array, 0, _buffer, offset, rows*columns*elementSize);
			System.arraycopy(source, 0, _buffer, offset, rows * columns * elementSize);
		else
		{
			// var source = data as Array;

			int stride = (columns * elementSize);
			for (int y = 0; y < rows; ++y)
				// Buffer.BlockCopy(source, stride * y, _buffer, offset + (rowSize * y), columns * elementSize);
				System.arraycopy(source, stride * y, _buffer, offset + (rowSize * y), columns * elementSize);
		}
	}

	private int setParameter(int offset, EffectParameter param)
	{
		final int elementSize = 4;
		final int rowSize = elementSize * 4;

		int rowsUsed = 0;

		EffectParameterCollection elements = param.getElements();
		if (elements.getCount() > 0)
		{
			for (int i = 0; i < elements.getCount(); ++i)
			{
				int rowsUsedSubParam = setParameter(offset, elements.getEffectParameter(i));
				offset += rowsUsedSubParam * rowSize;
				rowsUsed += rowsUsedSubParam;
			}
		}
		else if (param.getData() != null)
		{
			switch (param.getParameterType())
			{
				case Single:
				case Integer:
				case Boolean:
					// HLSL assumes matrices are column-major, whereas in-memory we use row-major.
					// TODO: HLSL can be told to use row-major. We should handle that too.
					if (param.getParameterClass() == EffectParameterClass.Matrix)
					{
						rowsUsed = param.getColumnCount();
						setData(offset, param.getColumnCount(), param.getRowCount(), param.getData());
					}
					else
					{
						rowsUsed = param.getRowCount();
						setData(offset, param.getRowCount(), param.getColumnCount(), param.getData());
					}
					break;
				default:
					throw new UnsupportedOperationException("Not supported!");
			}
		}

		return rowsUsed;
	}

	public void update(EffectParameterCollection parameters)
	{
		// TODO: We should be doing some sort of dirty state
		// testing here.
		//
		// It should let us skip all parameter updates if
		// nothing has changed. It should not be per-parameter
		// as that is why you should use multiple constant
		// buffers.

		// If our state key becomes larger than the
		// next state key then the keys have rolled
		// over and we need to reset.
		if (_stateKey > EffectParameter.getNextStateKey())
			_stateKey = 0;

		for (int p = 0; p < _parameters.length; ++p)
		{
			int index = _parameters[p];
			EffectParameter param = parameters.getEffectParameter(index);

			if (param.getStateKey() < _stateKey)
				continue;

			int offset = _offsets[p];
			_dirty = true;

			setParameter(offset, param);
		}

		_stateKey = EffectParameter.getNextStateKey();
	}
}
