package syntax.compilator;

import syntax.lang.letter.Operating;
import syntax.lang.letter.Terminal;

public interface CompilerInterface {
	
	void init();
	
	void processTerminal(Terminal terminal);
	
	void processOperating(Operating operating);
}
