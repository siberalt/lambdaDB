package syntax.scanner.lexem.recognizer;

import syntax.lang.letter.Terminal;
import syntax.scanner.LexemeScannerPointerInterface;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;

public class StringRecognizer extends AbstractAtomRecognizer
	implements LexemeRecognizerInterface
{
	protected char border;
	
	public StringRecognizer(char border, Enum<? extends Enum<?>> type) {
		super(type);
		this.setBorder(border);
	}

	public Terminal recognize(LexemeScannerPointerInterface pointer) throws UnexpectedEndOfInputException {
		char symbol = (char) pointer.next();
		StringBuilder buffer = new StringBuilder();
		
		if(symbol != this.border) {
			return null;
		}
		
		do {
			buffer.append(symbol);
			symbol = (char) pointer.next();
		}
		while(symbol != this.border);
		
		buffer.append(symbol);
		
		return new Terminal(this.type, buffer.toString());
	}

	@Override
	public Terminal create(Enum<? extends Enum<?>> type) {
		if(this.type == type){
			return new Terminal(type, border + "<string>" + border);
		}

		return null;
	}

	public void setBorder(char border) {
		this.border = border;
	}
}
