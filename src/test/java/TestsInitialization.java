import global.environment.Environment;
import global.environment.EnvironmentState;
import org.junit.Before;

public class TestsInitialization {
    @Before
    public void init(){
        Environment.setState(EnvironmentState.TEST);
    }
}
