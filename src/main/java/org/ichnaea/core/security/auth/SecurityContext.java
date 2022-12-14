package org.ichnaea.core.security.auth;


/**
 * Generic Interface for authentication/authorization management.
 *
 * @author Tomás Sánchez
 */
public interface SecurityContext {

    /**
     * Retrieves the authentication manager associated.
     *
     * @return an authentication manager.
     */
    AuthenticationManager getAuthenticationManager();

    /**
     * Retrieves the authorization manager associated.
     *
     * @return an authorization manager.
     */
    AuthorizationManager getAuthorizationManager();

    /**
     * Retrieves the current authenticated user.
     *
     * @return
     */
    SessionManager getSessionManager();
}
