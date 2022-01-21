package syntax.compilator.input;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteInput implements InputInterface {
    ByteArrayInputStream inputStream;

    public ByteInput(byte[] bytes){
        inputStream = new ByteArrayInputStream(bytes);
    }

    @Override
    public int readInt() throws IOException {
        byte[] buffer = new byte[4];
        inputStream.read(buffer);

        return ByteBuffer.wrap(buffer).getInt();
    }

    @Override
    public boolean readBoolean() throws IOException {
        return inputStream.read() == 1;
    }

    @Override
    public String readString() throws IOException {
        int length = readInt();
        byte[] buffer = new byte[length];
        inputStream.read(buffer);

        return new String(buffer);
    }
}
