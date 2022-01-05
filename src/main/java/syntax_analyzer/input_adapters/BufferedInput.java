package syntax_analyzer.input_adapters;

import java.io.BufferedReader;
import java.io.IOException;

public class BufferedInput {
    private long currentPos = 0;

    BufferedReader bufferedReader;

    public BufferedInput(BufferedReader bufferedReader) throws IOException {
        this.bufferedReader = bufferedReader;
    }

    public void reset() throws IOException {
        this.bufferedReader.reset();
    }

    public void previous() throws IOException {
        if (--this.currentPos < 0) {
            this.currentPos = 0;
        }

        this.bufferedReader.reset();
        this.bufferedReader.skip(this.currentPos);
    }

    public int next() {
        int symbol;

        try {
            symbol = this.bufferedReader.read();
        } catch (IOException exception) {
            symbol = InputInterface.END_OF_INPUT;
        }

        if (symbol != InputInterface.END_OF_INPUT) {
            this.currentPos++;
        }

        return symbol;
    }

    public long getCurrentPos() {
        return this.currentPos;
    }

    public void setCurrentPos(long position) throws IOException {
        this.bufferedReader.reset();
        this.bufferedReader.skip(position);
    }
}
