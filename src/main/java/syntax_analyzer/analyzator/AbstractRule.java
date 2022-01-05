package syntax_analyzer.analyzator;

import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.List;

public abstract class AbstractRule implements RuleInterface{	
	protected Enum<? extends Enum<?>> ruleId;

	@Override
	public Enum<? extends Enum<?>> getRuleId() {
		return ruleId;
	}
	
	public void setRuleId(Enum<? extends Enum<?>> ruleId) {
		this.ruleId = ruleId;
	}

	@Override
	public abstract List<Enum<? extends Enum<?>>> generate(
			TerminalScanner scanner, RuleManager ruleManager
	) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException;
}
