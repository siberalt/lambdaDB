package syntax.input_adapters;

import java.io.IOException;

public interface InputInterface {
	int END_OF_INPUT = -1;
	
	void reset() throws IOException;
	
	int next();
	
	void previous() throws IOException;
	
	long getCurrentPos();
	
	void setCurrentPos(long pos) throws IOException;
}
