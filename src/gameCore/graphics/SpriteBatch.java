package gameCore.graphics;

import gameCore.Color;
import gameCore.graphics.effect.Effect;
import gameCore.graphics.effect.EffectParameter;
import gameCore.graphics.effect.EffectPass;
import gameCore.graphics.effect.SpriteEffect;
import gameCore.graphics.states.BlendState;
import gameCore.graphics.states.DepthStencilState;
import gameCore.graphics.states.RasterizerState;
import gameCore.graphics.states.SamplerState;
import gameCore.math.Matrix;
import gameCore.math.Vector2;
import gameCore.math.Vector4;

import java.awt.Rectangle;

/**
 * Helper class for drawing text strings and sprites in one or more optimized batches.
 * 
 * @author Eric
 *
 */
public class SpriteBatch extends GraphicsResource
{
	private SpriteBatcher _batcher;

	SpriteSortMode _sortMode;
	BlendState _blendState;
	SamplerState _samplerState;
	DepthStencilState _depthStencilState;
	RasterizerState _rasterizerState;
	Effect _effect;
	boolean _beginCalled;

	Effect _spriteEffect;
	private EffectParameter _matrixTransform;
	private EffectPass _spritePass;

	Matrix _matrix;
	Rectangle _tempRect = new Rectangle(0, 0, 0, 0);
	Vector2 _texCoordTL = new Vector2(0.0f, 0.0f);
	Vector2 _texCoordBR = new Vector2(0.0f, 0.0f);

	public SpriteBatch(GraphicsDevice graphicsDevice)
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

	// TODO: Probably need to do more overrides to compensate for C# default parameters
	public void begin(SpriteSortMode sortMode, BlendState blendState, SamplerState samplerState,
			DepthStencilState depthStencilState, RasterizerState rasterizerState, Effect effect, Matrix transformMatrix)
	{
		if (_beginCalled)
			throw new IllegalStateException("Begin cannot be called again until End has been successfully called.");

		// defaults
		_sortMode = (blendState == null) ? SpriteSortMode.Deferred : sortMode;
		_blendState = (blendState == null) ? BlendState.AlphaBlend : blendState;
		_samplerState = (samplerState == null) ? SamplerState.LinearClamp : samplerState;
		_depthStencilState = (depthStencilState == null) ? DepthStencilState.None : depthStencilState;
		_rasterizerState = (rasterizerState == null) ? RasterizerState.CullCounterClockwise : rasterizerState;
		_effect = effect;
		_matrix = (transformMatrix == null) ? Matrix.identity() : transformMatrix;

		// Setup things now so a user can change them.
		if (sortMode.equals(SpriteSortMode.Immediate))
		{
			setup();
		}
		
		_beginCalled = true;
	}

	// NOTE: Need to keep these overrides to compensate for C# default parameters
	public void begin(SpriteSortMode sortMode, BlendState blendState)
	{
		begin(sortMode, blendState, SamplerState.LinearClamp, DepthStencilState.None,
				RasterizerState.CullCounterClockwise, null, Matrix.identity());
	}

	public void begin(SpriteSortMode sortMode, BlendState blendState, SamplerState samplerState,
			DepthStencilState depthStencilState, RasterizerState rasterizerState)
	{
		begin(sortMode, blendState, samplerState, depthStencilState, rasterizerState, null, Matrix.identity());
	}

	public void begin(SpriteSortMode sortMode, BlendState blendState, SamplerState samplerState,
			DepthStencilState depthStencilState, RasterizerState rasterizerState, Effect effect)
	{
		begin(sortMode, blendState, samplerState, depthStencilState, rasterizerState, effect, Matrix.identity());
	}

	public void end()
	{
		_beginCalled = false;

		// if (_sortMode != SpriteSortMode.Immediate)
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

	// TODO: SpriteFont
	/*
	 * void CheckValid(SpriteFont spriteFont, String text)
	 * {
	 * if (spriteFont == null)
	 * throw new NullPointerException("spriteFont");
	 * if (text == null)
	 * throw new NullPointerException("text");
	 * if (!_beginCalled)
	 * throw new IllegalStateException(
	 * "DrawString was called, but Begin has not yet been called. Begin must be called successfully before you can call DrawString."
	 * );
	 * }
	 * 
	 * void CheckValid(SpriteFont spriteFont, StringBuilder text)
	 * {
	 * if (spriteFont == null)
	 * throw new NullPointerException("spriteFont");
	 * if (text == null)
	 * throw new NullPointerException("text");
	 * if (!_beginCalled)
	 * throw new IllegalStateException(
	 * "DrawString was called, but Begin has not yet been called. Begin must be called successfully before you can call DrawString."
	 * );
	 * }
	 */

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
		draw(texture, position, drawRectangle, sourceRectangle, origin, rotation, scale, color, SpriteEffects.None,
				layerDepth);

	}

