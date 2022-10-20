package org.ichnaea.service

import org.ichnaea.model.Project
import org.ichnaea.respository.ProjectRepository
import java.util.*

class ProjectService(
    private val projectRepository: ProjectRepository = ProjectRepository,
) {

    fun findAll(): List<Project> = projectRepository.findAll()

    fun findById(id: Long): Optional<Project> = projectRepository.findById(id)

}