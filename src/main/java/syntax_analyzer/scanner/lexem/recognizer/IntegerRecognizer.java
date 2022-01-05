package syntax_analyzer.scanner.lexem.recognizer;

import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.LexemScannerPointerInterface;
import syntax_analyzer.scanner.exceptions.NegativePositionException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.helpers.CharHelperInterface;

import java.io.IOException;

public class IntegerRecognizer extends AbstractAtomRecognizer
	implements LexemeRecognizerInterface, CharHelperInterface
{

	public IntegerRecognizer(Enum<? extends Enum<?>> type) {
		super(type);
	}

	public Terminal recognize(LexemScannerPointerInterface pointer) throws UnexpectedEndOfInputException, IOException, NegativePositionException {
		char ch = (char)pointer.next();
		char firstCh = ch;
		
		if(!isSign(ch) && !isDigit(ch)) {
			return null;
		}
		
		StringBuilder buffer = new StringBuilder();
		do {
			buffer.append(ch);
			ch = (char) pointer.next();
		}
		while(isDigit(ch));
		
		pointer.previous();

		if(1 == buffer.length() && isSign(firstCh)){
			return null;
		}

		return new Terminal(this.type, buffer.toString());
	}
}
