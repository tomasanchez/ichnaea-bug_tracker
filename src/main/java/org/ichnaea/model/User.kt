package org.ichnaea.model

data class User(
    val userName: String,
    val password: String,
    val roleId: Long = 1,
    val role: Role = Role(RoleName.USER),
    val image: String? = null,
) : PersistentEntity() {

    fun toTableRow() = arrayOf(
        id,
        userName,
    )

    override fun toMap(): Map<String, Any> {
        val map = mapOf(
            "user_name" to userName,
            "role_id" to roleId,
            "password" to password,
        )

        image?.let { map.plus("image" to it) }

        return map
    }

}
