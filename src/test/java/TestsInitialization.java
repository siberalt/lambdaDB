import environment.Environment;
import environment.EnvironmentState;
import org.junit.Before;

public class TestsInitialization {
    @Before
    public void init(){
        Environment.setState(EnvironmentState.TEST);
    }
}
