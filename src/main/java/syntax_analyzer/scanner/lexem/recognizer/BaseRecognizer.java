package syntax_analyzer.scanner.lexem.recognizer;

import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.LexemScannerPointerInterface;
import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;

import java.io.IOException;
import java.util.Map;

public class BaseRecognizer implements LexemeRecognizerInterface {

    Map<Enum<? extends Enum<?>>, String> lexemeMapping;

    public BaseRecognizer(Map<Enum<? extends Enum<?>>, String> lexemeMapping) {
        this.lexemeMapping = lexemeMapping;
    }

    public Terminal recognize(LexemScannerPointerInterface pointer) throws UnexpectedEndOfInputException, GeneralScannerException, IOException {
        for(Map.Entry<Enum<? extends Enum<?>>, String> lexeme: lexemeMapping.entrySet()) {
            char[] view = lexeme.getValue().toCharArray();
            boolean matched = true;

            for (int i = 0; i < view.length; i++) {
                char ch = (char) pointer.get(i);

                if (notEqual(view[i], ch)) {
                    matched = false;
                    break;
                }
            }

            if(matched) {
                Terminal terminal = createMatched(pointer, lexeme);

                if(null != terminal){
                    return terminal;
                }
            }
        }

        return null;
    }

    protected boolean notEqual(char viewCh, char inputCh){
        return viewCh != inputCh;
    }

    protected Terminal createMatched(LexemScannerPointerInterface pointer, Map.Entry<Enum<? extends Enum<?>>, String> lexem) throws GeneralScannerException, UnexpectedEndOfInputException, IOException {
        pointer.get(lexem.getValue().length());
        pointer.resetTo(lexem.getValue().length());

        return new Terminal(lexem.getKey(), lexem.getValue());
    }
}
