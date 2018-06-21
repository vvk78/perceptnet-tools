package hello.app;


import org.testng.annotations.BeforeClass;


/**
 * created by vkorovkin on 20.06.2018
 */
public class BaseServiceViaRestClientTest {

    private static final Object _appLock = new Object();
    private static boolean appLaunched;

    @BeforeClass
    public void beforeClass() {
        synchronized (_appLock) {
            if (!appLaunched) {
                DemoApplication.main(new String[0]);
                RestServiceProvider.INSTANCE = new RestServiceProvider("http://localhost:8080");
                appLaunched = true;
            }
        }
    }

}
