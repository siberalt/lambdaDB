package syntax.scanner.input;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;

public class StringInput extends BufferedInput implements InputInterface {
    protected String input;

    public StringInput(String input) throws IOException {
        super(new BufferedReader(new CharArrayReader(input.toCharArray())));
        bufferedReader.mark(30000);
    }
}
