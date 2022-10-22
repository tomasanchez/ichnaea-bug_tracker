package org.ichnaea.service

import org.ichnaea.core.exception.UserAlreadyExistsException
import org.ichnaea.core.security.auth.UserDetails
import org.ichnaea.core.security.auth.UserDetailsService
import org.ichnaea.core.security.crypto.BCryptPasswordEncoder
import org.ichnaea.core.security.crypto.PasswordEncoder
import org.ichnaea.model.User
import org.ichnaea.respository.UserRepository
import org.ichnaea.respository.dao.UserDAORepository
import java.util.*

class UserService(
    private val userRepository: UserRepository = UserDAORepository(),
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder(),
) : TransactionalService<User>(userRepository), UserDetailsService {


    override fun loadUserByUsername(username: String): UserDetails? {
        return null;
    }

    /**
     * Persist a user with the given username.
     *
     * @param user the user data
     * @return the persisted user
     * @throws UserAlreadyExistsException when the username is taken.
     */
    override fun save(user: User): User {

        findByUsername(user.userName).ifPresent {
            throw UserAlreadyExistsException("Username '${it.userName}' is already in use.")
        }

        val secretPassword = passwordEncoder.encode(user.password)

        return super.save(
            user.copy(password = secretPassword)
        )
    }


    /**
     * Retrieves a user with the given id.
     *
     * @param username the unique username
     * @return the user or null if no user with the given username
     */
    private fun findByUsername(username: String): Optional<User> {
        return userRepository.findByUsername(username)
    }

}