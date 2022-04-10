package lambdaDB.core.storage.reader;

import lambdaDB.core.storage.object.Address;

import java.io.IOException;

public interface StorageReaderInterface {
    byte[] read(int amount) throws IOException;

    long readLong() throws IOException;

    boolean readBoolean() throws IOException;

    String readString() throws IOException;

    int readInt() throws IOException;

    byte readByte() throws IOException;

    Address getAddress() throws IOException;
}
