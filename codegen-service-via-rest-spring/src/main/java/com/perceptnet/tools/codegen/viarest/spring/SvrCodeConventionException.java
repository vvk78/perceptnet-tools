package com.perceptnet.tools.codegen.viarest.spring;

/**
 * This exception is thrown when tool detects violation of code conventions applied in ServiceViaRest approach
 *
 * created by vkorovkin on 14.06.2018
 */
public class SvrCodeConventionException extends RuntimeException {

    public SvrCodeConventionException() {
    }

    public SvrCodeConventionException(String message) {
        super(message);
    }

    public SvrCodeConventionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SvrCodeConventionException(Throwable cause) {
        super(cause);
    }

    public SvrCodeConventionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
