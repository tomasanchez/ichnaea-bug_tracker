package org.ichnaea.model

enum class RoleName {
    USER,
    ADMIN,
    TEST,
}

data class Role(
    val name: RoleName,
) : PersistentEntity() {
    override fun toMap(): Map<String, Any> = mapOf(
        "name" to name.name,
    )
}


data class Project(
    val name: String,
    var code: String =
        if (name.contains(" "))
            name.split(" ").joinToString("") { it[0].uppercase() }
        else name.uppercase().substring(0, 3),
    val description: String,
    val members: List<User> = listOf(),
    val issues: List<Issue> = listOf(),
) : PersistentEntity() {

    init {
        code = code.ifEmpty {
            if (name.contains(" "))
                name.split(" ").joinToString("") { it[0].uppercase() }
            else name.uppercase().substring(0, 3)
        }
    }

    fun toTableRow() = arrayOf(
        id,
        "$name ($code)",
    )

    override fun toMap(): Map<String, Any> = mapOf(
        "name" to name,
        "code" to code,
        "description" to description,
    )
}


enum class IssueStatus {
    TO_DO,
    BLOCKED,
    IN_PROGRESS,
    DONE,
}

data class Issue(
    val title: String,
    val description: String,
    val projectId: Long = -1L,
    val assigneeId: Long? = null,
    val assignee: User? = null,
    val status: IssueStatus = IssueStatus.TO_DO,
    val estimatedPoints: Int? = null,
    val realPoints: Int? = null,
) : PersistentEntity() {

    fun toTableRow() = arrayOf(
        id,
        title,
        assignee?.userName ?: "-",
        estimatedPoints ?: "-",
        realPoints ?: "-",
    )

    fun getStatusLabel() = this.status.name.replace("_", " ")

    override fun toMap(): Map<String, Any> {
        val mutableMap = mutableMapOf(
            "title" to title,
            "description" to description,
            "status" to status.name,
            "estimated_points" to estimatedPoints,
            "real_points" to realPoints,
            "user_id" to assigneeId,
        )

        @Suppress("UNCHECKED_CAST")
        return mutableMap.filter { it.value == null } as Map<String, Any>
    }

}