package org.ichnaea.respository

import org.ichnaea.model.Project
import org.ichnaea.model.User

object ProjectRepository : CrudRepository<Project>() {
    init {
        save(
            Project(
                name = "Project 1",
                description = "Description 1",
                owner = User("admin", ""),
            )
        )
    }
}