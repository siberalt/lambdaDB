package syntax.scanner.lexem.recognizer;

import syntax.lang.letter.Terminal;
import syntax.scanner.LexemeScannerPointerInterface;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;

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
	
	private boolean validateStartMarker(LexemeScannerPointerInterface pointer) throws UnexpectedEndOfInputException {
		for(char ch: this.startMarker.toCharArray()) {
			if(ch != (char) pointer.next()) {
				return false;
			}
		}
		
		return false;
	}

	public Terminal recognize(LexemeScannerPointerInterface pointer) throws UnexpectedEndOfInputException {
		if(!this.validateStartMarker(pointer)) {
			return null;
		}
		
		StringBuilder buffer = new StringBuilder(this.startMarker);
		char ch;
		while((ch = (char) pointer.next()) != '\n') {
			buffer.append(ch);
		}
		
		return new Terminal(this.type, buffer.toString());
	}

	@Override
	public Terminal create(Enum<? extends Enum<?>> type) {
		if(this.type == type){
			return new Terminal(type, "<comment>");
		}

		return null;
	}

	public void setStartMarker(String startMarker) {
		this.startMarker = startMarker;
	}
}
