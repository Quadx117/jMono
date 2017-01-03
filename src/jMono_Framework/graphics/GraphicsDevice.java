package jMono_Framework.graphics;

import jMono_Framework.Color;
import jMono_Framework.GraphicsDeviceInformation;
import jMono_Framework.Rectangle;
import jMono_Framework.dotNet.As;
import jMono_Framework.dotNet.events.Event;
import jMono_Framework.dotNet.events.EventArgs;
import jMono_Framework.graphics.effect.Effect;
import jMono_Framework.graphics.shader.ConstantBuffer;
import jMono_Framework.graphics.shader.ConstantBufferCollection;
import jMono_Framework.graphics.shader.Shader;
import jMono_Framework.graphics.shader.ShaderStage;
import jMono_Framework.graphics.states.BlendState;
import jMono_Framework.graphics.states.DepthFormat;
import jMono_Framework.graphics.states.DepthStencilState;
import jMono_Framework.graphics.states.RasterizerState;
import jMono_Framework.graphics.vertices.IVertexType;
import jMono_Framework.graphics.vertices.IndexBuffer;
import jMono_Framework.graphics.vertices.PrimitiveType;
import jMono_Framework.graphics.vertices.VertexBuffer;
import jMono_Framework.graphics.vertices.VertexDeclaration;
import jMono_Framework.graphics.vertices.VertexDeclarationCache;
import jMono_Framework.graphics.vertices.VertexPositionColorTexture;
import jMono_Framework.math.MathHelper;
import jMono_Framework.math.Vector2;
import jMono_Framework.math.Vector4;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicsDevice implements AutoCloseable
{
	private Viewport _viewport;
	private GraphicsProfile _graphicsProfile;

	private boolean _isDisposed;

	private BlendState _blendState;
	private BlendState _actualBlendState;
	private boolean _blendStateDirty;

	private BlendState _blendStateAdditive;
	private BlendState _blendStateAlphaBlend;
	private BlendState _blendStateNonPremultiplied;
	private BlendState _blendStateOpaque;

	private DepthStencilState _depthStencilState;
	private DepthStencilState _actualDepthStencilState;
	private boolean _depthStencilStateDirty;

	private DepthStencilState _depthStencilStateDefault;
	private DepthStencilState _depthStencilStateDepthRead;
	private DepthStencilState _depthStencilStateNone;

	private RasterizerState _rasterizerState;
	private RasterizerState _actualRasterizerState;
	private boolean _rasterizerStateDirty;

	private RasterizerState _rasterizerStateCullClockwise;
	private RasterizerState _rasterizerStateCullCounterClockwise;
	private RasterizerState _rasterizerStateCullNone;

	private Rectangle _scissorRectangle;
	private boolean _scissorRectangleDirty;

	private VertexBuffer _vertexBuffer;
	private boolean _vertexBufferDirty;

	private IndexBuffer _indexBuffer;
	private boolean _indexBufferDirty;

	private RenderTargetBinding[] _currentRenderTargetBindings = new RenderTargetBinding[4];
	private int _currentRenderTargetCount;

	private GraphicsCapabilities graphicsCapabilities;
	public GraphicsCapabilities getGraphicsCapabilities() { return graphicsCapabilities; }

	private TextureCollection vertexTextures;
	public TextureCollection getVertexTextures() { return vertexTextures; }

	private SamplerStateCollection vertexSamplerStates;
	public SamplerStateCollection getVertexSamplerStates() { return samplerStates; }

	private TextureCollection textures;
	public TextureCollection getTextures() { return textures; }

	private SamplerStateCollection samplerStates;
	public SamplerStateCollection getSamplerStates() { return samplerStates; }

	// On Intel Integrated graphics, there is a fast hw unit for doing
	// clears to colors where all components are either 0 or 255.
	// Despite XNA4 using Purple here, we use black (in Release) to avoid
	// performance warnings on Intel/Mesa
// #if DEBUG
	private static final Color DiscardColor = new Color(68, 34, 136, 255);
// #else
	// private static final Color DiscardColor = new Color(0, 0, 0, 255);
// #endif

	/**
	 * The active vertex shader.
	 */
	private Shader _vertexShader;
	private boolean _vertexShaderDirty;
	private boolean isVertexShaderDirty()
	{
		return _vertexShaderDirty;
	}

	/**
	 * The active pixel shader.
	 */
	private Shader _pixelShader;
	private boolean _pixelShaderDirty;
	private boolean isPixelShaderDirty()
	{
		return _pixelShaderDirty;
	}

	private final ConstantBufferCollection _vertexConstantBuffers = new ConstantBufferCollection(ShaderStage.Vertex, 16);
	private final ConstantBufferCollection _pixelConstantBuffers = new ConstantBufferCollection(ShaderStage.Pixel, 16);

	/**
	 * The cache of effects from unique byte streams.
	 */
	public Map<Integer, Effect> effectCache;

	// Resources may be added to and removed from the list from many threads.
	private final Object _resourcesLock = new Object();

	// Use WeakReference for the global resources list as we do not know when a resource
	// may be disposed and collected. We do not want to prevent a resource from being
	// collected by holding a strong reference to it in this list.
	private List<WeakReference<?>> _resources = new ArrayList<WeakReference<?>>();

	// NOTE: Already like that in the original code. Guess I don't need to do these than
	// TODO Graphics Device events need implementing
	public Event<EventArgs> deviceLost = new Event<EventArgs>();
	public Event<EventArgs> deviceReset = new Event<EventArgs>();
	public Event<EventArgs> deviceResetting = new Event<EventArgs>();
	public Event<ResourceCreatedEventArgs> resourceCreated = new Event<ResourceCreatedEventArgs>();
	public Event<ResourceDestroyedEventArgs> resourceDestroyed = new Event<ResourceDestroyedEventArgs>();
	public Event<EventArgs> disposing = new Event<EventArgs>();

	private boolean suppressEventHandlerWarningsUntilEventsAreProperlyImplemented()
	{
		return deviceLost != null &&
				resourceCreated != null &&
				resourceDestroyed != null &&
				disposing != null;
	}

	protected int maxTextureSlots;
	protected int maxVertexTextureSlots;

	public boolean isDisposed()
	{
		return _isDisposed;
	}

	public boolean isContentLost()
	{
		// We will just return IsDisposed for now
		// as that is the only case I can see for now
		return _isDisposed;
	}

	protected boolean isRenderTargetBound()
	{
		return _currentRenderTargetCount > 0;
	}

	private GraphicsAdapter adapter;
	public GraphicsAdapter getAdapter() { return adapter; }

	protected GraphicsMetrics _graphicsMetrics = new GraphicsMetrics();

	/**
	 * The rendering information for debugging and profiling.
	 * The metrics are reset every frame after draw within {@link GraphicsDevice#present()}.
	 * @return	The rendering information for debugging and profiling.
	 */
	public GraphicsMetrics getMetrics() { return _graphicsMetrics; }
	public void setGraphicsMetrics(GraphicsMetrics value) { _graphicsMetrics = value; }

	protected GraphicsDevice(GraphicsDeviceInformation gdi)
	{
		if (gdi.presentationParameters == null)
			throw new NullPointerException("presentationParameters");
		presentationParameters = gdi.presentationParameters;
		setup();
		this.graphicsCapabilities = new GraphicsCapabilities(this);
		setGraphicsProfile(gdi.graphicsProfile);
		initialize();
	}

	protected GraphicsDevice()
	{
		presentationParameters = new PresentationParameters();
		presentationParameters.setDepthStencilFormat(DepthFormat.Depth24);
		setup();
		this.graphicsCapabilities = new GraphicsCapabilities(this);
		initialize();
	}

	/**
	 * Initializes a new instance of the <see cref="GraphicsDevice" /> class.
	 * 
	 * @param adapter
	 *        The graphics adapter.
	 * @param graphicsProfile
	 *        The graphics profile.
	 * @param presentationParameters
	 *        The presentation options.
	 * @throws NullPointerException
	 *         If {@code presentationParameters} is {@code null}.
	 */
	public GraphicsDevice(GraphicsAdapter adapter, GraphicsProfile graphicsProfile, PresentationParameters presentationParameters)
	{
		this.adapter = adapter;
		if (presentationParameters == null)
			throw new NullPointerException("presentationParameters");
		this.presentationParameters = presentationParameters;
		setup();
		this.graphicsCapabilities = new GraphicsCapabilities(this);
		setGraphicsProfile(graphicsProfile);
		initialize();
	}

	private void setup()
	{
		// Initialize the main viewport
		_viewport = new Viewport(0, 0, getDisplayMode().getWidth(), getDisplayMode().getHeight());
		_viewport.setMaxDepth(1.0f);

		platformSetup();

		vertexTextures = new TextureCollection(this, maxVertexTextureSlots, true);
		vertexSamplerStates = new SamplerStateCollection(this, maxVertexTextureSlots, true);

		textures = new TextureCollection(this, maxTextureSlots, false);
		samplerStates = new SamplerStateCollection(this, maxTextureSlots, false);

		_blendStateAdditive = BlendState.Additive.clone();
		_blendStateAlphaBlend = BlendState.AlphaBlend.clone();
		_blendStateNonPremultiplied = BlendState.NonPremultiplied.clone();
		_blendStateOpaque = BlendState.Opaque.clone();

		setBlendState(BlendState.Opaque);

		_depthStencilStateDefault = DepthStencilState.Default.clone();
		_depthStencilStateDepthRead = DepthStencilState.DepthRead.clone();
		_depthStencilStateNone = DepthStencilState.None.clone();

		setDepthStencilState(DepthStencilState.Default);

		_rasterizerStateCullClockwise = RasterizerState.CullClockwise.clone();
		_rasterizerStateCullCounterClockwise = RasterizerState.CullCounterClockwise.clone();
		_rasterizerStateCullNone = RasterizerState.CullNone.clone();

		setRasterizerState(RasterizerState.CullCounterClockwise);

		effectCache = new HashMap<Integer, Effect>();
	}

	@Override
	public void finalize()
	{
		dispose(false);
	}

	protected void initialize()
	{
		platformInitialize();

		// Force set the default render states.
		_blendStateDirty = _depthStencilStateDirty = _rasterizerStateDirty = true;
		setBlendState(BlendState.Opaque);
		setDepthStencilState(DepthStencilState.Default);
		setRasterizerState(RasterizerState.CullCounterClockwise);

		// Clear the texture and sampler collections forcing
		// the state to be reapplied.
		vertexTextures.clear();
		vertexSamplerStates.clear();
		vertexTextures.clear();
		samplerStates.clear();

		// Clear constant buffers
		_vertexConstantBuffers.clear();
		_pixelConstantBuffers.clear();

		// Force set the buffers and shaders on next ApplyState() call
		_indexBufferDirty = true;
		_vertexBufferDirty = true;
		_vertexShaderDirty = true;
		_pixelShaderDirty = true;

		// Set the default scissor rect.
		_scissorRectangleDirty = true;
		_scissorRectangle = _viewport.getBounds();

		// Set the default render target.
		applyRenderTargets(null);
	}

	public RasterizerState getRasterizerState()
	{
		return _rasterizerState;
	}

	public void setRasterizerState(RasterizerState value)
	{
		if (value == null)
			throw new NullPointerException("value is null");

		// Don't set the same state twice!
		if (_rasterizerState == value)
			return;

		if (!value.getDepthClipEnable() && !getGraphicsCapabilities().supportsDepthClamp())
			throw new UnsupportedOperationException("Cannot set RasterizerState.DepthClipEnable to false on this graphics device");

		_rasterizerState = value;

		// Static state properties never actually get bound;
		// instead we use our GraphicsDevice-specific version of them.
		RasterizerState newRasterizerState = _rasterizerState;
		if (_rasterizerState == RasterizerState.CullClockwise)
			newRasterizerState = _rasterizerStateCullClockwise;
		else if (_rasterizerState == RasterizerState.CullCounterClockwise)
			newRasterizerState = _rasterizerStateCullCounterClockwise;
		else if (_rasterizerState == RasterizerState.CullNone)
			newRasterizerState = _rasterizerStateCullNone;

		newRasterizerState.bindToGraphicsDevice(this);

		_actualRasterizerState = newRasterizerState;

		_rasterizerStateDirty = true;
	}

	public BlendState getBlendState() { return _blendState; }

	public void setBlendState(BlendState value)
	{
		if (value == null)
			throw new NullPointerException("value");

		// Don't set the same state twice!
		if (_blendState == value)
			return;

		_blendState = value;

		// Static state properties never actually get bound;
		// instead we use our GraphicsDevice-specific version of them.
		BlendState newBlendState = _blendState;
		if (_blendState == BlendState.Additive)
			newBlendState = _blendStateAdditive;
		else if (_blendState == BlendState.AlphaBlend)
			newBlendState = _blendStateAlphaBlend;
		else if (_blendState == BlendState.NonPremultiplied)
			newBlendState = _blendStateNonPremultiplied;
		else if (_blendState == BlendState.Opaque)
			newBlendState = _blendStateOpaque;

		// Blend state is now bound to a device... no one should
		// be changing the state of the blend state object now!
		newBlendState.bindToGraphicsDevice(this);

		_actualBlendState = newBlendState;

		_blendStateDirty = true;
	}

	public DepthStencilState getDepthStencilState() { return _depthStencilState; }

	public void setDepthStencilState(DepthStencilState value)
	{
		if (value == null)
			throw new NullPointerException("value");

		// Don't set the same state twice!
		if (_depthStencilState == value)
			return;

		_depthStencilState = value;

		// Static state properties never actually get bound;
		// instead we use our GraphicsDevice-specific version of them.
		DepthStencilState newDepthStencilState = _depthStencilState;
		if (_depthStencilState == DepthStencilState.Default)
			newDepthStencilState = _depthStencilStateDefault;
		else if (_depthStencilState == DepthStencilState.DepthRead)
			newDepthStencilState = _depthStencilStateDepthRead;
		else if (_depthStencilState == DepthStencilState.None)
			newDepthStencilState = _depthStencilStateNone;

		newDepthStencilState.bindToGraphicsDevice(this);

		_actualDepthStencilState = newDepthStencilState;

		_depthStencilStateDirty = true;
	}

	protected void applyState(boolean applyShaders)
	{
		platformBeginApplyState();

		if (_blendStateDirty)
		{
			// TODO: see other GraphicsDevice files
//			_actualBlendState.PlatformApplyState(this);
			_blendStateDirty = false;
		}

		if (_depthStencilStateDirty)
		{
			// TODO: see other GraphicsDevice files
//			_actualDepthStencilState.PlatformApplyState(this);
			_depthStencilStateDirty = false;
		}

		if (_rasterizerStateDirty)
		{
			// TODO: see other GraphicsDevice files
//			_actualRasterizerState.PlatformApplyState(this);
			_rasterizerStateDirty = false;
		}

		platformApplyState(applyShaders);
	}

	public void clear(Color color)
	{
		int options = ClearOptions.Target.getValue() |		//
					  ClearOptions.DepthBuffer.getValue() |	//
					  ClearOptions.Stencil.getValue();
		platformClear(options, color.toVector4(), _viewport.getMaxDepth(), 0);
	}

	public void clear(ClearOptions options, Color color, float depth, int stencil)
	{
		platformClear(options.getValue(), color.toVector4(), depth, stencil);
	}

	public void clear(ClearOptions options, Vector4 color, float depth, int stencil)
	{
		platformClear(options.getValue(), color, depth, stencil);
	}

	@Override
	public void close()
	{
		dispose(true);
		// GC.SuppressFinalize(this);
	}

	protected void dispose(boolean disposing)
	{
		if (!_isDisposed)
		{
			if (disposing)
			{
				// Dispose of all remaining graphics resources before disposing of the graphics device
				synchronized (_resourcesLock)
				{
					for (WeakReference<?> resource : _resources)
					{
						AutoCloseable target = As.as(resource.get(), AutoCloseable.class);
						if (target != null)
							try
							{
								target.close();
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
					}
					_resources.clear();
				}

				// Clear the effect cache.
				effectCache.clear();

				platformDispose();
			}

			_isDisposed = true;
		}
	}

	protected void addResourceReference(WeakReference<?> resourceReference)
	{
		synchronized (_resourcesLock)
		{
			_resources.add(resourceReference);
		}
	}

	protected void removeResourceReference(WeakReference<?> resourceReference)
	{
		synchronized (_resourcesLock)
		{
			_resources.remove(resourceReference);
		}
	}

	public void present()
	{
		_graphicsMetrics = new GraphicsMetrics();
		platformPresent();
	}

	// NOTE: Already commented out in the original code
	/*
	 * public void Present(Rectangle? sourceRectangle, Rectangle? destinationRectangle, IntPtr
	 * overrideWindowHandle)
	 * {
	 * throw new UnsupportedOperationException();
	 * }
	 * 
	 * public void Reset()
	 * {
	 * // Manually resetting the device is not currently supported.
	 * throw new UnsupportedOperationException();
	 * }
	 */

// #if WINDOWS && DIRECTX
	public void reset(PresentationParameters presentationParameters)
	{
		this.presentationParameters = presentationParameters;

		// Update the back buffer.
		// createSizeDependentResources();
		applyRenderTargets(null);
	}
// #endif

	// NOTE: Already commented out in the original code
	/*
	 * public void Reset(PresentationParameters presentationParameters, GraphicsAdapter
	 * graphicsAdapter)
	 * {
	 * throw new UnsupportedOperationException();
	 * }
	 */

	/**
	 * Trigger the DeviceResetting event.
	 * Currently protected to allow the various platforms to send the event at the appropriate time.
	 */
	protected void onDeviceResetting()
	{
		if (deviceResetting != null)
			deviceResetting.handleEvent(this, EventArgs.Empty);

		synchronized (_resourcesLock)
		{
			for (WeakReference<?> resource : _resources)
			{
				GraphicsResource target = As.as(resource.get(), GraphicsResource.class);
				if (target != null)
					target.graphicsDeviceResetting();
			}

			// Remove references to resources that have been garbage collected.
			_resources.removeIf(wr -> wr.get() != null);
		}
	}

	/**
	 * Trigger the DeviceReset event to allow games to be notified of a device reset.
	 * Currently protected to allow the various platforms to send the event at the appropriate time.
	 */
	protected void onDeviceReset()
	{
		if (deviceReset != null)
			deviceReset.handleEvent(this, EventArgs.Empty);
	}

	public DisplayMode getDisplayMode()
	{
		return adapter.getCurrentDisplayMode();
	}

	public GraphicsDeviceStatus getGraphicsDeviceStatus()
	{
		return GraphicsDeviceStatus.Normal;
	}

	private PresentationParameters presentationParameters;
	public PresentationParameters getPresentationParameters() { return presentationParameters; }

	public Viewport getViewport()
	{
		return _viewport;
	}

	public void setViewport(Viewport value)
	{
		_viewport = value;
		platformSetViewport(value);
	}

	public GraphicsProfile getGraphicsProfile()
	{
		return _graphicsProfile;
	}

	public void setGraphicsProfile(GraphicsProfile value)
	{
		// Check if Profile is supported.
		// TODO: [DirectX] Recreate the Device using the new
		// feature level each time the Profile changes.
		if (value.ordinal() > getHighestSupportedGraphicsProfile(this).ordinal())
			throw new UnsupportedOperationException(String.format("Could not find a graphics device that supports the %s profile", value.toString()));
		_graphicsProfile = value;
		graphicsCapabilities.initialize(this);
	}

	public Rectangle getScissorRectangle()
	{
		return _scissorRectangle;
	}

	public void setScissorRectangle(Rectangle value)
	{
		if (_scissorRectangle.equals(value))
			return;

		_scissorRectangle = new Rectangle(value);
		_scissorRectangleDirty = true;
	}

	public int getRenderTargetCount()
	{
		return _currentRenderTargetCount;
	}

	public void setRenderTarget(RenderTarget2D renderTarget)
	{
		if (renderTarget == null)
			setRenderTargets((RenderTargetBinding[]) null);
		else
			setRenderTargets(new RenderTargetBinding(renderTarget));
	}

	public void setRenderTarget(RenderTargetCube renderTarget, CubeMapFace cubeMapFace)
	{
		if (renderTarget == null)
			setRenderTarget(null);
		else
			setRenderTargets(new RenderTargetBinding(renderTarget, cubeMapFace));
	}

	public void setRenderTargets(RenderTargetBinding... renderTargets)
	{
		// Avoid having to check for null and zero length.
		int renderTargetCount = 0;
		if (renderTargets != null)
		{
			renderTargetCount = renderTargets.length;
			if (renderTargetCount == 0)
				renderTargets = null;
		}

		// Try to early out if the current and new bindings are equal.
		if (_currentRenderTargetCount == renderTargetCount)
		{
			boolean isEqual = true;
			for (int i = 0; i < _currentRenderTargetCount; ++i)
			{
				if (_currentRenderTargetBindings[i].getRenderTarget() != renderTargets[i].getRenderTarget() ||
					_currentRenderTargetBindings[i].getArraySlice() != renderTargets[i].getArraySlice())
				{
					isEqual = false;
					break;
				}
			}

			if (isEqual)
				return;
		}

		applyRenderTargets(renderTargets);
	}

	public void applyRenderTargets(RenderTargetBinding[] renderTargets)
	{
		boolean clearTarget = false;

		platformResolveRenderTargets();

		// Clear the current bindings.
		Arrays.fill(_currentRenderTargetBindings, 0, _currentRenderTargetBindings.length, null);

		int renderTargetWidth;
		int renderTargetHeight;
		if (renderTargets == null)
		{
			_currentRenderTargetCount = 0;

			platformApplyDefaultRenderTarget();
			clearTarget = presentationParameters.renderTargetUsage == RenderTargetUsage.DiscardContents;

			renderTargetWidth = presentationParameters.getBackBufferWidth();
			renderTargetHeight = presentationParameters.getBackBufferHeight();
		}
		else
		{
			// Copy the new bindings.
			_currentRenderTargetBindings = Arrays.copyOf(renderTargets, renderTargets.length);
			_currentRenderTargetCount = renderTargets.length;

			IRenderTarget renderTarget = platformApplyRenderTargets();

			// We clear the render target if asked.
			clearTarget = renderTarget.getRenderTargetUsage().equals(RenderTargetUsage.DiscardContents);

			renderTargetWidth = renderTarget.getWidth();
			renderTargetHeight = renderTarget.getHeight();
		}

		// Set the viewport to the size of the first render target.
		_viewport = new Viewport(0, 0, renderTargetWidth, renderTargetHeight);

		// Set the scissor rectangle to the size of the first render target.
		_scissorRectangle = new Rectangle(0, 0, renderTargetWidth, renderTargetHeight);

		// In XNA 4, because of hardware limitations on Xbox, when
		// a render target doesn't have PreserveContents as its usage
		// it is cleared before being rendered to.
		if (clearTarget)
			clear(DiscardColor);
	}

	public RenderTargetBinding[] getRenderTargets()
	{
		// Return a correctly sized copy our protected array.
		RenderTargetBinding[] bindings = new RenderTargetBinding[_currentRenderTargetCount];
		bindings = Arrays.copyOf(_currentRenderTargetBindings, _currentRenderTargetCount);
		return bindings;
	}

	public void getRenderTargets(RenderTargetBinding[] outTargets)
	{
		assert (outTargets.length == _currentRenderTargetCount) : "Invalid outTargets array length!";
		outTargets = Arrays.copyOf(_currentRenderTargetBindings, _currentRenderTargetCount);
	}

	public void setVertexBuffer(VertexBuffer vertexBuffer)
	{
		if (_vertexBuffer == vertexBuffer)
			return;

		_vertexBuffer = vertexBuffer;
		_vertexBufferDirty = true;
	}

	private void setIndexBuffer(IndexBuffer indexBuffer)
	{
		if (_indexBuffer == indexBuffer)
			return;

		_indexBuffer = indexBuffer;
		_indexBufferDirty = true;
	}

	public IndexBuffer getIndices() { return _indexBuffer; }
	public void setIndices(IndexBuffer value) { setIndexBuffer(value); }

	protected Shader getVertexShader() { return _vertexShader; }

	public void setVertexShader(Shader value)
	{
		if (_vertexShader == value)
			return;

		_vertexShader = value;
		_vertexShaderDirty = true;
	}

	protected Shader getPixelShader() { return _pixelShader; }

	public void setPixelShader(Shader value)
	{
		if (_pixelShader == value)
			return;

		_pixelShader = value;
		_pixelShaderDirty = true;
	}

	public void setConstantBuffer(ShaderStage stage, int slot, ConstantBuffer buffer)
	{
		if (stage.equals(ShaderStage.Vertex))
			_vertexConstantBuffers.setConstantBufferCollection(slot, buffer);
		else
			_pixelConstantBuffers.setConstantBufferCollection(slot, buffer);
	}

	public boolean isResourcesLost;

	/**
	 * Draw geometry by indexing into the vertex buffer.
	 * 
	 * <p>
	 * Note that minVertexIndex and numVertices are unused in MonoGame and will be ignored.
	 * 
	 * @param primitiveType
	 *        The type of primitives in the index buffer.
	 * @param baseVertex
	 *        Used to offset the vertex range indexed from the vertex buffer.
	 * @param minVertexIndex
	 *        A hint of the lowest vertex indexed relative to baseVertex.
	 * @param numVertices
	 *        A hint of the maximum vertex indexed.
	 * @param startIndex
	 *        The index within the index buffer to start drawing from.
	 * @param primitiveCount
	 *        The number of primitives to render from the index buffer.
	 * @deprecated Use DrawIndexedPrimitives(PrimitiveType primitiveType, int baseVertex, int
	 *             startIndex, int primitiveCount) instead. In future versions this method can be
	 *             removed.
	 */
	@Deprecated
	public void drawIndexedPrimitives(PrimitiveType primitiveType, int baseVertex, int minVertexIndex, int numVertices,
			int startIndex, int primitiveCount)
	{
		drawIndexedPrimitives(primitiveType, baseVertex, startIndex, primitiveCount);
	}

	public void drawIndexedPrimitives(PrimitiveType primitiveType, int baseVertex, int startIndex, int primitiveCount)
	{
		if (_vertexShader == null)
			throw new UnsupportedOperationException("Vertex shader must be set before calling DrawIndexedPrimitives.");

		if (_vertexBuffer == null)
			throw new UnsupportedOperationException("Vertex buffer must be set before calling DrawIndexedPrimitives.");

		if (_indexBuffer == null)
			throw new UnsupportedOperationException("Index buffer must be set before calling DrawIndexedPrimitives.");

		if (primitiveCount <= 0)
			throw new IllegalArgumentException("primitiveCount is out of range");

		_graphicsMetrics._drawCount++;
		_graphicsMetrics._primitiveCount += (long) primitiveCount;

		platformDrawIndexedPrimitives(primitiveType, baseVertex, startIndex, primitiveCount);
	}

	public <T extends IVertexType> void drawUserPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset, int primitiveCount)
	{
		drawUserPrimitives(primitiveType, vertexData, vertexOffset, primitiveCount, VertexDeclarationCache.getVertexDeclaration());
	}

	public <T> void drawUserPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset, int primitiveCount, VertexDeclaration vertexDeclaration)
	{
		if (vertexData == null)
			throw new NullPointerException("vertexData is null");

		if (vertexData.length == 0)
			throw new IllegalArgumentException("vertexData == 0");

		if (vertexOffset < 0 || vertexOffset >= vertexData.length)
			throw new IllegalArgumentException("vertexOffset < 0 or >= vertexData.length");

		if (primitiveCount <= 0)
			throw new IllegalArgumentException("primitiveCount <= 0");

		int vertexCount = getElementCountArray(primitiveType, primitiveCount);

		if (vertexOffset + vertexCount > vertexData.length)
			throw new IllegalArgumentException("primitiveCount");

		if (vertexDeclaration == null)
			throw new NullPointerException("vertexDeclaration is null");

		_graphicsMetrics._drawCount++;
		_graphicsMetrics._primitiveCount += (long) primitiveCount;

		platformDrawUserPrimitives(primitiveType, vertexData, vertexOffset, vertexDeclaration, vertexCount);
	}

	public void drawPrimitives(PrimitiveType primitiveType, int vertexStart, int primitiveCount)
	{
		if (_vertexShader == null)
			throw new UnsupportedOperationException("Vertex shader must be set before calling DrawPrimitives.");

		if (_vertexBuffer == null)
			throw new IllegalStateException("Vertex buffer must be set before calling DrawPrimitives.");

		if (primitiveCount <= 0)
			throw new IllegalArgumentException("primitiveCount <= 0");

		int vertexCount = getElementCountArray(primitiveType, primitiveCount);

		_graphicsMetrics._drawCount++;
		_graphicsMetrics._primitiveCount += (long) primitiveCount;

		platformDrawPrimitives(primitiveType, vertexStart, vertexCount);
	}

	public <T extends IVertexType> void drawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset, int numVertices, short[] indexData, int indexOffset, int primitiveCount)
	{
		drawUserIndexedPrimitives(primitiveType, vertexData, vertexOffset, numVertices, indexData, indexOffset, primitiveCount, VertexDeclarationCache.getVertexDeclaration());
	}

	public <T> void drawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset, int numVertices, short[] indexData, int indexOffset, int primitiveCount, VertexDeclaration vertexDeclaration)
	{
		// These parameter checks are a duplicate of the checks in the int[] overload of DrawUserIndexedPrimitives.
		// Inlined here for efficiency.

		if (vertexData == null || vertexData.length == 0)
			throw new NullPointerException("vertexData is null");

		if (vertexOffset < 0 || vertexOffset >= vertexData.length)
			throw new IllegalArgumentException("vertexOffset is out of range");

		if (numVertices <= 0 || numVertices > vertexData.length)
			throw new IllegalArgumentException("numVertices is out of range");

		if (vertexOffset + numVertices > vertexData.length)
			throw new IllegalArgumentException("numVertices is out of range");

		if (indexData == null || indexData.length == 0)
			throw new NullPointerException("indexData is null");

		if (indexOffset < 0 || indexOffset >= indexData.length)
			throw new IllegalArgumentException("indexOffsetis is out of range");

		if (primitiveCount <= 0)
			throw new IllegalArgumentException("primitiveCount");

		if (indexOffset + getElementCountArray(primitiveType, primitiveCount) > indexData.length)
			throw new IllegalArgumentException("primitiveCount is out of range");

		if (vertexDeclaration == null)
			throw new NullPointerException("vertexDeclaration is null");

		_graphicsMetrics._drawCount++;
		_graphicsMetrics._primitiveCount += (long) primitiveCount;

		platformDrawUserIndexedPrimitives(primitiveType, vertexData, vertexOffset, numVertices, indexData, indexOffset, primitiveCount, vertexDeclaration);
	}

	public <T extends IVertexType> void drawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset, int numVertices, int[] indexData, int indexOffset, int primitiveCount)
	{
		drawUserIndexedPrimitives(primitiveType, vertexData, vertexOffset, numVertices, indexData, indexOffset, primitiveCount, VertexDeclarationCache.getVertexDeclaration());
	}

	public <T extends IVertexType> void drawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset, int numVertices, int[] indexData, int indexOffset, int primitiveCount, VertexDeclaration vertexDeclaration)
	{
		// These parameter checks are a duplicate of the checks in the short[] overload of DrawUserIndexedPrimitives.
		// Inlined here for efficiency.

		if (vertexData == null || vertexData.length == 0)
			throw new NullPointerException("vertexData is null");

		if (vertexOffset < 0 || vertexOffset >= vertexData.length)
			throw new IllegalArgumentException("vertexOffset is out of range");

		if (numVertices <= 0 || numVertices > vertexData.length)
			throw new IllegalArgumentException("numVertices is out of range");

		if (vertexOffset + numVertices > vertexData.length)
			throw new IllegalArgumentException("numVertices is out of range");

		if (indexData == null || indexData.length == 0)
			throw new NullPointerException("indexData is null");

		if (indexOffset < 0 || indexOffset >= indexData.length)
			throw new IllegalArgumentException("indexOffsetis is out of range");

		if (primitiveCount <= 0)
			throw new IllegalArgumentException("primitiveCount");

		if (indexOffset + getElementCountArray(primitiveType, primitiveCount) > indexData.length)
			throw new IllegalArgumentException("primitiveCount is out of range");

		if (vertexDeclaration == null)
			throw new NullPointerException("vertexDeclaration is null");

		_graphicsMetrics._drawCount++;
		_graphicsMetrics._primitiveCount += (long) primitiveCount;

		platformDrawUserIndexedPrimitives(primitiveType, vertexData, vertexOffset, numVertices, indexData, indexOffset, primitiveCount, vertexDeclaration);
	}

	private static int getElementCountArray(PrimitiveType primitiveType, int primitiveCount)
	{
		switch (primitiveType)
		{
			case LineList:
				return primitiveCount * 2;
			case LineStrip:
				return primitiveCount + 1;
			case TriangleList:
				return primitiveCount * 3;
			case TriangleStrip:
				return primitiveCount + 2;
			default:
				throw new UnsupportedOperationException("The " + primitiveType + "is not supported");
		}

	}

	public static GraphicsProfile getHighestSupportedGraphicsProfile(GraphicsDevice graphicsDevice)
	{
		return platformGetHighestSupportedGraphicsProfile(graphicsDevice);
	}

