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
    val code: String =
        if (name.contains(" "))
            name.split(" ").joinToString("") { it[0].uppercase() }
        else name.uppercase().substring(0, 3),
    val description: String,
    val owner: User,
    val members: List<User> = listOf(),
    val issues: List<Issue> = listOf(),
) : PersistentEntity() {
    fun toTableRow() = arrayOf(
        id,
        "$name ($code)",
    )
}


enum class IssueStatus {
    OPEN,
    IN_PROGRESS,
    CLOSED,
}

data class Issue(
    val title: String,
    val description: String,
    val project: Project,
    val assignee: User?,
    val reporter: User,
    val status: IssueStatus = IssueStatus.OPEN,
) : PersistentEntity()