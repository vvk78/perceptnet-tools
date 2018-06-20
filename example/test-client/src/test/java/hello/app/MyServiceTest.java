package hello.app;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static hello.app.RestServiceProvider.*;

/**
 * created by vkorovkin on 20.06.2018
 */
public class MyServiceTest {
    @BeforeClass
    public void beforeClass() {
        RestServiceProvider.INSTANCE = new RestServiceProvider("http://localhost:8080");
    }


    @Test
    public void testMyServiceGetMessageViaRest() {
        MessageDto msg = getMyService().getMessage();
        System.out.println("Message loaded: " + msg);
    }

}
