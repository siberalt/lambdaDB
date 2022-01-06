package syntax.analyzer;

import syntax.lang.letter.Terminal;
import syntax.scanner.Scanner;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;
import syntax.scanner.exceptions.UnknownLexemException;

import java.io.IOException;

public class TerminalScanner {
	Scanner scanner;
	Terminal firstTerminal = null;
	
	public TerminalScanner(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public void setFirstScanTerminal(Terminal firstTerminal) {
		this.firstTerminal = firstTerminal;
	}
	
	public Terminal scan() throws UnexpectedEndOfInputException, UnknownLexemException, GeneralScannerException, IOException {
		if(null != this.firstTerminal) {
			Terminal terminal = this.firstTerminal;
			this.firstTerminal = null;
			return terminal;
		}
		
		return this.scanner.scan();
	}
}
