package gameCore.graphics.effect;

public class EffectTechnique {

	private EffectPassCollection passes;
	public EffectPassCollection getPasses() { return passes; }
	
	private EffectAnnotationCollection annotations;
	public EffectAnnotationCollection getAnnotations() { return annotations; }
	
	private String name;
	public String getName() { return name; }
	
	protected EffectTechnique(Effect effect, EffectTechnique cloneSource)
	{
	    // Share all the immutable types.
	    this.name = cloneSource.getName();
	    this.annotations = cloneSource.getAnnotations();
	
	    // Clone the mutable types.
	    this.passes = cloneSource.getPasses().clone(effect);
	}
	
	protected EffectTechnique(Effect effect, String name, EffectPassCollection passes, EffectAnnotationCollection annotations)
	{
	    this.name = name;
	    this.passes = passes;
	    this.annotations = annotations;
	}
	
}
