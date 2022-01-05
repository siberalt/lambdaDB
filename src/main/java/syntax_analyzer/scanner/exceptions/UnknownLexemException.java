package syntax_analyzer.scanner.exceptions;

public class UnknownLexemException extends ScannerException {
	protected String unknownLexem;
	
	public UnknownLexemException(int line, int position, String message) {
		super(line, position, message);
	}
	
	public UnknownLexemException(String unknownLexem, int line, int position) {
		super(line, position, String.format("encountered unknown lexeme '%s'", unknownLexem));
		this.unknownLexem = unknownLexem;
	}

	public String getUnknownLexem() {
		return unknownLexem;
	}
}
