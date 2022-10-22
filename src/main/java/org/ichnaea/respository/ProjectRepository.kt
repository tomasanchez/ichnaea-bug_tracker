package org.ichnaea.respository

import org.ichnaea.model.Project
import org.ichnaea.model.User

object ProjectRepository : ListRepository<Project>() {
    init {

        val owner = User("admin", "")

        save(
            Project(
                name = "Ichnaea",
                description = "It is a Bug Tracker Project which has many bugs! " +
                        "Ironic. It could save others from bugs, but not itself.",
                owner = owner,
            )
        )

        save(
            Project(
                name = "Code for Good",
                description = "Showcase your tech skills and work alongside a team, guided by our technologists," +
                        " to solve real-world problems for social good organizations.",
                owner = owner,
            )
        )

    }
}