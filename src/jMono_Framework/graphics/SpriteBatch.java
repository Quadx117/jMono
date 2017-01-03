package jMono_Framework.graphics;

import jMono_Framework.Color;
import jMono_Framework.Rectangle;
import jMono_Framework.graphics.SpriteFont.CharacterSource;
import jMono_Framework.graphics.effect.Effect;
import jMono_Framework.graphics.effect.EffectParameter;
import jMono_Framework.graphics.effect.EffectPass;
import jMono_Framework.graphics.effect.SpriteEffect;
import jMono_Framework.graphics.states.BlendState;
import jMono_Framework.graphics.states.DepthStencilState;
import jMono_Framework.graphics.states.RasterizerState;
import jMono_Framework.graphics.states.SamplerState;
import jMono_Framework.math.Matrix;
import jMono_Framework.math.Vector2;
import jMono_Framework.math.Vector4;

/**
 * Helper class for drawing text strings and sprites in one or more optimized batches.
 * 
 * @author Eric Perron
 *
 */
public class SpriteBatch extends GraphicsResource
{
	private final SpriteBatcher _batcher;

	SpriteSortMode _sortMode;
	BlendState _blendState;
	SamplerState _samplerState;
	DepthStencilState _depthStencilState;
	RasterizerState _rasterizerState;
	Effect _effect;
	boolean _beginCalled;

	Effect _spriteEffect;
	private final EffectParameter _matrixTransform;
	private final EffectPass _spritePass;

	Matrix _matrix;
	Rectangle _tempRect = new Rectangle(0, 0, 0, 0);
	Vector2 _texCoordTL = new Vector2(0.0f, 0.0f);
	Vector2 _texCoordBR = new Vector2(0.0f, 0.0f);

	/**
	 * Constructs a {@code SpriteBatch}.
	 * 
	 * @param graphicsDevice
	 *        The {@link GraphicsDevice}, which will be used for sprite rendering.
	 * @throws NullPointerException
	 *         If {@code graphicsDevice} is null.
	 */
	public SpriteBatch(GraphicsDevice graphicsDevice) throws NullPointerException
	{
		if (graphicsDevice == null)
		{
			throw new NullPointerException("graphicsDevice");
		}

		this.setGraphicsDevice(graphicsDevice);

		// Use a custom SpriteEffect so we can control the transformation matrix
		_spriteEffect = new Effect(graphicsDevice, SpriteEffect.bytecode);
		_matrixTransform = _spriteEffect.getParameters().getEffectParameter("MatrixTransform");
		_spritePass = _spriteEffect.currentTechnique.getPasses().getEffectPass(0);

		_batcher = new SpriteBatcher(graphicsDevice);

		_beginCalled = false;
	}

	/**
	 * Begins a new sprite and text batch with the specified render state.
	 * 
	 * <p>
	 * This method uses optional parameters.
	 * 
	 * <p>
	 * The {@code #begin()} method should be called before drawing commands, and you cannot call it again before calling {@link #end()}.
	 * 
	 * @param sortMode
	 *        The drawing order for sprite and text drawing. {@link SpriteSortMode#Deferred} by default.
	 * @param blendState
	 *        State of the blending. Uses {@link BlendState#AlphaBlend} if null.
	 * @param samplerState
	 *        State of the sampler. Uses {@link SamplerState#LinearClamp} if null.
	 * @param depthStencilState
	 *        State of the depth-stencil buffer. Uses {@link DepthStencilState#None} if null.
	 * @param rasterizerState
	 *        State of the rasterization. Uses {@link RasterizerState#CullCounterClockwise} if null.
	 * @param effect
	 *        A custom {@link Effect} to override the default sprite effect. Uses default sprite effect if null.
	 * @param transformMatrix
	 *        An optional matrix used to transform the sprite geometry. Uses {@link Matrix#identity()} if null.
	 * @throws IllegalStateException
	 *         If {@code #begin()} is called another time without calling {@link #end()}.
	 */
	public void begin(
			SpriteSortMode sortMode,
			BlendState blendState,
			SamplerState samplerState,
			DepthStencilState depthStencilState,
			RasterizerState rasterizerState,
			Effect effect,
			Matrix transformMatrix) throws IllegalStateException
	{
		if (_beginCalled)
			throw new IllegalStateException("Begin cannot be called again until End has been successfully called.");

		// defaults
		_sortMode = (sortMode == null) ? SpriteSortMode.Deferred : sortMode;
		_blendState = (blendState == null) ? BlendState.AlphaBlend : blendState;
		_samplerState = (samplerState == null) ? SamplerState.LinearClamp : samplerState;
		_depthStencilState = (depthStencilState == null) ? DepthStencilState.None : depthStencilState;
		_rasterizerState = (rasterizerState == null) ? RasterizerState.CullCounterClockwise : rasterizerState;
		_effect = effect;
		_matrix = (transformMatrix == null) ? Matrix.identity() : transformMatrix;

		// Setup things now so a user can change them.
		if (SpriteSortMode.Immediate.equals(sortMode))
		{
			setup();
		}

		_beginCalled = true;
	}