//	###################################################################################################################
// 	#                                             SOFTWARE RENDERER                                                   #
//	###################################################################################################################

	// TODO: I added this for the software renderer, see where this should go.
	/** The array of pixels to be painted on the screen */
//	public int[] pixels;
	
	/** The object used to draw the image in the frame */
	private BufferedImage image;
	/** The BufferStrategy used in this game */
	private BufferStrategy bufferStrategy;
	/** The graphic context used to draw onto the component */
	private Graphics g;
	/** The array of pixels to be painted on the screen */
	private int[] pixels;
	
	private void platformSetup()
	{
		maxTextureSlots = 16;
		maxVertexTextureSlots = 16;
	}

	private void platformInitialize()
	{
		image = new BufferedImage(getPresentationParameters().getBackBufferWidth(), getPresentationParameters().getBackBufferHeight(), BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		// Initialize the buffer strategy
		getPresentationParameters().getDeviceWindowHandle().getGame().createBufferStrategy(3);
		bufferStrategy = getPresentationParameters().getDeviceWindowHandle().getGame().getBufferStrategy();
		g = bufferStrategy.getDrawGraphics();
	}

	private void platformClear(int clearOptions, Vector4 color, float depth, int stencil)
	{
		for (int i = 0; i < pixels.length; ++i)
		{
			pixels[i] = new Color(color).getPackedValue(); // TODO: check for speed (use the Vector4 and shifts instead ?)
		}
	}

	private void platformDispose()
	{
		
	}

	private void platformPresent()
	{
//		bufferStrategy = getBufferStrategy();
//		if (bufferStrategy == null)
//		{
//			createBufferStrategy(3);
//			return;
//		}

//		for (int i = 0; i < pixels.length; ++i)
//		{
//			screenPixels[i] = pixels[i];
//		}

		g = bufferStrategy.getDrawGraphics();
		g.drawImage(image, 0, 0, getPresentationParameters().getBackBufferWidth(), getPresentationParameters().getBackBufferHeight(), null);
		g.dispose();
		bufferStrategy.show();
	}

	private void platformSetViewport(final Viewport value)
	{

    }

	private void platformApplyDefaultRenderTarget()
	{

	}

	private void platformResolveRenderTargets()
	{
		// Resolving MSAA render targets should be done here.
	}

	private IRenderTarget platformApplyRenderTargets()
    {
        for (int i = 0; i < _currentRenderTargetCount; ++i)
        {
        	RenderTargetBinding binding = _currentRenderTargetBindings[i];
        	IRenderTarget target = (IRenderTarget)binding.getRenderTarget();
//            _currentRenderTargets[i] = target.getRenderTargetView(binding.getArraySlice());
        }

        // Use the depth from the first target.
        IRenderTarget renderTarget = (IRenderTarget)_currentRenderTargetBindings[0].getRenderTarget();
//        _currentDepthStencilView = renderTarget.GetDepthStencilView();

        // Set the targets.
//        lock (_d3dContext)
//            _d3dContext.OutputMerger.SetTargets(_currentDepthStencilView, _currentRenderTargets);

        return renderTarget;
    }

	private void platformBeginApplyState()
	{
//		Debug.Assert(_d3dContext != null, "The d3d context is null!");
	}

	private void platformApplyState(boolean applyShaders)
	{

	}

	private void platformDrawIndexedPrimitives(PrimitiveType primitiveType, int baseVertex, int startIndex, int primitiveCount)
	{
		throw new UnsupportedOperationException();
	}

	private <T> void platformDrawUserPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset, VertexDeclaration vertexDeclaration, int vertexCount)
	{
		throw new UnsupportedOperationException();
	}

	private void platformDrawPrimitives(PrimitiveType primitiveType, int vertexStart, int vertexCount)
	{
		throw new UnsupportedOperationException();
	}

	private <T> void platformDrawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset, int numVertices, short[] indexData, int indexOffset, int primitiveCount, VertexDeclaration vertexDeclaration)
	{
		applyState(true);
		
		// Note: For now we only support drawing quads from a triangle PrimitiveType
		if (primitiveType != PrimitiveType.TriangleList)
		{
			throw new UnsupportedOperationException("We currently only support PrimitiveType.TriangleList");
		}

		// TODO: add test code to make sure all 4 color information are the same ?.
		// TODO: check if not a quad ?
		VertexPositionColorTexture[] data = As.as(vertexData, VertexPositionColorTexture[].class);

		int destStartX, destStartY, destEndX, destEndY;
		int srcStartX, srcStartY, srcEndX, srcEndY;
		Texture2D texture = (Texture2D) getTextures().getTexture(0);
		Color tint;
		int i = 0;
//TimedBlock.beginTimedBlock("Draw");
		while (i < numVertices)
		{
			// TODO: rotation is stored in the position so need to use floats here.
			// TODO: Investigate how to use the positions to treat rotation.
			destStartX = (int) (data[i + 0].position.x);
			destStartY = (int) (data[i + 0].position.y);
			destEndX = (int) (data[i + 1].position.x);
			destEndY = (int) (data[i + 2].position.y);
			
			// TODO: Check if using floats is faster here.
			// NOTE: Add 0.5f so that the cast always return the right value (rounding error).
			srcStartX = (int) (data[i + 0].textureCoordinate.x * texture.width + 0.5f);
			srcStartY = (int) (data[i + 0].textureCoordinate.y * texture.height + 0.5f);
			srcEndX = (int) (data[i + 1].textureCoordinate.x * texture.width + 0.5f);
			srcEndY = (int) (data[i + 2].textureCoordinate.y * texture.height + 0.5f);
			
			tint = data[i + 0].color;

			Vector2 origin = new Vector2(data[i + 0].position.x, data[i + 0].position.y);
			Vector2 xAxis = new Vector2(data[i + 1].position.x - data[i + 0].position.x,
										data[i + 1].position.y - data[i + 0].position.y);
			Vector2 yAxis = new Vector2(data[i + 2].position.x - data[i + 0].position.x,
										data[i + 2].position.y - data[i + 0].position.y);
			
//			drawQuad(origin, xAxis, yAxis, srcStartX, srcStartY, srcEndX, srcEndY, texture.width, texture.height, texture.pixels, tint);
			drawQuad2(destStartX, destStartY, destEndX, destEndY, srcStartX, srcStartY, srcEndX, srcEndY, texture.width, texture.height, texture.getTexture(), tint);
			i += 4;
		}
//TimedBlock.endTimedBlock("Draw");
	}

	private <T> void platformDrawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset, int numVertices, int[] indexData, int indexOffset, int primitiveCount, VertexDeclaration vertexDeclaration)
	{
		throw new UnsupportedOperationException();
	}

	private static GraphicsProfile platformGetHighestSupportedGraphicsProfile(GraphicsDevice graphicsDevice)
	{
		return GraphicsProfile.Reach;
	}

	// NOTE: XNA uses premultiplied alpha by default and the color values in
	//		 the xnb files are premultiplied by the alpha channel.
	private void drawQuad(Vector2 origin, Vector2 xAxis, Vector2 yAxis,
						  int srcStartX, int srcStartY, int srcEndX, int srcEndY,
						  int srcWidth, int srcHeight, int[] srcPixels, Color tint)
	{
		// Note: Premultiply tint up front
		//Vector4 tintVec4 = Color.fromNonPremultiplied(tint.getRed(), tint.getGreen(), tint.getBlue(), tint.getAlpha()).toVector4();
		Vector4 tintVec4 = tint.toVector4();

		int oldWidth  = srcEndX - srcStartX;
		int oldHeight = srcEndY - srcStartY;

		// NOTE: Can't use the inv because of numerical imprecision which makes us fetch the wrong pixel
		//       sometimes for some ratio
//		float invXAxislengthSq = 1.0f / xAxis.lengthSquared();
//		float invYAxislengthSq = 1.0f / yAxis.lengthSquared();
		float xAxislengthSq = xAxis.lengthSquared();
		float yAxislengthSq = yAxis.lengthSquared();

		int screenWidth = this._viewport.getWidth();
		int screenHeight = this._viewport.getHeight();
		
		// check the bounds
		int minX = screenWidth;
		int maxX = 0;
		int minY = screenHeight;
		int maxY = 0;
		
		Vector2[] pos = new Vector2[]{ origin,
								   	   Vector2.add(origin, xAxis),
								   	   Vector2.add(origin, xAxis).add(yAxis),
								   	   Vector2.add(origin, yAxis) };
		for (int pIndex = 0; pIndex < pos.length; ++pIndex)
		{
			Vector2 testPos = pos[pIndex];
			int floorX = (int) Math.floor(testPos.x);
			int ceilX = (int) Math.ceil(testPos.x);
			int floorY = (int) Math.floor(testPos.y);
			int ceilY = (int) Math.ceil(testPos.y);
		
			if (minX > floorX) { minX = floorX; }
			if (minY > floorY) { minY = floorY; }
			if (maxX < ceilX)  { maxX = ceilX; }
			if (maxY < ceilY)  { maxY = ceilY; }
		}
		
		if (minX < 0) { minX = 0;}
		if (minY < 0) { minY = 0;}
		if (maxX > screenWidth)  { maxX = screenWidth; }
		if (maxY > screenHeight) { maxY = screenHeight; }
//TimedBlock.beginTimedBlock("ProcessPixel");
		// Cycle through all the sprites pixels. and apply tint and alpha-blending
		// Set our starting index position for the source Texture.
		int destRow = minX + minY * screenWidth;
		for (int y = minY; y < maxY; ++y)
		{
			int destIndex = destRow;
			for (int x = minX; x < maxX; ++x)
			{
				Vector2 pixelPos = new Vector2(x + 0.5f, y + 0.5f);
				Vector2 d = Vector2.subtract(pixelPos, origin);
				
				float edge0 = d.dotProduct(Vector2.negate(perp(xAxis)));
				float edge1 = Vector2.subtract(d, xAxis).dotProduct(Vector2.negate(perp(yAxis)));
				float edge2 = Vector2.subtract(d, xAxis).subtract(yAxis).dotProduct(perp(xAxis));
				float edge3 = Vector2.subtract(d, yAxis).dotProduct(perp(yAxis));

				if ((edge0 <= 0) &&
					(edge1 <= 0) &&
					(edge2 <= 0) &&
					(edge3 <= 0))
				{
					Vector4 foregroundColor;
					if (oldWidth != xAxis.x || oldHeight != yAxis.y)
					{
						// NOTE: Can't use the inv because of numerical imprecision which makes us fetch the wrong pixel
						//       sometimes for some ratio
//						float u = invXAxislengthSq * d.dotProduct(xAxis);
//						float v = invYAxislengthSq * d.dotProduct(yAxis);
						// NOTE: u and v ranges from 0 to 1 into the source texture
						float u = d.dotProduct(xAxis) / xAxislengthSq;
						float v = d.dotProduct(yAxis) / yAxislengthSq;
						
						float tX, tY;
						int fetchX, fetchY;
						// TODO: Add other resizing methods (see TextureFilter.java)
						switch (this.samplerStates.getSamplerStateCollection(0).getFilter())
						{
							case Point:
								// NOTE: Re-base our texture x to the [0-width] range and y to the [0-height] range
								tX = u * (float) (oldWidth);
								tY = v * (float) (oldHeight);
	
								fetchX = (int) tX + srcStartX;
								fetchY = (int) tY + srcStartY;
					
								int texelPtr = srcPixels[fetchX + fetchY * srcWidth];
								foregroundColor = new Vector4((float) ((texelPtr >> 16) & 0xFF),
				 					 	 					  (float) ((texelPtr >>  8) & 0xFF),
				 					 	 					  (float) ((texelPtr >>  0) & 0xFF),
				 					 	 					  (float) ((texelPtr >> 24) & 0xFF));
								foregroundColor = Vector4.divide(foregroundColor, 255);
								break;
								
							case Linear:
							default:
								// NOTE: Re-base our texture x to the [0-width] range and y to the [0-height] range
								tX = u * (float) (oldWidth) - 0.5f;
								tY = v * (float) (oldHeight) - 0.5f;
	
								fetchX = (int) tX;
								fetchY = (int) tY;
					
								float xDiff = tX - (float) fetchX;
								float yDiff = tY - (float) fetchY;
					
								// NOTE: Need to adjust the u and v here for when we use spriteSheets or spriteStrips
								fetchX += srcStartX;
								fetchY += srcStartY;
								
								int texelPtrA, texelPtrB, texelPtrC, texelPtrD;
								
								// TODO: Validate if it is possible to have different values for u, v and w
								//       and treat them separately if so.
								switch (this.samplerStates.getSamplerStateCollection(0).getAddressU())
								{
									case Wrap:
										int nextU = 1;
										if (xDiff < 0)
										{
											nextU = -1;
											xDiff = -xDiff;
										}
										int nextV = 1;
										if (yDiff < 0)
										{
											nextV = -1;
											yDiff = -yDiff;
										}
										texelPtrA = srcPixels[fetchX + fetchY * srcWidth];
										texelPtrB = srcPixels[((fetchX + nextU + srcWidth) % srcWidth) + (fetchY * srcWidth)];
										texelPtrC = srcPixels[fetchX + (((fetchY + nextV + srcHeight) % srcHeight) * srcWidth)];
										texelPtrD = srcPixels[((fetchX + nextU + srcWidth) % srcWidth) + (((fetchY + nextV + srcHeight) % srcHeight) * srcWidth)];
										break;
									// NOTE: When using spriteSheets, MonoGame allows blending of the last row or column of pixel in the current sprite
									//		 with the first row or column of the next sprite in the sheet.  This doesn't seem to be a good idea if the
									//		 sprites are tightly packed (i.e.: no fully translucent pixels on the boundary) so we don't allow it in our
									//		 software renderer.
									// TODO: Validate the note compared to OpenGL when we implement it. We probably want this to be the same on both renderer.
									case Clamp:
									default:
										if (xDiff < 0) xDiff = 0;
										if (yDiff < 0) yDiff = 0;
										int srcIndex = (fetchX + fetchY * srcWidth);
										texelPtrA = srcPixels[srcIndex];
										// NOTE: Need to MOD u by oldWidth and v by oldHeight to adjust the the boundary for when we use spriteSheets or spriteStrips
										texelPtrB = srcPixels[srcIndex + (((fetchX % oldWidth) == (oldWidth - 1)) ? 0 : 1)];
										texelPtrC = srcPixels[srcIndex + (((fetchY % oldHeight) == (oldHeight -1)) ? 0 : srcWidth)];
										texelPtrD = srcPixels[srcIndex + (((fetchY % oldHeight) == (oldHeight -1)) ? 0 : srcWidth) + (((fetchX % oldWidth) == (oldWidth - 1)) ? 0 : 1)];
										break;
								}
								
								Vector4 texelA = new Vector4((float) ((texelPtrA >> 16) & 0xFF),
									 					 	 (float) ((texelPtrA >>  8) & 0xFF),
									 					 	 (float) ((texelPtrA >>  0) & 0xFF),
									 					 	 (float) ((texelPtrA >> 24) & 0xFF));
								Vector4 texelB = new Vector4((float) ((texelPtrB >> 16) & 0xFF),
									 					 	 (float) ((texelPtrB >>  8) & 0xFF),
									 					 	 (float) ((texelPtrB >>  0) & 0xFF),
									 					 	 (float) ((texelPtrB >> 24) & 0xFF));
								Vector4 texelC = new Vector4((float) ((texelPtrC >> 16) & 0xFF),
									 					 	 (float) ((texelPtrC >>  8) & 0xFF),
									 					 	 (float) ((texelPtrC >>  0) & 0xFF),
									 					 	 (float) ((texelPtrC >> 24) & 0xFF));
								Vector4 texelD = new Vector4((float) ((texelPtrD >> 16) & 0xFF),
									 					 	 (float) ((texelPtrD >>  8) & 0xFF),
									 					 	 (float) ((texelPtrD >>  0) & 0xFF),
									 					 	 (float) ((texelPtrD >> 24) & 0xFF));
					
								// NOTE: Go from sRGB to "linear light" space
//								texelA = sRGB255ToLinear1(texelA);
//								texelB = sRGB255ToLinear1(texelB);
//								texelC = sRGB255ToLinear1(texelC);
//								texelD = sRGB255ToLinear1(texelD);
								texelA = Vector4.divide(texelA, 255);
								texelB = Vector4.divide(texelB, 255);
								texelC = Vector4.divide(texelC, 255);
								texelD = Vector4.divide(texelD, 255);
					
								foregroundColor = Vector4.lerp(Vector4.lerp(texelA, texelB, xDiff),
															   Vector4.lerp(texelC, texelD, xDiff),
															   yDiff);
						}
					}
					else
					{
						// NOTE: Re-base our texture x to the [0-width] range and y to the [0-height] range
						//       and add the offset for when we use spriteSheets or spriteStrips.
						int fetchX = x - (int) origin.x + srcStartX;
						int fetchY = y - (int) origin.y + srcStartY;
			
						int texelPtr = srcPixels[fetchX + fetchY * srcWidth];
						foregroundColor = new Vector4((float) ((texelPtr >> 16) & 0xFF),
		 	 					  					  (float) ((texelPtr >>  8) & 0xFF),
		 	 					  					  (float) ((texelPtr >>  0) & 0xFF),
		 	 					  					  (float) ((texelPtr >> 24) & 0xFF));
						foregroundColor = Vector4.divide(foregroundColor, 255);
					}
					
					// tint
					foregroundColor = Vector4.multiply(foregroundColor, tintVec4);
		
					Vector4 dest = new Vector4((float) ((pixels[x + y * screenWidth] >> 16) & 0xFF),
										   	   (float) ((pixels[x + y * screenWidth] >>  8) & 0xFF),
										   	   (float) ((pixels[x + y * screenWidth] >>  0) & 0xFF),
										   	   (float) ((pixels[x + y * screenWidth] >> 24) & 0xFF));
		
					// NOTE: Go from sRGB to "linear" brightness space
//					dest = sRGB255ToLinear1(dest);
					dest = Vector4.divide(dest, 255);
		
					Vector4 blended = Vector4.add(Vector4.multiply(dest, (1.0f - foregroundColor.w)), foregroundColor);
					blended = Vector4.clamp(blended, Vector4.zero(), Vector4.one());

					// NOTE: Go from "linear light" to sRGB space
//					Vector4 blended255 = linear1ToSRGB255(blended);
					Vector4 blended255 = Vector4.multiply(blended, 255);
		
					pixels[destIndex] = ((((int) blended255.w) << 24) & 0xff000000) |
									  	  ((((int) blended255.x) << 16) & 0xff0000)	|
									  	  ((((int) blended255.y) << 8) & 0xff00) |
									  	  ((int) blended255.z);
				}
				++destIndex;
			}
			destRow += screenWidth;
		}
//TimedBlock.endTimedBlock_Counted("ProcessPixel", (maxX - minX + 1) * (maxY - minY + 1));;
	}

	// TODO: This method is faster but I need to add rotation handling to it
	private void drawQuad2(int destStartX, int destStartY, int destEndX, int destEndY,
			  			   int srcStartX, int srcStartY, int srcEndX, int srcEndY,
			  			   int srcWidth, int srcHeight, int[] srcPixels, Color tint)
	{
		int screenWidth = this._viewport.getWidth();
		int screenHeight = this._viewport.getHeight();

		int oldWidth  = srcEndX - srcStartX;
		int oldHeight = srcEndY - srcStartY;
		int newWidth  = destEndX - destStartX;
		int newHeight = destEndY - destStartY;
		
		float xRatio = (float) oldWidth / (float) newWidth;
		float yRatio = (float) oldHeight / (float) newHeight;

		int minX = srcStartX;
		int minY = srcStartY;
		int maxX = minX + newWidth;
		int maxY = minY + newHeight;

		// check the bounds
		if (destStartX < 0)
		{
			minX += -destStartX;
			destStartX = 0;
		}
		if (destEndX > screenWidth)
		{
			maxX -= destEndX - screenWidth;
			destEndX = screenWidth;
		}
		if (destStartY < 0)
		{
			minY += -destStartY;
			destStartY = 0;
		}
		if (destEndY > screenHeight)
		{
			maxY -= destEndY - screenHeight;
			destEndY = screenHeight;
		}

		// Extract our tint components
		short rTint = tint.getRed();
		short gTint = tint.getGreen();
		short bTint = tint.getBlue();
		short aTint = tint.getAlpha();
//TimedBlock.beginTimedBlock("ProcessPixel");
		// Cycle through all the sprites pixels. and apply tint and alpha-blending
		// Set our starting index position for the source Texture.
		int destRow = destStartX + destStartY * screenWidth;
		for (int y = minY; y < maxY; ++y, destRow += screenWidth)
		{
			int destIndex = destRow;
			for (int x = minX; x < maxX; ++x)
			{
				// The color of the pixel about to be drawn.
				int srcColor;
				
				// The components of the pixel about to be drawn.
				int srcR, srcG, srcB, srcA;

				if (oldWidth != newWidth || oldHeight != newHeight)
				{
					int u, v;
					// TODO: Add other resizing methods (see TextureFilter.java)
					switch (this.samplerStates.getSamplerStateCollection(0).getFilter())
					{
						case Point:
							if (newWidth < 0 || newHeight < 0)
								throw new IllegalArgumentException("new width or new height cannot be less than 0");

							u = (int) (xRatio * ((x - srcStartX) + 0.5f)) + srcStartX;
							v = (int) (yRatio * ((y - srcStartY) + 0.5f)) + srcStartY;
							srcColor = srcPixels[u + v * srcWidth];
//							foregroundCol = srcPixels[(int) (((xRatio * ((x - srcStartX) + 0.5f))) + srcStartX) + (int) (((yRatio * ((y - srcStartY) + 0.5f))) + srcStartY) * srcWidth];
							srcR = (srcColor >> 16) & 0xff;
							srcG = (srcColor >> 8) & 0xff;
							srcB = (srcColor) & 0xff;
							srcA = (srcColor >> 24) & 0xff;
							break;

						case Linear:
						default:
							if (newWidth < 0 || newHeight < 0)
								throw new IllegalArgumentException("new width or new height cannot be less than 0");
							if (srcStartX < 0 || srcStartY < 0)
								throw new IllegalArgumentException("startX or startY cannot be less than 0");

							int texelA, texelB, texelC, texelD, srcIndex;
							float x_diff, y_diff;

							// NOTE: this formula was found by reverse engineering what MonoGame does
							//       so I can get the exact same result.
							float rU = -0.5f + (xRatio * ((x - srcStartX) + 0.5f));
							float rV = -0.5f + (yRatio * ((y - srcStartY) + 0.5f));
							u = (int) (rU);
							v = (int) (rV);
							x_diff = rU - u;
							y_diff = rV - v;

							// NOTE: Need to adjust the u and v here for when we use spriteSheets or spriteStrips
							u += srcStartX;
							v += srcStartY;

							// TODO: Validate if it is possible to have different values for u, v and w
							//       and treat them separately if so.
							switch (this.samplerStates.getSamplerStateCollection(0).getAddressU())
							{
								case Wrap:
									int nextU = 1;
									if (x_diff < 0)
									{
										nextU = -1;
										x_diff = -x_diff;
									}
									int nextV = 1;
									if (y_diff < 0)
									{
										nextV = -1;
										y_diff = -y_diff;
									}
									texelA = srcPixels[u + v * srcWidth];
									texelB = srcPixels[((u + nextU + srcWidth) % srcWidth) + (v * srcWidth)];
									texelC = srcPixels[u + (((v + nextV + srcHeight) % srcHeight) * srcWidth)];
									texelD = srcPixels[((u + nextU + srcWidth) % srcWidth) + (((v + nextV + srcHeight) % srcHeight) * srcWidth)];
									break;
								// NOTE: When using spriteSheets, MonoGame allows blending of the last row or column of pixel in the current sprite
								//		 with the first row or column of the next sprite in the sheet.  This doesn't seem to be a good idea if the
								//		 sprites are tightly packed (i.e.: no fully translucent pixels on the boundary) so we don't allow it in our
								//		 software renderer.
								// TODO: Validate the note compared to OpenGL when we implement it. We probably want this to be the same on both renderer.
								case Clamp:
								default:
									if (x_diff < 0) x_diff = 0;
									if (y_diff < 0) y_diff = 0;
									srcIndex = (u + v * srcWidth);
									texelA = srcPixels[srcIndex];
									// NOTE: Need to MOD u by oldWidth and v by oldHeight to adjust the the boundary for when we use spriteSheets or spriteStrips
									texelB = srcPixels[srcIndex + (((u % oldWidth) == (oldWidth - 1)) ? 0 : 1)];
									texelC = srcPixels[srcIndex + (((v % oldHeight) == (oldHeight -1)) ? 0 : srcWidth)];
									texelD = srcPixels[srcIndex + (((v % oldHeight) == (oldHeight -1)) ? 0 : srcWidth) + (((u % oldWidth) == (oldWidth - 1)) ? 0 : 1)];
									break;
							}

							float b = MathHelper.lerp(MathHelper.lerp(texelA & 0xff, texelB & 0xff, x_diff),
													  MathHelper.lerp(texelC & 0xff, texelD & 0xff, x_diff),
													  y_diff);
							float g = MathHelper.lerp(MathHelper.lerp((texelA >> 8) & 0xff, (texelB >> 8) & 0xff, x_diff),
													  MathHelper.lerp((texelC >> 8) & 0xff, (texelD >> 8) & 0xff, x_diff),
													  y_diff);
							float r = MathHelper.lerp(MathHelper.lerp((texelA >> 16) & 0xff, (texelB >> 16) & 0xff, x_diff),
													  MathHelper.lerp((texelC >> 16) & 0xff, (texelD >> 16) & 0xff, x_diff),
													  y_diff);
							float a = MathHelper.lerp(MathHelper.lerp((texelA >> 24) & 0xff, (texelB >> 24)& 0xff, x_diff),
													  MathHelper.lerp((texelC >> 24)& 0xff, (texelD >> 24)& 0xff, x_diff),
													  y_diff);

							// NOTE: Add 0.5f before casting so it rounds properly
							b += 0.5f;
							g += 0.5f;
							r += 0.5f;
							a += 0.5f;

//							srcColor = ((((int) a) << 24) & 0xff000000) | ((((int) r) << 16) & 0xff0000) | ((((int) g) << 8) & 0xff00) | ((int) b);
							srcR = (int) r;
							srcG = (int) g;
							srcB = (int) b;
							srcA = (int) a;
							break;
					}
				}
				else
				{
					srcColor = srcPixels[x + y * srcWidth];
					srcR = (srcColor >> 16) & 0xff;
					srcG = (srcColor >> 8) & 0xff;
					srcB = (srcColor) & 0xff;
					srcA = (srcColor >> 24) & 0xff;
				}

				// The components of the pixel about to be drawn.
//				srcR = (srcColor >> 16) & 0xff;
//				srcG = (srcColor >> 8) & 0xff;
//				srcB = (srcColor) & 0xff;
//				srcA = (srcColor >> 24) & 0xff;
	
				// Using Color.WHITE would have no effect so skip this
				if (!tint.equals(Color.White))
				{
					// Typical tint formula -> original component * tint / 255
					// also works with premultiplied alpha
					srcR = srcR * rTint / 255;
					srcG = srcG * gTint / 255;
					srcB = srcB * bTint / 255;
					srcA = srcA * aTint / 255;
				}

				// The resulting color of the pixel to be drawn
				int col;

				// If alpha is 255 it completely overrides the existing color.
				if (srcA == 255 || _blendState == BlendState.Opaque)
				{
					col = (srcA << 24 & 0xff000000) | (srcR << 16 & 0x00ff0000) |
						  (srcG << 8 & 0x0000ff00) | (srcB & 0x000000ff);
				}
				else if (_blendState == BlendState.AlphaBlend)
				{
					// The color of the pixel already there.
					int backgroundCol = pixels[destIndex];
					
					// Do the alpha-blending with the premultiplied alpha source.
					int backgroundR = (backgroundCol >> 16) & 0xff;
					int backgroundG = (backgroundCol >> 8) & 0xff;
					int backgroundB = (backgroundCol) & 0xff;
					// Typical over blend formula
					int r = srcR + (backgroundR * (255 - srcA) / 255);
					int g = srcG + (backgroundG * (255 - srcA) / 255);
					int b = srcB + (backgroundB * (255 - srcA) / 255);
		
					// NOTE: since this can produce a value greater than 255 for a single
					// channel we need to clamp those values.
					r = r > 255 ? 255 : r;
					g = g > 255 ? 255 : g;
					b = b > 255 ? 255 : b;

					col = r << 16 | g << 8 | b;
				}
				else
				{
					// TODO: Should do the other BlendStates
					// Defaults to BlendState.OPAQUE.
					col = srcA << 24 | srcR << 16 | srcG << 8 | srcB;
				}
				pixels[destIndex] = col;
				++destIndex;
			}
			// destRow += screenWidth;
		}
//TimedBlock.endTimedBlock_Counted("ProcessPixel", (maxX - minX + 1) * (maxY - minY + 1));;
	}

