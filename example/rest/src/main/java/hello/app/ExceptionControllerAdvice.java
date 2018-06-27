package hello.app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * created by vkorovkin on 27.06.2018
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    /**
     * Handler for business logic exceptions
     */
    @ExceptionHandler(BusinessLogicException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto handleBusinessLogicException(BusinessLogicException e, HttpServletRequest request) {
        return new ErrorDto("Error", e.getMessage(),
                e.getClass().getName(), e)
                .setErrorMessageKey(e.getMessage());
    }

    /**
     * Generic handler for any non-specifically dealt with exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto handleException(Exception e, HttpServletRequest request) {
        return new ErrorDto("Internal server error", e.getMessage(),
                e.getClass().getName(), e);
    }
}
