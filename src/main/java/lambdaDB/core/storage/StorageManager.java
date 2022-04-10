package lambdaDB.core.storage;

import lambdaDB.core.metadata.TableMetadata;
import lambdaDB.core.storage.object.Address;
import lambdaDB.core.storage.object.Block;
import lambdaDB.core.storage.object.DatabaseInfo;
import lambdaDB.core.storage.object.ObjectInterface;
import lambdaDB.core.storage.writer.SimpleStorageWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class StorageManager {
    protected byte defaultStorageId = -1;

    protected HashMap<Byte, MemoryManager> memoryManagers = new HashMap<>();

    protected DatabaseInfo objectMap;

    public void addStorage(byte id, StorageInterface storage) throws IOException {
        memoryManagers.put(id, new MemoryManager(id, storage));
    }

    public void init() throws IOException {
        for(var manager: memoryManagers.values()){
            manager.init();
        }
    }

    public void setDefaultStorageId(byte defaultStorageId) {
        this.defaultStorageId = defaultStorageId;
    }

    public byte getDefaultStorageId() {
        if (defaultStorageId < 0) {
            defaultStorageId = memoryManagers.keySet().iterator().next();
        }

        return defaultStorageId;
    }

    public StorageInterface getDefaultStorage() {
        return getStorage(getDefaultStorageId());
    }

    public MemoryManager getDefaultMemoryManager() {
        return getMemoryManager(getDefaultStorageId());
    }

    public Address allocate(ObjectInterface object, long size) throws IOException {
        MemoryManager manager = getDefaultMemoryManager();
        byte defaultId = getDefaultStorageId();
        StorageInterface storage = manager.getStorage();
        long available = storage.available();
        SimpleStorageWriter writer = new SimpleStorageWriter(this);

        if (available < size) {
            ArrayList<Block> blocks = new ArrayList<>();
            long remaining = size - available;
            HashSet<StorageInterface> allocateStorages = new HashSet<>();
            allocateStorages.add(storage);
            Block prevBlock = new Block()
                .setSize(available)
                .setType(object.getType());

            if (prevBlock.requiredSpace() < available) {
                prevBlock.setAddress(new Address(defaultId, manager.allocate(available)));
                blocks.add(prevBlock);
            }

            for (var set : memoryManagers.entrySet()) {
                manager = set.getValue();
                storage = set.getValue().getStorage();
                available = storage.available();

                if (!allocateStorages.contains(storage)) {
                    if (available < remaining) {
                        remaining = remaining - available;
                        Block block = new Block()
                            .setSize(available)
                            .setType(object.getType());

                        if (block.requiredSpace() < available) {
                            block.setAddress(new Address(set.getKey(), manager.allocate(available)));
                            blocks.add(block);
                        }

                        allocateStorages.add(storage);
                    }
                }
            }

            for (var block : blocks) {
                writer.setAddress(block.getAddress());
                block.write(writer);
            }

            return blocks.iterator().next().getAddress();
        }

        Block block = new Block()
            .setSize(size);

        Address address = new Address(defaultId, manager.allocate(size));
        writer.setAddress(address);
        block.write(writer);

        return address;
    }

    public void createTable(TableMetadata tableMetadata) {
        if (getMemoryManager(getDefaultStorageId()).hasTable(tableMetadata.getName())) {
            //TODO: throw exception already exists
        }


    }

    public StorageInterface getStorage(Address address) {
        return getStorage(address.getStorageId());
    }

    public StorageInterface getStorage(byte id) {
        return getMemoryManager(id).getStorage();
    }

    public MemoryManager getMemoryManager(byte id) {
        if (memoryManagers.containsKey(id)) {
            return memoryManagers.get(id);
        }

        // TODO: throw exception
        return null;
    }

    public MemoryManager getMemoryManager(Address address) {
        return getMemoryManager(address.getStorageId());
    }

    public Block getBlock(Address address) throws IOException {
        return getMemoryManager(address.getStorageId()).getBlock(address);
    }
}
