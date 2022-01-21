package lambdaDB.core.storage;

import lambdaDB.core.metadata.TableMetadata;

import java.util.HashMap;

public class StorageManager {
    protected int metadataStorageId = -1;

    HashMap<Integer, StorageInterface> storages = new HashMap<>();

    public void addStorage(StorageInterface storage){
        storages.put(storage.getId(), storage);
    }

    public void setMetadataStorageId(int metadataStorageId) {
        this.metadataStorageId = metadataStorageId;
    }

    public int getMetadataStorageId() {
        if(metadataStorageId < 0){
            metadataStorageId = storages.keySet().iterator().next();
        }

        return metadataStorageId;
    }

    public void saveTable(TableMetadata tableMetadata){

    }
}
