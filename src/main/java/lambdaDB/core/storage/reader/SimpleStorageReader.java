package lambdaDB.core.storage.reader;

import lambdaDB.core.storage.MemoryManager;

public class SimpleStorageReader extends AbstractStorageReader{
    public SimpleStorageReader(MemoryManager memoryManager) {
        super(memoryManager);
    }

    @Override
    public byte readByte() {
        return 0;
    }
}
