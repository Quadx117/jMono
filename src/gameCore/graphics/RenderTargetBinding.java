package gameCore.graphics;

// http://msdn.microsoft.com/en-us/library/ff434403.aspx
public class RenderTargetBinding
{
	private Texture _renderTarget;
	private int _arraySlice;

	public Texture getRenderTarget()
	{
		return _renderTarget;
	}

	public int getArraySlice()
	{
		return _arraySlice;
	}

	// TODO: Since this is a struct, Do I need a no-args constructor ?
	public RenderTargetBinding(RenderTarget2D renderTarget)
	{
		if (renderTarget == null)
			throw new NullPointerException("renderTarget");

		_renderTarget = renderTarget;
		// _arraySlice = (int)CubeMapFace.PositiveX;
		_arraySlice = CubeMapFace.PositiveX.ordinal();
	}

	public RenderTargetBinding(RenderTargetCube renderTarget, CubeMapFace cubeMapFace)
	{
		if (renderTarget == null)
			throw new NullPointerException("renderTarget");
		if (cubeMapFace.ordinal() < CubeMapFace.PositiveX.ordinal() ||	//
			cubeMapFace.ordinal() > CubeMapFace.NegativeZ.ordinal())
			throw new IllegalArgumentException("cubeMapFace is out of range");

		_renderTarget = renderTarget;
		// _arraySlice = (int)cubeMapFace;
		_arraySlice = cubeMapFace.ordinal();
	}

// #if DIRECTX

// 	public RenderTargetBinding(RenderTarget2D renderTarget, int arraySlice)
// 	{
// 		if (renderTarget == null)
// 			throw new NullPointerException("renderTarget");
// 		if (arraySlice < 0 || arraySlice >= renderTarget.ArraySize)
// 			throw new IllegalArgumentException("arraySlice");
// 		if (!renderTarget.getGraphicsDevice().getGraphicsCapabilities().supportsTextureArrays)
// 			throw new IllegalArgumentException("Texture arrays are not supported on this graphics device");
// 
// 		_renderTarget = renderTarget;
// 		_arraySlice = arraySlice;
//     }

//    public RenderTargetBinding(RenderTarget3D renderTarget)
//    {
//        if (renderTarget == null)
//            throw new NullPointerException("renderTarget");
//
//        _renderTarget = renderTarget;
//        _arraySlice = 0;
//    }

//    public RenderTargetBinding(RenderTarget3D renderTarget, int arraySlice)
//    {
//        if (renderTarget == null)
//            throw new NullPointerException("renderTarget");
//        if (arraySlice < 0 || arraySlice >= renderTarget.Depth)
//            throw new IllegalArgumentException("arraySlice");
//
//        _renderTarget = renderTarget;
//        _arraySlice = arraySlice;
//    }

// #endif 

    /*
	 * c# implicit operator overloading
	 * see :
	 * http://www.codeproject.com/Articles/15191/Understanding-Implicit-Operator-Overloading-in-C
	 * Cannot be done in Java
	 */
//    public static implicit operator RenderTargetBinding(RenderTarget2D renderTarget)
//    {
//        return new RenderTargetBinding(renderTarget);
//    }

// #if DIRECTX

//    public static implicit operator RenderTargetBinding(RenderTarget3D renderTarget)
//    {
//        return new RenderTargetBinding(renderTarget);
//    }

// #endif

}
