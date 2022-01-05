package syntax_analyzer.lang_objects;

public abstract class LangObject {

	protected Enum<? extends Enum<?>> type;
	
	public LangObject(Enum<? extends Enum<?>> type) {
		this.type = type;
	}
	
	public Terminal setType(Enum<? extends Enum<?>> type) {
		this.type = type;
        return null;
    }
	
	public Enum<? extends Enum<?>> getId(){
		return this.type;
	}
}
