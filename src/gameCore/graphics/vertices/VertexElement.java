package gameCore.graphics.vertices;

public class VertexElement {

	protected int _offset;
	protected VertexElementFormat _format;
	protected VertexElementUsage _usage;
	protected int _usageIndex;

	public int getOffset() {
		return this._offset;
	}

	public void setOffset(int value) {
		this._offset = value;
	}

	public VertexElementFormat getVertexElementFormat() {
		return this._format;
	}

	public void setVertexElementFormat(VertexElementFormat value) {
		this._format = value;
	}

	public VertexElementUsage getVertexElementUsage() {
		return this._usage;
	}

	public void setVertexElementUsage(VertexElementUsage value) {
		this._usage = value;
	}

	public int getUsageIndex() {
		return this._usageIndex;
	}

	public void setUsageIndex(int value) {
		this._usageIndex = value;
	}

	public VertexElement(int offset, VertexElementFormat elementFormat, VertexElementUsage elementUsage, int usageIndex)
	{
		this._offset = offset;
		this._usageIndex = usageIndex;
		this._format = elementFormat;
		this._usage = elementUsage;
	}

	@Override
	public int hashCode() {
		// TODO: Fix hashes
		return 0;
	}

	@Override
	public String toString() {
		return "{{Offset:" + this._offset + " Format:" + this.getVertexElementFormat() + " Usage:"
				+ this.getVertexElementUsage() + " UsageIndex: " + this.getUsageIndex() + "}}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		return (staticEquals(this, (VertexElement) obj));
	}

	public static boolean staticEquals(VertexElement left, VertexElement right) {
		return ((((left._offset == right._offset) && (left._usageIndex == right._usageIndex)) && (left._usage == right._usage)) && (left._format == right._format));
	}

	public static boolean notEquals(VertexElement left, VertexElement right) {
		return (!staticEquals(left, right));
	}

	// TODO: DIRECTX
	// #if DIRECTX
	//
	// protected SharpDX.Direct3D11.InputElement GetInputElement() {
	// var element = new SharpDX.Direct3D11.InputElement();
	//
	// switch (_usage)
	// {
	// case Graphics.VertexElementUsage.Position:
	// element.SemanticName = "SV_Position";
	// break;
	//
	// case Graphics.VertexElementUsage.Color:
	// element.SemanticName = "COLOR";
	// break;
	//
	// case Graphics.VertexElementUsage.Normal:
	// element.SemanticName = "NORMAL";
	// break;
	//
	// case Graphics.VertexElementUsage.TextureCoordinate:
	// element.SemanticName = "TEXCOORD";
	// break;
	//
	// case Graphics.VertexElementUsage.BlendIndices:
	// element.SemanticName = "BLENDINDICES";
	// break;
	//
	// case Graphics.VertexElementUsage.BlendWeight:
	// element.SemanticName = "BLENDWEIGHT";
	// break;
	//
	// case Graphics.VertexElementUsage.Binormal:
	// element.SemanticName = "BINORMAL";
	// break;
	//
	// case Graphics.VertexElementUsage.Tangent:
	// element.SemanticName = "TANGENT";
	// break;
	//
	// case Graphics.VertexElementUsage.PointSize:
	// element.SemanticName = "PSIZE";
	// break;
	//
	// default:
	// throw new NotSupportedException("Unknown vertex element usage!");
	// }
	//
	// element.SemanticIndex = _usageIndex;
	//
	// switch (_format)
	// {
	// case VertexElementFormat.Single:
	// element.Format = SharpDX.DXGI.Format.R32_Float;
	// break;
	//
	// case VertexElementFormat.Vector2:
	// element.Format = SharpDX.DXGI.Format.R32G32_Float;
	// break;
	//
	// case VertexElementFormat.Vector3:
	// element.Format = SharpDX.DXGI.Format.R32G32B32_Float;
	// break;
	//
	// case VertexElementFormat.Vector4:
	// element.Format = SharpDX.DXGI.Format.R32G32B32A32_Float;
	// break;
	//
	// case VertexElementFormat.Color:
	// element.Format = SharpDX.DXGI.Format.R8G8B8A8_UNorm;
	// break;
	//
	// case VertexElementFormat.Byte4:
	// element.Format = SharpDX.DXGI.Format.R8G8B8A8_UInt;
	// break;
	//
	// case VertexElementFormat.Short2:
	// element.Format = SharpDX.DXGI.Format.R16G16_SInt;
	// break;
	//
	// case VertexElementFormat.Short4:
	// element.Format = SharpDX.DXGI.Format.R16G16B16A16_SInt;
	// break;
	//
	// case VertexElementFormat.NormalizedShort2:
	// element.Format = SharpDX.DXGI.Format.R16G16_SNorm;
	// break;
	//
	// case VertexElementFormat.NormalizedShort4:
	// element.Format = SharpDX.DXGI.Format.R16G16B16A16_SNorm;
	// break;
	//
	// case VertexElementFormat.HalfVector2:
	// element.Format = SharpDX.DXGI.Format.R16G16_Float;
	// break;
	//
	// case VertexElementFormat.HalfVector4:
	// element.Format = SharpDX.DXGI.Format.R16G16B16A16_Float;
	// break;
	//
	// default:
	// throw new NotSupportedException("Unknown vertex element format!");
	// }
	//
	// element.Slot = 0;
	// element.AlignedByteOffset = _offset;
	//
	// // Note that instancing is only supported in
	// // feature level 9.3 and above.
	// element.Classification = SharpDX.Direct3D11.InputClassification.PerVertexData;
	// element.InstanceDataStepRate = 0;
	//
	// return element;
	// }
	//
	// #endif

}
