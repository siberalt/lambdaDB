package environment;

public class Environment {
    protected static EnvironmentState type;

    public static void setState(EnvironmentState type){
        Environment.type = type;
    }

    public static EnvironmentState getState(){
        return type;
    }

    public static boolean isProd(){
        return EnvironmentState.PROD.equals(type);
    }

    public static boolean isTest(){
        return EnvironmentState.TEST.equals(type);
    }

    public static boolean isStage(){
        return EnvironmentState.STAGE.equals(type);
    }

}
