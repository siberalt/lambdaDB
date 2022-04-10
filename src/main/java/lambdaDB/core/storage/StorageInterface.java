package lambdaDB.core.storage;

import java.io.IOException;

public interface StorageInterface {
    void read(byte[] buf, int amount) throws IOException;

    void write(byte[] buf, int amount) throws IOException;

    long size() throws IOException;

    void position(long pos) throws IOException;

    long position() throws IOException;

    long available();

    void close() throws IOException;
}
