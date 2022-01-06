package syntax.scanner.lexem.recognizer;

import syntax.lang.letter.Terminal;
import syntax.scanner.LexemeScannerPointerInterface;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;

import java.io.IOException;
import java.util.Map;

public class BaseRecognizer implements LexemeRecognizerInterface {

    Map<Enum<? extends Enum<?>>, String> lexemeMapping;

    public BaseRecognizer(Map<Enum<? extends Enum<?>>, String> lexemeMapping) {
        this.lexemeMapping = lexemeMapping;
    }

    public Terminal recognize(LexemeScannerPointerInterface pointer) throws UnexpectedEndOfInputException, GeneralScannerException, IOException {
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

    @Override
    public Terminal create(Enum<? extends Enum<?>> type) {
        if(lexemeMapping.containsKey(type)){
            return new Terminal(type, lexemeMapping.get(type));
        }

        return null;
    }

    protected boolean notEqual(char viewCh, char inputCh){
        return viewCh != inputCh;
    }

    protected Terminal createMatched(LexemeScannerPointerInterface pointer, Map.Entry<Enum<? extends Enum<?>>, String> lexem) throws GeneralScannerException, UnexpectedEndOfInputException, IOException {
        pointer.get(lexem.getValue().length());
        pointer.resetTo(lexem.getValue().length());

        return new Terminal(lexem.getKey(), lexem.getValue());
    }
}
