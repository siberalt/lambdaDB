package syntax_analyzer.compilator;

import syntax_analyzer.lang_objects.Operating;
import syntax_analyzer.lang_objects.Terminal;

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
}
