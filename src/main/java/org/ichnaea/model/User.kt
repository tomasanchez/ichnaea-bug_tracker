package org.ichnaea.model

data class User(
    val userName: String,
    val password: String,
    val roleId: Long = 1,
    val role: Role = Role(RoleName.USER),
    val profile: String? = null,
) : PersistentEntity() {

    fun toTableRow() = arrayOf(
        id,
        userName,
    )

    override fun toMap(): Map<String, Any> {
        return mapOf(
            "user_name" to userName,
            "role_id" to roleId,
            "password" to password,
        )
    }

}