/*	private void drawQuad3(int destStartX, int destStartY, int destEndX, int destEndY,
	  					  int srcStartX, int srcStartY, int srcEndX, int srcEndY,
	  					  int srcWidth, int srcHeight, int[] srcPixels, Color tint)
	{
		// Note: Premultiply tint up front
		//Vector4 tintVec4 = Color.fromNonPremultiplied(tint.getRed(), tint.getGreen(), tint.getBlue(), tint.getAlpha()).toVector4();
		Vector4 tintVec4 = tint.toVector4();

		Vector2 origin = new Vector2(destStartX, destStartY);
		Vector2 xAxis = new Vector2(destEndX - destStartX, 0f);
		Vector2 yAxis = new Vector2(0f, destEndY - destStartY);
		Rectangle clipRect = new Rectangle();

		float xAxisLength = xAxis.length();
		float yAxisLength = yAxis.length();

		Vector2 NxAxis = xAxis.multiply(yAxisLength / xAxisLength);
		Vector2 NyAxis = yAxis.multiply(xAxisLength / yAxisLength);

		float invXAxislengthSq = 1.0f / xAxis.lengthSquared();
		float invYAxislengthSq = 1.0f / yAxis.lengthSquared();

		Rectangle fillRect = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

		//int screenWidth = this._viewport.getWidth();
		//int screenHeight = this._viewport.getHeight();

		// check the bounds
		//int xMin = screenWidth;
		//int xMax = 0;
		//int yMin = screenHeight;
		//int yMax = 0;

		Vector2[] pos = new Vector2[]{ origin,
				   					   Vector2.add(origin, xAxis),
				   					   Vector2.add(origin, xAxis).add(yAxis),
				   					   Vector2.add(origin, yAxis) };
		for (int pIndex = 0; pIndex < pos.length; ++pIndex)
		{
			Vector2 testPos = pos[pIndex];
			int floorX = (int) Math.floor(testPos.x);
			int ceilX = (int) Math.ceil(testPos.x);
			int floorY = (int) Math.floor(testPos.y);
			int ceilY = (int) Math.ceil(testPos.y);

			//if (xMin > floorX) { xMin = floorX; }
			//if (yMin > floorY) { yMin = floorY; }
			//if (xMax < ceilX)  { xMax = ceilX; }
			//if (yMax < ceilY)  { yMax = ceilY; }
			if (fillRect.x > floorX) { fillRect.x = floorX; }
			if (fillRect.y > floorY) { fillRect.y = floorY; }
			if (fillRect.right()  < ceilX) { fillRect.width = ceilX - fillRect.x; }
			if (fillRect.bottom() < ceilY) { fillRect.height = ceilY - fillRect.y; }
		}

		fillRect = Rectangle.intersect(clipRect, fillRect);
		//if (xMin < 0) { xMin = 0;}
		//if (yMin < 0) { yMin = 0;}
		//if (xMax > screenWidth)  { xMax = screenWidth; }
		//if (yMax > screenHeight) { yMax = screenHeight; }

		if (fillRect.width > 0 && fillRect.height > 0)
		{
			Vector2 nXAxis = Vector2.multiply(xAxis, invXAxislengthSq);
			Vector2 nYAxis = Vector2.multiply(yAxis, invYAxislengthSq);

			// Cycle through all the sprites pixels. and apply tint and alpha-blending
			// Set our starting index position for the source Texture.
			int destRow = fillRect.x + fillRect.y * srcWidth;
			int yMin = fillRect.y;
			int yMax = fillRect.y + fillRect.height;
			int xMin = fillRect.x;
			int xMax = fillRect.x + fillRect.width;
			for (int y = yMin; y < yMax; ++y)
			{
				float pixelPy = (float) y;
				pixelPy -= origin.y;
				float PynX = pixelPy * nXAxis.y;
				float PynY = pixelPy * nYAxis.y;
				float pixelPx = (float) xMin;
				pixelPx -= origin.x;
				for (int x = xMin; x < xMax; ++x)
				{
					float u = (pixelPx * nXAxis.x) + PynX;
					float v = (pixelPx * nYAxis.x) + PynY;

//TODO: rendu ici dans drawRectangleQuickly
					float tX = u * (float) (texture.width);
					float tY = v * (float) (texture.height);

					int xx = (int) tX;
					int yy = (int) tY;

					float fX = tX - (float)xx;
					float fY = tY - (float)yy;

					// TODO: Delete these assert
					assert((xx >= 0) && (xx < texture.width));
					assert((yy >= 0) && (yy < texture.height));

					int srcIndex = (xx + yy * texture.width);
					int texelPtrA = texture.pixels[srcIndex];
					int texelPtrB = texture.pixels[srcIndex + ((xx == (texture.width - 1)) ? 0 : 1)];//((xx % (texture.width -1) == 0) ? 0 : 1)];
					int texelPtrC = texture.pixels[srcIndex + ((yy == (texture.height -1)) ? 0 : texture.height)];//((yy % (texture.height -1) == 0) ? 0 : texture.width)];
					int texelPtrD = texture.pixels[srcIndex + ((yy == (texture.height -1)) ? 0 : texture.height) + ((xx == (texture.width - 1)) ? 0 : 1)];// ((yy % (texture.height -1) == 0) ? 0 : texture.width) + ((xx % (texture.width -1) == 0) ? 0 : 1)];

					Vector4 texelA = new Vector4((float) ((texelPtrA >> 16) & 0xFF),
							 					 (float) ((texelPtrA >>  8) & 0xFF),
							 					 (float) ((texelPtrA >>  0) & 0xFF),
							 					 (float) ((texelPtrA >> 24) & 0xFF));
					Vector4 texelB = new Vector4((float) ((texelPtrB >> 16) & 0xFF),
							 					 (float) ((texelPtrB >>  8) & 0xFF),
							 					 (float) ((texelPtrB >>  0) & 0xFF),
							 					 (float) ((texelPtrB >> 24) & 0xFF));
					Vector4 texelC = new Vector4((float) ((texelPtrC >> 16) & 0xFF),
							 					 (float) ((texelPtrC >>  8) & 0xFF),
							 					 (float) ((texelPtrC >>  0) & 0xFF),
							 					 (float) ((texelPtrC >> 24) & 0xFF));
					Vector4 texelD = new Vector4((float) ((texelPtrD >> 16) & 0xFF),
							 					 (float) ((texelPtrD >>  8) & 0xFF),
							 					 (float) ((texelPtrD >>  0) & 0xFF),
							 					 (float) ((texelPtrD >> 24) & 0xFF));

					// NOTE: Go from sRGB to "linear light" space
//					texelA = sRGB255ToLinear1(texelA);
//					texelB = sRGB255ToLinear1(texelB);
//					texelC = sRGB255ToLinear1(texelC);
//					texelD = sRGB255ToLinear1(texelD);
					texelA = Vector4.divide(texelA, 255);
					texelB = Vector4.divide(texelB, 255);
					texelC = Vector4.divide(texelC, 255);
					texelD = Vector4.divide(texelD, 255);

					Vector4 texel;
					// TODO: Add other resizing methods (see TextureFilter.java)
					switch (this.samplerStates.getSamplerStateCollection(0).getFilter())
					{
						case Linear:
							texel = Vector4.lerp(Vector4.lerp(texelA, texelB, fX),
												 Vector4.lerp(texelC, texelD, fX),
												 fY);
							break;
//						case Point:
//							break;
						default:
							texel = Vector4.lerp(Vector4.lerp(texelA, texelB, fX),
									Vector4.lerp(texelC, texelD, fX),
									fY);
							break;
					}

					// tint
					texel = Vector4.multiply(texel, tintVec4);

					Vector4 dest = new Vector4((float) ((pixels[x + y * screenWidth] >> 16) & 0xFF),
											   (float) ((pixels[x + y * screenWidth] >>  8) & 0xFF),
											   (float) ((pixels[x + y * screenWidth] >>  0) & 0xFF),
											   (float) ((pixels[x + y * screenWidth] >> 24) & 0xFF));

					// NOTE: Go from sRGB to "linear" brightness space
//					dest = sRGB255ToLinear1(dest);
					dest = Vector4.divide(dest, 255);

					Vector4 blended = Vector4.add(Vector4.multiply(dest, (1.0f - texel.w)), texel);

					// NOTE: Go from "linear light" to sRGB space
//					Vector4 blended255 = linear1ToSRGB255(blended);
					Vector4 blended255 = Vector4.multiply(blended, 255);

					pixels[destIndex++] = ((((int) blended255.w) << 24) & 0xff000000) |
										  ((((int) blended255.x) << 16) & 0xff0000)	|
										  ((((int) blended255.y) << 8) & 0xff00) |
										  ((int) blended255.z);
				}
				destRow += screenWidth;
			}
		}
	}*/

	private Vector4	sRGB255ToLinear1(Vector4 c)
	{
		Vector4 result = new Vector4();

		float inv255 = 1.0f / 255.0f;

		result.x = (inv255 * c.x) * (inv255 * c.x);
		result.y = (inv255 * c.y) * (inv255 * c.y);
		result.z = (inv255 * c.z) * (inv255 * c.z);
		result.w = inv255 * c.w;

		return result;
	}

	private Vector4	linear1ToSRGB255(Vector4 c)
	{
		Vector4 result = new Vector4();

		float one255 = 255.0f;

		result.x = (float) (one255 * Math.sqrt(c.x));
		result.y = (float) (one255 * Math.sqrt(c.y));
		result.z = (float) (one255 * Math.sqrt(c.z));
		result.w = 255.0f * c.w;

		return result;
	}

	// TODO: should add this to the Vector class
	private Vector2 perp(Vector2 a)
	{
		return new Vector2(-a.y, a.x);
	}
}
