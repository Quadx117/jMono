package jMono_Framework.graphics;

import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicsAdapter implements AutoCloseable
{
    /**
     * Defines the driver type for graphics adapter. Usable only on DirectX platforms for now.
     * 
     * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
     *
     */
    public enum DriverType
    {
        /**
         * Hardware device been used for rendering. Maximum speed and performance.
         */
        Hardware,

        /**
         * Emulates the hardware device on CPU. Slowly, only for testing.
         */
        Reference,

        /**
         * Useful when {@link DriverType.Hardware} acceleration does not work.
         */
        FastSoftware
    }

    // private static ReadOnlyCollection<GraphicsAdapter> _adapters;
    private static List<GraphicsAdapter> _adapters;

    private DisplayModeCollection _supportedDisplayModes;

    private DisplayMode _currentDisplayMode;

    static
    // GraphicsAdapter()
    {
        // NOTE(Eric): Added this to initialize the Map
        Map<Integer, SurfaceFormat> map = new HashMap<>();
        // map.put(BufferedImage.TYPE_3BYTE_BGR, SurfaceFormat.Bgr32SRgb);
        map.put(BufferedImage.TYPE_4BYTE_ABGR, SurfaceFormat.Color);
        // map.put(BufferedImage.TYPE_USHORT_565_RGB, SurfaceFormat.Bgr565);
        formatTranslations = Collections.unmodifiableMap(map);

        // NOTE: An adapter is a monitor+device combination, so we expect
        // at lease one adapter per connected monitor.
        platformInitializeAdapters(_adapters);

        // The first adapter is considered the default.
        _adapters.get(0)._isDefaultAdapter = true;
    }

    public static GraphicsAdapter getDefaultAdapter()
    {
        return _adapters.get(0);
    }

    public static List<GraphicsAdapter> getAdapters()
    {
        return _adapters;
    }

    /**
     * Used to request creation of the reference graphics device, or the default hardware
     * accelerated device (when set to false).
     * <p>
     * This only works on DirectX platforms where a reference graphics device is available and must
     * be defined before the graphics device is created. It defaults to false.
     * 
     * @return {@code true} if {@link #UseDriverType} is set to {@link DriverType#Reference};
     *         {@code false} otherwise.
     */
    public static boolean useReferenceDevice()
    {
        return UseDriverType == DriverType.Reference;
    }

    public static void setUseReferenceDevice(boolean value)
    {
        UseDriverType = value ? DriverType.Reference : DriverType.Hardware;
    }

    /**
     * Used to request creation of a specific kind of driver.
     * <p>
     * These values only work on DirectX platforms and must be defined before the graphics device is
     * created. {@link DriverType#Hardware} by default.
     */
    public static DriverType UseDriverType;

    private String _description;

    public String getDescription()
    {
        return _description;
    }

    private int _deviceId;

    public int getDeviceId()
    {
        return _deviceId;
    }

    private String _deviceName;

    public String getDeviceName()
    {
        return _deviceName;
    }

    private int _vendorId;

    public int getVendorId()
    {
        return _vendorId;
    }

    private boolean _isDefaultAdapter;

    public boolean isDefaultAdapter()
    {
        return _isDefaultAdapter;
    }

    // TODO(Eric): IntPtr
    // public IntPtr MonitorHandle { get; private set; }

    private int _revision;

    public int getRevision()
    {
        return _revision;
    }

    private int _subSystemId;

    public int getSubSystemId()
    {
        return _subSystemId;
    }

    public DisplayModeCollection getSupportedDisplayModes()
    {
        return _supportedDisplayModes;
    }

    public DisplayMode getCurrentDisplayMode()
    {
        return _currentDisplayMode;
    }

    /**
     * Returns whether or not the {@link GraphicsAdapter#CurrentDisplayMode} is widescreen.
     * <p>
     * Common widescreen modes include 16:9, 16:10 and 2:1.
     * 
     * @return {@code true} if the {@link GraphicsAdapter#CurrentDisplayMode} is widescreen; false
     *         otherwise.
     */
    public boolean isWideScreen()
    {
        // Seems like XNA treats aspect ratios above 16:10 as wide screen.
        final float minWideScreenAspect = 16.0f / 10.0f;
        return getCurrentDisplayMode().getAspectRatio() >= minWideScreenAspect;
    }

    /*
     * public bool QueryRenderTargetFormat(
     * GraphicsProfile graphicsProfile,
     * SurfaceFormat format,
     * DepthFormat depthFormat,
     * int multiSampleCount,
     * out SurfaceFormat selectedFormat,
     * out DepthFormat selectedDepthFormat,
     * out int selectedMultiSampleCount)
     * {
     * throw new NotImplementedException();
     * }
     */

    public boolean isProfileSupported(GraphicsProfile graphicsProfile)
    {
        return platformIsProfileSupported(graphicsProfile);
    }

    @Override
    public void close()
    {
        // We don't keep any resources, so we have
        // nothing to do... just here for XNA compatibility.
    }

    //
    // NOTE(Eric): Defined in other files in MonoGame (ex: GraphicsAdapter.DirectX.cs)
    //

    // TODO(Eric): Check if this is equivalent to the DirectX initialization (step in MonoGame 3.6
    // code)
    // See java.awt.GraphicsEnvironment
    private static void platformInitializeAdapters(List<GraphicsAdapter> adapters)
    {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try
        {
            java.awt.GraphicsDevice[] screenDevices = graphicsEnvironment.getScreenDevices();
            int adapterCount = screenDevices.length;
            _adapters = new ArrayList<GraphicsAdapter>(adapterCount);

            for (int i = 0; i < adapterCount; ++i)
            {
                java.awt.GraphicsDevice device = screenDevices[i];
                GraphicsAdapter adapter = createAdapter(device);
                _adapters.add(adapter);
            }
        }
        catch (HeadlessException e)
        {
            e.printStackTrace();
        }
    }

    private static GraphicsAdapter createAdapter(java.awt.GraphicsDevice device)
    {
        GraphicsAdapter adapter = new GraphicsAdapter();
        int desktopWidth = device.getDefaultConfiguration().getDevice().getDisplayMode().getWidth();
        int desktopHeight = device.getDefaultConfiguration().getDevice().getDisplayMode().getHeight();

        List<DisplayMode> modes = new ArrayList<DisplayMode>();

        for (SurfaceFormat formatTranslation : formatTranslations.values())
        {
            java.awt.DisplayMode[] displayModes = device.getDisplayModes();
            for (java.awt.DisplayMode displayMode : displayModes)
            {
                DisplayMode mode = new DisplayMode(displayMode.getWidth(), displayMode.getHeight(), formatTranslation);
                if (modes.contains(mode))
                    continue;

                modes.add(mode);

                if (adapter._currentDisplayMode == null)
                {
                    if (mode.getWidth() == desktopWidth && mode.getHeight() == desktopHeight && mode.getFormat().equals(SurfaceFormat.Color))
                        adapter._currentDisplayMode = mode;
                }
            }
        }

        adapter._supportedDisplayModes = new DisplayModeCollection(modes);

        // (desktop mode wasn't found in the available modes)
        if (adapter._currentDisplayMode == null)
            adapter._currentDisplayMode = new DisplayMode(desktopWidth, desktopHeight, SurfaceFormat.Color);

        return adapter;
    }

    // TODO(Eric): See if I need to add more SurfaceFormat.
    private static final Map<Integer, SurfaceFormat> formatTranslations;

    private boolean platformIsProfileSupported(GraphicsProfile graphicsProfile)
    {
        // NOTE(Eric): This is good as long as we only have the software renderer.
        // Adjust this once we add lwjgl (or something else).
        switch (graphicsProfile)
        {
            case Reach:
                return true;
            case HiDef:
                return false;
            default:
                throw new IllegalArgumentException("Invalid GraphicsProfile");
        }
    }
}
