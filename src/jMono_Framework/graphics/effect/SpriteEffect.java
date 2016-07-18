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
public class SpriteEffect extends Effect
{
	EffectParameter matrixParam;

	public static byte[] bytecode = loadEffectResource(
// #if DIRECTX
			"jMono_Framework/graphics/effect/resources/SpriteEffect.dx11.mgfxo"
// #elif PSM
			// "Microsoft.Xna.Framework.PSSuite.Graphics.Resources.SpriteEffect.cgx" //FIXME: This shader is totally
			// incomplete
// #else
			// "gameCore/graphics/effect/resources/SpriteEffect.ogl.mgfxo"
// #endif
			);

	/**
	 * Creates a new SpriteEffect.
	 * 
	 * @param device
	 */
	public SpriteEffect(GraphicsDevice device)
	{
		super(device, bytecode);
		cacheEffectParameters();
	}

	/**
	 * Creates a new SpriteEffect by cloning parameter settings from an existing instance.
	 * 
	 * @param cloneSource
	 * 				The {@code SpriteEffect} used to construct this instance
	 */
	protected SpriteEffect(SpriteEffect cloneSource)
	{
		super(cloneSource);
		cacheEffectParameters();
	}

	/**
	 * Creates a clone of the current SpriteEffect instance.
	 */
	@Override
	public Effect clone()
	{
		return new SpriteEffect(this);
	}

	/**
	 * Looks up shortcut references to our effect parameters.
	 */
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
