package org.ichnaea.core.security.auth;

import java.io.Serializable;

/**
 * A representation of an authority given to a user.
 */
public interface GrantedAuthority extends Serializable {

    /**
     * An authority must be represented by a string.
     *
     * @return the granted authority
     */
    String getAuthority();
}