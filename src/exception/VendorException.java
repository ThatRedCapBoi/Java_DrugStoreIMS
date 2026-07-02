package exception;

public class VendorException extends Exception {

    public VendorException() {
        super();
    }

    public VendorException(String message) {
        super(message);
    }

    public VendorException(String message, Throwable cause) {
        super(message, cause);
    }
}
