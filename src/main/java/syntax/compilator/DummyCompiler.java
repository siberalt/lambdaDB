package syntax.compilator;

import syntax.compilator.output.command.writer.CommandWriterInterface;
import syntax.lang.letter.Operating;
import syntax.lang.letter.Terminal;

public class DummyCompiler implements CompilerInterface{
    @Override
    public void init() {
        System.out.println("Init compiler");
    }

    @Override
    public void processTerminal(Terminal terminal) {
        System.out.println("Compiler process terminal " + terminal.toString());
    }

    @Override
    public void processOperating(Operating operating) {
        System.out.println("Compiler process terminal " + operating.toString());
    }

    @Override
    public void compile(CommandWriterInterface writer) {

    }
}
