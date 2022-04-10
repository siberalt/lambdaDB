package lambdaDB.core.storage.object;

public class Address{
    protected byte storageId;

    protected long offset;

    public Address(byte storageId, long offset){
        this.storageId = storageId;
        this.offset = offset;
    }

    public byte getStorageId() {
        return storageId;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setStorageId(byte storageId) {
        this.storageId = storageId;
    }
}
