package syntax_analyzer.scanner.lexem.recognizer;

import syntax_analyzer.lang_objects.Terminal;
import syntax_analyzer.scanner.LexemScannerPointerInterface;
import syntax_analyzer.scanner.exceptions.UnexpectedEndOfInputException;

//SELECT * FROM table WHERE id = 1

public class CommentRecognizer extends AbstractAtomRecognizer
	implements LexemeRecognizerInterface
{
	
	protected String startMarker = "//";
	
	public CommentRecognizer(Enum<? extends Enum<?>> type) {
		super(type);
	}
	
	public CommentRecognizer(Enum<? extends Enum<?>> type, String startMarker) {
		super(type);
		this.setStartMarker(startMarker);
	}
	
	private boolean validateStartMarker(LexemScannerPointerInterface pointer) throws UnexpectedEndOfInputException {
		for(char ch: this.startMarker.toCharArray()) {
			if(ch != (char) pointer.next()) {
				return false;
			}
		}
		
		return false;
	}

	public Terminal recognize(LexemScannerPointerInterface pointer) throws UnexpectedEndOfInputException {
		if(!this.validateStartMarker(pointer)) {
			return null;
		}
		
		StringBuilder buffer = new StringBuilder(this.startMarker);
		char ch;
		while((ch = (char) pointer.next()) != '\n') {
			buffer.append(ch);
		};
		
		return new Terminal(this.type, buffer.toString());
	}

	public void setStartMarker(String startMarker) {
		this.startMarker = startMarker;
	}
}
