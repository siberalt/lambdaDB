package lambdaDB.core.storage.writer;

import lambdaDB.core.storage.StorageManager;
import lambdaDB.core.storage.object.ObjectSpace;

import java.io.IOException;

public class StorageWriter extends AbstractStorageWriter{
    StorageManager storageManager;

    public StorageWriter(StorageManager storageManager, ObjectSpace space) {
        storageManager.getMemoryManager(space.getStartBlock().getAddress());
        super(storage);
    }

    @Override
    public void write(byte[] buf, int amount) throws IOException {

    }
}
