package gameCore.graphics;

/**
 * Defines how vertex or index buffer data will be flushed during a SetData operation.
 * 
 * @author Eric
 *
 */
public enum SetDataOptions
{
	/**
	 * The SetData can overwrite the portions of existing data.
	 */
	None,

	/**
	 * The SetData will discard the entire buffer. A pointer to a new memory area is returned and
	 * rendering from the previous area do not stall.
	 */
	Discard,

	/**
	 * Defines how vertex or index buffer data will be flushed during a SetData operation.
	 */
	NoOverwrite
}
