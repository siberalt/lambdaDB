package syntax.analyzer;

import syntax.analyzer.exceptions.UndefinedRuleException;
import syntax.analyzer.exceptions.UnexpectedLexemesException;
import syntax.lang.LetterManagerInterface;
import syntax.lang.letter.LetterInterface;
import syntax.lang.letter.Terminal;
import syntax.scanner.Scanner;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.*;

public class RuleManager {

    protected HashMap<Enum<? extends Enum<?>>, RuleInterface> rules;

    protected Scanner scanner;

    protected TerminalScanner terminalScanner;

    protected LetterManagerInterface langManager;

    public RuleManager(Scanner scanner) {
        this.scanner = scanner;
        this.terminalScanner = new TerminalScanner(scanner);
        this.rules = new HashMap<>();
    }

    public TerminalScanner getTerminalScanner() {
        return terminalScanner;
    }

    public RuleManager setLangManager(LetterManagerInterface langObjectGenerator) {
        this.langManager = langObjectGenerator;

        return this;
    }

    public RuleManager(Scanner scanner, RuleInterface[] rules) {
        this(scanner);

        for (RuleInterface rule : rules) {
            this.addRule(rule);
        }
    }

    public RuleManager addRule(RuleInterface rule) {
        rules.put(rule.getRuleId(), rule);

        return this;
    }

    public List<LetterInterface> generateRule(Enum<? extends Enum<?>> ruleId) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        return generateRule(ruleId, true);
    }

    public List<LetterInterface> generateRule(Enum<? extends Enum<?>> ruleId, boolean throwException) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        if (rules.containsKey(ruleId)) {
            RuleInterface rule = rules.get(ruleId);
            scanner.savePosition();
            List<Enum<? extends Enum<?>>> ruleLetters = rule.generate(terminalScanner, this);
            scanner.restoreSavedPosition();
            LinkedList<LetterInterface> langObjects = null;

            if (null != ruleLetters) {
                langObjects = new LinkedList<>();

                for (Enum<? extends Enum<?>> letter : ruleLetters) {
                    langObjects.add(langManager.generate(letter));
                }

                Collections.reverse(langObjects);
            }

            return langObjects;
        } else if (throwException) {
            throw new UndefinedRuleException(ruleId.name());
        }

        return null;
    }

    public void throwUnexpectedLexemes(List<List<Enum<? extends Enum<?>>>> expectedLexemes, List<Terminal> actualTerminals) {
        LinkedList<String> lexemeSequences = new LinkedList<>();

        for (List<Enum<? extends Enum<?>>> list : expectedLexemes) {
            StringBuilder lexemeSequence = new StringBuilder();

            for (Enum<? extends Enum<?>> expected : list) {
                lexemeSequence.append(langManager.generate(expected).getView());
            }

            lexemeSequences.add(lexemeSequence.toString());
        }

        String expectedStr = String.join(", ", lexemeSequences);
        StringBuilder lexemeSequence = new StringBuilder();

        for (Terminal actualTerminal : actualTerminals) {
            if(null != actualTerminal) {
                lexemeSequence.append(actualTerminal.getView());
                lexemeSequence.append(" ");
            }
        }

        Terminal firstTerminal = actualTerminals.iterator().next();
        String message = String.format(
            "Unexpected symbols. Expected symbols are: %s. Got '%s' at line %s in column %s",
            expectedStr,
            lexemeSequence,
            firstTerminal.getLine(),
            firstTerminal.getColumn()
        );

        throw new UnexpectedLexemesException(message, expectedLexemes, actualTerminals);
    }

    public RuleInterface getRule(Enum<? extends Enum<?>> ruleId) {
        return rules.get(ruleId);
    }

    public RuleInterface findRuleByPrefix(Map<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>> rulesMapping) throws GeneralScannerException, UnknownLexemException, UnexpectedEndOfInputException, IOException {
        return findRuleByPrefix(rulesMapping, true);
    }

    public RuleInterface findRuleByPrefix(Map<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>> rulesMapping, boolean throwException) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        int maxInd = -1;
        RuleInterface matchedRule = null;

        LinkedList<Terminal> terminals = new LinkedList<>();

        for (Map.Entry<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>> ruleEntry : rulesMapping.entrySet()) {
            int i = 0;
            boolean matched = false;

            for (Enum<? extends Enum<?>> el : ruleEntry.getKey()) {
                if (i > maxInd) {
                    maxInd = i;
                    terminals.add(terminalScanner.scan());
                }

                matched = el.equals(terminals.get(i).getId());

                if (!matched) {
                    break;
                }

                i++;
            }

            if (matched && (i - 1 == maxInd)) {
                matchedRule = this.getRule(ruleEntry.getValue());
            }
        }

        if(null == matchedRule && throwException){
            LinkedList<List<Enum<? extends Enum<?>>>> expectedLexemes = new LinkedList<>();

            for (Enum<? extends Enum<?>> value : rulesMapping.values()) {
                expectedLexemes.add(new LinkedList<>(List.of(value)));
            }

            throwUnexpectedLexemes(expectedLexemes, terminals);
        }

        return matchedRule;
    }
}
