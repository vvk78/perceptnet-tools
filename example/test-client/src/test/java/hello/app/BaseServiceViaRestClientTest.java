package hello.app;

import org.testng.annotations.BeforeClass;


/**
 * created by vkorovkin on 20.06.2018
 */
public class BaseServiceViaRestClientTest {

    @BeforeClass
    public void beforeClass() {
        RestServiceProvider.INSTANCE = new RestServiceProvider("http://localhost:8080");
    }
}
