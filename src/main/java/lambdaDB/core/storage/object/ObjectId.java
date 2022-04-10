package lambdaDB.core.storage.object;

public class ObjectId {
    protected ObjectType type;

    protected String id;

    public ObjectId(ObjectType type, String id){
        this.type = type;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public ObjectType getType() {
        return type;
    }
}
