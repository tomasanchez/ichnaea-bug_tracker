package org.ichnaea.auth;

import lombok.Getter;
import org.ichnaea.core.security.auth.AuthenticationManager;
import org.ichnaea.core.security.auth.AuthorizationManager;
import org.ichnaea.core.security.auth.SecurityContext;
import org.ichnaea.core.security.auth.SessionManager;

@Getter
public class IchnaeaSecurityContext implements SecurityContext {

    private final AuthenticationManager authenticationManager;

    private final AuthorizationManager authorizationManager;

    private final SessionManager sessionManager;

    public IchnaeaSecurityContext(AuthenticationManager authenticationManager,
                                  AuthorizationManager authorizationManager, SessionManager sessionManager) {
        this.authenticationManager = authenticationManager;
        this.authorizationManager = authorizationManager;
        this.sessionManager = sessionManager;
    }

    @Override
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    @Override
    public AuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }
    
}
