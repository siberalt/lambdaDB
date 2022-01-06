package syntax.lang.letter;

public abstract class Letter {

	protected Enum<? extends Enum<?>> type;
	
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
}
