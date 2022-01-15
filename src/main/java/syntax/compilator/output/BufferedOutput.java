package syntax.compilator.output;

import java.io.BufferedWriter;
import java.io.IOException;

public class BufferedOutput {
    BufferedWriter bufferedWriter;

    public BufferedOutput(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    public BufferedOutput() {

    }

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    public void write(String value) throws IOException {
        bufferedWriter.write(value);
    }

    public void write(char[] value) throws IOException {
        bufferedWriter.write(value);
    }

    public void write(char value) throws IOException {
        bufferedWriter.write(value);
    }

    public void write(int value) throws IOException {
        bufferedWriter.write(value);
    }

    public void write(boolean value) throws IOException {
        bufferedWriter.write(String.valueOf(value));
    }

    public void write(byte value) throws IOException {
        bufferedWriter.write(value);
    }
}
