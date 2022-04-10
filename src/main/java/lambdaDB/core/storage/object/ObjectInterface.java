package lambdaDB.core.storage.object;

import lambdaDB.core.storage.writer.StorageWriterInterface;

public interface ObjectInterface {
    ObjectType getType();

    long requiredSpace();

    void write(StorageWriterInterface writer);

}
