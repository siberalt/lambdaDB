package syntax_analyzer.scanner.exceptions;

public class NegativePositionException extends GeneralScannerException{
    public NegativePositionException(long pos) {
        super("Negative position: " + pos + " is not allowed.");
    }
}
