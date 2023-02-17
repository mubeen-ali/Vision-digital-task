package au.net.horizondigital.assessment.exceptions;

public class DataAlreadyExists extends RuntimeException{
    public DataAlreadyExists() {
        super();
    }

    public DataAlreadyExists(String message) {
        super(message);
    }

    public DataAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAlreadyExists(Throwable cause) {
        super(cause);
    }
}
