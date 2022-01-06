package syntax.scanner.exceptions;

public class ScannerException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int line;

	protected int position;

	public ScannerException(int line, int position, String message) {
		super(String.format(
				"Scanner has thrown an exception at line %d, in position %d. Message: %s",
				line,
				position,
				message
			)
		);

		this.line = line;
		this.position = position;
	}

	public int getLine() {
		return line;
	}

	public int getPosition() {
		return position;
	}
}
