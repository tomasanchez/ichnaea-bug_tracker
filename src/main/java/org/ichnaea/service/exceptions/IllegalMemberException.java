package org.ichnaea.service.exceptions;

public class IllegalMemberException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public IllegalMemberException(String message) {
        super(message);
    }

    public IllegalMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalMemberException(Throwable cause) {
        super(cause);
    }
    
}
