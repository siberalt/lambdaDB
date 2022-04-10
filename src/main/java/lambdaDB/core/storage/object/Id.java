package lambdaDB.core.storage.object;

public class Id {
    protected ObjectType type;

    protected String name;

    static final ObjectType[] objectTypes = ObjectType.values();

    public Id(ObjectType type, String name){
        this.type = type;
        this.name = name;
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