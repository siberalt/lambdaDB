package syntax.compilator.output.command.writer;

import syntax.compilator.output.OutputInterface;
import syntax.compilator.output.command.Command;
import syntax.compilator.output.command.Operand;

import java.io.IOException;

public class MachineViewWriter implements CommandWriterInterface {

    protected OutputInterface output;

    public MachineViewWriter(OutputInterface output) {
        this.output = output;
    }

    @Override
    public void write(Command command) throws IOException {
        output.write(command.getId().ordinal());

        for (Operand<?> operand : command.getOperands()) {
            if (operand.getValue() instanceof String) {
                String operandString = (String) operand.getValue();
                output.write(operandString.length());
                output.write(operandString);
            }

            if (operand.getValue() instanceof Integer) {
                output.write((Integer) operand.getValue());
            }

            if (operand.getValue() instanceof Boolean) {
                output.write((byte) ((Boolean) operand.getValue() ? 1 : 0));
            }
        }
    }
}