	// NOTE: Need to keep these overrides to compensate for C# default parameters
	public void begin()
	{
		begin(SpriteSortMode.Deferred, null, null, null, null, null, null);
	}

	public void begin(SpriteSortMode sortMode)
	{
		begin (sortMode, null, null, null, null, null, null);
	}

	public void begin(SpriteSortMode sortMode, BlendState blendState)
	{
		begin(sortMode, blendState, null, null, null, null, null);
	}

	public void begin(SpriteSortMode sortMode, BlendState blendState, SamplerState samplerState)
	{
		begin(sortMode, blendState, samplerState, null, null, null, null);
	}

	public void begin(SpriteSortMode sortMode, BlendState blendState, SamplerState samplerState, DepthStencilState depthStencilState)
	{
		begin(sortMode, blendState, samplerState, depthStencilState, null, null, null);
	}

	public void begin(SpriteSortMode sortMode, BlendState blendState, SamplerState samplerState, DepthStencilState depthStencilState, RasterizerState rasterizerState)
	{
		begin(sortMode, blendState, samplerState, depthStencilState, rasterizerState, null, null);
	}

	public void begin(SpriteSortMode sortMode, BlendState blendState, SamplerState samplerState, DepthStencilState depthStencilState, RasterizerState rasterizerState, Effect effect)
	{
			begin(sortMode, blendState, samplerState, depthStencilState, rasterizerState, effect, null);
	}

	/**
	 * Flushes all batched text and sprites to the screen.
	 * 
	 * <p>
	 * This command should be called after {@link #begin()} and drawing commands.
	 */
	public void end()
	{
		_beginCalled = false;

		if (!(_sortMode.equals(SpriteSortMode.Immediate)))
			setup();

		_batcher.drawBatch(_sortMode, _effect);
	}

	void setup()
	{
		GraphicsDevice gd = graphicsDevice;
		gd.setBlendState(_blendState);
		gd.setDepthStencilState(_depthStencilState);
		gd.setRasterizerState(_rasterizerState);
		gd.getSamplerStates().setSamplerStateCollection(0, _samplerState);

		// Setup the default sprite effect.
		Viewport vp = gd.getViewport();

		Matrix projection = Matrix.identity();
		Matrix.createOrthographicOffCenter(0, vp.getWidth(), vp.getHeight(), 0, -1, 0, projection);
// #if !DIRECTX
		// GL requires a half pixel offset to match DX.
		projection.M41 += -0.5f * projection.M11;
		projection.M42 += -0.5f * projection.M22;
// #endif
		Matrix.multiply(_matrix, projection, projection);

		_matrixTransform.setValue(projection);
		_spritePass.apply();
	}

	void checkValid(Texture2D texture)
	{
		if (texture == null)
			throw new NullPointerException("texture");
		if (!_beginCalled)
			throw new IllegalStateException(
					"Draw was called, but Begin has not yet been called. Begin must be called successfully before you can call Draw.");
	}

