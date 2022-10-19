package org.ichnaea.model

enum class RoleName {
    USER,
    ADMIN,
}

data class Role(
    val name: RoleName,
) : PersistentEntity()


data class Project(
    val name: String,
    val description: String,
    val owner: User,
    val members: List<User> = listOf(),
) : PersistentEntity() {
    fun toTableRow() = arrayOf(
        name,
        this.createdAt.toLocalDate().toString(),
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