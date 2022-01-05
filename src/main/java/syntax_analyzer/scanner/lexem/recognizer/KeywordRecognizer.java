package syntax_analyzer.scanner.lexem.recognizer;

import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.LexemScannerPointerInterface;
import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.helpers.CharHelperInterface;

import java.io.IOException;
import java.util.Map;

public class KeywordRecognizer extends BaseRecognizer
        implements LexemeRecognizerInterface, CharHelperInterface {

    public KeywordRecognizer(Map<Enum<? extends Enum<?>>, String> lexemsMapping) {
        super(lexemsMapping);
    }

    @Override
    protected boolean notEqual(char viewCh, char inputCh) {
        return toLowerCase(viewCh) != toLowerCase(inputCh);
    }

    @Override
    protected Terminal createMatched(LexemScannerPointerInterface pointer, Map.Entry<Enum<? extends Enum<?>>, String> keyword) throws GeneralScannerException, UnexpectedEndOfInputException, IOException {
        char ch = (char) pointer.get(keyword.getValue().length());

        if (!isBigLetter(ch) && !isSmallLetter(ch) && !isDigit(ch) && !isUnderline(ch)) {
            pointer.resetTo(keyword.getValue().length());

            return new Terminal(keyword.getKey(), keyword.getValue());
        }

        return null;
    }
}
