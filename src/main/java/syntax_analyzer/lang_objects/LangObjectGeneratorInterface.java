package syntax_analyzer.lang_objects;

public interface LangObjectGeneratorInterface {

	LangObjectInterface generate(Enum<? extends Enum<?>> description);
	
	boolean isTerminalType(Enum<? extends Enum<?>> type);
	
	boolean isNotTerminalType(Enum<? extends Enum<?>> type);
	
	boolean isOperatingType(Enum<? extends Enum<?>> type);
}
