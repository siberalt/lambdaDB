package syntax.lang;

import syntax.lang.exceptions.UnknownLetterException;
import syntax.lang.letter.LetterInterface;
import syntax.lang.letter.NotTerminal;
import syntax.lang.letter.Operating;
import syntax.lang.letter.Terminal;
import syntax.scanner.lexem.recognizer.LexemeRecognizerInterface;

import java.util.List;

public class LanguageManager<E extends Enum<E>> implements LangManagerInterface {
    interface SubAlphabetValidator {
        boolean hasItem(Enum<? extends Enum<?>> type);
    }

    protected List<LexemeRecognizerInterface> lexemeRecognizers;

    protected SubAlphabetValidator terminalValidator;

    protected SubAlphabetValidator notTerminalValidator;

    protected SubAlphabetValidator operatingValidator;

    protected E[] alphabet;

    protected Class<E> alphabetClass;

    public LanguageManager(Class<E> alphabetClass) {
        this.alphabetClass = alphabetClass;
        this.alphabet = alphabetClass.getEnumConstants();
    }

    public LanguageManager<E> registerTerminalTypesByPrefix(String terminalPrefix) {
        terminalValidator = createPrefixSubAlphabetValidator(terminalPrefix);

        return this;
    }

    public LanguageManager<E> registerNotTerminalTypesByPrefix(String notTerminalPrefix) {
        notTerminalValidator = createPrefixSubAlphabetValidator(notTerminalPrefix);

        return this;
    }

    public LanguageManager<E> registerOperatingTypesByPrefix(String operatingPrefix) {
        operatingValidator = createPrefixSubAlphabetValidator(operatingPrefix);

        return this;
    }

    protected SubAlphabetValidator createPrefixSubAlphabetValidator(String prefix) {
        return type -> type.name().startsWith(prefix);
    }

    public LetterInterface generate(Enum<? extends Enum<?>> description) {
        if (isTerminalType(description)) {
            return generateTerminal(description);

        } else if (isNotTerminalType(description)) {
            return new NotTerminal(description);

        } else if (isOperatingType(description)) {
            return new Operating(description);

        } else {
            throw new UnknownLetterException(description);
        }
    }

    public Terminal generateTerminal(Enum<? extends Enum<?>> description){
        Terminal terminal = null;

        for(LexemeRecognizerInterface recognizer: lexemeRecognizers){
            terminal = recognizer.create(description);

            if(null != terminal){
                break;
            }
        }

        return terminal;
    }

    public boolean isTerminalType(Enum<? extends Enum<?>> type) {
        return type.getClass() == alphabetClass && terminalValidator.hasItem(type);
    }

    public boolean isNotTerminalType(Enum<? extends Enum<?>> type) {
        return type.getClass() == alphabetClass && notTerminalValidator.hasItem(type);
    }

    public boolean isOperatingType(Enum<? extends Enum<?>> type) {
        return type.getClass() == alphabetClass && operatingValidator.hasItem(type);
    }

    public LanguageManager<E> setLexemeRecognizers(List<LexemeRecognizerInterface> lexemeRecognizers) {
        this.lexemeRecognizers = lexemeRecognizers;

        return this;
    }
}
