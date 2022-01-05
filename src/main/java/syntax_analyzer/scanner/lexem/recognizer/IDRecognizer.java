package syntax_analyzer.scanner.lexem.recognizer;

import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.LexemScannerPointerInterface;
import syntax_analyzer.scanner.exceptions.NegativePositionException;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;
import syntax_analyzer.scanner.helpers.CharHelperInterface;

import java.io.IOException;

public class IDRecognizer extends AbstractAtomRecognizer
	implements LexemeRecognizerInterface, CharHelperInterface
{
	public IDRecognizer(Enum<? extends Enum<?>> type) {
		super(type);
	}

	public Terminal recognize(LexemScannerPointerInterface pointer) throws UnexpectedEndOfInputException, IOException, NegativePositionException {
		char ch = (char) pointer.next();
		
		if(!isSmallLetter(ch) && !isBigLetter(ch)) {
			return null;
		}
		
		StringBuffer buffer = new StringBuffer("");

		do {
			buffer.append(ch);
			ch = (char) pointer.next();
		}
		while(isLetter(ch) || isDigit(ch) || isUnderline(ch));

		pointer.previous();
		
		return new Terminal(this.type, buffer.toString());
	}
}
