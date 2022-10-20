package org.ichnaea.respository

import org.ichnaea.model.Project
import org.ichnaea.model.User

object ProjectRepository : CrudRepository<Project>() {
    init {

        save(
            Project(
                name = "Ichnaea",
                description = "Description 1",
                owner = User("admin", ""),
            )
        )

        save(
            Project(
                name = "Code for Good",
                description = "Description 1",
                owner = User("admin", ""),
            )
        )

    }
}