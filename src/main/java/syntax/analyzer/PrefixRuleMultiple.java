package syntax.analyzer;

import syntax.lang.letter.Terminal;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PrefixRuleMultiple extends AbstractRule implements RuleInterface {
    protected Map<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]> prefixes;

    protected boolean throwException = true;

    public PrefixRuleMultiple(Enum<? extends Enum<?>> ruleId, Map<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]> prefixes) {
        this.prefixes = prefixes;
        this.ruleId = ruleId;
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

    @Override
    public List<Enum<? extends Enum<?>>> generate(TerminalScanner scanner, RuleManager ruleManager) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        int maxInd = -1, maxMatch = -1;
        Enum<? extends Enum<?>>[] matchedSequence = null;
        LinkedList<Terminal> terminals = new LinkedList<>();

        for (Map.Entry<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>[]> ruleEntry : prefixes.entrySet()) {
            int i = 0;
            boolean matched = false;

            for (Enum<? extends Enum<?>> el : ruleEntry.getKey()) {
                if (i > maxInd) {
                    maxInd = i;
                    terminals.add(scanner.scan());
                }

                matched = el.equals(terminals.get(i).getId());

                if (!matched) {
                    break;
                }

                i++;
            }

            if (matched && (i >= maxMatch)) {
                maxMatch = i;
                matchedSequence = ruleEntry.getValue();
            }
        }

        if (null == matchedSequence && throwException) {
            LinkedList<List<Enum<? extends Enum<?>>>> expectedLexemes = new LinkedList<>();

            for (Enum<? extends Enum<?>>[] value : prefixes.values()) {
                expectedLexemes.add(new LinkedList<>(Arrays.asList(value)));
            }

            ruleManager.throwUnexpectedLexemes(expectedLexemes, terminals);
        }

        return null == matchedSequence
            ? null
            : Arrays.asList(matchedSequence);
    }
}
