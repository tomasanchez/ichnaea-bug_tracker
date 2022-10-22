package org.ichnaea.config;


import lombok.Getter;
import lombok.Setter;
import org.ichnaea.auth.IchnaeaAuthenticationProvider;
import org.ichnaea.auth.IchnaeaSecurityContext;
import org.ichnaea.auth.IchnaeaSessionProvider;
import org.ichnaea.core.config.SecurityConfiguration;
import org.ichnaea.core.security.auth.AuthorizationManager;
import org.ichnaea.core.security.auth.SecurityContext;
import org.ichnaea.core.security.auth.SessionManager;
import org.ichnaea.core.security.crypto.BCryptPasswordEncoder;
import org.ichnaea.core.security.crypto.PasswordEncoder;
import org.ichnaea.service.UserService;

/**
 * A Security Configuration.
 */
@Getter
@Setter
public final class SecurityConfig extends SecurityConfiguration {

    /**
     * Obtains the used password-encoder.
     *
     * @return the used password-encoder
     */
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Generates an Authentication Provider.
     *
     * @return a Simple Authentication Provider with the current <code>PasswordEncoder</code> and
     * <code>UserDetailsService</code>
     */
    public static IchnaeaAuthenticationProvider authenticationProvider() {
        IchnaeaAuthenticationProvider authProvider = new IchnaeaAuthenticationProvider();
        authProvider.setPasswordEncoder(getPasswordEncoder());
        authProvider.setUserDetailsService(new UserService());
        return authProvider;
    }

    /**
     * Builds an Authorization Provider.
     *
     * @return a Simple <code>AuthorizationProvider</code>.
     */
    public static AuthorizationManager authorizationProvider() {
        return null;
    }

    public static SessionManager sessionProvider() {
        IchnaeaSessionProvider sessionProvider = new IchnaeaSessionProvider();
        sessionProvider.setAuthenticationManager(authenticationProvider());
        return sessionProvider;
    }

    @Override
    public void configure() {

        SecurityContext context =
                new IchnaeaSecurityContext(authenticationProvider(),
                        authorizationProvider(),
                        sessionProvider());

        setSecurityContext(context);
    }

}