package syntax.compilator.output;

import syntax.compilator.input.CharInput;
import syntax.compilator.input.InputInterface;

import java.io.*;

public class CharOutput extends BufferedOutput implements OutputInterface {
    protected CharArrayWriter charWriter = new CharArrayWriter();

    public CharOutput(){
        setBufferedWriter(new BufferedWriter(charWriter));
    }

    public CharOutput(CharArrayWriter charWriter){
        this.charWriter = charWriter;
        setBufferedWriter(new BufferedWriter(charWriter));
    }

    public InputInterface getInput() throws IOException {
        bufferedWriter.flush();
        return new CharInput(charWriter.toCharArray());
    }
}
