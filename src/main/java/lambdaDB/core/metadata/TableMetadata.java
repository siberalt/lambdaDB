package lambdaDB.core.metadata;

import java.util.LinkedList;
import java.util.List;

public class TableMetadata {
    String name;

    List<FieldMetadata> fields = new LinkedList<>();

    public String getName() {
        return name;
    }

    public TableMetadata setName(String name) {
        this.name = name;

        return this;
    }

    public List<FieldMetadata> getFields() {
        return fields;
    }
}
