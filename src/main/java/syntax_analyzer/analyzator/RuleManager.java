package syntax_analyzer.analyzator;

import syntax_analyzer.analyzator.exceptions.UndefinedRuleException;
import syntax_analyzer.lang_objects.LangObjectGeneratorInterface;
import syntax_analyzer.lang_objects.LangObjectInterface;
import syntax_analyzer.scanner.Scanner;
import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;

import java.io.IOException;
import java.util.*;

public class RuleManager {

    protected HashMap<Enum<? extends Enum<?>>, RuleInterface> rules;

    protected Scanner scanner;

    protected TerminalScanner terminalScanner;

    protected LangObjectGeneratorInterface langObjectGenerator;

    public RuleManager(Scanner scanner) {
        this.scanner = scanner;
        this.terminalScanner = new TerminalScanner(scanner);
        this.rules = new HashMap<>();
    }

    public TerminalScanner getTerminalScanner() {
        return terminalScanner;
    }

    public RuleManager setLangObjectGenerator(LangObjectGeneratorInterface langObjectGenerator) {
        this.langObjectGenerator = langObjectGenerator;

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

    public List<LangObjectInterface> generateRule(Enum<? extends Enum<?>> ruleId) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        return generateRule(ruleId, true);
    }

    public List<LangObjectInterface> generateRule(Enum<? extends Enum<?>> ruleId, boolean throwException) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        if (rules.containsKey(ruleId)) {
            RuleInterface rule = rules.get(ruleId);
            scanner.savePosition();
            List<Enum<? extends Enum<?>>> ruleLetters = rule.generate(terminalScanner, this);
            scanner.restoreSavedPosition();
            LinkedList<LangObjectInterface> langObjects = null;

            if(null != ruleLetters) {
                langObjects = new LinkedList<>();

                for (Enum<? extends Enum<?>> letter : ruleLetters) {
                    langObjects.add(langObjectGenerator.generate(letter));
                }

                Collections.reverse(langObjects);
            }

            return langObjects;
        } else if (throwException) {
            throw new UndefinedRuleException(ruleId.name());
        }

        return null;
    }

    public RuleInterface getRule(Enum<? extends Enum<?>> ruleId) {
        return rules.get(ruleId);
    }

    public RuleInterface findRuleByPrefix(Map<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>> rulesMapping) throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        int maxInd = -1;
        RuleInterface matchedRule = null;

        LinkedList<Enum<? extends Enum<?>>> terminals = new LinkedList<>();

        for (Map.Entry<Enum<? extends Enum<?>>[], Enum<? extends Enum<?>>> ruleEntry : rulesMapping.entrySet()) {
            int i = 0;
            boolean matched = false;

            for (Enum<? extends Enum<?>> el : ruleEntry.getKey()) {
                if (i > maxInd) {
                    maxInd = i;
                    terminals.add(terminalScanner.scan().getId());
                }

                matched = el.equals(terminals.get(i));

                if (!matched) {
                    break;
                }

                i++;
            }

            if (matched && (i - 1 == maxInd)) {
                matchedRule = this.getRule(ruleEntry.getValue());
            }
        }

        return matchedRule;
    }
}
