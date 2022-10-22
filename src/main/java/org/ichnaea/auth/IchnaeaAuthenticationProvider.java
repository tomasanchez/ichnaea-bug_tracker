package org.ichnaea.auth;

import lombok.Getter;
import lombok.Setter;
import org.ichnaea.core.security.auth.Authentication;
import org.ichnaea.core.security.auth.AuthenticationProvider;
import org.ichnaea.core.security.auth.UsernamePasswordAuthenticationToken;
import org.ichnaea.core.security.user.UserDetails;

@Getter
@Setter
public class IchnaeaAuthenticationProvider extends AuthenticationProvider {

    @Override
    protected UserDetails retrieveUser(String username, Authentication authentication) {
        return getUserDetailsService().loadUserByUsername(username);
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication, UserDetails user) {
        String password = authentication.getCredentials().toString();
        String encodedPassword = getPasswordEncoder().encode(password);
        return new UsernamePasswordAuthenticationToken(user, user.getAuthorities(), encodedPassword,
                true);
    }

}