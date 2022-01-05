package syntax_analyzer.compilator;

import syntax_analyzer.lang_objects.Operating;
import syntax_analyzer.lang_objects.Terminal;

public interface CompilerInterface {
	
	public void init();
	
	public void processTerminal(Terminal terminal);
	
	public void processOperating(Operating operating); 
}
