package lambdaDB.core.storage.object.reader;

import lambdaDB.core.storage.object.Address;
import lambdaDB.core.storage.object.Block;
import lambdaDB.core.storage.object.ObjectInterface;
import lambdaDB.core.storage.reader.StorageReaderInterface;

import java.io.IOException;

public class BlockReader implements ObjectReaderInterface{
    @Override
    public ObjectInterface read(StorageReaderInterface reader) throws IOException {
        return readBlock(reader);
    }

    public Block readBlock(StorageReaderInterface reader) throws IOException {
        long offset = reader.getAddress();

        return new Block()
            .setAddress(new Address(reader.readInt(), offset))
            .setSize(reader.readLong())
            ;
    }
}
