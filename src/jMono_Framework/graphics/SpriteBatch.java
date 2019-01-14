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

/**
 * Helper class for drawing text strings and sprites in one or more optimized batches.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public class SpriteBatch extends GraphicsResource
{
    // #region Private Fields
    final SpriteBatcher _batcher;

    SpriteSortMode _sortMode;
    BlendState _blendState;
    SamplerState _samplerState;
    DepthStencilState _depthStencilState;
    RasterizerState _rasterizerState;
    Effect _effect;
    boolean _beginCalled;

    Effect _spriteEffect;
    final EffectParameter _matrixTransform;
    final EffectPass _spritePass;

    Matrix _matrix;
    private Viewport _lastViewport = new Viewport();
    private Matrix _projection = new Matrix();
    Rectangle _tempRect = new Rectangle(0, 0, 0, 0);
    Vector2 _texCoordTL = new Vector2(0.0f, 0.0f);
    Vector2 _texCoordBR = new Vector2(0.0f, 0.0f);

    protected static boolean NeedsHalfPixelOffset;

    /**
     * Constructs a {@link SpriteBatch}.
     * 
     * @param graphicsDevice
     *        The {@link GraphicsDevice}, which will be used for sprite rendering.
     * @throws NullPointerException
     *         If {@code graphicsDevice} is {@code null}.
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
     * <p>
     * This method uses optional parameters.
     * <p>
     * The {@code #begin()} method should be called before drawing commands, and you cannot call it
     * again before calling {@link #end()}.
     * 
     * @param sortMode
     *        The drawing order for sprite and text drawing. {@link SpriteSortMode#Deferred} by
     *        default.
     * @param blendState
     *        State of the blending. Uses {@link BlendState#AlphaBlend} if null.
     * @param samplerState
     *        State of the sampler. Uses {@link SamplerState#LinearClamp} if null.
     * @param depthStencilState
     *        State of the depth-stencil buffer. Uses {@link DepthStencilState#None} if null.
     * @param rasterizerState
     *        State of the rasterization. Uses {@link RasterizerState#CullCounterClockwise} if null.
     * @param effect
     *        A custom {@link Effect} to override the default sprite effect. Uses default sprite
     *        effect if null.
     * @param transformMatrix
     *        An optional matrix used to transform the sprite geometry. Uses
     *        {@link Matrix#identity()} if null.
     * @throws IllegalStateException
     *         If {@code #begin()} is called another time without calling {@link #end()}.
     */
    public void begin(SpriteSortMode sortMode,
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
        _matrix = transformMatrix;

        // Setup things now so a user can change them.
        if (SpriteSortMode.Immediate.equals(sortMode))
        {
            setup();
        }

        _beginCalled = true;
    }

    /**
     * Flushes all batched text and sprites to the screen.
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

        Viewport vp = gd.getViewport();
        if ((vp.getWidth() != _lastViewport.getWidth()) || (vp.getHeight() != _lastViewport.getHeight()))
        {
            // Normal 3D cameras look into the -z direction (z = 1 is in font of z = 0). The
            // sprite batch layer depth is the opposite (z = 0 is in front of z = 1).
            // --> We get the correct matrix with near plane 0 and far plane -1.
            Matrix.createOrthographicOffCenter(0, vp.getWidth(), vp.getHeight(), 0, 0, -1, _projection);

            // Some platforms require a half pixel offset to match DX.
            if (NeedsHalfPixelOffset)
            {
                _projection.m41 += -0.5f * _projection.m11;
                _projection.m42 += -0.5f * _projection.m22;
            }

            _lastViewport = vp;
        }

        if (_matrix != null)
            _matrixTransform.setValue(_matrix.multiply(_projection));
        else
            _matrixTransform.setValue(_projection);

        _spritePass.apply();
    }

    void checkValid(Texture2D texture)
    {
        if (texture == null)
            throw new NullPointerException("texture");
        if (!_beginCalled)
            throw new IllegalStateException("Draw was called, but Begin has not yet been called. Begin must be called successfully before you can call Draw.");
    }

    void checkValid(SpriteFont spriteFont, String text)
    {
        if (spriteFont == null)
            throw new NullPointerException("spriteFont");
        if (text == null)
            throw new NullPointerException("text");
        if (!_beginCalled)
            throw new IllegalStateException("DrawString was called, but Begin has not yet been called. Begin must be called successfully before you can call DrawString.");
    }

    void checkValid(SpriteFont spriteFont, StringBuilder text)
    {
        if (spriteFont == null)
            throw new NullPointerException("spriteFont");
        if (text == null)
            throw new NullPointerException("text");
        if (!_beginCalled)
            throw new IllegalStateException("DrawString was called, but Begin has not yet been called. Begin must be called successfully before you can call DrawString.");
    }

    /**
     * Submit a sprite for drawing in the current batch.
     * <p>
     * This overload uses optional parameters.<br>
     * This overload requires only one of {@code position} and {@code destinationRectangle} been
     * used.
     * 
     * @param texture
     *        The {@link Texture2D} to draw. Required.
     * @param position
     *        The drawing location on screen or null if {@code destinationRectangle} is used.
     * @param destinationRectangle
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
     * @deprecated In future versions this method can be removed.
     */
    @Deprecated
    public void draw(Texture2D texture,
                     Vector2 position,
                     Rectangle destinationRectangle,
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
            color = Color.White();
        if (origin == null)
            origin = Vector2.Zero();
        if (scale == null)
            scale = Vector2.One();

        // If both destinationRectangle and position are null, or if both have been assigned a
        // value, raise an error
        if ((destinationRectangle == null && position == null) || (destinationRectangle != null && position != null))
        {
            throw new IllegalArgumentException("Expected destinationRectangle or position, but received neither or both.");
        }
        else if (position != null)
        {
            // Call draw() using position
            draw(texture, position, sourceRectangle, color, rotation, origin, scale, effects, layerDepth);
        }
        else
        {
            // Call Draw() using destinationRectangle
            draw(texture, destinationRectangle, sourceRectangle, color, rotation, origin, effects, layerDepth);
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
    public void draw(Texture2D texture,
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

        SpriteBatchItem item = _batcher.createBatchItem();
        item.texture = texture;

        // set sortKey based on SpriteSortMode.
        switch (_sortMode)
        {
        // Comparison of Texture objects.
            case Texture:
                item.sortKey = texture.getSortingKey();
                break;
            // Comparison of Depth
            case FrontToBack:
                item.sortKey = layerDepth;
                break;
            // Comparison of Depth in reverse
            case BackToFront:
                item.sortKey = -layerDepth;
                break;
            default:
                break;
        }

        origin.multiply(scale);

        float w, h;
        if (sourceRectangle != null)
        {
            Rectangle srcRect = new Rectangle(sourceRectangle);
            w = srcRect.width * scale.x;
            h = srcRect.height * scale.y;
            _texCoordTL.x = srcRect.x * texture.getTexelWidth();
            _texCoordTL.y = srcRect.y * texture.getTexelHeight();
            _texCoordBR.x = (srcRect.x + srcRect.width) * texture.getTexelWidth();
            _texCoordBR.y = (srcRect.y + srcRect.height) * texture.getTexelHeight();
        }
        else
        {
            w = texture.width * scale.x;
            h = texture.height * scale.y;
            _texCoordTL = Vector2.Zero();
            _texCoordBR = Vector2.One();
        }

        if ((effects.getValue() & SpriteEffects.FlipVertically.getValue()) != 0)
        {
            float temp = _texCoordBR.y;
            _texCoordBR.y = _texCoordTL.y;
            _texCoordTL.y = temp;
        }
        if ((effects.getValue() & SpriteEffects.FlipHorizontally.getValue()) != 0)
        {
            float temp = _texCoordBR.x;
            _texCoordBR.x = _texCoordTL.x;
            _texCoordTL.x = temp;
        }

        if (rotation == 0f)
        {
            item.set(position.x - origin.x,
                     position.y - origin.y,
                     w,
                     h,
                     color,
                     _texCoordTL,
                     _texCoordBR,
                     layerDepth);
        }
        else
        {
            item.set(position.x,
                     position.y,
                     -origin.x,
                     -origin.y,
                     w,
                     h,
                     (float) Math.sin(rotation),
                     (float) Math.cos(rotation),
                     color,
                     _texCoordTL,
                     _texCoordBR,
                     layerDepth);
        }

        flushIfNeeded();
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
    public void draw(Texture2D texture,
                     Vector2 position,
                     Rectangle sourceRectangle,
                     Color color,
                     float rotation,
                     Vector2 origin,
                     float scale,
                     SpriteEffects effects,
                     float layerDepth)
    {
        Vector2 scaleVec = new Vector2(scale, scale);
        draw(texture, position, sourceRectangle, color, rotation, origin, scaleVec, effects, layerDepth);
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
    public void draw(Texture2D texture,
                     Rectangle destinationRectangle,
                     Rectangle sourceRectangle,
                     Color color,
                     float rotation,
                     Vector2 origin,
                     SpriteEffects effects,
                     float layerDepth)
    {
        checkValid(texture);

        SpriteBatchItem item = _batcher.createBatchItem();
        item.texture = texture;

        // set sortKey based on SpriteSortMode.
        switch (_sortMode)
        {
        // Comparison of Texture objects.
            case Texture:
                item.sortKey = texture.getSortingKey();
                break;
            // Comparison of Depth
            case FrontToBack:
                item.sortKey = layerDepth;
                break;
            // Comparison of Depth in reverse
            case BackToFront:
                item.sortKey = -layerDepth;
                break;
            default:
                break;
        }

        if (sourceRectangle != null)
        {
            Rectangle srcRect = new Rectangle(sourceRectangle);
            _texCoordTL.x = srcRect.x * texture.getTexelWidth();
            _texCoordTL.y = srcRect.y * texture.getTexelHeight();
            _texCoordBR.x = (srcRect.x + srcRect.width) * texture.getTexelWidth();
            _texCoordBR.y = (srcRect.y + srcRect.height) * texture.getTexelHeight();

            if (srcRect.width != 0)
                origin.x = origin.x * (float) destinationRectangle.width / (float) srcRect.width;
            else
                origin.x = origin.x * (float) destinationRectangle.width * texture.getTexelWidth();
            if (srcRect.height != 0)
                origin.y = origin.y * (float) destinationRectangle.height / (float) srcRect.height;
            else
                origin.y = origin.y * (float) destinationRectangle.height * texture.getTexelHeight();
        }
        else
        {
            _texCoordTL = Vector2.Zero();
            _texCoordBR = Vector2.One();

            origin.x = origin.x * (float) destinationRectangle.width * texture.getTexelWidth();
            origin.y = origin.y * (float) destinationRectangle.height * texture.getTexelHeight();
        }

        if ((effects.getValue() & SpriteEffects.FlipVertically.getValue()) != 0)
        {
            float temp = _texCoordBR.y;
            _texCoordBR.y = _texCoordTL.y;
            _texCoordTL.y = temp;
        }
        if ((effects.getValue() & SpriteEffects.FlipHorizontally.getValue()) != 0)
        {
            float temp = _texCoordBR.x;
            _texCoordBR.x = _texCoordTL.x;
            _texCoordTL.x = temp;
        }

        if (rotation == 0f)
        {
            item.set(destinationRectangle.x - origin.x,
                     destinationRectangle.y - origin.y,
                     destinationRectangle.width,
                     destinationRectangle.height,
                     color,
                     _texCoordTL,
                     _texCoordBR,
                     layerDepth);
        }
        else
        {
            item.set(destinationRectangle.x,
                     destinationRectangle.y,
                     -origin.x,
                     -origin.y,
                     destinationRectangle.width,
                     destinationRectangle.height,
                     (float) Math.sin(rotation),
                     (float) Math.cos(rotation),
                     color,
                     _texCoordTL,
                     _texCoordBR,
                     layerDepth);
        }

        flushIfNeeded();
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
        checkValid(texture);

        SpriteBatchItem item = _batcher.createBatchItem();
        item.texture = texture;

        // set sortKey based on SpriteSortMode.
        item.sortKey = _sortMode == SpriteSortMode.Texture ? texture.getSortingKey() : 0;

        Vector2 size;

        if (sourceRectangle != null)
        {
            Rectangle srcRect = new Rectangle(sourceRectangle);
            size = new Vector2(srcRect.width, srcRect.height);
            _texCoordTL.x = srcRect.x * texture.getTexelWidth();
            _texCoordTL.y = srcRect.y * texture.getTexelHeight();
            _texCoordBR.x = (srcRect.x + srcRect.width) * texture.getTexelWidth();
            _texCoordBR.y = (srcRect.y + srcRect.height) * texture.getTexelHeight();
        }
        else
        {
            size = new Vector2(texture.width, texture.height);
            _texCoordTL = Vector2.Zero();
            _texCoordBR = Vector2.One();
        }

        item.set(position.x,
                 position.y,
                 size.x,
                 size.y,
                 color,
                 _texCoordTL,
                 _texCoordBR,
                 0);

        flushIfNeeded();
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
        checkValid(texture);

        SpriteBatchItem item = _batcher.createBatchItem();
        item.texture = texture;

        // set sortKey based on SpriteSortMode.
        item.sortKey = _sortMode == SpriteSortMode.Texture ? texture.getSortingKey() : 0;

        if (sourceRectangle != null)
        {
            Rectangle srcRect = new Rectangle(sourceRectangle);
            _texCoordTL.x = srcRect.x * texture.getTexelWidth();
            _texCoordTL.y = srcRect.y * texture.getTexelHeight();
            _texCoordBR.x = (srcRect.x + srcRect.width) * texture.getTexelWidth();
            _texCoordBR.y = (srcRect.y + srcRect.height) * texture.getTexelHeight();
        }
        else
        {
            _texCoordTL = Vector2.Zero();
            _texCoordBR = Vector2.One();
        }

        item.set(destinationRectangle.x,
                 destinationRectangle.y,
                 destinationRectangle.width,
                 destinationRectangle.height,
                 color,
                 _texCoordTL,
                 _texCoordBR,
                 0);

        flushIfNeeded();
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
        checkValid(texture);

        SpriteBatchItem item = _batcher.createBatchItem();
        item.texture = texture;

        // set sortKey based on SpriteSortMode.
        item.sortKey = _sortMode == SpriteSortMode.Texture ? texture.getSortingKey() : 0;

        item.set(position.x,
                 position.y,
                 texture.width,
                 texture.height,
                 color,
                 Vector2.Zero(),
                 Vector2.One(),
                 0);

        flushIfNeeded();
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
        checkValid(texture);

        SpriteBatchItem item = _batcher.createBatchItem();
        item.texture = texture;

        // set sortKey based on SpriteSortMode.
        item.sortKey = _sortMode == SpriteSortMode.Texture ? texture.getSortingKey() : 0;

        item.set(destinationRectangle.x,
                 destinationRectangle.y,
                 destinationRectangle.width,
                 destinationRectangle.height,
                 color,
                 Vector2.Zero(),
                 Vector2.One(),
                 0);

        flushIfNeeded();
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

        float sortKey = (_sortMode == SpriteSortMode.Texture) ? spriteFont.getTexture().getSortingKey() : 0;

        // Get the default glyph here once.
        SpriteFont.Glyph defaultGlyph = null;
        if (spriteFont.defaultCharacter != null)
            defaultGlyph = spriteFont.getGlyphs().get(spriteFont.defaultCharacter.charValue());

        SpriteFont.Glyph currentGlyph = spriteFont.new Glyph(); // SpriteFont.Glyph.Empty;
        Vector2 offset = Vector2.Zero();
        boolean firstGlyphOfLine = true;

        for (int i = 0; i < text.length(); ++i)
        {
            char c = text.charAt(i);

            if (c == '\r')
                continue;

            if (c == '\n')
            {
                offset.x = 0;
                offset.y += spriteFont.lineSpacing;
                firstGlyphOfLine = true;
                continue;
            }

            currentGlyph = spriteFont.getGlyphs().get(c);
            if (currentGlyph == null)
            {
                if (defaultGlyph == null)
                    throw new IllegalArgumentException("text: " + SpriteFont.Errors.TextContainsUnresolvableCharacters);

                currentGlyph = defaultGlyph;
            }

            // The first character on a line might have a negative left side bearing.
            // In this scenario, SpriteBatch/SpriteFont normally offset the text to the right,
            // so that text does not hang off the left side of its rectangle.
            if (firstGlyphOfLine)
            {
                offset.x = Math.max(currentGlyph.leftSideBearing, 0);
                firstGlyphOfLine = false;
            }
            else
            {
                offset.x += spriteFont.spacing + currentGlyph.leftSideBearing;
            }

            Vector2 p = new Vector2(offset);
            p.x += currentGlyph.cropping.x;
            p.y += currentGlyph.cropping.y;
            p.add(position);

            SpriteBatchItem item = _batcher.createBatchItem();
            item.texture = spriteFont.getTexture();
            item.sortKey = sortKey;

            _texCoordTL.x = currentGlyph.boundsInTexture.x * spriteFont.getTexture().getTexelWidth();
            _texCoordTL.y = currentGlyph.boundsInTexture.y * spriteFont.getTexture().getTexelHeight();
            _texCoordBR.x = (currentGlyph.boundsInTexture.x + currentGlyph.boundsInTexture.width) * spriteFont.getTexture().getTexelWidth();
            _texCoordBR.y = (currentGlyph.boundsInTexture.y + currentGlyph.boundsInTexture.height) * spriteFont.getTexture().getTexelHeight();

            item.set(p.x,
                     p.y,
                     currentGlyph.boundsInTexture.width,
                     currentGlyph.boundsInTexture.height,
                     color,
                     _texCoordTL,
                     _texCoordBR,
                     0);

            offset.x += currentGlyph.width + currentGlyph.rightSideBearing;
        }

        // We need to flush if we're using Immediate sort mode.
        flushIfNeeded();
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
    public void drawString(SpriteFont spriteFont, String text, Vector2 position, Color color,
                           float rotation, Vector2 origin, float scale, SpriteEffects effects, float layerDepth)
    {
        Vector2 scaleVec = new Vector2(scale, scale);
        drawString(spriteFont, text, position, color, rotation, origin, scaleVec, effects, layerDepth);
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
    public void drawString(SpriteFont spriteFont, String text, Vector2 position, Color color,
                           float rotation, Vector2 origin, Vector2 scale, SpriteEffects effects, float layerDepth)
    {
        checkValid(spriteFont, text);

        float sortKey = 0;
        // set SsortKey based on SpriteSortMode.
        switch (_sortMode)
        {
        // Comparison of Texture objects.
            case Texture:
                sortKey = spriteFont.getTexture().getSortingKey();
                break;
            // Comparison of Depth
            case FrontToBack:
                sortKey = layerDepth;
                break;
            // Comparison of Depth in reverse
            case BackToFront:
                sortKey = -layerDepth;
                break;
            default:
                break;
        }

        Vector2 flipAdjustment = Vector2.Zero();

        boolean flippedVert = (effects.getValue() & SpriteEffects.FlipVertically.getValue()) == SpriteEffects.FlipVertically.getValue();
        boolean flippedHorz = (effects.getValue() & SpriteEffects.FlipHorizontally.getValue()) == SpriteEffects.FlipHorizontally.getValue();

        if (flippedVert || flippedHorz)
        {
            Vector2 size = new Vector2();

            CharacterSource source = spriteFont.new CharacterSource(text);
            spriteFont.measureString(source, size);

            if (flippedHorz)
            {
                origin.x *= -1;
                flipAdjustment.x = -size.x;
            }

            if (flippedVert)
            {
                origin.y *= -1;
                flipAdjustment.y = spriteFont.lineSpacing - size.y;
            }
        }

        Matrix transformation = Matrix.identity();
        float cos = 0, sin = 0;
        if (rotation == 0)
        {
            transformation.m11 = (flippedHorz ? -scale.x : scale.x);
            transformation.m22 = (flippedVert ? -scale.y : scale.y);
            transformation.m41 = ((flipAdjustment.x - origin.x) * transformation.m11) + position.x;
            transformation.m42 = ((flipAdjustment.y - origin.y) * transformation.m22) + position.y;
        }
        else
        {
            cos = (float) Math.cos(rotation);
            sin = (float) Math.sin(rotation);
            transformation.m11 = (flippedHorz ? -scale.x : scale.x) * cos;
            transformation.m12 = (flippedHorz ? -scale.x : scale.x) * sin;
            transformation.m21 = (flippedVert ? -scale.y : scale.y) * (-sin);
            transformation.m22 = (flippedVert ? -scale.y : scale.y) * cos;
            transformation.m41 = (((flipAdjustment.x - origin.x) * transformation.m11) + (flipAdjustment.y - origin.y) * transformation.m21) + position.x;
            transformation.m42 = (((flipAdjustment.x - origin.x) * transformation.m12) + (flipAdjustment.y - origin.y) * transformation.m22) + position.y;
        }

        // Get the default glyph here once.
        SpriteFont.Glyph defaultGlyph = null;
        if (spriteFont.defaultCharacter != null)
            defaultGlyph = spriteFont.getGlyphs().get(spriteFont.defaultCharacter.charValue());

        SpriteFont.Glyph currentGlyph = spriteFont.new Glyph(); // SpriteFont.Glyph.Empty;
        Vector2 offset = Vector2.Zero();
        boolean firstGlyphOfLine = true;

        for (int i = 0; i < text.length(); ++i)
        {
            char c = text.charAt(i);

            if (c == '\r')
                continue;

            if (c == '\n')
            {
                offset.x = 0;
                offset.y += spriteFont.lineSpacing;
                firstGlyphOfLine = true;
                continue;
            }

            currentGlyph = spriteFont.getGlyphs().get(c);
            if (currentGlyph == null)
            {
                if (defaultGlyph == null)
                    throw new IllegalArgumentException("text: " + SpriteFont.Errors.TextContainsUnresolvableCharacters);

                currentGlyph = defaultGlyph;
            }

            // The first character on a line might have a negative left side bearing.
            // In this scenario, SpriteBatch/SpriteFont normally offset the text to the right,
            // so that text does not hang off the left side of its rectangle.
            if (firstGlyphOfLine)
            {
                offset.x = Math.max(currentGlyph.leftSideBearing, 0);
                firstGlyphOfLine = false;
            }
            else
            {
                offset.x += spriteFont.spacing + currentGlyph.leftSideBearing;
            }

            Vector2 p = new Vector2(offset);

            if (flippedHorz)
                p.x += currentGlyph.boundsInTexture.width;
            p.x += currentGlyph.cropping.x;

            if (flippedVert)
                p.y += currentGlyph.boundsInTexture.height - spriteFont.lineSpacing;
            p.y += currentGlyph.cropping.y;

            Vector2.transform(p, transformation, p);

            SpriteBatchItem item = _batcher.createBatchItem();
            item.texture = spriteFont.getTexture();
            item.sortKey = sortKey;

            _texCoordTL.x = currentGlyph.boundsInTexture.x * spriteFont.getTexture().getTexelWidth();
            _texCoordTL.y = currentGlyph.boundsInTexture.y * spriteFont.getTexture().getTexelHeight();
            _texCoordBR.x = (currentGlyph.boundsInTexture.x + currentGlyph.boundsInTexture.width) * spriteFont.getTexture().getTexelWidth();
            _texCoordBR.y = (currentGlyph.boundsInTexture.y + currentGlyph.boundsInTexture.height) * spriteFont.getTexture().getTexelHeight();

            if ((effects.getValue() & SpriteEffects.FlipVertically.getValue()) != 0)
            {
                float temp = _texCoordBR.y;
                _texCoordBR.y = _texCoordTL.y;
                _texCoordTL.y = temp;
            }
            if ((effects.getValue() & SpriteEffects.FlipHorizontally.getValue()) != 0)
            {
                float temp = _texCoordBR.x;
                _texCoordBR.x = _texCoordTL.x;
                _texCoordTL.x = temp;
            }

            if (rotation == 0f)
            {
                item.set(p.x,
                         p.y,
                         currentGlyph.boundsInTexture.width * scale.x,
                         currentGlyph.boundsInTexture.height * scale.y,
                         color,
                         _texCoordTL,
                         _texCoordBR,
                         layerDepth);
            }
            else
            {
                item.set(p.x,
                         p.y,
                         0,
                         0,
                         currentGlyph.boundsInTexture.width * scale.x,
                         currentGlyph.boundsInTexture.height * scale.y,
                         sin,
                         cos,
                         color,
                         _texCoordTL,
                         _texCoordBR,
                         layerDepth);
            }

            offset.x += currentGlyph.width + currentGlyph.rightSideBearing;
        }

        // We need to flush if we're using Immediate sort mode.
        flushIfNeeded();
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

        float sortKey = (_sortMode == SpriteSortMode.Texture) ? spriteFont.getTexture().getSortingKey() : 0;

        // Get the default glyph here once.
        SpriteFont.Glyph defaultGlyph = null;
        if (spriteFont.defaultCharacter != null)
            defaultGlyph = spriteFont.getGlyphs().get(spriteFont.defaultCharacter.charValue());

        SpriteFont.Glyph currentGlyph = spriteFont.new Glyph(); // SpriteFont.Glyph.Empty;
        Vector2 offset = Vector2.Zero();
        boolean firstGlyphOfLine = true;

        for (int i = 0; i < text.length(); ++i)
        {
            char c = text.charAt(i);

            if (c == '\r')
                continue;

            if (c == '\n')
            {
                offset.x = 0;
                offset.y += spriteFont.lineSpacing;
                firstGlyphOfLine = true;
                continue;
            }

            currentGlyph = spriteFont.getGlyphs().get(c);
            if (currentGlyph == null)
            {
                if (defaultGlyph == null)
                    throw new IllegalArgumentException("text: " + SpriteFont.Errors.TextContainsUnresolvableCharacters);

                currentGlyph = defaultGlyph;
            }

            // The first character on a line might have a negative left side bearing.
            // In this scenario, SpriteBatch/SpriteFont normally offset the text to the right,
            // so that text does not hang off the left side of its rectangle.
            if (firstGlyphOfLine)
            {
                offset.x = Math.max(currentGlyph.leftSideBearing, 0);
                firstGlyphOfLine = false;
            }
            else
            {
                offset.x += spriteFont.spacing + currentGlyph.leftSideBearing;
            }

            Vector2 p = new Vector2(offset);
            p.x += currentGlyph.cropping.x;
            p.y += currentGlyph.cropping.y;
            p.add(position);

            SpriteBatchItem item = _batcher.createBatchItem();
            item.texture = spriteFont.getTexture();
            item.sortKey = sortKey;

            _texCoordTL.x = currentGlyph.boundsInTexture.x * spriteFont.getTexture().getTexelWidth();
            _texCoordTL.y = currentGlyph.boundsInTexture.y * spriteFont.getTexture().getTexelHeight();
            _texCoordBR.x = (currentGlyph.boundsInTexture.x + currentGlyph.boundsInTexture.width) * spriteFont.getTexture().getTexelWidth();
            _texCoordBR.y = (currentGlyph.boundsInTexture.y + currentGlyph.boundsInTexture.height) * spriteFont.getTexture().getTexelHeight();

            item.set(p.x,
                     p.y,
                     currentGlyph.boundsInTexture.width,
                     currentGlyph.boundsInTexture.height,
                     color,
                     _texCoordTL,
                     _texCoordBR,
                     0);

            offset.x += currentGlyph.width + currentGlyph.rightSideBearing;
        }

        // We need to flush if we're using Immediate sort mode.
        flushIfNeeded();
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
    public void drawString(SpriteFont spriteFont, StringBuilder text, Vector2 position, Color color,
                           float rotation, Vector2 origin, float scale, SpriteEffects effects, float layerDepth)
    {
        Vector2 scaleVec = new Vector2(scale, scale);
        drawString(spriteFont, text, position, color, rotation, origin, scaleVec, effects, layerDepth);
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
    public void drawString(SpriteFont spriteFont, StringBuilder text, Vector2 position, Color color,
                           float rotation, Vector2 origin, Vector2 scale, SpriteEffects effects, float layerDepth)
    {
        checkValid(spriteFont, text);

        float sortKey = 0;
        // set SsortKey based on SpriteSortMode.
        switch (_sortMode)
        {
        // Comparison of Texture objects.
            case Texture:
                sortKey = spriteFont.getTexture().getSortingKey();
                break;
            // Comparison of Depth
            case FrontToBack:
                sortKey = layerDepth;
                break;
            // Comparison of Depth in reverse
            case BackToFront:
                sortKey = -layerDepth;
                break;
            default:
                break;
        }

        Vector2 flipAdjustment = Vector2.Zero();

        boolean flippedVert = (effects.getValue() & SpriteEffects.FlipVertically.getValue()) == SpriteEffects.FlipVertically.getValue();
        boolean flippedHorz = (effects.getValue() & SpriteEffects.FlipHorizontally.getValue()) == SpriteEffects.FlipHorizontally.getValue();

        if (flippedVert || flippedHorz)
        {
            CharacterSource source = spriteFont.new CharacterSource(text);
            Vector2 size = new Vector2();
            spriteFont.measureString(source, size);

            if (flippedHorz)
            {
                origin.x *= -1;
                flipAdjustment.x = -size.x;
            }

            if (flippedVert)
            {
                origin.y *= -1;
                flipAdjustment.y = spriteFont.lineSpacing - size.y;
            }
        }

        Matrix transformation = Matrix.identity();
        float cos = 0, sin = 0;
        if (rotation == 0)
        {
            transformation.m11 = (flippedHorz ? -scale.x : scale.x);
            transformation.m22 = (flippedVert ? -scale.y : scale.y);
            transformation.m41 = ((flipAdjustment.x - origin.x) * transformation.m11) + position.x;
            transformation.m42 = ((flipAdjustment.y - origin.y) * transformation.m22) + position.y;
        }
        else
        {
            cos = (float) Math.cos(rotation);
            sin = (float) Math.sin(rotation);
            transformation.m11 = (flippedHorz ? -scale.x : scale.x) * cos;
            transformation.m12 = (flippedHorz ? -scale.x : scale.x) * sin;
            transformation.m21 = (flippedVert ? -scale.y : scale.y) * (-sin);
            transformation.m22 = (flippedVert ? -scale.y : scale.y) * cos;
            transformation.m41 = (((flipAdjustment.x - origin.x) * transformation.m11) + (flipAdjustment.y - origin.y) * transformation.m21) + position.x;
            transformation.m42 = (((flipAdjustment.x - origin.x) * transformation.m12) + (flipAdjustment.y - origin.y) * transformation.m22) + position.y;
        }

        // Get the default glyph here once.
        SpriteFont.Glyph defaultGlyph = null;
        if (spriteFont.defaultCharacter != null)
            defaultGlyph = spriteFont.getGlyphs().get(spriteFont.defaultCharacter.charValue());

        SpriteFont.Glyph currentGlyph = spriteFont.new Glyph(); // SpriteFont.Glyph.Empty;
        Vector2 offset = Vector2.Zero();
        boolean firstGlyphOfLine = true;

        for (int i = 0; i < text.length(); ++i)
        {
            char c = text.charAt(i);

            if (c == '\r')
                continue;

            if (c == '\n')
            {
                offset.x = 0;
                offset.y += spriteFont.lineSpacing;
                firstGlyphOfLine = true;
                continue;
            }

            currentGlyph = spriteFont.getGlyphs().get(c);
            if (currentGlyph == null)
            {
                if (defaultGlyph == null)
                    throw new IllegalArgumentException("text: " + SpriteFont.Errors.TextContainsUnresolvableCharacters);

                currentGlyph = defaultGlyph;
            }

            // The first character on a line might have a negative left side bearing.
            // In this scenario, SpriteBatch/SpriteFont normally offset the text to the right,
            // so that text does not hang off the left side of its rectangle.
            if (firstGlyphOfLine)
            {
                offset.x = Math.max(currentGlyph.leftSideBearing, 0);
                firstGlyphOfLine = false;
            }
            else
            {
                offset.x += spriteFont.spacing + currentGlyph.leftSideBearing;
            }

            Vector2 p = new Vector2(offset);

            if (flippedHorz)
                p.x += currentGlyph.boundsInTexture.width;
            p.x += currentGlyph.cropping.x;

            if (flippedVert)
                p.y += currentGlyph.boundsInTexture.height - spriteFont.lineSpacing;
            p.y += currentGlyph.cropping.y;

            Vector2.transform(p, transformation, p);

            SpriteBatchItem item = _batcher.createBatchItem();
            item.texture = spriteFont.getTexture();
            item.sortKey = sortKey;

            _texCoordTL.x = currentGlyph.boundsInTexture.x * spriteFont.getTexture().getTexelWidth();
            _texCoordTL.y = currentGlyph.boundsInTexture.y * spriteFont.getTexture().getTexelHeight();
            _texCoordBR.x = (currentGlyph.boundsInTexture.x + currentGlyph.boundsInTexture.width) * spriteFont.getTexture().getTexelWidth();
            _texCoordBR.y = (currentGlyph.boundsInTexture.y + currentGlyph.boundsInTexture.height) * spriteFont.getTexture().getTexelHeight();

            if ((effects.getValue() & SpriteEffects.FlipVertically.getValue()) != 0)
            {
                float temp = _texCoordBR.y;
                _texCoordBR.y = _texCoordTL.y;
                _texCoordTL.y = temp;
            }
            if ((effects.getValue() & SpriteEffects.FlipHorizontally.getValue()) != 0)
            {
                float temp = _texCoordBR.x;
                _texCoordBR.x = _texCoordTL.x;
                _texCoordTL.x = temp;
            }

            if (rotation == 0f)
            {
                item.set(p.x,
                         p.y,
                         currentGlyph.boundsInTexture.width * scale.x,
                         currentGlyph.boundsInTexture.height * scale.y,
                         color,
                         _texCoordTL,
                         _texCoordBR,
                         layerDepth);
            }
            else
            {
                item.set(p.x,
                         p.y,
                         0,
                         0,
                         currentGlyph.boundsInTexture.width * scale.x,
                         currentGlyph.boundsInTexture.height * scale.y,
                         sin,
                         cos,
                         color,
                         _texCoordTL,
                         _texCoordBR,
                         layerDepth);
            }

            offset.x += currentGlyph.width + currentGlyph.rightSideBearing;
        }

        // We need to flush if we're using Immediate sort mode.
        flushIfNeeded();
    }

    /**
     * Immediately releases the unmanaged resources used by this object.
     * 
     * @param disposing
     *        {@code true} to release both managed and unmanaged resources; {@code false} to release
     *        only unmanaged resources.
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

    // TODO(Eric): Add other begin overloads if needed.
    // NOTE(Eric): I added these begin method overloads because of default parameters in C#
    public void begin()
    {
        begin(SpriteSortMode.Deferred, null, null, null, null, null, null);
    }

    public void begin(SpriteSortMode sortMode)
    {
        begin(sortMode, null, null, null, null, null, null);
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

    // TODO(Eric): Add other draw overloads if needed.
    // NOTE(Eric): I added these draw method overloads because of default parameters in C#
    public void draw(Texture2D texture, Rectangle destinationRectangle, Rectangle sourceRectangle, Vector2 origin,
                     float rotation, Vector2 scale, Color color, SpriteEffects effects, float layerDepth)
    {
        draw(texture, null, destinationRectangle, sourceRectangle, origin, rotation, scale, color, effects, layerDepth);

    }

    public void draw(Texture2D texture, Vector2 position, Rectangle sourceRectangle, Vector2 origin, float rotation,
                     Vector2 scale, Color color, SpriteEffects effects, float layerDepth)
    {
        draw(texture, position, null, sourceRectangle, origin, rotation, scale, color, effects, layerDepth);

    }

    public void draw(Texture2D texture, Vector2 position, Rectangle destinationRectangle, Rectangle originRectangle,
                     float rotation, Vector2 scale, Color color, SpriteEffects effects, float layerDepth)
    {
        draw(texture, position, destinationRectangle, originRectangle, null, rotation, scale, color, effects, layerDepth);

    }

    public void draw(Texture2D texture, Vector2 position, Rectangle destinationRectangle, Rectangle sourceRectangle,
                     float rotation, Vector2 scale, Color color, float layerDepth, SpriteEffects effects)
    {
        draw(texture, position, destinationRectangle, sourceRectangle, null, rotation, scale, color, effects, layerDepth);

    }

    public void draw(Texture2D texture, Vector2 position, Rectangle destinationRectangle, Rectangle sourceRectangle,
                     Vector2 origin, Vector2 scale, Color color, SpriteEffects effects, float layerDepth)
    {
        draw(texture, position, destinationRectangle, sourceRectangle, origin, 0.0f, scale, color, effects, layerDepth);

    }

    public void draw(Texture2D texture, Vector2 position, Rectangle destinationRectangle, Rectangle sourceRectangle,
                     Vector2 origin, float rotation, Color color, SpriteEffects effects, float layerDepth)
    {
        draw(texture, position, destinationRectangle, sourceRectangle, origin, rotation, null, color, effects, layerDepth);

    }

    public void draw(Texture2D texture, Vector2 position, Rectangle destinationRectangle, Rectangle sourceRectangle,
                     Vector2 origin, float rotation, Vector2 scale, SpriteEffects effects, float layerDepth)
    {
        draw(texture, position, destinationRectangle, sourceRectangle, origin, rotation, scale, null, effects, layerDepth);

    }

    public void draw(Texture2D texture, Vector2 position, Rectangle destinationRectangle, Rectangle sourceRectangle,
                     Vector2 origin, float rotation, Vector2 scale, Color color, float layerDepth)
    {
        draw(texture, position, destinationRectangle, sourceRectangle, origin, rotation, scale, color, SpriteEffects.None, layerDepth);

    }

    public void draw(Texture2D texture, Vector2 position, Rectangle destinationRectangle, Rectangle sourceRectangle,
                     Vector2 origin, float rotation, Vector2 scale, Color color, SpriteEffects effects)
    {
        draw(texture, position, destinationRectangle, sourceRectangle, origin, rotation, scale, color, effects, 0.0f);

    }

}
