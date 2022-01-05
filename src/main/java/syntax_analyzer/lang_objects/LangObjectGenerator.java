package syntax_analyzer.lang_objects;

public class LangObjectGenerator<E extends Enum<E>> implements LangObjectGeneratorInterface {
	interface SubAlphabetValidator{
		boolean hasItem(Enum<? extends Enum<?>> type);
	}
	
	protected SubAlphabetValidator terminalValidator;
	
	protected SubAlphabetValidator notTerminalValidator;
	
	protected SubAlphabetValidator operatingValidator;
	
	protected E[] alphabet;
	
	protected Class<E> alphabetClass;
	
	public LangObjectGenerator(Class<E> alphabetClass) {
		this.alphabetClass = alphabetClass;
		this.alphabet = alphabetClass.getEnumConstants();
	}
	
	public LangObjectGenerator<E> registerTerminalTypesByPrefix(String terminalPrefix) {
		terminalValidator = createPrefixSubAlphabetValidator(terminalPrefix);
		
		return this;
	}
	
	public LangObjectGenerator<E> registerNotTerminalTypesByPrefix(String notTerminalPrefix) {
		notTerminalValidator = createPrefixSubAlphabetValidator(notTerminalPrefix);
		
		return this;
	}

	public LangObjectGenerator<E> registerOperatingTypesByPrefix(String operatingPrefix) {
		operatingValidator = createPrefixSubAlphabetValidator(operatingPrefix);
		
		return this;
	}
	
	protected SubAlphabetValidator createPrefixSubAlphabetValidator(String prefix) {
		return type -> type.name().startsWith(prefix);
	}
	
	public LangObjectInterface generate(Enum<? extends Enum<?>> description) {
		if(isTerminalType(description)) {
			return new Terminal(description);
			
		}else if(isNotTerminalType(description)) {
			return new NotTerminal(description);
			
		}else if(isOperatingType(description)) {
			return new Operating(description);
			
		}else {
			//TODO throw exception unknown type
			
			return null;
		}
	}
	
	public boolean isTerminalType(Enum<? extends Enum<?>> type) {
		return type.getClass() == alphabetClass && terminalValidator.hasItem(type);
	}
	
	public boolean isNotTerminalType(Enum<? extends Enum<?>> type) {
		return type.getClass() == alphabetClass && notTerminalValidator.hasItem(type);
	}
	
	public boolean isOperatingType(Enum<? extends Enum<?>> type) {
		return type.getClass() == alphabetClass && operatingValidator.hasItem(type);
	}
}
