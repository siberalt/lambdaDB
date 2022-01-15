package syntax.compilator.output.command;

public class Operand<E> {
    E value;

    public Operand(E value){
        setValue(value);
    }

    public void setValue(E value) {
        this.value = value;
    }

    public E getValue() {
        return value;
    }
}
