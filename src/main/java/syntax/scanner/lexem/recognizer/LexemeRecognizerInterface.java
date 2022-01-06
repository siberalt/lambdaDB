package syntax.scanner.lexem.recognizer;

import syntax.lang.letter.Terminal;
import syntax.scanner.LexemeScannerPointerInterface;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;

import java.io.IOException;

public interface LexemeRecognizerInterface {
    Terminal recognize(LexemeScannerPointerInterface pointer) throws UnexpectedEndOfInputException, GeneralScannerException, IOException;

    Terminal create(Enum<? extends Enum<?>> type);
}
