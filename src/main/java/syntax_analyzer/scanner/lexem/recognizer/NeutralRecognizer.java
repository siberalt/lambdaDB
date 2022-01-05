package syntax_analyzer.scanner.lexem.recognizer;

import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.LexemScannerPointerInterface;
import syntax_analyzer.scanner.exceptions.NegativePositionException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.helpers.CharHelperInterface;

import java.io.IOException;

public class NeutralRecognizer extends AbstractAtomRecognizer
	implements LexemeRecognizerInterface, CharHelperInterface
{
	protected String neutralChars = " \r\t\n";
	
	public NeutralRecognizer(Enum<? extends Enum<?>> type) {
		super(type);
	}
	
	public NeutralRecognizer(Enum<? extends Enum<?>> type, String neutralChars) {
		super(type);
		this.setNeutralChars(neutralChars);
	}
	
	public Terminal recognize(LexemScannerPointerInterface pointer) throws UnexpectedEndOfInputException, IOException, NegativePositionException {
		boolean isFound = false;
		char ch = (char) pointer.next();
		
		while(inArray(ch, this.neutralChars)) {
			isFound = true;
			ch = (char) pointer.next();
		}
		pointer.previous();

		return isFound ? new Terminal(this.type): null;
	}

	public void setNeutralChars(String chars) {
		this.neutralChars = chars;
	}
}

