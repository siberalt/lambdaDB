package lambdaDB.core.storage.reader;

import global.helpers.ByteHelper;
import lambdaDB.core.storage.MemoryManager;
import lambdaDB.core.storage.object.Address;

import java.io.IOException;

public abstract class AbstractStorageReader implements StorageReaderInterface {
    protected MemoryManager memoryManager;

    public AbstractStorageReader(MemoryManager storage) {
        this.memoryManager = storage;
    }

    @Override
    public byte[] read(int amount) throws IOException {
        byte[] buffer = new byte[(int) amount];
        memoryManager.getStorage().read(buffer, amount);

        return buffer;
    }

    public long getPosition() throws IOException {
        return memoryManager.getStorage().position();
    }

    public void setPosition(long position) throws IOException {
        this.memoryManager.position(position);
    }

    @Override
    public long readLong() throws IOException {
        return ByteHelper.BytesToLong(read(8));
    }

    @Override
    public boolean readBoolean() throws IOException {
        return false;
    }

    @Override
    public String readString() throws IOException {
        int size = readInt();
        byte[] buffer = read(size * 2);

        return new String(buffer);
    }

    @Override
    public int readInt() throws IOException {
        return ByteHelper.BytesToInt(read(4));
    }

    public byte readByte() throws IOException {
        return read(1)[0];
    }

    @Override
    public Address getAddress() throws IOException {
        return new Address(memoryManager.getId(), memoryManager.position());
    }
}
