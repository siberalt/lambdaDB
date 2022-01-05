package syntax_analyzer.scanner.exceptions;

public class UnexpectedEndOfInputException extends ScannerException {

    public UnexpectedEndOfInputException(int line, int position, String message) {
        super(line, position, message);
    }

    public UnexpectedEndOfInputException(int line, int position) {
        super(line, position, "Unexpected end of input!");
    }
}
