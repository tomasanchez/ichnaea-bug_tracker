package org.ichnaea.model

data class User(
    val userName: String,
    val password: String,
    val role: Role = Role(RoleName.USER),
) : PersistentEntity() {
    fun toTableRow() = arrayOf(
        id,
        userName,
    )
}
