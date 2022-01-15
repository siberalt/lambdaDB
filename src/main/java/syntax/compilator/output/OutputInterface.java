package syntax.compilator.output;

import java.io.IOException;

public interface OutputInterface {
	void write(String value) throws IOException;

	void write(char[] value) throws IOException;

	void write(char c) throws IOException;

	void write(int i) throws IOException;

	void write(boolean value) throws IOException;

	void write(byte value) throws IOException;
}
