package lambdaDB.core.storage.reader;

import lambdaDB.core.storage.StorageManager;
import lambdaDB.core.storage.object.Address;
import lambdaDB.core.storage.object.Block;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BlockStorageReader extends AbstractStorageReader{
    protected StorageManager storageManager;

    protected Block currentBlock;

    protected long endPosition;

    protected long currentPosition;

    public BlockStorageReader(Block block, StorageManager storageManager){
        super(storageManager.getMemoryManager(block.getAddress()));
        this.storageManager = storageManager;
        currentPosition = block.getAddress().getOffset();
        endPosition = currentPosition + block.getSize();
    }

    public byte[] read(int amount) throws IOException {
        memoryManager.position(currentPosition);
        long nextPosition = amount + currentPosition;

        if(nextPosition > endPosition){
            int remainAmount = (int) (endPosition - currentPosition);
            byte[] buff1 = new byte[remainAmount];
            memoryManager.read(buff1, remainAmount);

            currentBlock = storageManager.getBlock(currentBlock.getNextBlock().getAddress());
            memoryManager = storageManager.getMemoryManager(currentBlock.getNextBlock().getAddress());
            currentPosition = currentBlock.getAddress().getOffset();

            byte[] buff2 = read((int) (nextPosition - remainAmount));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(buff1);
            outputStream.write(buff2);

            return outputStream.toByteArray();
        }

        byte[] buff = new byte[(int) amount];
        memoryManager.read(buff, amount);
        currentPosition += amount;

        return buff;
    }

    @Override
    public Address getAddress() throws IOException {
        return super.getAddress();
    }
}
