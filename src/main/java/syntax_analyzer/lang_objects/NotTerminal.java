package syntax_analyzer.lang_objects;

public class NotTerminal extends LangObject implements LangObjectInterface {

    public NotTerminal(Enum<? extends Enum<?>> type) {
        super(type);
    }

    public LangObjectType getType() {
        return LangObjectType.NOTERMINAL;
    }

    public String toString() {
        return String.format("NotTerminal{type=%s}", type.name());
    }
}
