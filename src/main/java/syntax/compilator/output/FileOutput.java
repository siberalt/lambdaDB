package syntax.compilator.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileOutput extends BufferedOutput implements OutputInterface {
    public FileOutput(String filename) throws IOException {
        super(new BufferedWriter(new FileWriter(filename)));
    }
}

