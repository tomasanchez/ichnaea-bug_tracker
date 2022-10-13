package org.ichnaea.service

import org.ichnaea.core.security.auth.UserDetails
import org.ichnaea.core.security.auth.UserDetailsService
import org.ichnaea.core.security.crypto.BCryptPasswordEncoder
import org.ichnaea.core.security.crypto.PasswordEncoder
import org.ichnaea.model.User
import org.ichnaea.respository.UserRepository

class UserService(
    private val userRepository: UserRepository = UserRepository(),
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder(),
) : UserDetailsService {


    override fun loadUserByUsername(username: String): UserDetails? {
        return null;
    }

    fun save(user: User): User {
        val encodedUser = user.copy(password = passwordEncoder.encode(user.password))

        return userRepository.save(encodedUser)
    }

}