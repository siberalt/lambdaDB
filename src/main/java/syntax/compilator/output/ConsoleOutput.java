package syntax.compilator.output;

import java.io.IOException;

public class ConsoleOutput implements OutputInterface{

    @Override
    public void write(String value) {
        System.out.print(value);
    }

    @Override
    public void write(char[] value) {
        System.out.print(value);
    }

    @Override
    public void write(char c) {
        System.out.print(c);
    }

    @Override
    public void write(int i) {
        System.out.print(i);
    }

    @Override
    public void write(boolean value) {
        System.out.print(value);
    }

    @Override
    public void write(byte value) {
        System.out.print(value);
    }
}
