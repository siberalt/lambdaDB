package syntax.lang.letter;

public interface LetterInterface {
	
	LetterType getType();
	
	Enum<? extends Enum<?>> getId();
}
