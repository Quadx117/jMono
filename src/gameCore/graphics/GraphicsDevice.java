package gameCore.graphics;

import gameCore.Color;
import gameCore.GraphicsDeviceInformation;
import gameCore.events.EventArgs;
import gameCore.events.EventHandler;
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
import gameCore.utilities.As;

import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicsDevice implements AutoCloseable
{
	// TODO: I added this for testing, see where this should go.
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

	protected GraphicsCapabilities getGraphicsCapabilities()
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
	public EventHandler<EventArgs> deviceLost;
	public EventHandler<EventArgs> deviceReset;
	public EventHandler<EventArgs> deviceResetting;
	public EventHandler<ResourceCreatedEventArgs> resourceCreated;
	public EventHandler<ResourceDestroyedEventArgs> resourceDestroyed;
	public EventHandler<EventArgs> disposing;

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

	// TODO: GraphicsMetrics
	// protected GraphicsMetrics _graphicsMetrics;

	// / <summary>
	// / The rendering information for debugging and profiling.
	// / The metrics are reset every frame after draw within <see cref="GraphicsDevice.Present"/>.
	// / </summary>
	// public GraphicsMetrics getMetrics() { return _graphicsMetrics; }
	// public void setGraphicsMetrics(GraphicsMetrics value) { _graphicsMetrics = value; } }

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

		// TODO: I added this for testing. Check where that would go
		pixels = new int[adapter.getCurrentDisplayMode().getWidth() * adapter.getCurrentDisplayMode().getHeight()];
		// end TODO

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
		// TODO: Need to change this to #if WINDOWS
		_viewport = new Viewport(0, 0, getDisplayMode().getWidth(), getDisplayMode().getHeight());
		_viewport.setMaxDepth(1.0f);

		// platformSetup();

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
		// platformInitialize();

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
		// platformClear(options, color.toVector4(), _viewport.maxDepth, 0);
		// TODO: Test code not part of the original code.
		for (int i = 0; i < pixels.length; ++i)
		{
			pixels[i] = color.getPackedValue();
		}
	}

	public void clear(ClearOptions options, Color color, float depth, int stencil)
	{
		// platformClear(options, color.toVector4(), depth, stencil);
	}

	public void clear(ClearOptions options, Vector4 color, float depth, int stencil)
	{
		// platformClear(options, color, depth, stencil);
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
		// _graphicsMetrics = new GraphicsMetrics();
		// platformPresent();
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

	// / <summary>
	// / Trigger the DeviceResetting event
	// / Currently protected to allow the various platforms to send the event at the appropriate
	// time.
	// / </summary>
	protected void onDeviceResetting()
	{
		if (deviceResetting != null)
			deviceResetting.accept(this, EventArgs.Empty);

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
			deviceReset.accept(this, EventArgs.Empty);
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
		// platformSetViewport(value);
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

		_scissorRectangle = value;
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

			// platformApplyDefaultRenderTarget();
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

		// _graphicsMetrics._drawCount++;
		// _graphicsMetrics._primitiveCount += primitiveCount;

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

		// _graphicsMetrics._drawCount++;
		// _graphicsMetrics._primitiveCount += primitiveCount;

		// platformDrawUserPrimitives(primitiveType, vertexData, vertexOffset, vertexDeclaration,
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

		// _graphicsMetrics._drawCount++;
		// _graphicsMetrics._primitiveCount += primitiveCount;

		// platformDrawPrimitives(primitiveType, vertexStart, vertexCount);
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

		// _graphicsMetrics._drawCount++;
		// _graphicsMetrics._primitiveCount += primitiveCount;

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

		// _graphicsMetrics._drawCount++;
		// _graphicsMetrics._primitiveCount += primitiveCount;

		// platformDrawUserIndexedPrimitives(primitiveType, vertexData, vertexOffset, numVertices,
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
		// TODO: I,ve put this until OpenGL is implemented. This enables us to run with the software
		// renderer.
		return GraphicsProfile.Reach;
	}

	// TODO: I added this to draw
	// where T : struct
	private <T> void platformDrawUserIndexedPrimitives(PrimitiveType primitiveType, T[] vertexData, int vertexOffset,
			int numVertices, short[] indexData, int indexOffset, int primitiveCount, VertexDeclaration vertexDeclaration)
	{
		// Note: For now we only support drawing quads from a triangle PrimitiveType
		if (primitiveType != PrimitiveType.TriangleList)
		{
			throw new UnsupportedOperationException("We currently only support PrimitiveType.TriangleList");
		}
		
		VertexPositionColorTexture[] data = As.as(vertexData, VertexPositionColorTexture[].class);
		
		int startX, startY, endX, endY, color;
		int i = 0;
		while (data[i] != null)
		{
			startX = (int) (data[i + 0].position.x);
			startY = (int) (data[i + 0].position.y);
			endX = (int) (data[i + 1].position.x);
			endY = (int) (data[i + 2].position.y);
			color = data[i + 0].color.getPackedValue();
			// TODO: add test code to make sure all 4 color information are the same.
			// TODO: check if not a quad
			drawRectangle(startX, startY, endX, endY, color);
			i += 4;
		}
/*		for (int j = 0; i < vertexData.length; i += 4)
		{
			x = (int) (data[i + 0].position.x);
			y = (int) (data[i + 0].position.y);
			width = (int) (data[i + 1].position.x) - x;
			height = (int) (data[i + 2].position.y) - y;
			color = data[i + 0].color.getPackedValue();
			// TODO: add test code to make sure all 4 color information are the same.
			// TODO: check if not a quad
			drawRectangle(x, y, width, height, color);
		}*/
		// int indexCount = getElementCountArray(primitiveType, primitiveCount);
		// int startVertex = setUserVertexBuffer(vertexData, vertexOffset, numVertices, vertexDeclaration);
		// var startIndex = setUserIndexBuffer(indexData, indexOffset, indexCount);

		// lock (_d3dContext)
		// {
		// ApplyState(true);

		// _d3dContext.InputAssembler.PrimitiveTopology = ToPrimitiveTopology(primitiveType);
		// _d3dContext.DrawIndexed(indexCount, startIndex, startVertex);
		// }
	}

	// where T : struct
//	private <T> int setUserVertexBuffer(T[] vertexData, int vertexOffset, int vertexCount, VertexDeclaration vertexDecl) 
//	{
//	    DynamicVertexBuffer buffer;
//	
//	    if (!_userVertexBuffers.TryGetValue(vertexDecl.HashKey, out buffer) || buffer.VertexCount < vertexCount)
//	    {
//	        // Dispose the previous buffer if we have one.
//	        if (buffer != null)
//	            buffer.Dispose();
//	
//	        buffer = new DynamicVertexBuffer(this, vertexDecl, Math.Max(vertexCount, 2000), BufferUsage.WriteOnly);
//	        _userVertexBuffers[vertexDecl.HashKey] = buffer;
//	    }
//	
//	    int startVertex = buffer.UserOffset;
//	
//	
//	    if ((vertexCount + buffer.UserOffset) < buffer.VertexCount)
//	    {
//	        buffer.UserOffset += vertexCount;
//	        buffer.SetData(startVertex * vertexDecl.VertexStride, vertexData, vertexOffset, vertexCount, vertexDecl.VertexStride, SetDataOptions.NoOverwrite);
//	    }
//	    else
//	    {
//	        buffer.UserOffset = vertexCount;
//	        buffer.SetData(vertexData, vertexOffset, vertexCount, SetDataOptions.Discard);
//	        startVertex = 0;
//	    }
//	
//	    SetVertexBuffer(buffer);
//	
//	    return startVertex;
//	}
	
	private void drawRectangle(int startX, int startY, int endX, int endY, int color)
	{
		int screenWidth = this.getAdapter().getCurrentDisplayMode().getWidth();
		int screenHeight = this.getAdapter().getCurrentDisplayMode().getHeight();
		
		// check the bounds
		if (startX < 0)
		{
			startX = 0;
		}
		if (startY < 0)
		{
			startY = 0;
		}
		if (endX > screenWidth)
		{
			endX = screenWidth;
		}
		if (endY > screenHeight)
		{
			endY = screenHeight;
		}
		
		for (int y = startY; y < endY; ++y)
		{
			for (int x = startX; x < endX; ++x)
			{
				pixels[x + y * screenWidth] = color;
			}
		}
	}
}
