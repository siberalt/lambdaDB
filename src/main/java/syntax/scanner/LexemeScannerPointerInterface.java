package syntax.scanner;

import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.NegativePositionException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;

import java.io.IOException;

public interface LexemeScannerPointerInterface {
	int END_OF_INPUT = -1;
	
	int next() throws UnexpectedEndOfInputException;

	int get(int pos) throws UnexpectedEndOfInputException, GeneralScannerException, IOException;
	
	void previous() throws UnexpectedEndOfInputException, IOException, NegativePositionException;
	
	void error(String message);

	void resetTo(int i) throws UnexpectedEndOfInputException, IOException, NegativePositionException;
}
