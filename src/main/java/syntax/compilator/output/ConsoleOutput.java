package syntax.compilator.output;

import java.io.IOException;

public class ConsoleOutput implements OutputInterface{

    @Override
    public void write(String value) throws IOException {
        System.out.print(value);
    }

    @Override
    public void write(char[] value) throws IOException {
        System.out.print(value);
    }

    @Override
    public void write(char c) throws IOException {
        System.out.print(c);
    }

    @Override
    public void write(int i) throws IOException {
        System.out.print(i);
    }

    @Override
    public void write(boolean value) throws IOException {
        System.out.print(value);
    }

    @Override
    public void write(byte value) throws IOException {
        System.out.print(value);
    }
}
