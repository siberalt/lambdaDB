package lambdaDB.core.storage.object;

import lambdaDB.core.storage.StorageManager;
import lambdaDB.core.storage.reader.StorageReaderInterface;
import lambdaDB.core.storage.writer.StorageWriterInterface;

import java.io.IOException;
import java.util.HashMap;

public class Block {
    protected final int HEADER_SIZE = 17;

    protected static HashMap<Block, Address> nextBlockAddresses = new HashMap<>();

    protected long size = 1024;

    protected Address address;

    protected Block nextBlock;

    public Block setAddress(Address address) {
        this.address = address;

        return this;
    }

    public Block setSize(long size) {
        this.size = size;

        return this;
    }

    public Address getAddress() {
        return address;
    }

    public long getSize() {
        return size;
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    public static Block load(StorageReaderInterface reader) throws IOException {
        Address address = reader.getAddress();
        Block block = new Block()
            .setSize(reader.readLong())
            .setAddress(address);

        byte storageId = reader.readByte();

        if(0 != storageId) {
            Address nextBlockAddress = new Address(storageId, reader.readLong());
            nextBlockAddresses.put(block, nextBlockAddress);
        }

        return block;
    }

    public static void bind(StorageManager storageManager) throws IOException {
        for(var entry : nextBlockAddresses.entrySet()){
            entry.getKey().setNextBlock(storageManager.getBlock(entry.getValue()));
        }

        nextBlockAddresses.clear();
    }

    public static void save(Block block, StorageWriterInterface writer) {

    }
}
