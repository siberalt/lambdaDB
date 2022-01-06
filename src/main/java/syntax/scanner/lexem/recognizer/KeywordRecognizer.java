package syntax.scanner.lexem.recognizer;

import syntax.lang.letter.Terminal;
import syntax.scanner.LexemeScannerPointerInterface;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.helpers.CharHelperInterface;

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
    protected Terminal createMatched(LexemeScannerPointerInterface pointer, Map.Entry<Enum<? extends Enum<?>>, String> keyword) throws GeneralScannerException, UnexpectedEndOfInputException, IOException {
        char ch = (char) pointer.get(keyword.getValue().length());

        if (!isBigLetter(ch) && !isSmallLetter(ch) && !isDigit(ch) && !isUnderline(ch)) {
            pointer.resetTo(keyword.getValue().length());

            return new Terminal(keyword.getKey(), keyword.getValue());
        }

        return null;
    }
}
