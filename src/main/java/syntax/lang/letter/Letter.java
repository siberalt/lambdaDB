package syntax.lang.letter;

public abstract class Letter {

	protected Enum<? extends Enum<?>> type;

	protected String view;
	
	public Letter(Enum<? extends Enum<?>> type) {
		this.type = type;
	}
	
	public Terminal setType(Enum<? extends Enum<?>> type) {
		this.type = type;
        return null;
    }
	
	public Enum<? extends Enum<?>> getId(){
		return this.type;
	}

	public Enum<? extends Enum<?>> getType() {
		return type;
	}

	public Letter setView(String view) {
		this.view = view;

		return this;
	}

	public String getView() {
		return view;
	}
}
