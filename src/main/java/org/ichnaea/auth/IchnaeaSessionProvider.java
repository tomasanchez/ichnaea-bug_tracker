package org.ichnaea.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ichnaea.core.exception.AuthenticationException;
import org.ichnaea.core.exception.BadCredentialsException;
import org.ichnaea.core.security.auth.Authentication;
import org.ichnaea.core.security.auth.AuthenticationManager;
import org.ichnaea.core.security.auth.SessionManager;
import org.ichnaea.core.security.auth.UsernamePasswordAuthenticationToken;
import org.ichnaea.model.User;

import java.util.Objects;
import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IchnaeaSessionProvider implements SessionManager {

    User currentUser;

    AuthenticationManager authenticationManager;

    public void attemptAuthentication(String username, String password, Consumer<Authentication> onSuccess,
                                      Consumer<AuthenticationException> onFailure) {

        if (Objects.isNull(username) || Objects.isNull(password)) {
            throw new BadCredentialsException("No credentials were provided");
        }

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            onSuccess.accept(authenticationManager.authenticate(token));
        } catch (AuthenticationException e) {
            closeSession();
            onFailure.accept(e);
        }

    }

    public void closeSession() {
        setCurrentUser(null);
    }

    public User getSession() {
        return getCurrentUser();
    }

    public void setSession(User user) {
        setCurrentUser(user);
    }

}
