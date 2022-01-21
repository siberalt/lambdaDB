package lambdaDB.core.metadata;

public class FieldMetadata {
    protected String name;

    protected FieldType type;

    protected DefaultValue defaultValue = null;

    protected boolean notNull = false;

    protected boolean autoIncrement = false;

    protected boolean primaryKey = false;

    public FieldMetadata setDefaultValue(String defaultValue){
        this.defaultValue = new DefaultValue(defaultValue);

        return this;
    }

    public FieldMetadata setDefaultValue(boolean defaultValue){
        this.defaultValue = new DefaultValue(defaultValue);

        return this;
    }

    public FieldMetadata setDefaultValue(int defaultValue){
        this.defaultValue = new DefaultValue(defaultValue);

        return this;
    }

    public FieldMetadata setDefaultValue(DefaultValue defaultValue) {
        this.defaultValue = defaultValue;

        return this;
    }

    public FieldMetadata setName(String name) {
        this.name = name;

        return this;
    }

    public FieldMetadata setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;

        return this;
    }

    public FieldMetadata setNotNull(boolean notNull) {
        this.notNull = notNull;

        return this;
    }

    public FieldMetadata setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;

        return this;
    }

    public FieldMetadata setType(FieldType type) {
        this.type = type;

        return this;
    }
}
