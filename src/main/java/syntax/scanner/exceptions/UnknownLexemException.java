package syntax.scanner.exceptions;

public class UnknownLexemException extends ScannerException {
	protected String unknownLexem;
	
	public UnknownLexemException(int line, int position, String message) {
		super(line, position, message);
	}
	
	public UnknownLexemException(String unknownLexeme, int line, int position) {
		super(line, position, String.format("encountered unknown lexeme '%s'", unknownLexeme));
		this.unknownLexem = unknownLexeme;
	}

	public String getUnknownLexeme() {
		return unknownLexem;
	}
}
