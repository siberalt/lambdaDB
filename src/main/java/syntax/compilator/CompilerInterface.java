package syntax.compilator;

import syntax.compilator.output.command.writer.CommandWriterInterface;
import syntax.lang.letter.Operating;
import syntax.lang.letter.Terminal;

import java.io.IOException;

public interface CompilerInterface {
	
	void init();
	
	void processTerminal(Terminal terminal);
	
	void processOperating(Operating operating);

	void compile(CommandWriterInterface writer) throws IOException;
}