	void checkValid(SpriteFont spriteFont, String text)
	{
		if (spriteFont == null)
			throw new NullPointerException("spriteFont");
		if (text == null)
			throw new NullPointerException("text");
		if (!_beginCalled)
			throw new IllegalStateException(
					"DrawString was called, but Begin has not yet been called. Begin must be called successfully before you can call DrawString.");
	}

	void checkValid(SpriteFont spriteFont, StringBuilder text)
	{
		if (spriteFont == null)
			throw new NullPointerException("spriteFont");
		if (text == null)
			throw new NullPointerException("text");
		if (!_beginCalled)
			throw new IllegalStateException(
					"DrawString was called, but Begin has not yet been called. Begin must be called successfully before you can call DrawString.");
	}

	// Overloads because of default parameters in C#
	public void draw(Texture2D texture, Rectangle drawRectangle, Rectangle sourceRectangle, Vector2 origin,
					 float rotation, Vector2 scale, Color color, SpriteEffects effects, float layerDepth)
	{
		draw(texture, null, drawRectangle, sourceRectangle, origin, rotation, scale, color, effects, layerDepth);

	}

	public void draw(Texture2D texture, Vector2 position, Rectangle sourceRectangle, Vector2 origin, float rotation,
					 Vector2 scale, Color color, SpriteEffects effects, float layerDepth)
	{
		draw(texture, position, null, sourceRectangle, origin, rotation, scale, color, effects, layerDepth);

	}

	public void draw(Texture2D texture, Vector2 position, Rectangle drawRectangle, Rectangle originRectangle,
					 float rotation, Vector2 scale, Color color, SpriteEffects effects, float layerDepth)
	{
		draw(texture, position, drawRectangle, originRectangle, null, rotation, scale, color, effects, layerDepth);

	}

	public void draw(Texture2D texture, Vector2 position, Rectangle drawRectangle, Rectangle sourceRectangle,
					 float rotation, Vector2 scale, Color color, float layerDepth, SpriteEffects effects)
	{
		draw(texture, position, drawRectangle, sourceRectangle, null, rotation, scale, color, effects, layerDepth);

	}

	public void draw(Texture2D texture, Vector2 position, Rectangle drawRectangle, Rectangle sourceRectangle,
					 Vector2 origin, Vector2 scale, Color color, SpriteEffects effects, float layerDepth)
	{
		draw(texture, position, drawRectangle, sourceRectangle, origin, 0.0f, scale, color, effects, layerDepth);

	}

	public void draw(Texture2D texture, Vector2 position, Rectangle drawRectangle, Rectangle sourceRectangle,
					 Vector2 origin, float rotation, Color color, SpriteEffects effects, float layerDepth)
	{
		draw(texture, position, drawRectangle, sourceRectangle, origin, rotation, null, color, effects, layerDepth);

	}

	public void draw(Texture2D texture, Vector2 position, Rectangle drawRectangle, Rectangle sourceRectangle,
					 Vector2 origin, float rotation, Vector2 scale, SpriteEffects effects, float layerDepth)
	{
		draw(texture, position, drawRectangle, sourceRectangle, origin, rotation, scale, null, effects, layerDepth);

	}

	public void draw(Texture2D texture, Vector2 position, Rectangle drawRectangle, Rectangle sourceRectangle,
					 Vector2 origin, float rotation, Vector2 scale, Color color, float layerDepth)
	{
		draw(texture, position, drawRectangle, sourceRectangle, origin, rotation, scale, color, SpriteEffects.None, layerDepth);

	}

	public void draw(Texture2D texture, Vector2 position, Rectangle drawRectangle, Rectangle sourceRectangle,
					 Vector2 origin, float rotation, Vector2 scale, Color color, SpriteEffects effects)
	{
		draw(texture, position, drawRectangle, sourceRectangle, origin, rotation, scale, color, effects, 0.0f);

	}

