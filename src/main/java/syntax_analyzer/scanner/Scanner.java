package syntax_analyzer.scanner;

import syntax_analyzer.input_adapters.InputInterface;
import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.exceptions.UnknownLexemException;
import syntax_analyzer.scanner.lexem.recognizer.LexemeRecognizerInterface;

import java.io.IOException;
import java.util.*;

public class Scanner {

    protected List<LexemeRecognizerInterface> lexemeRecognizers;

    protected Enum<? extends Enum<?>>[] ignoredLexemeTypes;

    protected ScannerPointer pointer;

    protected ScannerBuffer buffer = new ScannerBuffer();

    public Scanner(InputInterface inputAdapter) {
        pointer = new ScannerPointer(inputAdapter);
        init();
    }

    public Scanner() {
        init();
    }

    protected void init() {
        buffer.clear();
    }

    public void setInput(InputInterface inputAdapter) {
        pointer = new ScannerPointer(inputAdapter);
        init();
    }

    public void setIgnoredLexemeTypes(Enum<? extends Enum<?>>[] types) {
        this.ignoredLexemeTypes = types;
    }

    public Enum<? extends Enum<?>>[] getIgnoredLexemeTypes() {
        return this.ignoredLexemeTypes;
    }

    public void setLexemeRecognizers(List<LexemeRecognizerInterface> lexemeRecognizers) {
        this.lexemeRecognizers = lexemeRecognizers;
    }

    public void setLexemeRecognizers(LexemeRecognizerInterface[] lexemeRecognizers) {
        this.lexemeRecognizers = new ArrayList<>();
        this.lexemeRecognizers.addAll(Arrays.asList(lexemeRecognizers));
    }

    public ArrayList<Terminal> retrieveTerminals() throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        ArrayList<Terminal> lexemes = new ArrayList<>();
        Terminal lexeme;

        while (null != (lexeme = this.scan())) {
            lexemes.add(lexeme);
        }

        return lexemes;
    }

    public void savePosition() {
        buffer.saveMark();
    }

    public void restoreSavedPosition() throws GeneralScannerException {
        buffer.restoreSavedMark();
    }

    public Terminal scan() throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
        if (buffer.hasNext()) {
            return buffer.next();
        }

        pointer.enableBuffering(true);
        pointer.bufferingFromCurrentPos();

        while (!pointer.isEnd() && pointer.next() != InputInterface.END_OF_INPUT) {
            pointer.previous();

            int lineNumber = pointer.getLineNumber();
            int columnNumber = pointer.getColumnNumber();
            boolean found = false;
            long lastPos = pointer.getCurrentPos();
            for (LexemeRecognizerInterface recognizer : this.lexemeRecognizers) {
                Terminal terminal = recognizer.recognize(pointer);

                if (null != terminal) {
                    if (!this.isIgnoredType(terminal.getId())) {
                        buffer.save(terminal);

                        return terminal
                            .setColumn(columnNumber)
                            .setLine(lineNumber);
                    }

                    found = true;
                    break;
                } else {
                    pointer.setCurrentPos(lastPos);
                }
            }

            if (!found) {
                throw new UnknownLexemException(
                    pointer.getBufferContents(),
                    lineNumber,
                    columnNumber
                );
            }

            pointer.bufferingFromCurrentPos();
        }

        return null;
    }

    public boolean isIgnoredType(Enum<? extends Enum<?>> type) {
        for (Enum<? extends Enum<?>> ignoredType : this.ignoredLexemeTypes) {
            if (type.ordinal() == ignoredType.ordinal()) {
                return true;
            }
        }

        return false;
    }
}
