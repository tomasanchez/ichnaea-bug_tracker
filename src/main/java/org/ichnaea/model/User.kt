package org.ichnaea.model

data class User(
    val userName: String,
    val password: String,
) : PersistentEntity()
