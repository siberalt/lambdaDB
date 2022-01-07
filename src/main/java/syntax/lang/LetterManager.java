package syntax.lang;

import syntax.lang.exceptions.UnknownLetterException;
import syntax.lang.letter.LetterInterface;
import syntax.lang.letter.NotTerminal;
import syntax.lang.letter.Operating;
import syntax.lang.letter.Terminal;
import syntax.scanner.lexem.recognizer.LexemeRecognizerInterface;

import java.util.List;

public class LetterManager<E extends Enum<E>> implements LetterManagerInterface {
    interface TypeRecognizerInterface {
        boolean hasItem(Enum<? extends Enum<?>> type);
    }

    public interface NotTerminalGeneratorInterface{
        NotTerminal generate(Enum<? extends Enum<?>> type);
    }

    protected NotTerminalGeneratorInterface notTerminalGenerator;

    protected List<LexemeRecognizerInterface> lexemeRecognizers;

    protected TypeRecognizerInterface terminalRecognizer;

    protected TypeRecognizerInterface notTerminalRecognizer;

    protected TypeRecognizerInterface operatingRecognizer;

    protected E[] alphabet;

    protected Class<E> alphabetClass;

    public LetterManager(Class<E> alphabetClass) {
        this.alphabetClass = alphabetClass;
        this.alphabet = alphabetClass.getEnumConstants();
    }

    public LetterManager<E> registerTerminalTypesByPrefix(String terminalPrefix) {
        terminalRecognizer = createPrefixSubAlphabetValidator(terminalPrefix);

        return this;
    }

    public LetterManager<E> registerNotTerminalTypesByPrefix(String notTerminalPrefix) {
        notTerminalRecognizer = createPrefixSubAlphabetValidator(notTerminalPrefix);

        return this;
    }

    public LetterManager<E> registerOperatingTypesByPrefix(String operatingPrefix) {
        operatingRecognizer = createPrefixSubAlphabetValidator(operatingPrefix);

        return this;
    }

    protected TypeRecognizerInterface createPrefixSubAlphabetValidator(String prefix) {
        return type -> type.name().startsWith(prefix);
    }

    public LetterInterface generate(Enum<? extends Enum<?>> description) {
        if (isTerminalType(description)) {
            return generateTerminal(description);

        } else if (isNotTerminalType(description)) {
            return generateNotTerminal(description);

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

    public LetterManager<E> setNotTerminalGenerator(NotTerminalGeneratorInterface notTerminalGenerator) {
        this.notTerminalGenerator = notTerminalGenerator;

        return this;
    }

    public NotTerminal generateNotTerminal(Enum<? extends Enum<?>> description){
        return notTerminalGenerator.generate(description);
    }

    public boolean isTerminalType(Enum<? extends Enum<?>> type) {
        return type.getClass() == alphabetClass && terminalRecognizer.hasItem(type);
    }

    public boolean isNotTerminalType(Enum<? extends Enum<?>> type) {
        return type.getClass() == alphabetClass && notTerminalRecognizer.hasItem(type);
    }

    public boolean isOperatingType(Enum<? extends Enum<?>> type) {
        return type.getClass() == alphabetClass && operatingRecognizer.hasItem(type);
    }

    public LetterManager<E> setLexemeRecognizers(List<LexemeRecognizerInterface> lexemeRecognizers) {
        this.lexemeRecognizers = lexemeRecognizers;

        return this;
    }
}
