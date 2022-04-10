package lambdaDB.core.storage;

import global.helpers.ByteHelper;
import lambdaDB.core.storage.object.ObjectSpace;

import java.io.IOException;

public class MemoryAccess {
    protected ObjectSpace space;

    public MemoryAccess(ObjectSpace space){
        this.space = space;
    }

    public int readInt(){

    }

    public long readLong(){

    }

    public void write(byte[] buf, int amount) throws IOException {
        storage.write(buf, amount);
    }

    public void writeLong(long value) throws IOException {
        write(ByteHelper.LongToBytes(value), 8);
    }

    public void writeBoolean(boolean value) throws IOException {
        write(new byte[]{(byte) (value ? 1 : 0)}, 1);
    }

    public void writeString(String value) throws IOException {
        write(ByteHelper.intToBytes(value.length()), 4);
        write(value.getBytes(), value.length());
    }

    public void writeInt(int value) throws IOException {
        write(ByteHelper.intToBytes(value), 8);
    }
}
