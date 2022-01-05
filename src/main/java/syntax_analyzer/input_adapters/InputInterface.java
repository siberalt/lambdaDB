package syntax_analyzer.input_adapters;

import java.io.IOException;

public interface InputInterface {
	static final int END_OF_INPUT = -1;
	
	public void reset() throws IOException;
	
	public int next();
	
	public void previous() throws IOException;
	
	public long getCurrentPos();
	
	public void setCurrentPos(long pos) throws IOException;
}
