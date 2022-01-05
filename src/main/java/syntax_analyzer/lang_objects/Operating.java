package syntax_analyzer.lang_objects;

public class Operating extends LangObject implements LangObjectInterface {

	public Operating(Enum<? extends Enum<?>> type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public LangObjectType getType() {
		return LangObjectType.OPERATING;
	}
}
