package lambdaDB.core.storage.writer;

import lambdaDB.core.storage.StorageManager;
import lambdaDB.core.storage.object.Address;

import java.io.IOException;

public class SimpleStorageWriter extends AbstractStorageWriter {
    protected StorageManager storageManager;

    public SimpleStorageWriter(StorageManager storageManager) {
        super(storageManager.getDefaultStorage());
    }

    public void setAddress(Address address) throws IOException {
        this.storage = storageManager.getStorage(address);
        this.storage.position(address.getOffset());
    }

    @Override
    public void write(byte[] buf, int amount) throws IOException {

    }
}
