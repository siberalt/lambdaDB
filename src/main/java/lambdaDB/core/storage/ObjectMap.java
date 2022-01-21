package lambdaDB.core.storage;

import java.util.HashMap;

public class ObjectMap {
    public static class Address{
        protected int storageId;

        protected long offset;

        public Address(int storageId, long offset){
            this.storageId = storageId;
            this.offset = offset;
        }

        public int getStorageId() {
            return storageId;
        }

        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        public void setStorageId(int storageId) {
            this.storageId = storageId;
        }
    }

    public static class Id {
        protected ObjectType type;

        protected String name;

        static final ObjectType[] objectTypes = ObjectType.values();

        public Id(ObjectType type, String name){
            this.type = type;
            this.name = name;
        }

        public static Id read(StorageInterface storage){
            return new Id(objectTypes[storage.readInt()], storage.readString());
        }

        public void setType(ObjectType type) {
            this.type = type;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public ObjectType getType() {
            return type;
        }
    }

    StorageInterface storage;

    public ObjectMap(StorageInterface storage) {
        load(storage);
    }

    public void load(StorageInterface storage) {
        this.storage = storage;
    }
}
