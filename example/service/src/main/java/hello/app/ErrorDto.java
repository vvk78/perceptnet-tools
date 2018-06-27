
package hello.app;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class is used to transfer data about server-side problems (business logic exception etc.) to a rest client.
 * <p>
 * (<b>Strictly speaking this class is not real part of service-api</b> as it is for implicit data transfer
 * and so it rather corresponds to the web service layer, not to API. )
 * <p>
 * Created by korovkin on 27.06.2018.
 */
public class ErrorDto {
    private String header;
    private String message;
    private String errorMessageKey;
    private String exceptionClass;
    private String details;
    private Object cargo;
    private Object errorData;

    public ErrorDto() {
    }

    public ErrorDto(String header, String message) {
        this.header = header;
        this.message = message;
    }

    public ErrorDto(String header, String message, String exceptionClass, String details) {
        this.header = header;
        this.message = message;
        this.exceptionClass = exceptionClass;
        this.details = details;
    }

    public ErrorDto(String header, String message, String exceptionClass, Throwable t) {
        this.header = header;
        this.message = message;
        this.exceptionClass = exceptionClass;
        this.details = getStackTraceAsString(t);
    }

    public ErrorDto(String header, Throwable e) {
        this.header = header;

        this.message = e.getMessage();
        this.exceptionClass = e.getClass().getName();
        this.details = getStackTraceAsString(e);
    }

    public ErrorDto(String header, Throwable e, Object errorData) {
        this(header, e);
        this.errorData = errorData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Object getCargo() {
        return cargo;
    }

    public void setCargo(Object cargo) {
        this.cargo = cargo;
    }

    public Object getErrorData() {
        return errorData;
    }

    public void setErrorData(Object errorData) {
        this.errorData = errorData;
    }

    public String getErrorMessageKey() {
        return errorMessageKey;
    }

    public ErrorDto setErrorMessageKey(String errorMessageKey) {
        this.errorMessageKey = errorMessageKey;
        return this;
    }

    /**
     * Gets stacktrace as string. (Implemented just in order not to have external dependencies)
     * @param t throwable to get stacktrace of
     */
    private String getStackTraceAsString(Throwable t) {
        StringWriter sw = new StringWriter(15000); //typical stacktrace size
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
