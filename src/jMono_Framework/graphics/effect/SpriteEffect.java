package jMono_Framework.graphics.effect;

import jMono_Framework.graphics.GraphicsDevice;
import jMono_Framework.graphics.Viewport;
import jMono_Framework.math.Matrix;

/**
 * The default effect used by SpriteBatch.
 * 
 * @author Eric Perron
 *
 */
public class SpriteEffect extends Effect {

    EffectParameter matrixParam;

    // TODO: Find a way to switch on platform type ?
    // TODO: If so, need to do it also in Shader and other classes
    public static byte[] bytecode = loadEffectResource(
//#if DIRECTX
    	"jMono_Framework/graphics/effect/resources/SpriteEffect.dx11.mgfxo"
//#elif PSM
    //    "Microsoft.Xna.Framework.PSSuite.Graphics.Resources.SpriteEffect.cgx" //FIXME: This shader is totally incomplete
//#else
         //"gameCore/graphics/effect/resources/SpriteEffect.ogl.mgfxo"
//#endif
    );

    /**
     * Creates a new SpriteEffect.
     * @param device
     */
    public SpriteEffect(GraphicsDevice device)
    {
    	super(device, bytecode);
        cacheEffectParameters();
    }

    /// <summary>
    /// Creates a new SpriteEffect by cloning parameter settings from an existing instance.
    /// </summary>
    protected SpriteEffect(SpriteEffect cloneSource)
    {
    	super(cloneSource);
        cacheEffectParameters();
    }


    /// <summary>
    /// Creates a clone of the current SpriteEffect instance.
    /// </summary>
    @Override
    public Effect clone()
    {
        return new SpriteEffect(this);
    }


    /// <summary>
    /// Looks up shortcut references to our effect parameters.
    /// </summary>
    void cacheEffectParameters()
    {
        matrixParam = parameters.getEffectParameter("MatrixTransform");
    }

    /**
     * Lazily computes derived parameter values immediately before applying the effect.
     */
    @Override
    protected boolean onApply()
    {
        Viewport viewport = graphicsDevice.getViewport();

        Matrix projection = Matrix.createOrthographicOffCenter(0, viewport.getWidth(), viewport.getHeight(), 0, 0, 1);
        Matrix halfPixelOffset = Matrix.createTranslation(-0.5f, -0.5f, 0);

        matrixParam.setValue(Matrix.multiply(halfPixelOffset, projection));

        return false;
    }

}
