package syntax_analyzer.analyzator;

import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ListRule extends AbstractRule implements RuleInterface {
	
	protected static Enum<? extends Enum<?>> defaultDelimiter = null;
	
	protected List<Enum<? extends Enum<?>>> delimiters;
	
	protected Enum<? extends Enum<?>>[] items;
	
	public ListRule(Enum<? extends Enum<?>> delimiter,
				    Enum<? extends Enum<?>>[] items,
				    Enum<? extends Enum<?>> list) {
		this.delimiters = new ArrayList<>();
		this.delimiters.add(delimiter);
		this.items = items;
		this.ruleId = list;
	}
	
	public ListRule(Enum<? extends Enum<?>>[] delimiters,
		    		Enum<? extends Enum<?>>[] items,
		    		Enum<? extends Enum<?>> list) {
		this.delimiters = Arrays.asList(delimiters);
		this.items = items;
		this.ruleId = list;
	}
	
	public ListRule(Enum<? extends Enum<?>>[] items,
		    		Enum<? extends Enum<?>> list) {
		this.delimiters = new ArrayList<>();
		this.delimiters.add(defaultDelimiter);
		this.items = items;
		this.ruleId = list;
	}
	
	public static void setDefaultDelimiter(Enum<? extends Enum<?>> delimiter) {
		defaultDelimiter = delimiter;
	}
	
	@Override
	public LinkedList<Enum<? extends Enum<?>>> generate(
			TerminalScanner scanner, RuleManager ruleManager
	) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
		LinkedList<Enum<? extends Enum<?>>> langObjects = new LinkedList<>();
		Terminal terminal = scanner.scan();

		for(Enum<? extends Enum<?>> delimiter: delimiters) {
			if(delimiter == terminal.getId()) {
				langObjects.add(delimiter);
				langObjects.addAll(Arrays.asList(items));
				langObjects.add(ruleId);
				
				return langObjects;
			}
		}
		
		return null;
	}
}
