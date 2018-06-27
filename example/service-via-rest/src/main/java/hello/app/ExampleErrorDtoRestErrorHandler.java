package hello.app;

import com.perceptnet.restclient.MessageConverter;
import com.perceptnet.restclient.RestInvocationErrorHandler;
import com.perceptnet.restclient.RestInvocationException;

import java.lang.reflect.Method;

/**
 * created by vkorovkin on 27.06.2018
 */
public class ExampleErrorDtoRestErrorHandler implements RestInvocationErrorHandler {
    @Override
    public void handle(RestInvocationException e, MessageConverter messageConverter, Method method) throws Throwable {
        if (e.getResponseBody() != null && !e.getResponseBody().isEmpty()) {
            ErrorDto err = null;
            try {
                err = messageConverter.parse(ErrorDto.class, e.getResponseBody());
            } catch (Exception ignore) {
            }
            if (err != null && err.getExceptionClass().equals(BusinessLogicException.class.getName())) {
                throw new BusinessLogicException(err.getMessage(), e);
            }

        }
    }
}