	/**
	 * Submit a sprite for drawing in the current batch.
	 * 
	 * <p>
	 * This overload uses optional parameters.<br>
	 * This overload requires only one of {@code position} and {@code destinationRectangle} been used.
	 * 
	 * @param texture
	 *        The {@link Texture2D} to draw. Required.
	 * @param position
	 *        The drawing location on screen or null if {@code destinationRectangle} is used.
	 * @param drawRectangle
	 *        The drawing bounds on screen or null if {@code position} is used.
	 * @param sourceRectangle
	 *        An optional region on the texture which will be rendered. If null, draws full texture.
	 * @param origin
	 *        An optional center of rotation. Uses {@link Vector2#Zero()} if null.
	 * @param rotation
	 *        An optional rotation of this sprite. 0 by default.
	 * @param scale
	 *        An optional scale vector. Uses {@link Vector2#One()} if null.
	 * @param color
	 *        An optional color mask. Uses {@link Color#White} if null.
	 * @param effects
	 *        The optional drawing modificator. {@link SpriteEffects#None} by default.
	 * @param layerDepth
	 *        An optional depth of the layer of this sprite. 0 by default.
	 * @throws IllegalArgumentException
	 *         If both {@code position} and {@code destinationRectangle} null or both are assigned.
	 */
	public void draw(
			Texture2D texture,
			Vector2 position,
			Rectangle drawRectangle,
			Rectangle sourceRectangle,
			Vector2 origin,
			float rotation,
			Vector2 scale,
			Color color,
			SpriteEffects effects,
			float layerDepth) throws IllegalArgumentException
	{
		// Assign default values to null parameters here, as they are not compile-time constants
		if (color == null)
			color = Color.White;
		if (origin == null)
			origin = Vector2.Zero();
		if (scale == null)
			scale = Vector2.One();

		// If both drawRectangle and position are null, or if both have been assigned a value, raise an error
		if ((drawRectangle == null && position == null) || (drawRectangle != null && position != null))
		{
			throw new IllegalArgumentException("Expected drawRectangle or position, but received neither or both.");
		}
		else if (position != null)
		{
			// Call Draw() using position
			draw(texture, position, sourceRectangle, color, rotation, origin, scale, effects, layerDepth);
		}
		else
		{
			// Call Draw() using drawRectangle
			draw(texture, drawRectangle, sourceRectangle, color, rotation, origin, effects, layerDepth);
		}
	}

	/**
	 * Submit a sprite for drawing in the current batch.
	 * 
	 * @param texture
	 *        The {@link Texture2D} to draw.
	 * @param position
	 *        The drawing location on screen.
	 * @param sourceRectangle
	 *        An optional region on the texture which will be rendered. If null, draws full texture.
	 * @param color
	 *        An color mask.
	 * @param rotation
	 *        An rotation of this sprite.
	 * @param origin
	 *        Center of rotation.
	 * @param scale
	 *        A scale vector.
	 * @param effects
	 *        The drawing modificator.
	 * @param layerDepth
	 *        The depth of the layer of this sprite.
	 */
	public void draw(
			Texture2D texture,
			Vector2 position,
			Rectangle sourceRectangle,
			Color color,
			float rotation,
			Vector2 origin,
			Vector2 scale,
			SpriteEffects effects,
			float layerDepth)
	{
		checkValid(texture);

		float w = texture.width * scale.x;
		float h = texture.height * scale.y;
		if (sourceRectangle != null)
		{
			w = sourceRectangle.width * scale.x;
			h = sourceRectangle.height * scale.y;
		}

		drawInternal(
				texture,
				new Vector4(position.x, position.y, w, h),
				sourceRectangle,
				color,
				rotation,
				Vector2.multiply(origin, scale),
				effects,
				layerDepth,
				true);
	}

	/**
	 * Submit a sprite for drawing in the current batch.
	 * 
	 * @param texture
	 *        The {@link Texture2D} to draw.
	 * @param position
	 *        The drawing location on screen.
	 * @param sourceRectangle
	 *        An optional region on the texture which will be rendered. If null, draws full texture.
	 * @param color
	 *        An color mask.
	 * @param rotation
	 *        An rotation of this sprite.
	 * @param origin
	 *        Center of rotation.
	 * @param scale
	 *        A scale factor.
	 * @param effects
	 *        The drawing modificator.
	 * @param layerDepth
	 *        The depth of the layer of this sprite.
	 */
	public void draw(
			Texture2D texture,
			Vector2 position,
			Rectangle sourceRectangle,
			Color color,
			float rotation,
			Vector2 origin,
			float scale,
			SpriteEffects effects,
			float layerDepth)
	{
		checkValid(texture);

		float w = texture.width * scale;
		float h = texture.height * scale;
		if (sourceRectangle != null)
		{
			w = sourceRectangle.width * scale;
			h = sourceRectangle.height * scale;
		}

		drawInternal(
				texture,
				new Vector4(position.x, position.y, w, h),
				sourceRectangle,
				color,
				rotation,
				Vector2.multiply(origin, scale),
				effects,
				layerDepth,
				true);
	}

