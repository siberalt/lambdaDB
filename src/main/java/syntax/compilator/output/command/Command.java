package syntax.compilator.output.command;

import java.util.Collection;
import java.util.LinkedList;

public class Command {

    protected Collection<Operand<?>> operands = new LinkedList<>();

    protected Enum<? extends Enum<?>> id;

    public Command(Enum<? extends Enum<?>> id){
        setId(id);
    }

    public Command(){

    }

    public Command setId(Enum<? extends Enum<?>> id) {
        this.id = id;

        return this;
    }

    public Command append(Operand<?> operand){
        operands.add(operand);

        return this;
    }

    public Command append(String operand){
        operands.add(new Operand<>(operand));

        return this;
    }

    public Command append(Boolean operand){
        operands.add(new Operand<>(operand));

        return this;
    }

    public Command append(Integer operand){
        operands.add(new Operand<>(operand));

        return this;
    }

    public Command setOperands(Collection<Operand<?>> operands) {
        this.operands = operands;

        return this;
    }

    public Collection<Operand<?>> getOperands(){
        return this.operands;
    }

    public Enum<? extends Enum<?>> getId(){
        return this.id;
    }
}
