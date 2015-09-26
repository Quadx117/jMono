package gameCore.graphics.effect;

// NOTE: This TODO is in the orignial code
//TODO: This class needs to be finished!
public class EffectAnnotation {

	protected EffectAnnotation (EffectParameterClass class_,
								EffectParameterType type,
								String name,
								int rowCount,
								int columnCount,
								String semantic,
								Object data)
		{
			this.parameterClass = class_;
			this.parameterType = type;
			this.name = name;
			this.rowCount = rowCount;
			this.columnCount = columnCount;
			this.semantic = semantic;
		}

	protected EffectAnnotation (EffectParameter parameter)
	{
		this.parameterClass = parameter.getParameterClass();
		this.parameterType = parameter.getParameterType();
		this.name = parameter.getName();
		this.rowCount = parameter.getRowCount();
		this.columnCount = parameter.getColumnCount();
		this.semantic = parameter.getSemantic();
	}

	private EffectParameterClass parameterClass;
	public EffectParameterClass getParameterClass() { return parameterClass;}

	private EffectParameterType parameterType;
	public EffectParameterType getParameterType() { return parameterType;}
	
	private String name;
	public String getName() { return name;}
	
	private int rowCount;
	public int getRowCount() { return rowCount;}
	
	private int columnCount;
	public int getColumnCount() { return columnCount;}
	
	private String semantic;
	public String getSemantic() { return semantic;}

}
