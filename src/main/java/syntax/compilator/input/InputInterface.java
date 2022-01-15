package syntax.compilator.input;

import java.io.IOException;

public interface InputInterface {
    int readInt() throws IOException;

    boolean readBoolean() throws IOException;

    String readString() throws IOException;
}
