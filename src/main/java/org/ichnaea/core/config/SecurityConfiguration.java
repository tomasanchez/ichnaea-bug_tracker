package org.ichnaea.core.config;

import org.ichnaea.core.mvc.controller.Controller;
import org.ichnaea.core.security.auth.SecurityContext;

public abstract class SecurityConfiguration implements SimpleConfiguration {

    /**
     * Sets the SecurityContext for authentication/authorization.
     *
     * @param context the holder of the authentication/authorization information
     */
    protected void setSecurityContext(SecurityContext context) {
        Controller.setSecurityContext(context);
    }

}

