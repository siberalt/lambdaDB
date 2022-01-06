package syntax.scanner.lexem.recognizer;

import syntax.lang.letter.Terminal;
import syntax.scanner.LexemeScannerPointerInterface;
import syntax.scanner.exceptions.NegativePositionException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.helpers.CharHelperInterface;

import java.io.IOException;

public class IDRecognizer extends AbstractAtomRecognizer
	implements LexemeRecognizerInterface, CharHelperInterface
{
	public IDRecognizer(Enum<? extends Enum<?>> type) {
		super(type);
	}

	public Terminal recognize(LexemeScannerPointerInterface pointer) throws UnexpectedEndOfInputException, IOException, NegativePositionException {
		char ch = (char) pointer.next();
		
		if(!isSmallLetter(ch) && !isBigLetter(ch)) {
			return null;
		}
		
		StringBuilder buffer = new StringBuilder();

		do {
			buffer.append(ch);
			ch = (char) pointer.next();
		}
		while(isLetter(ch) || isDigit(ch) || isUnderline(ch));

		pointer.previous();
		
		return new Terminal(this.type, buffer.toString());
	}

	@Override
	public Terminal create(Enum<? extends Enum<?>> type) {
		if(this.type == type){
			return new Terminal(type, "<identifier>");
		}

		return null;
	}
}
