package syntax_analyzer.analyzator;

import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.util.Arrays;
import java.util.List;

public class CallbackRule extends AbstractRule implements RuleInterface {
	public interface RuleDescription{
		Enum<? extends Enum<?>>[] generate(TerminalScanner scanner);
	}
	
	protected RuleDescription ruleDescription;

	public CallbackRule(RuleDescription ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	@Override
	public List<Enum<? extends Enum<?>>> generate(TerminalScanner scanner, RuleManager ruleManager)
			throws UnexpectedEndOfInputException, UnknownLexemException {		
		return Arrays.asList(ruleDescription.generate(scanner));
	}	
}


