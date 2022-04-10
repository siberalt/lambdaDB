package lambdaDB.core.storage.loader;

import lambdaDB.core.storage.reader.StorageReaderInterface;

public interface LoaderInterface {
    void load(StorageReaderInterface reader);
}
