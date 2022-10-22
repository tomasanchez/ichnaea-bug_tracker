package org.ichnaea.core.security.auth;

import org.ichnaea.core.exception.AuthenticationException;

import java.util.function.Consumer;

public interface SessionManager {

    /**
     * Attempts to initialize a session by authenticating a user.
     *
     * @param username to be authenticated.
     * @param password credentials.
     */
    void attemptAuthentication(String username, String password, Consumer<Authentication> onSuccess,
                               Consumer<AuthenticationException> onFailure);

    void closeSession();

    Object getSession();
}
