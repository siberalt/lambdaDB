package lambdaDB.core.storage.writer;

import java.io.IOException;

public interface StorageWriterInterface {
    void write(byte[] buf, int amount) throws IOException;

    void writeLong(long value) throws IOException;

    void writeBoolean(boolean value) throws IOException;

    void writeString(String value) throws IOException;

    void writeInt(int value) throws IOException;

    long getOffset() throws IOException;
}
