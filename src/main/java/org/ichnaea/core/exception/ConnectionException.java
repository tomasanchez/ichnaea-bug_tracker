package org.ichnaea.core.exception;

public class ConnectionException extends DatabaseException {

    public ConnectionException(String msg) {
        super(msg);
    }

    public ConnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
