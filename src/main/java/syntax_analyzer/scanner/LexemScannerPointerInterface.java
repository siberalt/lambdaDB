package syntax_analyzer.scanner;

import syntax_analyzer.scanner.exceptions.GeneralScannerException;
import syntax_analyzer.scanner.exceptions.NegativePositionException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;

import java.io.IOException;

public interface LexemScannerPointerInterface {
	public final int END_OF_INPUT = -1;
	
	public int next() throws UnexpectedEndOfInputException;

	public int get(int pos) throws UnexpectedEndOfInputException, GeneralScannerException, IOException;
	
	public void previous() throws UnexpectedEndOfInputException, IOException, NegativePositionException;
	
	public void error(String message);

	void resetTo(int i) throws UnexpectedEndOfInputException, IOException, NegativePositionException;
}
