package com.perceptnet.tools.codegen.viarest.spring;

/**
 * This exception is thrown when tool detects violation of code conventions applied in ServiceViaRest approach
 *
 * created by vkorovkin on 14.06.2018
 */
public class CodeConventionException extends RuntimeException {

    public CodeConventionException() {
    }

    public CodeConventionException(String message) {
        super(message);
    }

    public CodeConventionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeConventionException(Throwable cause) {
        super(cause);
    }

    public CodeConventionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
