package syntax.analyzer;

import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FallbackPrefixRuleMultiple extends PrefixRuleMultiple {
    protected Enum<? extends Enum<?>>[] fallbackRule;

    public FallbackPrefixRuleMultiple(
        Enum<? extends Enum<?>> ruleId,
        Enum<? extends Enum<?>>[] fallbackRule,
        Map<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]> prefixes) {
        super(ruleId, prefixes);
        setThrowException(false);
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
