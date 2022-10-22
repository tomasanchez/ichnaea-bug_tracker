package org.ichnaea.respository;

import org.ichnaea.model.User;

import java.util.Optional;

public interface UserRepository extends PersistentEntityRepository<User> {

    Optional<User> findByUsername(String username);
}
