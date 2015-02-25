package ksbysample.webapp.basic.exception;

public class InvalidRequestException extends Exception {

    private static final long serialVersionUID = -2148809788806855935L;

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

}
