package syntax.compilator.input;

import java.io.BufferedReader;
import java.io.IOException;

public class BufferedInput implements InputInterface{
    protected BufferedReader bufferedReader;

    public BufferedInput(BufferedReader bufferedReader){
        this.bufferedReader = bufferedReader;
    }

    public BufferedInput(){}

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    @Override
    public int readInt() throws IOException {
        char[] buffer = new char[4];
        bufferedReader.read(buffer, 0, buffer.length);

        int value = 0;

        for(int i = buffer.length - 1; i >= 0 ; i--){
            value = (value | buffer[i]) << i * 8;
        }

        return value;
    }

    @Override
    public boolean readBoolean() throws IOException {
        char[] buffer = new char[1];
        bufferedReader.read(buffer, 0, buffer.length);

        return buffer[0] == 1;
    }

    @Override
    public String readString() throws IOException {
        int stringLen = readInt();

        char[] buffer = new char[stringLen];
        bufferedReader.read(buffer, 0, buffer.length);

        return new String(buffer);
    }
}
