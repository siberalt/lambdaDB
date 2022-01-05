package syntax_analyzer.analyzator;

import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PrefixRuleEx extends AbstractRule implements RuleInterface {
    protected Map<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]> prefixes;

    public PrefixRuleEx(Enum<? extends Enum<?>> ruleId, Map<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]> prefixes) {
        this.prefixes = prefixes;
        this.ruleId = ruleId;
    }

    @Override
    public List<Enum<? extends Enum<?>>> generate(TerminalScanner scanner, RuleManager ruleManager) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        int maxInd = -1;
        Enum<? extends Enum<?>>[] matchedSequence = null;

        LinkedList<Enum<? extends Enum<?>>> terminals = new LinkedList<>();

        for (Map.Entry<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]> ruleEntry : prefixes.entrySet()) {
            int i = 0;
            boolean matched = false;

            for (Enum<? extends Enum<?>> el : ruleEntry.getKey()) {
                if (i > maxInd) {
                    maxInd = i;
                    terminals.add(scanner.scan().getId());
                }

                matched = el.equals(terminals.get(i));

                if (!matched) {
                    break;
                }

                i++;
            }

            if (matched && (i >= maxInd)) {
                matchedSequence = ruleEntry.getValue();
            }
        }

        return matchedSequence != null
            ? Arrays.asList(matchedSequence)
            : null;
    }
}
