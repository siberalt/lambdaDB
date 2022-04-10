package lambdaDB.core.storage.saver;

import lambdaDB.core.storage.writer.StorageWriterInterface;

public interface SaverInterface {
    void save(StorageWriterInterface writer);
}
