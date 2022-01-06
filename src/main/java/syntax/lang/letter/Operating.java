package syntax.lang.letter;

public class Operating extends Letter implements LetterInterface {

	public Operating(Enum<? extends Enum<?>> type) {
		super(type);
	}

	@Override
	public LetterType getType() {
		return LetterType.OPERATING;
	}
}
