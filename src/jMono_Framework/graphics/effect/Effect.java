package jMono_Framework.graphics.effect;

import jMono_Framework.Color;
import jMono_Framework.dotNet.BitConverter;
import jMono_Framework.dotNet.io.BinaryReader;
import jMono_Framework.dotNet.io.MemoryStream;
import jMono_Framework.graphics.ColorWriteChannels;
import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.GraphicsResource;
import jMono_Framework.graphics.shader.ConstantBuffer;
import jMono_Framework.graphics.shader.Shader;
import jMono_Framework.graphics.states.Blend;
import jMono_Framework.graphics.states.BlendFunction;
import jMono_Framework.graphics.states.BlendState;
import jMono_Framework.graphics.states.CompareFunction;
import jMono_Framework.graphics.states.CullMode;
import jMono_Framework.graphics.states.DepthStencilState;
import jMono_Framework.graphics.states.FillMode;
import jMono_Framework.graphics.states.RasterizerState;
import jMono_Framework.graphics.states.StencilOperation;
import jMono_Framework.utilities.ByteStreams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class Effect extends GraphicsResource
{
	// C# struct
	class MGFXHeader
	{
		/**
		 * The MonoGame Effect file format header identifier ("MGFX").
		 */
		public final int MGFXSignature = (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))
											? 0x5846474D
											: 0x4D474658;

		/**
		 * The current MonoGame Effect file format versions used to detect old packaged content.
		 * 
		 * <p>
		 * We should avoid supporting old versions for very long if at all as users should be
		 * rebuilding content when packaging their game.
		 */
		public static final int MGFXVersion = 7;

		public int signature;
		public int version;
		public int profile;
		public int effectKey;
		public int headerSize;
	}

	protected EffectParameterCollection parameters;
	public EffectParameterCollection getParameters() { return parameters; }

	private EffectTechniqueCollection techniques;
	public EffectTechniqueCollection getTechniques() { return techniques; }

	public EffectTechnique currentTechnique;

	protected ConstantBuffer[] constantBuffers;
	protected ConstantBuffer[] getConstantBuffers() { return constantBuffers; }

	private Shader[] _shaders;

	private boolean _isClone;

	protected Effect(GraphicsDevice graphicsDevice)
	{
		if (graphicsDevice == null)
			throw new NullPointerException("Graphics Device Cannot Be Null");

		this.setGraphicsDevice(graphicsDevice);
	}

	protected Effect(Effect cloneSource)
	{
		this(cloneSource.getGraphicsDevice());
		_isClone = true;
		clone(cloneSource);
	}

	public Effect(GraphicsDevice graphicsDevice, byte[] effectCode)
	{
		this(graphicsDevice);
		// By default we currently cache all unique byte streams
		// and use cloning to populate the effect with parameters,
		// techniques, and passes.
		//
		// This means all the immutable types in an effect:
		//
		// - Shaders
		// - Annotations
		// - Names
		// - State Objects
		//
		// Are shared for every instance of an effect while the
		// parameter values and constant buffers are copied.
		//
		// This might need to change slightly if/when we support
		// shared constant buffers as 'new' should return unique
		// effects without any shared instance state.

		// Read the header
		MGFXHeader header = readHeader(effectCode);
		int effectKey = header.effectKey;
		int headerSize = header.headerSize;

		// First look for it in the cache.
		Effect cloneSource = graphicsDevice.effectCache.get(header.effectKey);
		if (!graphicsDevice.effectCache.containsKey(header.effectKey))
		{
//			try (ByteArrayInputStream stream = new ByteArrayInputStream(effectCode, headerSize, effectCode.length - headerSize))
			try (MemoryStream stream = new MemoryStream(effectCode, headerSize, effectCode.length - headerSize))
			{
				try (BinaryReader reader = new BinaryReader(stream))
				{
					// Create one.
					cloneSource = new Effect(graphicsDevice);
					cloneSource.readEffect(reader);

					// Cache the effect for later in its original unmodified state.
					graphicsDevice.effectCache.put(effectKey, cloneSource);
				}
			}
		}

		// Clone it.
		_isClone = true;
		clone(cloneSource);
	}

	private MGFXHeader readHeader(byte[] effectCode)
	{
		MGFXHeader header = new MGFXHeader();
		int i = 0;
		header.signature = BitConverter.toInt32(effectCode, i);
		i += 4;
		header.version = (int) effectCode[i++];
		header.profile = (int) effectCode[i++];
		header.effectKey = BitConverter.toInt32(effectCode, i);
		i += 4;
		header.headerSize = i;

		if (header.signature != header.MGFXSignature)
				throw new RuntimeException("This does not appear to be a MonoGame MGFX file!");
		if (header.version < MGFXHeader.MGFXVersion)
				throw new RuntimeException("This MGFX effect is for an older release of MonoGame and needs to be rebuilt.");
		if (header.version > MGFXHeader.MGFXVersion)
				throw new RuntimeException("This MGFX effect seems to be for a newer release of MonoGame.");

// #if DIRECTX
		if (header.profile != 1)
// #else
		// if (header.profile != 0)
				throw new RuntimeException("This MGFX effect was built for a different platform!");
		return header;
	}

	// / <summary>
	// / Clone the source into this existing object.
	// / </summary>
	// / <remarks>
	// / Note this is not overloaded in derived classes on purpose. This is
	// / only a reason this exists is for caching effects.
	// / </remarks>
	// / <param name="cloneSource">The source effect to clone from.</param>
	private void clone(Effect cloneSource)
	{
		assert (_isClone) : "Cannot clone into non-cloned effect!";

		// Copy the mutable members of the effect.
		parameters = cloneSource.parameters.clone();
		techniques = cloneSource.techniques.clone(this);

		// Make a copy of the immutable constant buffers.
		constantBuffers = new ConstantBuffer[cloneSource.constantBuffers.length];
		for (int i = 0; i < cloneSource.constantBuffers.length; ++i)
			constantBuffers[i] = new ConstantBuffer(cloneSource.constantBuffers[i]);

		// Find and set the current technique.
		for (int i = 0; i < cloneSource.techniques.getCount(); ++i)
		{
			if (cloneSource.techniques.getEffectTechnique(i) == cloneSource.currentTechnique)
			{
				currentTechnique = techniques.getEffectTechnique(i);
				break;
			}
		}

		// Take a reference to the original shader list.
		_shaders = cloneSource._shaders;
	}

	// / <summary>
	// / Returns a deep copy of the effect where immutable types
	// / are shared and mutable data is duplicated.
	// / </summary>
	// / <remarks>
	// / See "Cloning an Effect" in MSDN:
	// / http://msdn.microsoft.com/en-us/library/windows/desktop/ff476138(v=vs.85).aspx
	// / </remarks>
	// / <returns>The cloned effect.</returns>
	public Effect clone()
	{
		return new Effect(this);
	}

	protected boolean onApply()
	{
		return false;
	}

	@Override
	protected void dispose(boolean disposing)
	{
		if (!isDisposed())
		{
			if (disposing)
			{
				if (!_isClone)
				{
					// Only the clone source can dispose the shaders.
					if (_shaders != null)
					{
						for (Shader shader : _shaders)
							shader.close();
					}
				}

				if (constantBuffers != null)
				{
					for (ConstantBuffer buffer : constantBuffers)
						buffer.close();
					constantBuffers = null;
				}
			}
		}
		super.dispose(disposing);
	}

	@Override
	protected void graphicsDeviceResetting()
	{
		for (int i = 0; i < constantBuffers.length; ++i)
			constantBuffers[i].clear();
	}

	protected static byte[] loadEffectResource(String name)
	{
//#if WINRT
		// var assembly = typeof(Effect).GetTypeInfo().Assembly;
//#else
		// var assembly = typeof(Effect).Assembly;
		ClassLoader assembly = Effect.class.getClassLoader();
//#endif

		// var stream = assembly.GetManifestResourceStream(name);
		InputStream stream = assembly.getResourceAsStream(name);

		try (ByteArrayOutputStream ms = new ByteArrayOutputStream())
		{
			ByteStreams.copy(stream, ms);
			return ms.toByteArray();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// if we get there something went wrong and we get garbage
		return null;
	}

	private void readEffect(BinaryReader reader)
	{
		// TODO: Maybe we should be reading in a string
		// table here to save some bytes in the file.

		// Read in all the constant buffers.
		int buffers = (int) reader.readByte();
		constantBuffers = new ConstantBuffer[buffers];
		for (int c = 0; c < buffers; ++c)
		{

// #if OPENGL
			// string name = reader.ReadString();
// #else
			String name = null;
// #endif

			// Create the backing system memory buffer.
			int sizeInBytes = (int) reader.readInt16();

			// Read the parameter index values.
			int[] parameters = new int[reader.readByte()];
			int[] offsets = new int[parameters.length];
			for (int i = 0; i < parameters.length; ++i)
			{
				parameters[i] = (int) reader.readByte();
				offsets[i] = (int) reader.readUInt16();
			}

			// TODO: Do I need to close this ?
			ConstantBuffer buffer = new ConstantBuffer(graphicsDevice,
													   sizeInBytes,
													   parameters,
													   offsets,
													   name);
			constantBuffers[c] = buffer;
			buffer.close();	// TODO: I added this, see if it doesn't create problems
		}

		// Read in all the shader objects.
		int shaders = (int) reader.readByte();
		_shaders = new Shader[shaders];
		for (int s = 0; s < shaders; ++s)
			_shaders[s] = new Shader(graphicsDevice, reader);

		// Read in the parameters.
		parameters = readParameters(reader);

		// Read the techniques.
		int techniqueCount = (int) reader.readByte();
		EffectTechnique[] techniques = new EffectTechnique[techniqueCount];
		for (int t = 0; t < techniqueCount; ++t)
		{
			String name = reader.readString();

			EffectAnnotationCollection annotations = readAnnotations(reader);

			EffectPassCollection passes = readPasses(reader, this, _shaders);

			techniques[t] = new EffectTechnique(this, name, passes, annotations);
		}

		this.techniques = new EffectTechniqueCollection(techniques);
		currentTechnique = techniques[0];
	}

	private static EffectAnnotationCollection readAnnotations(BinaryReader reader)
	{
		int count = (int) reader.readByte();
		if (count == 0)
			return EffectAnnotationCollection.Empty;

		EffectAnnotation[] annotations = new EffectAnnotation[count];

		// NOTE: This TODO was already in the original code
		// TODO: Annotations are not implemented!

		return new EffectAnnotationCollection(annotations);
	}

	private static EffectPassCollection readPasses(BinaryReader reader, Effect effect, Shader[] shaders)
	{
		int count = (int) reader.readByte();
		EffectPass[] passes = new EffectPass[count];

		for (int i = 0; i < count; ++i)
		{
			String name = reader.readString();
			EffectAnnotationCollection annotations = readAnnotations(reader);

			// Get the vertex shader.
			Shader vertexShader = null;
//			int shaderIndex = Byte.toUnsignedInt(reader.readByte());
			// NOTE: avoid the method call by doing the unsigned cast directly.
			int shaderIndex = ((int) reader.readByte()) & 0xff;
			if (shaderIndex != 255)
				vertexShader = shaders[shaderIndex];

			// Get the pixel shader.
			Shader pixelShader = null;
			shaderIndex = (int) reader.readByte();
			if (shaderIndex != 255)
				pixelShader = shaders[shaderIndex];

			BlendState blend = null;
			DepthStencilState depth = null;
			RasterizerState raster = null;
			if (reader.readBoolean())
			{
				blend = new BlendState();
				blend.setAlphaBlendFunction(BlendFunction.valueOf(reader.readByte()));
				blend.setAlphaDestinationBlend(Blend.valueOf(reader.readByte()));
				blend.setAlphaSourceBlend(Blend.valueOf(reader.readByte()));
				blend.setBlendFactor(new Color(reader.readByte(), reader.readByte(), reader.readByte(), reader
						.readByte()));
				blend.setColorBlendFunction(BlendFunction.valueOf(reader.readByte()));
				blend.setColorDestinationBlend(Blend.valueOf(reader.readByte()));
				blend.setColorSourceBlend(Blend.valueOf(reader.readByte()));
				blend.setColorWriteChannels(ColorWriteChannels.valueOf(reader.readByte()));
				blend.setColorWriteChannels1(ColorWriteChannels.valueOf(reader.readByte()));
				blend.setColorWriteChannels2(ColorWriteChannels.valueOf(reader.readByte()));
				blend.setColorWriteChannels3(ColorWriteChannels.valueOf(reader.readByte()));
				blend.setMultiSampleMask(reader.readInt32());
			}
			if (reader.readBoolean())
			{
				depth = new DepthStencilState();
				depth.setCounterClockwiseStencilDepthBufferFail(StencilOperation.valueOf(reader.readByte()));
				depth.setCounterClockwiseStencilFail(StencilOperation.valueOf(reader.readByte()));
				depth.setCounterClockwiseStencilFunction(CompareFunction.valueOf(reader.readByte()));
				depth.setCounterClockwiseStencilPass(StencilOperation.valueOf(reader.readByte()));
				depth.setDepthBufferEnable(reader.readBoolean());
				depth.setDepthBufferFunction(CompareFunction.valueOf(reader.readByte()));
				depth.setDepthBufferWriteEnable(reader.readBoolean());
				depth.setReferenceStencil(reader.readInt32());
				depth.setStencilDepthBufferFail(StencilOperation.valueOf(reader.readByte()));
				depth.setStencilEnable(reader.readBoolean());
				depth.setStencilFail(StencilOperation.valueOf(reader.readByte()));
				depth.setStencilFunction(CompareFunction.valueOf(reader.readByte()));
				depth.setStencilMask(reader.readInt32());
				depth.setStencilPass(StencilOperation.valueOf(reader.readByte()));
				depth.setStencilWriteMask(reader.readInt32());
				depth.setTwoSidedStencilMode(reader.readBoolean());
			}
			if (reader.readBoolean())
			{
				raster = new RasterizerState();
				raster.setCullMode(CullMode.valueOf(reader.readByte()));
				raster.setDepthBias(reader.readSingle());
				raster.setFillMode(FillMode.valueOf(reader.readByte()));
				raster.setMultiSampleAntiAlias(reader.readBoolean());
				raster.setScissorTestEnable(reader.readBoolean());
				raster.setSlopeScaleDepthBias(reader.readSingle());
			}

			passes[i] = new EffectPass(effect, name, vertexShader, pixelShader, blend, depth, raster, annotations);
		}

		return new EffectPassCollection(passes);
	}

	private static EffectParameterCollection readParameters(BinaryReader reader)
	{
		int count = (int) reader.readByte();
		if (count == 0)
			return EffectParameterCollection.Empty;

		EffectParameter[] parameters = new EffectParameter[count];
		for (int i = 0; i < count; ++i)
		{
			EffectParameterClass class_ = EffectParameterClass.valueOf(reader.readByte());
			EffectParameterType type = EffectParameterType.valueOf(reader.readByte());
			String name = reader.readString();
			String semantic = reader.readString();
			EffectAnnotationCollection annotations = readAnnotations(reader);
			int rowCount = (int) reader.readByte();
			int columnCount = (int) reader.readByte();

			EffectParameterCollection elements = readParameters(reader);
			EffectParameterCollection structMembers = readParameters(reader);

			Object data = null;
			if (elements.getCount() == 0 && structMembers.getCount() == 0)
			{
				switch (type)
				{
					case Boolean:
					case Integer:
// #if DIRECTX
					// Under DirectX we properly store integers and booleans
					// in an integer type.
					//
					// MojoShader on the other hand stores everything in float
					// types which is why this code is disabled under OpenGL.
					{
						int[] buffer = new int[rowCount * columnCount];
						for (int j = 0; j < buffer.length; ++j)
							buffer[j] = reader.readInt32();
						data = buffer;
						break;
					}
// #endif

					case Single:
					{
						float[] buffer = new float[rowCount * columnCount];
						for (int j = 0; j < buffer.length; ++j)
							buffer[j] = reader.readSingle();
						data = buffer;
						break;
					}

					case String:
						// TODO: We have not investigated what a string
						// type should do in the parameter list. Till then
						// throw to let the user know.
						throw new UnsupportedOperationException();

					default:
						// NOTE: We skip over all other types as they
						// don't get added to the constant buffer.
						break;
				}
			}

			parameters[i] = new EffectParameter(
					class_, type, name, rowCount, columnCount, semantic,
					annotations, elements, structMembers, data);
		}

		return new EffectParameterCollection(parameters);
	}
}
