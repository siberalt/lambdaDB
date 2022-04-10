package lambdaDB.core.storage.writer;

import global.helpers.ByteHelper;
import lambdaDB.core.storage.StorageInterface;

import java.io.IOException;

public abstract class AbstractStorageWriter implements StorageWriterInterface {
    StorageInterface storage;

    public AbstractStorageWriter(StorageInterface storage) {
        this.storage = storage;
    }

    public void write(byte[] buf, int amount) throws IOException{
        storage.write(buf, amount);
    }

    @Override
    public void writeLong(long value) throws IOException {
        write(ByteHelper.LongToBytes(value), 8);
    }

    @Override
    public void writeBoolean(boolean value) throws IOException {
        write(new byte[]{(byte) (value ? 1 : 0)}, 1);
    }

    @Override
    public void writeString(String value) throws IOException {
        write(ByteHelper.intToBytes(value.length()), 4);
        write(value.getBytes(), value.length());
    }

    @Override
    public void writeInt(int value) throws IOException {
        write(ByteHelper.intToBytes(value), 8);
    }

    @Override
    public long getOffset() throws IOException {
        return storage.position();
    }
}
