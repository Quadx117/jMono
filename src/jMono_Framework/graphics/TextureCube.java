package jMono_Framework.graphics;

import jMono_Framework.Rectangle;
import jMono_Framework.utilities.ReflectionHelpers;

public class TextureCube extends Texture
{
    protected int size;

    /**
     * Returns the width and height of the cube map face in pixels.
     * 
     * @return The width and height of a cube map face in pixels.
     */
    public int getSize()
    {
        return size;
    }

    public TextureCube(GraphicsDevice graphicsDevice, int size, boolean mipMap, SurfaceFormat format)
    {
        this(graphicsDevice, size, mipMap, format, false);
    }

    protected TextureCube(GraphicsDevice graphicsDevice, int size, boolean mipMap, SurfaceFormat format, boolean renderTarget)
    {
        if (graphicsDevice == null)
            throw new NullPointerException("graphicsDevice"); // ,
                                                              // FrameworkResources.ResourceCreationWhenDeviceIsNull);
        if (size <= 0)
            throw new IllegalArgumentException("size: Cube size must be greater than zero");

        this.setGraphicsDevice(graphicsDevice);
        this.size = size;
        this._format = format;
        this._levelCount = mipMap ? calculateMipLevels(size) : 1;

        platformConstruct(graphicsDevice, size, mipMap, format, renderTarget);
    }

    /**
     * Returns a copy of cube texture data specifying a cube map face.
     * 
     * @param <T>
     *        The type of data.
     * @param cubeMapFace
     *        The cube map face.
     * @param data
     *        The data.
     */
    public <T> void getData(CubeMapFace cubeMapFace, T[] data)
    {
        if (data == null)
            throw new NullPointerException("data");
        getData(cubeMapFace, 0, null, data, 0, data.length);
    }

    public <T> void getData(CubeMapFace cubeMapFace, T[] data, int startIndex, int elementCount)
    {
        getData(cubeMapFace, 0, null, data, startIndex, elementCount);
    }

    public <T> void getData(CubeMapFace cubeMapFace, int level, Rectangle rect, T[] data, int startIndex, int elementCount)
    {
        Rectangle checkedRect = new Rectangle();
        validateParams(level, rect, data, startIndex, elementCount, checkedRect);
        platformGetData(cubeMapFace, level, checkedRect, data, startIndex, elementCount);
    }

    public <T> void setData(CubeMapFace face, T[] data)
    {
        if (data == null)
            throw new NullPointerException("data");
        setData(face, 0, null, data, 0, data.length);
    }

    public <T> void setData(CubeMapFace face, T[] data, int startIndex, int elementCount)
    {
        setData(face, 0, null, data, startIndex, elementCount);
    }

    public <T> void setData(CubeMapFace face, int level, Rectangle rect, T[] data, int startIndex, int elementCount)
    {
        Rectangle checkedRect = new Rectangle();
        validateParams(level, rect, data, startIndex, elementCount, checkedRect);
        platformSetData(face, level, checkedRect, data, startIndex, elementCount);
    }

