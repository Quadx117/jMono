package gameCore.graphics.effect;

import gameCore.graphics.GraphicsDevice;
import gameCore.graphics.SamplerStateCollection;
import gameCore.graphics.Texture;
import gameCore.graphics.TextureCollection;
import gameCore.graphics.shader.ConstantBuffer;
import gameCore.graphics.shader.Shader;
import gameCore.graphics.shader.Shader.SamplerInfo;
import gameCore.graphics.shader.ShaderStage;
import gameCore.graphics.states.BlendState;
import gameCore.graphics.states.DepthStencilState;
import gameCore.graphics.states.RasterizerState;

public class EffectPass {

	private Effect _effect;
	
	private Shader _pixelShader;
	private Shader _vertexShader;
	
	private BlendState _blendState;
	private DepthStencilState _depthStencilState;
	private RasterizerState _rasterizerState;
	
	private String name;
	public String getName() { return name; }
	
	private EffectAnnotationCollection annotations;
	public EffectAnnotationCollection getAnnotations() { return annotations; }
	
	protected EffectPass(Effect effect, 
						 String name,
						 Shader vertexShader, 
						 Shader pixelShader, 
						 BlendState blendState, 
						 DepthStencilState depthStencilState, 
						 RasterizerState rasterizerState,
						 EffectAnnotationCollection annotations )
	{
		assert(effect != null) : "Got a null effect!";
		assert(annotations != null) : "Got a null annotation collection!";
		
		_effect = effect;
		
		this.name = name;
		
		_vertexShader = vertexShader;
		_pixelShader = pixelShader;
		
		_blendState = blendState;
		_depthStencilState = depthStencilState;
		_rasterizerState = rasterizerState;
		
		this.annotations = annotations;
	}
	
	protected EffectPass(Effect effect, EffectPass cloneSource)
	{
		assert(effect != null) : "Got a null effect!";
		assert(cloneSource != null) : "Got a null cloneSource!";
	
		_effect = effect;
	
		// Share all the immutable types.
		name = cloneSource.getName();
		_blendState = cloneSource._blendState;
		_depthStencilState = cloneSource._depthStencilState;
		_rasterizerState = cloneSource._rasterizerState;
		annotations = cloneSource.annotations;
		_vertexShader = cloneSource._vertexShader;
		_pixelShader = cloneSource._pixelShader;

	}

	public void apply()
	{
		// Set/get the correct shader handle/cleanups.
		//
		// TODO: This "re-apply" if the shader index changes
		// trick is sort of ugly.  We should probably rework
		// this to use some sort of "technique/pass redirect".
		//
		if (_effect.onApply())
		{
			_effect.currentTechnique.getPasses().getEffectPass(0).apply();
			return;
		}

		GraphicsDevice device = _effect.getGraphicsDevice();

//#if OPENGL || DIRECTX

		if (_vertexShader != null)
		{
			device.setVertexShader(_vertexShader);

			// Update the texture parameters.
			setShaderSamplers(_vertexShader, device.getVertexTextures(), device.getVertexSamplerStates());
			
			// Update the constant buffers.
			for (int c = 0; c < _vertexShader.getCBuffers().length; ++c)
			{
				ConstantBuffer cb = _effect.constantBuffers[_vertexShader.getCBuffers()[c]];
				cb.update(_effect.parameters);
				device.setConstantBuffer(ShaderStage.Vertex, c, cb);
			}
		}

		if (_pixelShader != null)
		{
			device.setPixelShader(_pixelShader);

			// Update the texture parameters.
			setShaderSamplers(_pixelShader, device.getTextures(), device.getSamplerStates());
			
			// Update the constant buffers.
			for (int c = 0; c < _pixelShader.getCBuffers().length; ++c)
			{
				ConstantBuffer cb = _effect.constantBuffers[_pixelShader.getCBuffers()[c]];
				cb.update(_effect.parameters);
				device.setConstantBuffer(ShaderStage.Pixel, c, cb);
			}
		}

// #endif

		// Set the render states if we have some.
		if (_rasterizerState != null)
			device.setRasterizerState(_rasterizerState);
		if (_blendState != null)
			device.setBlendState(_blendState);
		if (_depthStencilState != null)
			device.setDepthStencilState(_depthStencilState);
	}
	
	private void setShaderSamplers(Shader shader, TextureCollection textures, SamplerStateCollection samplerStates)
	{
		for (SamplerInfo sampler : shader.getSamplers())
		{
			EffectParameter param = _effect.parameters.getEffectParameter(sampler.parameter);
			Texture texture = (Texture) param.getData();// as Texture;

			textures.setTexture(sampler.textureSlot, texture);

			// If there is a sampler state set it.
			if (sampler.state != null)
				samplerStates.setSamplerStateCollection(sampler.samplerSlot, sampler.state);
		}
	}

}
