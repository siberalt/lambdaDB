package syntax_analyzer.scanner.lexem.recognizer;

import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.LexemScannerPointerInterface;
import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;

import java.io.IOException;

public interface LexemeRecognizerInterface {
    Terminal recognize(LexemScannerPointerInterface pointer) throws UnexpectedEndOfInputException, GeneralScannerException, IOException;
}
