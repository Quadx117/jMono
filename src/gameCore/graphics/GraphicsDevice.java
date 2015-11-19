package gameCore.graphics;

import gameCore.Color;
import gameCore.GraphicsDeviceInformation;
import gameCore.Rectangle;
import gameCore.dotNet.As;
import gameCore.dotNet.events.Event;
import gameCore.dotNet.events.EventArgs;
import gameCore.graphics.effect.Effect;
import gameCore.graphics.shader.ConstantBuffer;
import gameCore.graphics.shader.ConstantBufferCollection;
import gameCore.graphics.shader.Shader;
import gameCore.graphics.shader.ShaderStage;
import gameCore.graphics.states.BlendState;
import gameCore.graphics.states.DepthFormat;
import gameCore.graphics.states.DepthStencilState;
import gameCore.graphics.states.RasterizerState;
import gameCore.graphics.vertices.IVertexType;
import gameCore.graphics.vertices.IndexBuffer;
import gameCore.graphics.vertices.PrimitiveType;
import gameCore.graphics.vertices.VertexBuffer;
import gameCore.graphics.vertices.VertexDeclaration;
import gameCore.graphics.vertices.VertexDeclarationCache;
import gameCore.graphics.vertices.VertexPositionColorTexture;
import gameCore.math.Vector4;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicsDevice implements AutoCloseable
{
	// TODO: I added this for the software renderer, see where this should go.
	/** The array of pixels to be painted on the screen */
	public int[] pixels;

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

	public GraphicsCapabilities getGraphicsCapabilities()
	{
		return graphicsCapabilities;
	}

	private TextureCollection vertexTextures;

	public TextureCollection getVertexTextures()
	{
		return vertexTextures;
	}

	private SamplerStateCollection vertexSamplerStates;

	public SamplerStateCollection getVertexSamplerStates()
	{
		return samplerStates;
	}

	private TextureCollection textures;

	public TextureCollection getTextures()
	{
		return vertexTextures;
	}

	private SamplerStateCollection samplerStates;

	public SamplerStateCollection getSamplerStates()
	{
		return samplerStates;
	}

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

	private ConstantBufferCollection _vertexConstantBuffers = new ConstantBufferCollection(ShaderStage.Vertex, 16);
	private ConstantBufferCollection _pixelConstantBuffers = new ConstantBufferCollection(ShaderStage.Pixel, 16);

	/**
	 * The cache of effects from unique byte streams.
	 */
	public Map<Integer, Effect> effectCache;

	// Resources may be added to and removed from the list from many threads.
	private Object _resourcesLock = new Object();

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

	// TODO: This is initialized in other GraphicsDevice files
	protected int maxTextureSlots = 16;
	protected int maxVertexTextureSlots = 16;

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

	public GraphicsAdapter getAdapter()
	{
		return adapter;
	}

	protected GraphicsMetrics _graphicsMetrics = new GraphicsMetrics();

	// / <summary>
	// / The rendering information for debugging and profiling.
	// / The metrics are reset every frame after draw within <see cref="GraphicsDevice.Present"/>.
	// / </summary>
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
	public GraphicsDevice(GraphicsAdapter adapter, GraphicsProfile graphicsProfile,
			PresentationParameters presentationParameters)
	{
		this.adapter = adapter;

		if (presentationParameters == null)
			throw new NullPointerException("presentationParameters");
		this.presentationParameters = presentationParameters;
		setup();
		this.graphicsCapabilities = new GraphicsCapabilities(this);
		setGraphicsProfile(graphicsProfile);
		initialize();
		
		// TODO: I added this for the software renderer. Check where that would go
		pixels = new int[this.getViewport().getWidth() * this.getViewport().getHeight()];
		// end TODO
	}

	private void setup()
	{
		// Initialize the main viewport
		// TODO: Need to change this to #if WINDOWS
		_viewport = new Viewport(0, 0, getDisplayMode().getWidth(), getDisplayMode().getHeight());
		_viewport.setMaxDepth(1.0f);

		// TODO: See other GraphicsDevice files
		// PlatformSetup();

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
		// TODO: See other GraphicsDevice files
		// PlatformInitialize();

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
			throw new UnsupportedOperationException(
					"Cannot set RasterizerState.DepthClipEnable to false on this graphics device");

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

	public BlendState getBlendState()
	{
		return _blendState;
	}

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

	public DepthStencilState getDepthStencilState()
	{
		return _depthStencilState;
	}

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
		// TODO: see other GraphicsDevice files
		// PlatformBeginApplyState();

		if (_blendStateDirty)
		{
			// TODO: see other GraphicsDevice files
			// _actualBlendState.PlatformApplyState(this);
			_blendStateDirty = false;
		}

		if (_depthStencilStateDirty)
		{
			// TODO: see other GraphicsDevice files
			// _actualDepthStencilState.PlatformApplyState(this);
			_depthStencilStateDirty = false;
		}

		if (_rasterizerStateDirty)
		{
			// TODO: see other GraphicsDevice files
			// _actualRasterizerState.PlatformApplyState(this);
			_rasterizerStateDirty = false;
		}

		// TODO: see other GraphicsDevice files
		// PlatformApplyState(applyShaders);
	}

	public void clear(Color color)
	{
		int options = ClearOptions.Target.getValue() |		//
					  ClearOptions.DepthBuffer.getValue() |	//
					  ClearOptions.Stencil.getValue();
		// TODO: See other GraphicsDevice files
		// PlatformClear(options, color.toVector4(), _viewport.maxDepth, 0);
		// TODO: Test code not part of the original code.
		for (int i = 0; i < pixels.length; ++i)
		{
			pixels[i] = color.getPackedValue();
		}
	}

	public void clear(ClearOptions options, Color color, float depth, int stencil)
	{
		// TODO: See other GraphicsDevice files
		// PlatformClear(options, color.toVector4(), depth, stencil);
	}

	public void clear(ClearOptions options, Vector4 color, float depth, int stencil)
	{
		// TODO: See other GraphicsDevice files
		// PlatformClear(options, color, depth, stencil);
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
				// Dispose of all remaining graphics resources before disposing of the graphics
				// device
				synchronized (_resourcesLock)
				{
					for (WeakReference<?> resource : _resources)
					{
						AutoCloseable target = (AutoCloseable) resource.get();
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

				// platformDispose();
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
		// TODO: See other GraphicsDevice files
		// PlatformPresent();
	}

	// NOTE: Already commented out in the original code
	/*
	 * public void Present(Rectangle? sourceRectangle, Rectangle? destinationRectangle, IntPtr
	 * overrideWindowHandle)
	 * {
	 * throw new NotImplementedException();
	 * }
	 * 
	 * public void Reset()
	 * {
	 * // Manually resetting the device is not currently supported.
	 * throw new NotImplementedException();
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
	 * throw new NotImplementedException();
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
				GraphicsResource target = (GraphicsResource) resource.get();
				if (target != null)
					target.graphicsDeviceResetting();
			}

			// Remove references to resources that have been garbage collected.
			_resources.removeIf(wr -> wr.get() != null);
		}
	}

	// / <summary>
	// / Trigger the DeviceReset event to allow games to be notified of a device reset.
	// / Currently protected to allow the various platforms to send the event at the appropriate
	// time.
	// / </summary>
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

	public PresentationParameters getPresentationParameters()
	{
		return presentationParameters;
	}

	public Viewport getViewport()
	{
		return _viewport;
	}

	public void setViewport(Viewport value)
	{
		_viewport = value;
		// TODO: See other GraphicsDevice files
		// PlatformSetViewport(value);
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
			throw new UnsupportedOperationException(String.format(
					"Could not find a graphics device that supports the %s profile", value.toString()));
		_graphicsProfile = value;
		graphicsCapabilities.initialize(this);
	}

	public Rectangle getScissorRectangle()
	{
		return _scissorRectangle;
	}

	public void setScissorRectangle(Rectangle value)
	{
		if (_scissorRectangle == value)
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

		// platformResolveRenderTargets();

		// Clear the current bindings.
		// Array.Clear(_currentRenderTargetBindings, 0, _currentRenderTargetBindings.Length);
		Arrays.fill(_currentRenderTargetBindings, 0, _currentRenderTargetBindings.length, null);

		int renderTargetWidth;
		int renderTargetHeight;
		if (renderTargets == null)
		{
			_currentRenderTargetCount = 0;

			// TODO: See other GraphicsDevice files
			// PlatformApplyDefaultRenderTarget();
			clearTarget = presentationParameters.renderTargetUsage == RenderTargetUsage.DiscardContents;

			renderTargetWidth = presentationParameters.getBackBufferWidth();
			renderTargetHeight = presentationParameters.getBackBufferHeight();
		}
		else
		{
			// Copy the new bindings.
			// Array.Copy(renderTargets, _currentRenderTargetBindings, renderTargets.Length);
			_currentRenderTargetBindings = Arrays.copyOf(renderTargets, renderTargets.length);

			_currentRenderTargetCount = renderTargets.length;

			// RenderTargets renderTarget = platformApplyRenderTargets();
			IRenderTarget renderTarget = (IRenderTarget) _currentRenderTargetBindings[0].getRenderTarget();

			// We clear the render target if asked.
			clearTarget = renderTarget.getRenderTargetUsage() == RenderTargetUsage.DiscardContents;

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

	public IndexBuffer getIndices()
	{
		return _indexBuffer;
	}

	public void setIndices(IndexBuffer value)
	{
		setIndexBuffer(value);
	}

	protected Shader getVertexShader()
	{
		return _vertexShader;
	}

	public void setVertexShader(Shader value)
	{
		if (_vertexShader == value)
			return;

		_vertexShader = value;
		_vertexShaderDirty = true;
	}

	protected Shader getPixelShader()
	{
		return _pixelShader;
	}

	public void setPixelShader(Shader value)
	{
		if (_pixelShader == value)
			return;

		_pixelShader = value;
		_pixelShaderDirty = true;
	}

	public void setConstantBuffer(ShaderStage stage, int slot, ConstantBuffer buffer)
	{
		if (stage == ShaderStage.Vertex)
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
		_graphicsMetrics._primitiveCount += primitiveCount;

		// TODO: See other GraphicsDevice files
		// PlatformDrawIndexedPrimitives(primitiveType, baseVertex, startIndex, primitiveCount);
	}

	// where T : struct, IVertexType
	public <T extends IVertexType> void drawUserPrimitives(PrimitiveType primitiveType, T[] vertexData,
			int vertexOffset, int primitiveCount)
	{
		drawUserPrimitives(primitiveType, vertexData, vertexOffset, primitiveCount,
				VertexDeclarationCache.getVertexDeclaration());
	}

	// where T : struct
	public <T> void drawUserPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset,
			int primitiveCount, VertexDeclaration vertexDeclaration)
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
		_graphicsMetrics._primitiveCount += primitiveCount;

		// TODO: See other GraphicsDevice files
		// PlatformDrawUserPrimitives(primitiveType, vertexData, vertexOffset, vertexDeclaration,
		// vertexCount);
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
		_graphicsMetrics._primitiveCount += primitiveCount;

		// TODO: See other GraphicsDevice files
		// PlatformDrawPrimitives(primitiveType, vertexStart, vertexCount);
	}

	// where T : struct, IVertexType
	public <T extends IVertexType> void drawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData,
			int vertexOffset, int numVertices, short[] indexData, int indexOffset, int primitiveCount)
	{
		drawUserIndexedPrimitives(primitiveType, vertexData, vertexOffset, numVertices, indexData, indexOffset,
				primitiveCount, VertexDeclarationCache.getVertexDeclaration());
	}

	// where T : struct
	public <T> void drawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset,
			int numVertices, short[] indexData, int indexOffset, int primitiveCount, VertexDeclaration vertexDeclaration)
	{
		// These parameter checks are a duplicate of the checks in the int[] overload of
		// DrawUserIndexedPrimitives.
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
		_graphicsMetrics._primitiveCount += primitiveCount;

		// TODO: See other GraphicsDevice files
		// PlatformDrawUserIndexedPrimitives(primitiveType, vertexData, vertexOffset, numVertices,
		// indexData, indexOffset, primitiveCount, vertexDeclaration);
		platformDrawUserIndexedPrimitives(primitiveType, vertexData, vertexOffset, numVertices, indexData, indexOffset,
				primitiveCount, vertexDeclaration);
	}

	// where T : struct, IVertexType
	public <T extends IVertexType> void drawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData,
			int vertexOffset, int numVertices, int[] indexData, int indexOffset, int primitiveCount)
	{
		drawUserIndexedPrimitives(primitiveType, vertexData, vertexOffset, numVertices, indexData, indexOffset,
				primitiveCount, VertexDeclarationCache.getVertexDeclaration());
	}

	// where T : struct, IVertexType
	public <T extends IVertexType> void drawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData,
			int vertexOffset, int numVertices, int[] indexData, int indexOffset, int primitiveCount,
			VertexDeclaration vertexDeclaration)
	{
		// These parameter checks are a duplicate of the checks in the short[] overload of
		// DrawUserIndexedPrimitives.
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
		_graphicsMetrics._primitiveCount += primitiveCount;

		// TODO: See other GraphicsDevice files
		// PlatformDrawUserIndexedPrimitives(primitiveType, vertexData, vertexOffset, numVertices,
		// indexData, indexOffset, primitiveCount, vertexDeclaration);
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
		// return platformGetHighestSupportedGraphicsProfile(graphicsDevice);
		// TODO: I've put this until OpenGL is implemented. This enables us to run with the software
		// renderer.
		return GraphicsProfile.Reach;
	}

	// TODO: I added this to draw with the software renderer.
	// where T : struct
	private <T> void platformDrawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset,
			int numVertices, short[] indexData, int indexOffset, int primitiveCount, VertexDeclaration vertexDeclaration)
	{
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
		int destWidth, destHeight, srcWidth, srcHeight;
		Texture2D texture = (Texture2D) getTextures().getTexture(0);
		Color tint;
		int i = 0;

		while (i < numVertices)
		{
			destStartX = (int) (data[i + 0].position.x);
			destStartY = (int) (data[i + 0].position.y);
			destEndX = (int) (data[i + 1].position.x);
			destEndY = (int) (data[i + 2].position.y);
			
			// NOTE: Add 0.5f so that the cast always return the right value
			//		 (rounding error).
			srcStartX = (int) (data[i + 0].textureCoordinate.x * texture.width + 0.5f);
			srcStartY = (int) (data[i + 0].textureCoordinate.y * texture.height + 0.5f);
			srcEndX = (int) (data[i + 1].textureCoordinate.x * texture.width + 0.5f);
			srcEndY = (int) (data[i + 2].textureCoordinate.y * texture.height + 0.5f);
			
			tint = data[i + 0].color;

			destWidth = (destEndX - destStartX);
			destHeight = (destEndY - destStartY);
			srcWidth = (srcEndX - srcStartX);
			srcHeight = (srcEndY - srcStartY);
			int textureWidth = texture.width;
			// Determine if we need to resize our sprite. If so, we set our local variables
			// accordingly.
			// Otherwise, we set our local variables to the original's sprite values.
			int[] texturePixels = new int[destWidth * destHeight];

			if (destWidth != srcWidth || destHeight != srcHeight)
			{
				// TODO: Add other resizing methods (see TextureFilter.java)
				switch (this.samplerStates.getSamplerStateCollection(0).getFilter())
				{
					case Linear:
						texturePixels = resizeBilinear(texture, srcStartX, srcStartY, srcWidth, srcHeight, destWidth, destHeight);
						textureWidth = destWidth;
						break;
					case Point:
						texturePixels = resizeNearestNeighbor(texture, srcStartX, srcStartY, srcWidth, srcHeight, destWidth, destHeight);
						textureWidth = destWidth;
						break;
					default:
						texturePixels = resizeNearestNeighbor(texture, srcStartX, srcStartY, srcWidth, srcHeight, destWidth, destHeight);
						textureWidth = destWidth;
						break;
				}
			}
			else
			{
				texturePixels = texture.pixels;
			}
			drawQuad(destStartX, destStartY, destEndX, destEndY, srcStartX, srcStartY, textureWidth, texturePixels, tint);
			i += 4;
		}

		// int indexCount = getElementCountArray(primitiveType, primitiveCount);
		// int startVertex = setUserVertexBuffer(vertexData, vertexOffset, numVertices,
		// vertexDeclaration);
		// var startIndex = setUserIndexBuffer(indexData, indexOffset, indexCount);

		// lock (_d3dContext)
		// {
		// ApplyState(true);

		// _d3dContext.InputAssembler.PrimitiveTopology = ToPrimitiveTopology(primitiveType);
		// _d3dContext.DrawIndexed(indexCount, startIndex, startVertex);
		// }
	}

	// where T : struct
	// private <T> int setUserVertexBuffer(T[] vertexData, int vertexOffset, int vertexCount,
	// VertexDeclaration vertexDecl)
	// {
	// DynamicVertexBuffer buffer;
	//
	// if (!_userVertexBuffers.TryGetValue(vertexDecl.HashKey, out buffer) || buffer.VertexCount <
	// vertexCount)
	// {
	// // Dispose the previous buffer if we have one.
	// if (buffer != null)
	// buffer.Dispose();
	//
	// buffer = new DynamicVertexBuffer(this, vertexDecl, Math.Max(vertexCount, 2000),
	// BufferUsage.WriteOnly);
	// _userVertexBuffers[vertexDecl.HashKey] = buffer;
	// }
	//
	// int startVertex = buffer.UserOffset;
	//
	//
	// if ((vertexCount + buffer.UserOffset) < buffer.VertexCount)
	// {
	// buffer.UserOffset += vertexCount;
	// buffer.SetData(startVertex * vertexDecl.VertexStride, vertexData, vertexOffset, vertexCount,
	// vertexDecl.VertexStride, SetDataOptions.NoOverwrite);
	// }
	// else
	// {
	// buffer.UserOffset = vertexCount;
	// buffer.SetData(vertexData, vertexOffset, vertexCount, SetDataOptions.Discard);
	// startVertex = 0;
	// }
	//
	// SetVertexBuffer(buffer);
	//
	// return startVertex;
	// }

	// NOTE: XNA uses premultiplied alpha by default and the color values in
	//		 the xnb files are premultiplied.
	private void drawQuad(int destStartX, int destStartY, int destEndX, int destEndY,
						  int srcStartX, int srcStartY, int srcWidth, int[] srcPixels, Color tint)
	{
		int screenWidth = this._viewport.getWidth();
		int screenHeight = this._viewport.getHeight();

		// check the bounds
		int sourceOffsetX = srcStartX;
		if (destStartX < 0)
		{
			sourceOffsetX += -destStartX;
			destStartX = 0;
		}
		
		int sourceOffsetY = srcStartY;
		if (destStartY < 0)
		{
			sourceOffsetY += -destStartY;
			destStartY = 0;
		}
		
		if (destEndX > screenWidth)
		{
			destEndX = screenWidth;
		}
		
		if (destEndY > screenHeight)
		{
			destEndY = screenHeight;
		}

		// Extract our tint components
		short rTint = tint.getRed();
		short gTint = tint.getGreen();
		short bTint = tint.getBlue();
		short aTint = tint.getAlpha();

		// Cycle through all the sprites pixels. and apply tint and alpha-blending
		// Set our starting index position for the source Texture.
		int sourceRow = sourceOffsetX + sourceOffsetY * srcWidth;
		for (int y = destStartY; y < destEndY; ++y, sourceRow += srcWidth)
		{
			int sourceIndex = sourceRow;
			for (int x = destStartX; x < destEndX; ++x)
			{
				// The color of the pixel about to be drawn.
				int foregroundCol = srcPixels[sourceIndex];

				// The color of the pixel already there.
				int backgroundCol = pixels[x + y * screenWidth];

				// The components of the pixel about to be drawn.
				int foregroundR = (foregroundCol >> 16) & 0xff;
				int foregroundG = (foregroundCol >> 8) & 0xff;
				int foregroundB = (foregroundCol) & 0xff;
				int foregroundA = (foregroundCol >> 24) & 0xff;
				
				// Using Color.WHITE would have no effect so skip this
				if (!tint.equals(Color.White))
				{
					// Typical tint formula -> original component * tint / 255
					// also works with premultiplied alpha
					foregroundR = foregroundR * rTint / 255;
					foregroundG = foregroundG * gTint / 255;
					foregroundB = foregroundB * bTint / 255;
					foregroundA = foregroundA * aTint / 255;
				}

				// The resulting color of the pixel to be drawn
				int col;
				
				// If alpha is 255 it completely overrides the existing color.
				if (foregroundA == 255 || _blendState == BlendState.Opaque)
				{
					col = (foregroundA << 24 & 0xff000000) | (foregroundR << 16 & 0x00ff0000) |
						  (foregroundG << 8 & 0x0000ff00) | (foregroundB & 0x000000ff);
				}
				else if (_blendState == BlendState.AlphaBlend)
				{
					// Do the alpha-blending with the premultiplied alpha source.
					int backgroundR = (backgroundCol >> 16) & 0xff;
					int backgroundG = (backgroundCol >> 8) & 0xff;
					int backgroundB = (backgroundCol) & 0xff;
					// Typical over blend formula
					int r = foregroundR + (backgroundR * (255 - foregroundA) / 255);
					int g = foregroundG + (backgroundG * (255 - foregroundA) / 255);
					int b = foregroundB + (backgroundB * (255 - foregroundA) / 255);
					
					// NOTE: for some reason, spritefonts could produce value greater
					// than 255 for a single channel so we need to clamp to value.
					r = r > 255 ? 255 : r;
					g = g > 255 ? 255 : g;
					b = b > 255 ? 255 : b;
					
					col = r << 16 | g << 8 | b;
				}
				else
				{
					// TODO: Should do the other BlendStates
					// Defaults to BlendState.OPAQUE after tinting.
					col = foregroundA << 24 | foregroundR << 16 | foregroundG << 8 | foregroundB;
				}
				pixels[x + y * screenWidth] = col;
				++sourceIndex;
			}
			// sourceRow += sourceWidth;
		}
	}
	
	// TODO: This is probably SamplerState.LinearClamp
	public int[] resizeBilinear(Texture2D tex, int startX, int startY,int oldWidth, int oldHeight, int newWidth, int newHeight)
			throws IllegalArgumentException {
		if (newWidth < 0 || newHeight < 0)
			throw new IllegalArgumentException("new width or new height cannot be less than 0");
		int[] temp = new int[newWidth * newHeight];

		int a, b, c, d, x, y, srcIndex;
		float x_ratio = ((float) (oldWidth - 1)) / newWidth;
		float y_ratio = ((float) (oldHeight - 1)) / newHeight;
		float x_diff, y_diff, blue, red, green, alpha;
		int destIndex = 0;

		for (int i = startY; i < newHeight; ++i) {
			for (int j = startX; j < newWidth; ++j) {
				x = (int) (x_ratio * j);
				y = (int) (y_ratio * i);
				x_diff = (x_ratio * j) - x;
				y_diff = (y_ratio * i) - y;
				srcIndex = (x + y * oldWidth);
				a = tex.pixels[srcIndex];
				b = tex.pixels[srcIndex + 1];
				c = tex.pixels[srcIndex + oldWidth];
				d = tex.pixels[srcIndex + oldWidth + 1];

				// blue element
				// Yb = Ab(1-w)(1-h) + Bb(w)(1-h) + Cb(h)(1-w) + Db(wh)
				blue = (a & 0xff) * (1 - x_diff) * (1 - y_diff) + (b & 0xff) * (x_diff) * (1 - y_diff) + (c & 0xff)
						* (y_diff) * (1 - x_diff) + (d & 0xff) * (x_diff * y_diff);

				// green element
				// Yg = Ag(1-w)(1-h) + Bg(w)(1-h) + Cg(h)(1-w) + Dg(wh)
				green = ((a >> 8) & 0xff) * (1 - x_diff) * (1 - y_diff) + ((b >> 8) & 0xff) * (x_diff) * (1 - y_diff)
						+ ((c >> 8) & 0xff) * (y_diff) * (1 - x_diff) + ((d >> 8) & 0xff) * (x_diff * y_diff);

				// red element
				// Yr = Ar(1-w)(1-h) + Br(w)(1-h) + Cr(h)(1-w) + Dr(wh)
				red = ((a >> 16) & 0xff) * (1 - x_diff) * (1 - y_diff) + ((b >> 16) & 0xff) * (x_diff) * (1 - y_diff)
						+ ((c >> 16) & 0xff) * (y_diff) * (1 - x_diff) + ((d >> 16) & 0xff) * (x_diff * y_diff);

				// alpha element
				// Ya = Aa(1-w)(1-h) + Ba(w)(1-h) + Ca(h)(1-w) + Da(wh)
				alpha = ((a >> 24) & 0xff) * (1 - x_diff) * (1 - y_diff) + ((b >> 24) & 0xff) * (x_diff) * (1 - y_diff)
						+ ((c >> 24) & 0xff) * (y_diff) * (1 - x_diff) + ((d >> 24) & 0xff) * (x_diff * y_diff);

				temp[destIndex++] = ((((int) alpha) << 24) & 0xff000000) | ((((int) red) << 16) & 0xff0000)
						| ((((int) green) << 8) & 0xff00) | ((int) blue);
			}
		}
		return temp;
	}
	
	// TODO: This is probably SamplerState.PointClamp
	/**
	 * Resizes a portion of an ARGB sprite using the nearest neighbor
	 * interpolation algorithm. Useful for a animation strips. The new width or
	 * the new height cannot be less than or equal to 0.
	 * 
	 * @param texture
	 *        The original texture to be resized.
	 * @param startX
	 *        The starting location on the x axis.
	 * @param startY
	 *        The starting location on the y axis.
	 * @param oldWidth
	 *        The old sprite width.
	 * @param oldHeight
	 *        The old sprite height.
	 * @param newWidth
	 *        The new sprite width.
	 * @param newHeight
	 *        The new sprite height.
	 * @return The reference array of pixels containing the resized sprite's
	 *         pixels.
	 * @throws IllegalArgumentException
	 *         If the new width or the new height is less than or equal to
	 *         0.
	 */
	public int[] resizeNearestNeighbor(Texture2D texture, int startX, int startY,int oldWidth, int oldHeight, int newWidth, int newHeight)
	{
		if (newWidth < 0 || newHeight < 0)
			throw new IllegalArgumentException("new width or new height cannot be less than 0");
		int[] temp = new int[newWidth * newHeight];
		float xRatio = (float) newWidth / (float) oldWidth;
		float yRatio = (float) newHeight / (float) oldHeight;

		for (int y = startY; y < newHeight; ++y)
		{
			for (int x = startX; x < newWidth; ++x)
			{
				// TODO: This needs to be fixed
				temp[x + y * newWidth] = texture.pixels[(int) (x / xRatio) + (int) (y / yRatio) * oldWidth];
			}
		}
		return temp;
	}
}
