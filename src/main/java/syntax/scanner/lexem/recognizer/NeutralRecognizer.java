package syntax.scanner.lexem.recognizer;

import syntax.lang.letter.Terminal;
import syntax.scanner.LexemeScannerPointerInterface;
import syntax.scanner.exceptions.NegativePositionException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.helpers.CharHelperInterface;

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
	
	public Terminal recognize(LexemeScannerPointerInterface pointer) throws UnexpectedEndOfInputException, IOException, NegativePositionException {
		boolean isFound = false;
		char ch = (char) pointer.next();
		
		while(inArray(ch, this.neutralChars)) {
			isFound = true;
			ch = (char) pointer.next();
		}
		pointer.previous();

		return isFound ? new Terminal(this.type): null;
	}

	@Override
	public Terminal create(Enum<? extends Enum<?>> type) {
		if(this.type == type){
			return new Terminal(type, "<neutral>");
		}

		return null;
	}

	public void setNeutralChars(String chars) {
		this.neutralChars = chars;
	}
}

