package gameCore.graphics.shader;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to Cache the links between Vertex/Pixel Shaders and Constant Buffers. It will
 * be responsible for linking the programs under OpenGL if they have not been linked before. If an
 * existing link exists it will be resused.
 * 
 * @author Eric Perron
 *
 */
public class ShaderProgramCache {

	public class ShaderProgram {

		public int program;

		private HashMap<String, Integer> _uniformLocations = new HashMap<String, Integer>();

		public ShaderProgram(int program) {
			this.program = program;
		}

		public int getUniformLocation(String name) {
			if (_uniformLocations.containsKey(name))
				return _uniformLocations.get(name);

			int location = GL.GetUniformLocation(program, name);
			GraphicsExtensions.CheckGLError();
			_uniformLocations.put(name, location);
			return location;
		}
	}

	private HashMap<Integer, ShaderProgram> _programCache = new HashMap<Integer, ShaderProgram>();
	boolean disposed;

	@Override
	public void finalize() {
		dispose(false);
	}

	// / <summary>
	// / Clear the program cache releasing all shader programs.
	// / </summary>
	public void clear() {
		for (Map.Entry<Integer, ShaderProgram> pair : _programCache.entrySet()) {
			if (GL.IsProgram(pair.getValue().program)) {
				// #if MONOMAC
				// GL.DeleteProgram(pair.Value.Program, null);
				// #else
				GL.DeleteProgram(pair.getValue().program);
				// #endif
				GraphicsExtensions.CheckGLError();
			}
		}
		_programCache.clear();
	}

	public ShaderProgram GetProgram(Shader vertexShader, Shader pixelShader) {
		// TODO: We should be hashing in the mix of constant
		// buffers here as well. This would allow us to optimize
		// setting uniforms to only when a constant buffer changes.

		int key = vertexShader.HashKey | pixelShader.HashKey;
		if (!_programCache.containsKey(key)) {
			// the key does not exist so we need to link the programs
			Link(vertexShader, pixelShader);
		}

		return _programCache.get(key);
	}

	private void Link(Shader vertexShader, Shader pixelShader)
        {
            // NOTE: No need to worry about background threads here
            // as this is only called at draw time when we're in the
            // main drawing thread.
            var program = GL.CreateProgram();
            GraphicsExtensions.CheckGLError();

            GL.AttachShader(program, vertexShader.GetShaderHandle());
            GraphicsExtensions.CheckGLError();

            GL.AttachShader(program, pixelShader.GetShaderHandle());
            GraphicsExtensions.CheckGLError();

            //vertexShader.BindVertexAttributes(program);

            GL.LinkProgram(program);
            GraphicsExtensions.CheckGLError();

            GL.UseProgram(program);
            GraphicsExtensions.CheckGLError();

            vertexShader.GetVertexAttributeLocations(program);

            pixelShader.ApplySamplerTextureUnits(program);

            var linked = 0;

//#if GLES && !ANGLE && !ANDROID
//            GL.GetProgram(program, GetProgramParameterName.LinkStatus, ref linked);
//#else
            GL.GetProgram(program, GetProgramParameterName.LinkStatus, out linked);
//#endif
            GraphicsExtensions.LogGLError("VertexShaderCache.Link(), GL.GetProgram");
            if (linked == 0)
            {
//#if !GLES
//                var log = GL.GetProgramInfoLog(program);
//                Console.WriteLine(log);
//#endif
                GL.DetachShader(program, vertexShader.GetShaderHandle());
                GL.DetachShader(program, pixelShader.GetShaderHandle());
//#if MONOMAC
//                GL.DeleteProgram(1, ref program);
//#else
                GL.DeleteProgram(program);
//#endif
                throw new UnsupportedOperationException("Unable to link effect program");
            }

            ShaderProgram shaderProgram = new ShaderProgram(program);

            _programCache.put(vertexShader.HashKey | pixelShader.HashKey, shaderProgram);
        }

	// TODO: equivalent is finalize(). Do I need this ?
	// public void Dispose() {
	// Dispose(true);
	// GC.SuppressFinalize(this);
	// }

	protected void Dispose(boolean disposing) {
		if (!disposed) {
			if (disposing)
				clear();
			disposed = true;
		}
	}
}
