package lambdaDB.core.storage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public class FileStorage implements StorageInterface {
    protected FileChannel fileChannel;

    protected String filename;

    public FileStorage(String filename) throws IOException {
        this.filename = filename;
        fileChannel = FileChannel.open(Path.of(filename));
    }

    @Override
    public void read(byte[] buf, int amount) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buf, 0, amount);

        fileChannel.read(byteBuffer);
    }

    @Override
    public void write(byte[] buf, int amount) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buf, 0, amount);

        fileChannel.write(byteBuffer);
    }

    @Override
    public long size() throws IOException {
        return fileChannel.size();
    }

    @Override
    public void position(long pos) throws IOException {
        fileChannel.position(pos);
    }

    @Override
    public long position() throws IOException {
        return fileChannel.position();
    }

    @Override
    public long available() {
        return Long.MAX_VALUE;
    }

    @Override
    public void close() throws IOException {
        fileChannel.close();
    }
}
