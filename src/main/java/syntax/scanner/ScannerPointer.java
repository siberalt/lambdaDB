package syntax.scanner;

import syntax.scanner.input.InputInterface;
import syntax.scanner.exceptions.GeneralScannerException;
import syntax.scanner.exceptions.NegativePositionException;
import syntax.scanner.exceptions.UnexpectedEndOfInputException;

import java.io.IOException;
import java.util.LinkedList;

public class ScannerPointer implements LexemeScannerPointerInterface {
	
	protected long currentPosition = 0;
	
	protected InputInterface input;
	
	protected long endPosition = Long.MAX_VALUE;
	
	protected LinkedList<Long> lineBreaksPositions;
	
	protected int column = 0;
	
	protected long maxPosition = 0;

	private static final int MAX_BUFFER_SIZE = 255;
	
	protected int bufferSize = 0;
	
	protected char[] buffer = new char[MAX_BUFFER_SIZE];
	
	protected long bufferMark;
	
	protected boolean isBufferEnabled = false;
	
	public ScannerPointer(InputInterface input) {
		this.input = input;
		lineBreaksPositions = new LinkedList<>();
		lineBreaksPositions.add(0L);
	}
	
	public boolean isEnd() {
		return this.endPosition <= this.currentPosition;
	}

	public int get(int pos) throws UnexpectedEndOfInputException, GeneralScannerException, IOException {
		if(hasInBuffer(pos)){

			return getFromBuffer(pos);
		}else if (0 <= pos){
			setCurrentPos(bufferMark + pos);

			return next();
		}

		throw new GeneralScannerException("Invalid position " + pos + ". Negative position is not allowed");
	}

	public int next() throws UnexpectedEndOfInputException {
		if(this.isEnd()) {
			throw new UnexpectedEndOfInputException(this.getLineNumber(), getColumnNumber());
		}
		
		int symbol; char ch;
		
		if(this.isBufferEnabled && this.hasInBuffer(this.currentPosition)) {
			ch = this.getFromBuffer(this.currentPosition);
			symbol = ch;
		}else {
			symbol = this.input.next();
			ch = (char)symbol;
			if(this.isBufferEnabled) {
				pushToBuffer(ch);
			}
		}
		
		if(symbol == InputInterface.END_OF_INPUT) {
			this.endPosition = this.currentPosition + 1;
		}
		
		if(ch == '\n') {
			this.column = 0;
			
			if(!this.lineBreaksPositions.contains(this.currentPosition)) {
				this.lineBreaksPositions.add(this.getCurrentPos());
			}
		}else {
			this.column++;
		}
		
		this.currentPosition++;
		
		if(this.currentPosition > this.maxPosition) {
			this.maxPosition = this.currentPosition;
		}
		
		return symbol;
	}

	private void pushToBuffer(char ch) {
		if(this.bufferMark <= this.currentPosition) {
			this.buffer[(int) (this.currentPosition - this.bufferMark)] = ch;
			this.bufferSize++;
		}
	}
	
	private char getFromBuffer(long position) {
		return this.buffer[(int) (position - this.bufferMark)];
	}
	
	private boolean hasInBuffer(long position) {
		return this.bufferMark <= position && position < this.bufferMark + this.bufferSize;
	}
	
	public void previous() throws UnexpectedEndOfInputException, IOException, NegativePositionException {
		this.setCurrentPos(this.currentPosition - 1);
	}
	
	public long getCurrentPos() {
		return this.currentPosition;
	}
	
	public void skipAheadTo(long dest) throws UnexpectedEndOfInputException {
		for(long i = this.currentPosition; i <= dest; i++) {
			this.next();
		}
	}
	
	public void setCurrentPos(long pos) throws UnexpectedEndOfInputException, IOException, NegativePositionException {
		if(pos > this.maxPosition) {
			this.skipAheadTo(pos);
		}
		
		if(pos < 0) {
			throw new NegativePositionException(pos);
		}
		
		this.currentPosition = pos;
		
		long lastLinePos = this.lineBreaksPositions.removeLast();
		
		while(pos < lastLinePos) {
			lastLinePos = this.lineBreaksPositions.removeLast();
		}
		
		if(!(this.isBufferEnabled && this.hasInBuffer(this.currentPosition))) {
			this.input.setCurrentPos(this.currentPosition);
		}
		
		this.column = (int) (pos - lastLinePos + (lastLinePos == 0 ? 1 : 0));
		this.lineBreaksPositions.add(lastLinePos);
	}
	
	public int getLineNumber() {
		return this.lineBreaksPositions.size();
	}
	
	public int getColumnNumber() {
		return this.column;
	}
	
	public void enableBuffering(boolean enable) {
		this.isBufferEnabled = enable;
	}
	
	public void bufferingFromCurrentPos() throws IOException {
		
		if (this.isBufferEnabled) {
			this.bufferSize = 0;
			this.bufferMark = this.currentPosition;
			this.input.setCurrentPos(this.currentPosition);
		}
	}
	
	public String getBufferContents() {
		if(this.currentPosition > this.bufferMark + this.bufferSize 
			|| this.currentPosition < this.bufferMark) {
			return "";
		}
		
		StringBuilder result = new StringBuilder();
		
		for(int i = 0; i < this.currentPosition - this.bufferMark + 1; i++) {
			result.append(this.buffer[i]);
		}
		
		return result.toString();
	}
	
	public void error(String message) {
		
		
	}

	@Override
	public void resetTo(int i) throws UnexpectedEndOfInputException, IOException, NegativePositionException {
		setCurrentPos(bufferMark + i);
	}
}
