package lambdaDB.core.metadata;

public class DefaultValue {
    protected Object value;

    public DefaultValue(String value){
        this.value = value;
    }

    public DefaultValue(int value){
        this.value = value;
    }

    public DefaultValue(boolean value){
        this.value = value;
    }

    public boolean isNull(){
        return this.value == null;
    }

    public Object getValue() {
        return value;
    }
}
