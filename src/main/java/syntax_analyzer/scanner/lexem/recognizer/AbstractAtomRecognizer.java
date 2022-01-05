package syntax_analyzer.scanner.lexem.recognizer;

public class AbstractAtomRecognizer {
	//protected Type type;
	protected Enum<? extends Enum<?>> type;
	
	public AbstractAtomRecognizer(Enum<? extends Enum<?>> type) {
		this.setType(type);
	}

	public void setType(Enum<? extends Enum<?>> type) {
		this.type = type;
	}
	
	public Enum<? extends Enum<?>> getType() {
		return this.type;
	}
}
