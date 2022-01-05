package syntax_analyzer.scanner.lexem.recognizer;

import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.LexemScannerPointerInterface;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;

public class StringRecognizer extends AbstractAtomRecognizer
	implements LexemeRecognizerInterface
{
	protected char border;
	
	public StringRecognizer(char border, Enum<? extends Enum<?>> type) {
		super(type);
		this.setBorder(border);
	}

	public Terminal recognize(LexemScannerPointerInterface pointer) throws UnexpectedEndOfInputException {
		char symbol = (char) pointer.next();
		StringBuilder buffer = new StringBuilder("");
		
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

	public void setBorder(char border) {
		this.border = border;
	}
}