    private <T> void validateParams(int level, Rectangle rect, T[] data, int startIndex,
                                    int elementCount, Rectangle checkedRect)
    {
        Rectangle textureBounds = new Rectangle(0, 0, Math.max(size >> level, 1), Math.max(size >> level, 1));
        checkedRect = (rect != null) ? rect : textureBounds;
        if (level < 0 || level >= getLevelCount())
            throw new IllegalArgumentException("level must be smaller than the number of levels in this texture.");
        if (!textureBounds.contains(checkedRect) || checkedRect.width <= 0 || checkedRect.height <= 0)
            throw new IllegalArgumentException("rect: Rectangle must be inside the texture bounds");
        if (data == null)
            throw new NullPointerException("data");
        int tSize = ReflectionHelpers.sizeOf(data.getClass());
        int fSize = getFormat().getSize();
        if (tSize > fSize || fSize % tSize != 0)
            throw new IllegalArgumentException("Type T is of an invalid size for the format of this texture.");
        if (startIndex < 0 || startIndex >= data.length)
            throw new IllegalArgumentException("startIndex must be at least zero and smaller than data.Length.");
        if (data.length < startIndex + elementCount)
            throw new IllegalArgumentException("The data array is too small.");

        int dataByteSize;
        if (getFormat().isCompressedFormat())
        {
            // round x and y down to next multiple of four; width and height up to next multiple of
            // four
            int roundedWidth = (checkedRect.width + 3) & ~0x3;
            int roundedHeight = (checkedRect.height + 3) & ~0x3;
            checkedRect.x = checkedRect.x & ~0x3;
            checkedRect.y = checkedRect.y & ~0x3;
            // #if OPENGL
            // OpenGL only: The last two mip levels require the width and height to be
            // passed as 2x2 and 1x1, but there needs to be enough data passed to occupy
            // a 4x4 block.
            // checkedRect.Width < 4 && textureBounds.Width < 4 ? textureBounds.Width :
            // roundedWidth,
            // checkedRect.Height < 4 && textureBounds.Height < 4 ? textureBounds.Height :
            // roundedHeight);
            // #else
            checkedRect.width = roundedWidth;
            checkedRect.height = roundedHeight;
            // #endif
            dataByteSize = roundedWidth * roundedHeight * fSize / 16;
        }
        else
        {
            dataByteSize = checkedRect.width * checkedRect.height * fSize;
        }
        if (elementCount * tSize != dataByteSize)
            throw new IllegalArgumentException(String.format("elementCount is not the right size, " +
                                                             "elementCount * sizeof(T) is {0}, but data size is {1}.",
                                                             elementCount * tSize, dataByteSize));
    }

    // @formatter:off
    // ########################################################################
    // #                        Platform specific code                        #
    // ########################################################################
    // @formatter:on

    // NOTE(Eric): Adapted from TextureCube.DirectX.cs

    private boolean _renderTarget;
    private boolean _mipMap;

    private void platformConstruct(GraphicsDevice graphicsDevice, int size, boolean mipMap, SurfaceFormat format, boolean renderTarget)
    {
        _renderTarget = renderTarget;
        _mipMap = mipMap;

        // Create texture
        getTexture();
    }

    // TODO(Eric): More research on this
    // internal override SharpDX.Direct3D11.Resource CreateTexture()
    @Override
    protected int[] createTexture()
    {
        // var description = new Texture2DDescription
        // {
        // Width = size,
        // Height = size,
        // MipLevels = _levelCount,
        // ArraySize = 6, // A texture cube is a 2D texture array with 6 textures.
        // Format = SharpDXHelper.ToFormat(_format),
        // BindFlags = BindFlags.ShaderResource,
        // CpuAccessFlags = CpuAccessFlags.None,
        // SampleDescription = { Count = 1, Quality = 0 },
        // Usage = ResourceUsage.Default,
        // OptionFlags = ResourceOptionFlags.TextureCube
        // };

        if (_renderTarget)
        {
            // description.BindFlags |= BindFlags.RenderTarget;
            // if (_mipMap)
            // description.OptionFlags |= ResourceOptionFlags.GenerateMipMaps;
        }

        // return new SharpDX.Direct3D11.Texture2D(GraphicsDevice._d3dDevice, description);
        return new int[size]; // TODO: Need to validate that
    }

