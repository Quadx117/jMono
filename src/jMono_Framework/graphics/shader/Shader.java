package jMono_Framework.graphics.shader;

import jMono_Framework.Color;
import jMono_Framework.dotNet.io.BinaryReader;
import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.GraphicsResource;
import jMono_Framework.graphics.states.SamplerState;
import jMono_Framework.graphics.states.TextureAddressMode;
import jMono_Framework.graphics.states.TextureFilter;

import java.util.HashMap;
import java.util.Map;

enum SamplerType
{
	Sampler2D,			//
	SamplerCube,		//
	SamplerVolume,		//
	Sampler1D;			//

	private static Map<Integer, SamplerType> map = new HashMap<Integer, SamplerType>();

	static
	{
		for (SamplerType samplerType : SamplerType.values())
		{
			map.put(samplerType.ordinal(), samplerType);
		}
	}

	public static SamplerType valueOf(int samplerType)
	{
		return map.get(samplerType);
	}
}

public class Shader extends GraphicsResource
{

	// TODO: We should convert the sampler info below
	// into the start of a Shader reflection API.

	// C# struct
	public class SamplerInfo
	{
		public SamplerType type;
		public int textureSlot;
		public int samplerSlot;
		public String name;
		public SamplerState state;

		// TODO: This should be moved to EffectPass.
		public int parameter;
	}

	/**
	 * A hash value which can be used to compare shaders.
	 */
	private int hashKey;
	protected int getHashKey() { return hashKey; }

	private SamplerInfo[] samplers;
	public SamplerInfo[] getSamplers() { return samplers; }

	private int[] CBuffers;
	public int[] getCBuffers() { return CBuffers; }

	private ShaderStage stage;
	public ShaderStage getStage() { return stage; }

	public Shader(GraphicsDevice device, BinaryReader reader)
	{
		setGraphicsDevice(device);

		boolean isVertexShader = reader.readBoolean();
		stage = isVertexShader ? ShaderStage.Vertex : ShaderStage.Pixel;

		int shaderLength = reader.readInt32();
		byte[] shaderBytecode = reader.readBytes(shaderLength);

		int samplerCount = (int) reader.readByte();
		samplers = new SamplerInfo[samplerCount];
		for (int s = 0; s < samplerCount; ++s)
		{
			samplers[s] = new SamplerInfo();
			samplers[s].type = SamplerType.valueOf(reader.readByte());
			samplers[s].textureSlot = reader.readByte();
			samplers[s].samplerSlot = reader.readByte();

			if (reader.readBoolean())
			{
				samplers[s].state = new SamplerState();
				samplers[s].state.setAddressU(TextureAddressMode.valueOf(reader.readByte()));
				samplers[s].state.setAddressV(TextureAddressMode.valueOf(reader.readByte()));
				samplers[s].state.setAddressW(TextureAddressMode.valueOf(reader.readByte()));
				samplers[s].state.setBorderColor(new Color(
						reader.readByte(),	//
						reader.readByte(),	//
						reader.readByte(),	//
						reader.readByte()));
				samplers[s].state.setFilter(TextureFilter.valueOf(reader.readByte()));
				samplers[s].state.setMaxAnisotropy(reader.readInt32());
				samplers[s].state.setMaxMipLevel(reader.readInt32());
				samplers[s].state.setMipMapLevelOfDetailBias(reader.readSingle());
			}

// #if OPENGL
			// Samplers[s].name = reader.ReadString();
// #else
			samplers[s].name = null;
// #endif
			samplers[s].parameter = reader.readByte();
		}

		int cbufferCount = (int) reader.readByte();
		CBuffers = new int[cbufferCount];
		for (int c = 0; c < cbufferCount; ++c)
			CBuffers[c] = reader.readByte();

		// TODO: see other Shader files
		// PlatformConstruct(reader, isVertexShader, shaderBytecode);
	}

	@Override
	protected void graphicsDeviceResetting()
	{
		// TODO: see other Shader files
		// PlatformGraphicsDeviceResetting();
	}
}
