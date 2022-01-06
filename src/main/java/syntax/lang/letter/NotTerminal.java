package syntax.lang.letter;

public class NotTerminal extends Letter implements LetterInterface {

    public NotTerminal(Enum<? extends Enum<?>> type) {
        super(type);
    }

    public LetterType getType() {
        return LetterType.NOTERMINAL;
    }

    public String toString() {
        return String.format("NotTerminal{type=%s}", type.name());
    }
}
