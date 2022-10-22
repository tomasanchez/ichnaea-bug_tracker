package org.ichnaea.core.security.auth;

import org.ichnaea.core.exception.AuthenticationException;

/**
 * Process an {@link Authentication} request.
 */
public interface AuthenticationManager {

    /**
     * Attempts to authenticate, returning a fully populated <code>Authentication</code> object.
     *
     * @param authentication the authentication request object
     * @return a fully authenticated object including credentials
     * @throws AuthenticationException if authentication fails
     */
    Authentication authenticate(Authentication authentication)
            throws AuthenticationException;

}