	public void draw(Texture2D texture, Vector2 position, Rectangle drawRectangle, Rectangle sourceRectangle,
			Vector2 origin, float rotation, Vector2 scale, Color color, SpriteEffects effects)
	{
		draw(texture, position, drawRectangle, sourceRectangle, origin, rotation, scale, color, effects, 0.0f);

	}

	// Overload for calling Draw() with named parameters
	/**
	 * This is a MonoGame Extension method for calling Draw() using named parameters. It is not
	 * available in the standard XNA Framework.
	 * 
	 * @param texture
	 *        The Texture2D to draw. Required.
	 * @param position
	 *        The position to draw at. If left empty, the method will draw at drawRectangle instead.
	 * @param drawRectangle
	 *        The rectangle to draw at. If left empty, the method will draw at position instead.
	 * @param sourceRectangle
	 *        The source rectangle of the texture. Default is null
	 * @param origin
	 *        Origin of the texture. Default is Vector2.Zero
	 * @param rotation
	 *        Rotation of the texture. Default is 0f
	 * @param scale
	 *        The scale of the texture as a Vector2. Default is Vector2.One
	 * @param color
	 *        Color of the texture. Default is Color.White
	 * @param effect
	 *        SpriteEffect to draw with. Default is SpriteEffects.None
	 * @param depth
	 *        Draw depth. Default is 0f.
	 */
	public void draw(Texture2D texture, Vector2 position, Rectangle drawRectangle, Rectangle sourceRectangle,
			Vector2 origin, float rotation, Vector2 scale, Color color, SpriteEffects effects, float layerDepth)
	{
		// Assign default values to null parameters here, as they are not compile-time constants
		if (color == null)
			color = Color.White;
		if (origin == null)
			origin = Vector2.ZERO;
		if (scale == null)
			scale = Vector2.ONE;

		// If both drawRectangle and position are null, or if both have been assigned a value, raise
		// an error
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

	public void draw(Texture2D texture, Vector2 position, Rectangle sourceRectangle, Color color, float rotation,
			Vector2 origin, Vector2 scale, SpriteEffects effects, float layerDepth)
	{
		checkValid(texture);

		float w = texture.width * scale.x;
		float h = texture.height * scale.y;
		if (sourceRectangle != null)
		{
			w = sourceRectangle.width * scale.x;
			h = sourceRectangle.height * scale.y;
		}

		drawInternal(texture, new Vector4(position.x, position.y, w, h), sourceRectangle, color, rotation, new Vector2(
				origin.getX() * scale.getX(), origin.getY() * scale.getY()), effects, layerDepth, true);
	}

	public void draw(Texture2D texture, Vector2 position, Rectangle sourceRectangle, Color color, float rotation,
			Vector2 origin, float scale, SpriteEffects effects, float layerDepth)
	{
		checkValid(texture);

		float w = texture.width * scale;
		float h = texture.height * scale;
		if (sourceRectangle != null)
		{
			w = sourceRectangle.width * scale;
			h = sourceRectangle.height * scale;
		}

		drawInternal(texture, new Vector4(position.x, position.y, w, h), sourceRectangle, color, rotation, new Vector2(
				origin.getX() * scale, origin.getY() * scale), effects, layerDepth, true);
	}

	public void draw(Texture2D texture, Rectangle destinationRectangle, Rectangle sourceRectangle, Color color,
			float rotation, Vector2 origin, SpriteEffects effects, float layerDepth)
	{
		checkValid(texture);

		drawInternal(										//
				texture,									//
				new Vector4(destinationRectangle.x,			//
							destinationRectangle.y,			//
							destinationRectangle.width,		//
							destinationRectangle.height),	//
				sourceRectangle,							//
				color,										//
				rotation,									//
				new Vector2(								//
						origin.x * ((float) destinationRectangle.width /	//
								(float) ((sourceRectangle != null && sourceRectangle.width != 0) ?	//
											sourceRectangle.width : texture.width)),	//
						origin.y							//
								* ((float) destinationRectangle.height)	/ 	//
								(float) ((sourceRectangle != null && sourceRectangle.height != 0) ?	//
											sourceRectangle.height : texture.height)),	//
				effects,									//
				layerDepth,									//
				true);										//
	}

	protected void drawInternal(Texture2D texture, Vector4 destinationRectangle, Rectangle sourceRectangle,
			Color color, float rotation, Vector2 origin, SpriteEffects effect, float depth, boolean autoFlush)
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
		//if ((effect.equals(SpriteEffects.FlipVertically)) || (effect.equals(SpriteEffects.FlipBothAxis)))
		{
			float temp = _texCoordBR.y;
			_texCoordBR.y = _texCoordTL.y;
			_texCoordTL.y = temp;
		}
		if ((effect.getValue() & SpriteEffects.FlipHorizontally.getValue()) != 0)
		// if ((effect.equals(SpriteEffects.FlipHorizontally)) || (effect.equals(SpriteEffects.FlipBothAxis)))
		{
			float temp = _texCoordBR.x;
			_texCoordBR.x = _texCoordTL.x;
			_texCoordTL.x = temp;
		}

		item.set(destinationRectangle.x, destinationRectangle.y, -origin.x, -origin.y, destinationRectangle.z,
				destinationRectangle.w, (float) Math.sin(rotation), (float) Math.cos(rotation), color, _texCoordTL,
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

	public void draw(Texture2D texture, Vector2 position, Rectangle sourceRectangle, Color color)
	{
		draw(texture, position, sourceRectangle, color, 0f, Vector2.ZERO, 1f, SpriteEffects.None, 0f);
	}

	public void draw(Texture2D texture, Rectangle destinationRectangle, Rectangle sourceRectangle, Color color)
	{
		draw(texture, destinationRectangle, sourceRectangle, color, 0, Vector2.ZERO, SpriteEffects.None, 0f);
	}

	public void draw(Texture2D texture, Vector2 position, Color color)
	{
		draw(texture, position, null, color);
	}

	public void draw(Texture2D texture, Rectangle rectangle, Color color)
	{
		draw(texture, rectangle, null, color);
	}

	// TODO: DrawString
	/*
	 * public void DrawString (SpriteFont spriteFont, String text, Vector2 position, Color color)
	 * {
	 * CheckValid(spriteFont, text);
	 * 
	 * var source = new SpriteFont.CharacterSource(text);
	 * spriteFont.DrawInto (
	 * this, source, position, color, 0, Vector2.ZERO, Vector2.ONE, SpriteEffects.None, 0f);
	 * }
	 * 
	 * public void DrawString (
	 * SpriteFont spriteFont, String text, Vector2 position, Color color,
	 * float rotation, Vector2 origin, float scale, SpriteEffects effects, float depth)
	 * {
	 * CheckValid(spriteFont, text);
	 * 
	 * Vector2 scaleVec = new Vector2(scale, scale);
	 * var source = new SpriteFont.CharacterSource(text);
	 * spriteFont.DrawInto(this, source, position, color, rotation, origin, scaleVec, effects,
	 * depth);
	 * }
	 * 
	 * public void DrawString (
	 * SpriteFont spriteFont, String text, Vector2 position, Color color,
	 * float rotation, Vector2 origin, Vector2 scale, SpriteEffects effect, float depth)
	 * {
	 * CheckValid(spriteFont, text);
	 * 
	 * var source = new SpriteFont.CharacterSource(text);
	 * spriteFont.DrawInto(this, source, position, color, rotation, origin, scale, effect, depth);
	 * }
	 * 
	 * public void DrawString (SpriteFont spriteFont, StringBuilder text, Vector2 position, Color
	 * color)
	 * {
	 * CheckValid(spriteFont, text);
	 * 
	 * var source = new SpriteFont.CharacterSource(text);
	 * spriteFont.DrawInto(this, source, position, color, 0, Vector2.ZERO, Vector2.ONE,
	 * SpriteEffects.None, 0f);
	 * }
	 * 
	 * public void DrawString (
	 * SpriteFont spriteFont, StringBuilder text, Vector2 position, Color color,
	 * float rotation, Vector2 origin, float scale, SpriteEffects effects, float depth)
	 * {
	 * CheckValid(spriteFont, text);
	 * 
	 * Vector2 scaleVec = new Vector2(scale, scale);
	 * var source = new SpriteFont.CharacterSource(text);
	 * spriteFont.DrawInto(this, source, position, color, rotation, origin, scaleVec, effects,
	 * depth);
	 * }
	 * 
	 * public void DrawString (
	 * SpriteFont spriteFont, StringBuilder text, Vector2 position, Color color,
	 * float rotation, Vector2 origin, Vector2 scale, SpriteEffects effect, float depth)
	 * {
	 * CheckValid(spriteFont, text);
	 * 
	 * var source = new SpriteFont.CharacterSource(text);
	 * spriteFont.DrawInto(this, source, position, color, rotation, origin, scale, effect, depth);
	 * }
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
