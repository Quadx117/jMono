package jMono_Framework.graphics.shader;

public class ConstantBufferCollection
{
	private ConstantBuffer[] _buffers;

	private ShaderStage _stage;
	public ShaderStage getStage() { return this._stage; }

	@SuppressWarnings("unused")
	private int _valid;

	public ConstantBufferCollection(ShaderStage stage, int maxBuffers)
	{
		_stage = stage;
		_buffers = new ConstantBuffer[maxBuffers];
		_valid = 0;
	}

	public ConstantBuffer getConstantBufferCollection(int index)
	{
		return _buffers[index];
	}

	public void setConstantBufferCollection(int index, ConstantBuffer value)
	{
		if (_buffers[index] == value)
			return;

		if (value != null)
		{
			_buffers[index] = value;
			_valid |= 1 << index;
		}
		else
		{
			_buffers[index] = null;
			_valid &= ~(1 << index);
		}
	}

	public void clear()
	{
		for (int i = 0; i < _buffers.length; ++i)
			_buffers[i] = null;

		_valid = 0;
	}

// #if DIRECTX
	// protected void SetConstantBuffers(GraphicsDevice device)
// #elif WEB
	// protected void SetConstantBuffers(GraphicsDevice device, int shaderProgram)
// #elif OPENGL || PSM
	// protected void SetConstantBuffers(GraphicsDevice device, ShaderProgram shaderProgram)
// #endif
	// {
	// If there are no constant buffers then skip it.
	// if (_valid == 0)
	// return;

	// var valid = _valid;

	// for (var i = 0; i < _buffers.Length; i++)
	// {
	// var buffer = _buffers[i];
	// if (buffer != null && !buffer.IsDisposed)
	// {
// #if DIRECTX
	// buffer.PlatformApply(device, _stage, i);
// #elif OPENGL || PSM || WEB
	// buffer.PlatformApply(device, shaderProgram);
// #endif
	// }

	// Early out if this is the last one.
	// valid &= ~(1 << i);
	// if (valid == 0)
	// return;
	// }
	// }
}
