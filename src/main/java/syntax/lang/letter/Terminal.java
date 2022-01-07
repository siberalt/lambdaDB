package syntax.lang.letter;

public class Terminal extends Letter implements LetterInterface {
    protected String view;

    protected int line;

    protected int column;

    public Terminal(Enum<? extends Enum<?>> type) {
        super(type);
    }

    public Terminal(Enum<? extends Enum<?>> type, String view) {
        super(type);
        this.view = view;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public Terminal setType(Enum<? extends Enum<?>> type) {
        super.setType(type);

        return this;
    }

    public Terminal setColumn(int column) {
        this.column = column;

        return this;
    }

    public Terminal setLine(int line) {
        this.line = line;

        return this;
    }

    public String getView() {
        return view;
    }

    public LetterType getType() {
        return LetterType.TERMINAL;
    }

    @Override
    public String toString() {
        return String.format("Terminal{view='%s', type=%s}", view, type.name());
    }
}