	/**
	 * Submit a sprite for drawing in the current batch.
	 * 
	 * @param texture
	 *        The {@link Texture2D} to draw.
	 * @param destinationRectangle
	 *        The drawing bounds on screen.
	 * @param sourceRectangle
	 *        An optional region on the texture which will be rendered. If null, draws full texture.
	 * @param color
	 *        An color mask.
	 * @param rotation
	 *        An rotation of this sprite.
	 * @param origin
	 *        Center of rotation.
	 * @param effects
	 *        The drawing modificator.
	 * @param layerDepth
	 *        The depth of the layer of this sprite.
	 */
	public void draw(
			Texture2D texture,
			Rectangle destinationRectangle,
			Rectangle sourceRectangle,
			Color color,
			float rotation,
			Vector2 origin,
			SpriteEffects effects,
			float layerDepth)
	{
		checkValid(texture);

		drawInternal(
				texture,
				new Vector4(destinationRectangle.x,
							destinationRectangle.y,
							destinationRectangle.width,
							destinationRectangle.height),
				sourceRectangle,
				color,
				rotation,
				new Vector2(
						origin.x * ((float) destinationRectangle.width /
								(float) ((sourceRectangle != null && sourceRectangle.width != 0) ?
										sourceRectangle.width
										: texture.width)),
						origin.y * ((float) destinationRectangle.height) /
								(float) ((sourceRectangle != null && sourceRectangle.height != 0) ?
										sourceRectangle.height
										: texture.height)),
				effects,
				layerDepth,
				true);
	}

	protected void drawInternal(
			Texture2D texture,
			Vector4 destinationRectangle,
			Rectangle sourceRectangle,
			Color color,
			float rotation,
			Vector2 origin,
			SpriteEffects effect,
			float depth,
			boolean autoFlush)
	{
		SpriteBatchItem item = _batcher.createBatchItem();

		item.depth = depth;
		item.texture = texture;

		if (sourceRectangle != null)
		{
			_tempRect = new Rectangle(sourceRectangle);
		}
		else
		{
			_tempRect.x = 0;
			_tempRect.y = 0;
			_tempRect.width = texture.width;
			_tempRect.height = texture.height;
		}

		_texCoordTL.x = (_tempRect.x / (float) texture.width);
		_texCoordTL.y = (_tempRect.y / (float) texture.height);
		_texCoordBR.x = (_tempRect.x + _tempRect.width) / (float) texture.width;
		_texCoordBR.y = (_tempRect.y + _tempRect.height) / (float) texture.height;

		if ((effect.getValue() & SpriteEffects.FlipVertically.getValue()) != 0)
		{
			float temp = _texCoordBR.y;
			_texCoordBR.y = _texCoordTL.y;
			_texCoordTL.y = temp;
		}
		if ((effect.getValue() & SpriteEffects.FlipHorizontally.getValue()) != 0)
		{
			float temp = _texCoordBR.x;
			_texCoordBR.x = _texCoordTL.x;
			_texCoordTL.x = temp;
		}

		item.set(destinationRectangle.x,
				 destinationRectangle.y,
				 -origin.x, -origin.y,
				 destinationRectangle.z,
				 destinationRectangle.w,
				 (float) Math.sin(rotation),
				 (float) Math.cos(rotation),
				 color,
				 _texCoordTL,
				_texCoordBR);

		if (autoFlush)
		{
			flushIfNeeded();
		}
	}

