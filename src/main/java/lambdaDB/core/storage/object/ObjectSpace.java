package lambdaDB.core.storage.object;

import lambdaDB.core.storage.StorageManager;
import lambdaDB.core.storage.reader.BlockStorageReader;

import java.io.IOException;
import java.util.HashMap;

public class ObjectSpace {
    protected StorageManager storageManager;

    protected Address dataAddress;

    protected Block startBlock;

    protected Block endBlock;

    protected HashMap<ObjectId, Address> childAddresses = new HashMap<>();

    public static ObjectSpace load(Block block, StorageManager manager) throws IOException {
        BlockStorageReader reader = new BlockStorageReader(block, manager);
        ObjectSpace objectSpace = new ObjectSpace();
        byte val = reader.readByte();

        while(val != ObjectType.NONE.ordinal()){
            ObjectId id = new ObjectId(ObjectType.values()[val], reader.readString());
            Address address = new Address(reader.readByte(), reader.readLong());
            objectSpace.addChildAddress(id, address);
            val = reader.readByte();
        }

        objectSpace.dataAddress = reader.getAddress();

        return objectSpace;
    }

//    public ObjectSpace(Block block, StorageManager manager){
//        startBlock = block;
//        endBlock = block;
//        storageManager = manager;
//
//        while(null != endBlock.getNextBlock()){
//            endBlock = endBlock.getNextBlock();
//        }
//    }

    public Block getStartBlock() {
        return startBlock;
    }

    public Block getEndBlock() {
        return endBlock;
    }

    protected ObjectSpace addChildAddress(ObjectId objectId, Address address){
        childAddresses.put(objectId, address);

        return this;
    }

    public long getTotalSize(){
        Block block = startBlock;
        long totalSize = 0;

        while (null != block) {
            totalSize += block.getSize();
            block = block.getNextBlock();
        }

        return totalSize;
    }

    public long getDataSize() throws IOException {
        Block block = storageManager.getBlock(dataAddress);
        long totalSize = block.getAddress().getOffset() - dataAddress.getOffset();

        while (null != block) {
            totalSize += block.getSize();
            block = block.getNextBlock();
        }

        return totalSize;
    }
}
