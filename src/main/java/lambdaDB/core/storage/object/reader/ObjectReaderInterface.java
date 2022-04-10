package lambdaDB.core.storage.object.reader;

import lambdaDB.core.storage.object.ObjectInterface;
import lambdaDB.core.storage.reader.StorageReaderInterface;

import java.io.IOException;

public interface ObjectReaderInterface {
    ObjectInterface read(StorageReaderInterface storage) throws IOException;
}
