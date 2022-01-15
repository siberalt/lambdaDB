package syntax.compilator.output.command.writer;

import syntax.compilator.output.command.Command;

import java.io.IOException;

public interface CommandWriterInterface {
    void write(Command command) throws IOException;
}
