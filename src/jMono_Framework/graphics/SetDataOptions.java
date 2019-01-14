package jMono_Framework.graphics;

/**
 * Defines how vertex or index buffer data will be flushed during a setData operation.
 * 
 * @author Eric Perron (based on the XNA Framework from Microsoft and MonoGame)
 *
 */
public enum SetDataOptions
{
    /**
     * The setData can overwrite the portions of existing data.
     */
    None,

    /**
     * The SSetData will discard the entire buffer. A pointer to a new memory area is returned and
     * rendering from the previous area do not stall.
     */
    Discard,

    /**
     * The setData operation will not overwrite existing data. This allows the driver to return
     * immediately from a SetData operation and continue rendering.
     */
    NoOverwrite
}
