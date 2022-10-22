package org.ichnaea.core.exception;

/**
 * Corresponds to a denied request do to lack of authorities.
 */
public class UnauthorizedException extends AuthorizationException {

    /**
     * Constructs an {@code UnauthorizedException} with the specified message and no root cause.
     *
     * @param msg the detail message
     */
    public UnauthorizedException(String msg) {
        super(msg);
    }

    /**
     * Constructs an {@code UnauthorizedException} with the specified message and root cause.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public UnauthorizedException(String msg, Throwable cause) {
        super(msg, cause);
    }

}