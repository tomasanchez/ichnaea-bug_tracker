package org.ichnaea.core.security.auth;

import org.ichnaea.core.exception.AuthorizationException;

import java.util.Collection;

/**
 * Verifies an {@link Authentication} request.
 */
public interface AuthorizationManager {

    /**
     * Attempts to authorize, halting the request if could not authorize.
     *
     * @param authentication the authentication request object
     * @param authorities    the authorites required for the authentication to have
     * @throws AuthorizationException
     */
    void authorize(Authentication authentication, Collection<GrantedAuthority> authorities)
            throws AuthorizationException;


    /**
     * Attempts to authorize, halting the request if could not authorize.
     *
     * @param authentication the authentication request object
     * @throws AuthorizationException
     */
    void authorize(Authentication authentication) throws AuthorizationException;
}