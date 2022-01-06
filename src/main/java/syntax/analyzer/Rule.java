package syntax.analyzer;

import java.util.Arrays;
import java.util.List;

public class Rule implements RuleInterface{
	protected Enum<? extends Enum<?>> descriptor;
		
	protected Enum<? extends Enum<?>>[] ruleDescription;
	
	public Rule(Enum<? extends Enum<?>> descriptor, Enum<? extends Enum<?>>[] ruleDescription) {
		this.ruleDescription = ruleDescription;
		this.descriptor = descriptor;
	}
	
	@Override
	public List<Enum<? extends Enum<?>>> generate(TerminalScanner scanner, RuleManager ruleManager){
		return Arrays.asList(ruleDescription);
	}

	@Override
	public Enum<? extends Enum<?>> getRuleId() {
		return descriptor;
	}
}

