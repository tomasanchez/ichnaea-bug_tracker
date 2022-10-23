package org.ichnaea.respository.list

import org.ichnaea.model.Project

object ProjectListRepository : ListRepository<Project>() {
    init {

        save(
            Project(
                name = "Ichnaea",
                description = "It is a Bug Tracker Project which has many bugs! Ironic. It could save others from bugs, but not itself.",
            )
        )

        save(
            Project(
                name = "Code for Good",
                description = "Showcase your tech skills and work alongside a team, guided by our technologists," +
                        " to solve real-world problems for social good organizations.",
            )
        )

    }
}