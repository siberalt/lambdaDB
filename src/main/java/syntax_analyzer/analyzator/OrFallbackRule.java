package syntax_analyzer.analyzator;

import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class OrFallbackRule extends OrRule {

    protected Enum<? extends Enum<?>>[] fallbackRule;

    @SafeVarargs
    public OrFallbackRule(
            Enum<? extends Enum<?>> ruleId,
            Enum<? extends Enum<?>>[] fallbackRule,
            Enum<? extends Enum<?>>... operands) {

        super(ruleId, operands);
        this.fallbackRule = fallbackRule;
    }

    @SafeVarargs
    public OrFallbackRule(
            Enum<? extends Enum<?>> ruleId,
            Enum<? extends Enum<?>>[] fallbackRule,
            Enum<? extends Enum<?>>[]... operands) {

        super(ruleId, operands);
        this.fallbackRule = fallbackRule;
    }

    public List<Enum<? extends Enum<?>>> generate(TerminalScanner scanner, RuleManager ruleManager) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        List<Enum<? extends Enum<?>>> ruleLetters = super.generate(scanner, ruleManager);

        if (null == ruleLetters && null != this.fallbackRule) {
            return Arrays.asList(this.fallbackRule);
        }

        return ruleLetters;
    }
}
