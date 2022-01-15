package syntax.compilator.output.command.writer;

import syntax.compilator.output.OutputInterface;
import syntax.compilator.output.command.Command;
import syntax.compilator.output.command.Operand;

import java.io.IOException;

public class HumanViewWriter implements CommandWriterInterface {
    protected OutputInterface output;

    protected Boolean outputCommandId = false;

    protected int rowNumber = 0;

    public HumanViewWriter(OutputInterface output) {
        this.output = output;
    }

    @Override
    public void write(Command command) throws IOException {
        output.write(rowNumber + ") ");
        output.write(command.getId().name());

        if (outputCommandId) {
            output.write("(" + command.getId().ordinal() + ")");
        }

        output.write(" ");

        for (Operand<?> operand : command.getOperands()) {
            output.write(operand.getValue() + " ");
        }

        output.write('\n');
        rowNumber++;
    }

    public HumanViewWriter setOutputCommandId(Boolean outputCommandId) {
        this.outputCommandId = outputCommandId;

        return this;
    }
}