    private <T> void platformGetData(CubeMapFace cubeMapFace, int level, Rectangle rect, T[] data, int startIndex, int elementCount)
    {
        // @formatter:off
        // Create a temp staging resource for copying the data.
        // 
        // TODO: Like in Texture2D, we should probably be pooling these staging resources
        // and not creating a new one each time.
        //
//      var min = _format.IsCompressedFormat() ? 4 : 1;
//      var levelSize = Math.Max(size >> level, min);
//
//      var desc = new Texture2DDescription
//      {
//          Width = levelSize,
//          Height = levelSize,
//          MipLevels = 1,
//          ArraySize = 1,
//          Format = SharpDXHelper.ToFormat(_format),
//          SampleDescription = new SampleDescription(1, 0),
//          BindFlags = BindFlags.None,
//          CpuAccessFlags = CpuAccessFlags.Read,
//          Usage = ResourceUsage.Staging,
//          OptionFlags = ResourceOptionFlags.None,
//      };
//
//      var d3dContext = GraphicsDevice._d3dContext;
//      using (var stagingTex = new SharpDX.Direct3D11.Texture2D(GraphicsDevice._d3dDevice, desc))
//      {
//          lock (d3dContext)
//          {
//              // Copy the data from the GPU to the staging texture.
//              var subresourceIndex = CalculateSubresourceIndex(cubeMapFace, level);
//              var elementsInRow = rect.Width;
//              var rows = rect.Height;
//              var region = new ResourceRegion(rect.Left, rect.Top, 0, rect.Right, rect.Bottom, 1);
//              d3dContext.CopySubresourceRegion(GetTexture(), subresourceIndex, region, stagingTex, 0);
//
//              // Copy the data to the array.
//              DataStream stream = null;
//              try
//              {
//                  var databox = d3dContext.MapSubresource(stagingTex, 0, MapMode.Read, MapFlags.None, out stream);
//
//                  var elementSize = _format.GetSize();
//                  if (_format.IsCompressedFormat())
//                  {
//                      // for 4x4 block compression formats an element is one block, so elementsInRow
//                      // and number of rows are 1/4 of number of pixels in width and height of the rectangle
//                      elementsInRow /= 4;
//                      rows /= 4;
//                  }
//                  var rowSize = elementSize * elementsInRow;
//                  if (rowSize == databox.RowPitch)
//                      stream.ReadRange(data, startIndex, elementCount);
//                  else
//                  {
//                      // Some drivers may add pitch to rows.
//                      // We need to copy each row separatly and skip trailing zeros.
//                      stream.Seek(0, SeekOrigin.Begin);
//
//                      var elementSizeInByte = Utilities.ReflectionHelpers.SizeOf<T>.Get();
//                      for (var row = 0; row < rows; row++)
//                      {
//                          int i;
//                          for (i = row * rowSize / elementSizeInByte; i < (row + 1) * rowSize / elementSizeInByte; i++)
//                              data[i + startIndex] = stream.Read<T>();
//
//                          if (i >= elementCount)
//                              break;
//
//                          stream.Seek(databox.RowPitch - rowSize, SeekOrigin.Current);
//                      }
//                  }
//              }
//              finally
//              {
//                  SharpDX.Utilities.Dispose(ref stream);
//              }
//          }
//      }
        // @formatter:on
    }

    // private <T> void platformSetData(CubeMapFace face, int level, IntPtr dataPtr, int xOffset,
    // int yOffset, int width, int height)
    private <T> void platformSetData(CubeMapFace face, int level, Rectangle rect, T[] data, int startIndex, int elementCount)
    {
       // @formatter:off
//     var elementSizeInByte = Utilities.ReflectionHelpers.SizeOf<T>.Get();
//     var dataHandle = GCHandle.Alloc(data, GCHandleType.Pinned);
//     // Use try..finally to make sure dataHandle is freed in case of an error
//     try
//     {
//         var dataPtr = (IntPtr) (dataHandle.AddrOfPinnedObject().ToInt64() + startIndex*elementSizeInByte);
//         var box = new DataBox(dataPtr, GetPitch(rect.Width), 0);
//   
//         var subresourceIndex = CalculateSubresourceIndex(face, level);
//   
//         var region = new ResourceRegion
//         {
//             Top = rect.Top,
//             Front = 0,
//             Back = 1,
//             Bottom = rect.Bottom,
//             Left = rect.Left,
//             Right = rect.Right
//         };
//   
//         var d3dContext = GraphicsDevice._d3dContext;
//         lock (d3dContext)
//             d3dContext.UpdateSubresource(box, GetTexture(), subresourceIndex, region);
//     }
//     finally
//     {
//         dataHandle.Free();
//     }
       // @formatter:on
    }

    private int calculateSubresourceIndex(CubeMapFace face, int level)
    {
        // TODO(Eric): Need to validate (int) face in C# VS face.ordinal()
        return face.ordinal() * _levelCount + level;
    }

}
