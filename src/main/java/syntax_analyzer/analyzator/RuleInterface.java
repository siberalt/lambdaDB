package syntax_analyzer.analyzator;

import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.List;

public interface RuleInterface {	
	public Enum<? extends Enum<?>> getRuleId();
	
	public List<Enum<? extends Enum<?>>> generate(TerminalScanner scanner, RuleManager ruleManager) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException;
}
