package syntax_analyzer.lang_objects;

public interface LangObjectInterface {
	
	LangObjectType getType();
	
	Enum<? extends Enum<?>> getId();
}
