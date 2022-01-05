package syntax_analyzer.analyzator;

import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PrefixRule extends AbstractRule implements RuleInterface {
	protected Map<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>> prefixes;
	
	public PrefixRule(Enum<? extends Enum<?>> ruleId, Map<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>> prefixes) {
		this.prefixes = prefixes;
		this.ruleId = ruleId;
	}

	@Override
	public List<Enum<? extends Enum<?>>> generate(TerminalScanner scanner, RuleManager ruleManager)
			throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
		RuleInterface rule = ruleManager.findRuleByPrefix(this.prefixes);
		
		if(null == rule) {
			return null;
		}
		
		LinkedList<Enum<? extends Enum<?>>> list = new LinkedList<>();
		list.add(rule.getRuleId());
		
		return list;
	}
}
