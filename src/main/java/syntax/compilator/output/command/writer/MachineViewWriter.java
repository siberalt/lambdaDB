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
        output.write(integerToChar(command.getId().ordinal()));

        for (Operand<?> operand : command.getOperands()) {
            if (operand.getValue() instanceof String) {
                String operandString = (String) operand.getValue();
                output.write(integerToChar(operandString.length()));
                output.write(operandString);
            }

            if (operand.getValue() instanceof Integer) {
                output.write(integerToChar((Integer) operand.getValue()));
            }

            if (operand.getValue() instanceof Boolean) {
                output.write((byte) ((Boolean) operand.getValue() ? 1 : 0));
            }
        }
    }

    public char[] integerToChar(int integer) {
        char[] chars = new char[4];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ((integer >> i * 8) & 0xFF);
        }

        return chars;
    }
}
