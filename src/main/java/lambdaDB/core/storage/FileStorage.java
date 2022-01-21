package lambdaDB.core.storage;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public class FileStorage implements StorageInterface {
    protected int id;

    FileChannel fileChannel;

    String filename;

    public FileStorage(int id, String filename) {
        this.filename = filename;
        this.id = id;
    }

    public void init() throws IOException {
        fileChannel = FileChannel.open(Path.of(filename));
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int readInt() {
        return 0;
    }

    @Override
    public String readString() {
        return null;
    }
}
