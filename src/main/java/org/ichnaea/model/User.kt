package org.ichnaea.model

data class User(
    val userName: String,
    val password: String,
    val role: Role = Role(RoleName.USER),
    val profile: String? = null,
) : PersistentEntity() {

    fun toTableRow() = arrayOf(
        id,
        userName,
    )
}
