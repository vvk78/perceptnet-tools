package hello.app;


import org.testng.annotations.Test;

import static hello.app.RestServiceProvider.*;

/**
 * created by vkorovkin on 20.06.2018
 */
public class MessageServiceTest extends BaseServiceViaRestClientTest {


    @Test
    public void testMyServiceGetMessageViaRest() {
        MessageDto msg = getMessageService().getMessage();
        System.out.println("Message loaded: " + msg);
    }

}
