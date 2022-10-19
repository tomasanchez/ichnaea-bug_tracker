package org.ichnaea.service

import org.ichnaea.model.Project
import org.ichnaea.respository.ProjectRepository

class ProjectService(
    private val projectRepository: ProjectRepository = ProjectRepository,
) {

    fun findAll(): List<Project> = projectRepository.findAll()

}