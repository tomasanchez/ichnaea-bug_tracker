package org.ichnaea.core.security.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.ichnaea.core.security.user.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

/**
 * A generic implementation of a username and password authentication token.
 */
@Data
@AllArgsConstructor
public class UsernamePasswordAuthenticationToken implements Authentication {

    private static final long serialVersionUID = 1L;

    private final Object principal;
    private final Collection<? extends GrantedAuthority> authorities;
    private Object credentials;
    private boolean isAuthenticated;

    /**
     * Safe constructor as the {@link #isAuthenticated()} will return <code>false</code>
     *
     * @param principal   the principal username
     * @param credentials the password credentials
     */
    public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
        authorities = Collections.emptyList();
    }


    @Override
    public String getName() {

        if (principal instanceof String) {
            return (String) principal;
        }

        if (this.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) this.getPrincipal()).getUsername();
        }

        if (this.getPrincipal() instanceof Principal) {
            return ((Principal) this.getPrincipal()).getName();
        }

        return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

}
