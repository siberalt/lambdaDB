package lambdaDB.core.storage;

import lambdaDB.core.storage.object.*;
import lambdaDB.core.storage.reader.SimpleStorageReader;

import java.io.IOException;
import java.util.ArrayList;

public class MemoryManager implements StorageInterface{
    protected StorageInterface storage;

    protected byte id;

    ArrayList<Block> blocks = new ArrayList<>();

    public MemoryManager(byte id, StorageInterface storage) throws IOException {
        this.storage = storage;
        this.id = id;
    }

    public void init() throws IOException {
        blocks.clear();
        loadBlocks();
    }

    protected void loadBlocks() throws IOException {
        storage.position(0);

        if(0 != storage.size()){
            SimpleStorageReader reader = new SimpleStorageReader(this);
            long position = 0;

            while (position < storage.size()) {
                Block block = Block.load(reader);
                blocks.add(block);
                position += block.getSize();
                storage.position(position);
            }
        }
    }

    protected long allocate(long size) throws IOException {
        //storage.truncate(size);

        return storage.position();
    }

    public StorageInterface getStorage() {
        return storage;
    }

    public Block getBlock(Address address) throws IOException {
        long savedPos = storage.position();

        storage.position(address.getOffset());
        Block block = blockReader.readBlock(new SimpleStorageReader(this));
        storage.position(savedPos);

        return block;
    }

    @Override
    public void read(byte[] buf, int amount) throws IOException {
        this.storage.read(buf, amount);
    }

    @Override
    public void write(byte[] buf, int amount) throws IOException {
        this.storage.write(buf, amount);
    }

    @Override
    public long size() throws IOException {
        return this.storage.size();
    }

    @Override
    public void position(long pos) throws IOException {
        this.storage.position(pos);
    }

    @Override
    public long position() throws IOException {
        return this.storage.position();
    }

    @Override
    public long available() {
        return this.storage.available();
    }

    @Override
    public void close() throws IOException {
        this.storage.close();
    }

    public byte getId() {
        return this.id;
    }
}
