package syntax.scanner.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileInput extends BufferedInput implements InputInterface {
    public FileInput(String filename) throws IOException {
        super(new BufferedReader(new FileReader(filename)));
        bufferedReader.mark(30000);
    }
}