	// Mark the end of a draw operation for Immediate SpriteSortMode.
	protected void flushIfNeeded()
	{
		if (_sortMode.equals(SpriteSortMode.Immediate))
		{
			_batcher.drawBatch(_sortMode, _effect);
		}
	}

	/**
	 * Submit a sprite for drawing in the current batch.
	 * 
	 * @param texture
	 *        The {@link Texture2D} to draw.
	 * @param position
	 *        The drawing location on screen.
	 * @param sourceRectangle
	 *        An optional region on the texture which will be rendered. If null, draws full texture.
	 * @param color
	 *        A color mask.
	 */
	public void draw(Texture2D texture, Vector2 position, Rectangle sourceRectangle, Color color)
	{
		draw(texture, position, sourceRectangle, color, 0f, Vector2.Zero(), 1f, SpriteEffects.None, 0f);
	}

	/**
	 * Submit a sprite for drawing in the current batch.
	 * 
	 * @param texture
	 *        The {@link Texture2D} to draw.
	 * @param destinationRectangle
	 *        The drawing bounds on screen.
	 * @param sourceRectangle
	 *        An optional region on the texture which will be rendered. If null, draws full texture.
	 * @param color
	 *        A color mask.
	 *        
	 */
	public void draw(Texture2D texture, Rectangle destinationRectangle, Rectangle sourceRectangle, Color color)
	{
		draw(texture, destinationRectangle, sourceRectangle, color, 0, Vector2.Zero(), SpriteEffects.None, 0f);
	}

	/**
	 * Submit a sprite for drawing in the current batch.
	 * 
	 * @param texture
	 *        The {@link Texture2D} to draw.
	 * @param position
	 *        The drawing location on screen.
	 * @param color
	 *        A color mask.
	 */
	public void draw(Texture2D texture, Vector2 position, Color color)
	{
		draw(texture, position, null, color);
	}

	/**
	 * Submit a sprite for drawing in the current batch.
	 * 
	 * @param texture
	 *        The {@link Texture2D} to draw.
	 * @param destinationRectangle
	 *        The drawing bounds on screen.
	 * @param color
	 *        A color mask.
	 */
	public void draw(Texture2D texture, Rectangle destinationRectangle, Color color)
	{
		draw(texture, destinationRectangle, null, color);
	}

	/**
	 * Submit a text string of sprites for drawing in the current batch.
	 * 
	 * @param spriteFont
	 *        A font used to render the text with.
	 * @param text
	 *        The text which will be drawn.
	 * @param position
	 *        The drawing location on screen.
	 * @param color
	 *        A color mask.
	 */
	public void drawString(SpriteFont spriteFont, String text, Vector2 position, Color color)
	{
		checkValid(spriteFont, text);

		CharacterSource source = spriteFont.new CharacterSource(text);
		spriteFont.drawInto(
				this, source, position, color, 0, Vector2.Zero(), Vector2.One(), SpriteEffects.None, 0f);
	}

	/**
	 * Submit a text string of sprites for drawing in the current batch.
	 * 
	 * @param spriteFont
	 *        A font used to render the text with.
	 * @param text
	 *        The text which will be drawn.
	 * @param position
	 *        The drawing location on screen.
	 * @param color
	 *        A color mask.
	 * @param rotation
	 *        A rotation of this string.
	 * @param origin
	 *        Center of the rotation.
	 * @param scale
	 *        A scaling of this string.
	 * @param effects
	 *        Modificators for drawing.
	 * @param layerDepth
	 *        A depth of the layer of this string.
	 */
	public void drawString(
			SpriteFont spriteFont, String text, Vector2 position, Color color,
			float rotation, Vector2 origin, float scale, SpriteEffects effects, float layerDepth)
	{
		checkValid(spriteFont, text);

		Vector2 scaleVec = new Vector2(scale, scale);
		CharacterSource source = spriteFont.new CharacterSource(text);
		spriteFont.drawInto(this, source, position, color, rotation, origin, scaleVec, effects, layerDepth);
	}

