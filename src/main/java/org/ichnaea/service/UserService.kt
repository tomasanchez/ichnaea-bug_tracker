package org.ichnaea.service

import org.ichnaea.core.exception.EntityNotFoundException
import org.ichnaea.core.exception.UserAlreadyExistsException
import org.ichnaea.core.security.auth.GrantedAuthority
import org.ichnaea.core.security.auth.SimpleGrantedAuthority
import org.ichnaea.core.security.crypto.BCryptPasswordEncoder
import org.ichnaea.core.security.crypto.PasswordEncoder
import org.ichnaea.core.security.user.AuthUser
import org.ichnaea.core.security.user.UserDetails
import org.ichnaea.core.security.user.UserDetailsService
import org.ichnaea.model.User
import org.ichnaea.respository.UserRepository
import org.ichnaea.respository.dao.UserDAORepository
import java.util.*

class UserService(
    private val userRepository: UserRepository = UserDAORepository(),
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder(),
) : TransactionalService<User>(userRepository), UserDetailsService {
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
    fun findByUsername(username: String): Optional<User> {
        return userRepository.findByUsername(username)
    }

    override fun loadUserByUsername(username: String): UserDetails {

        val user: User =
            try {
                userRepository.findByUsername(username).get()
            } catch (e: Exception) {
                throw EntityNotFoundException("User not found")
            }

        val enabled = true;
        val accountNonExpired = true;
        val credentialsNonExpired = true;
        val accountNonLocked = true;

        return AuthUser(
            user.userName,
            user.password,
            enabled,
            accountNonExpired,
            credentialsNonExpired,
            accountNonLocked,
            getAuthorities(user)
        )
    }

    /**
     * Retrieves an user privileges.
     *
     * @param user to be checked
     * @return a list of privileges (granted authorities)
     */
    private fun getAuthorities(user: User): List<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(user.role.toString()))
    }

}