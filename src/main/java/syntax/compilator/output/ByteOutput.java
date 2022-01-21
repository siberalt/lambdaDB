package syntax.compilator.output;

import global.helpers.ByteHelper;
import syntax.compilator.input.ByteInput;
import syntax.compilator.input.InputInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteOutput implements OutputInterface {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Override
    public void write(String value) throws IOException {
        outputStream.write(value.getBytes());
    }

    @Override
    public void write(char[] value) throws IOException {
        outputStream.write(new String(value).getBytes());
    }

    @Override
    public void write(char c) throws IOException {
        outputStream.write(String.valueOf(c).getBytes());
    }

    @Override
    public void write(int i) throws IOException {
        outputStream.write(ByteHelper.intToByteArray(i));
    }

    @Override
    public void write(boolean value) throws IOException {
        byte[] v = new byte[]{(byte) (value ? 1 : 0)};
        outputStream.write(v);
    }

    @Override
    public void write(byte value) throws IOException {
        outputStream.write(new byte[]{value});
    }

    public InputInterface getInput() {
        return new ByteInput(outputStream.toByteArray());
    }
}