	/**
	 * Submit a text string of sprites for drawing in the current batch.
	 * 
	 * @param spriteFont
	 *        A font used to render the text with.
	 * @param text
	 *        The text which will be drawn.
	 * @param position
	 *        The drawing location on screen.
	 * @param color
	 *        A color mask.
	 * @param rotation
	 *        A rotation of this string.
	 * @param origin
	 *        Center of the rotation.
	 * @param scale
	 *        A scaling of this string.
	 * @param effects
	 *        Modificators for drawing.
	 * @param layerDepth
	 *        A depth of the layer of this string.
	 */
	public void drawString(
			SpriteFont spriteFont, String text, Vector2 position, Color color,
			float rotation, Vector2 origin, Vector2 scale, SpriteEffects effects, float layerDepth)
	{
		checkValid(spriteFont, text);

		CharacterSource source = spriteFont.new CharacterSource(text);
		spriteFont.drawInto(this, source, position, color, rotation, origin, scale, effects, layerDepth);
	}

	/**
	 * Submit a text string of sprites for drawing in the current batch.
	 * 
	 * @param spriteFont
	 *        A font used to render the text with.
	 * @param text
	 *        The text which will be drawn.
	 * @param position
	 *        The drawing location on screen.
	 * @param color
	 *        A color mask.
	 */
	public void drawString(SpriteFont spriteFont, StringBuilder text, Vector2 position, Color color)
	{
		checkValid(spriteFont, text);

		CharacterSource source = spriteFont.new CharacterSource(text);
		spriteFont.drawInto(this, source, position, color, 0, Vector2.Zero(), Vector2.One(), SpriteEffects.None, 0f);
	}

	/**
	 * Submit a text string of sprites for drawing in the current batch.
	 * 
	 * @param spriteFont
	 *        A font used to render the text with.
	 * @param text
	 *        The text which will be drawn.
	 * @param position
	 *        The drawing location on screen.
	 * @param color
	 *        A color mask.
	 * @param rotation
	 *        A rotation of this string.
	 * @param origin
	 *        Center of the rotation.
	 * @param scale
	 *        A scaling of this string.
	 * @param effects
	 *        Modificators for drawing.
	 * @param layerDepth
	 *        A depth of the layer of this string.
	 */
	public void drawString(
			SpriteFont spriteFont, StringBuilder text, Vector2 position, Color color,
			float rotation, Vector2 origin, float scale, SpriteEffects effects, float layerDepth)
	{
		checkValid(spriteFont, text);

		Vector2 scaleVec = new Vector2(scale, scale);
		CharacterSource source = spriteFont.new CharacterSource(text);
		spriteFont.drawInto(this, source, position, color, rotation, origin, scaleVec, effects, layerDepth);
	}

	/**
	 * Submit a text string of sprites for drawing in the current batch.
	 * 
	 * @param spriteFont
	 *        A font used to render the text with.
	 * @param text
	 *        The text which will be drawn.
	 * @param position
	 *        The drawing location on screen.
	 * @param color
	 *        A color mask.
	 * @param rotation
	 *        A rotation of this string.
	 * @param origin
	 *        Center of the rotation.
	 * @param scale
	 *        A scaling of this string.
	 * @param effects
	 *        Modificators for drawing.
	 * @param layerDepth
	 *        A depth of the layer of this string.
	 */
	public void drawString(
			SpriteFont spriteFont, StringBuilder text, Vector2 position, Color color,
			float rotation, Vector2 origin, Vector2 scale, SpriteEffects effects, float layerDepth)
	{
		checkValid(spriteFont, text);

		CharacterSource source = spriteFont.new CharacterSource(text);
		spriteFont.drawInto(this, source, position, color, rotation, origin, scale, effects, layerDepth);
	}

	/**
	 * Immediately releases the unmanaged resources used by this object.
	 * 
	 * @param disposing {@code true} to release both managed and unmanaged resources; {@code false} to release only unmanaged resources.
	 */
	@Override
	protected void dispose(boolean disposing)
	{
		if (!isDisposed())
		{
			if (disposing)
			{
				if (_spriteEffect != null)
				{
					_spriteEffect.close();
					_spriteEffect = null;
				}
			}
		}
		super.dispose(disposing);
	}

}
