package org.ichnaea.service

import org.ichnaea.core.security.crypto.BCryptPasswordEncoder
import org.ichnaea.core.security.crypto.PasswordEncoder
import org.ichnaea.model.User
import org.ichnaea.respository.UserRepository

class UserDetailsService(
    private val userRepository: UserRepository = UserRepository(),
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder(),
) {

    fun loadUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun save(user: User): User {
        val encodedUser = user.copy(password = passwordEncoder.encode(user.password))

        return userRepository.save(encodedUser)
    }

}