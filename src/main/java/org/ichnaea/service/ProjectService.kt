package org.ichnaea.service

import org.ichnaea.model.Project
import org.ichnaea.respository.ProjectRepository

class ProjectService(
    projectRepository: ProjectRepository = ProjectRepository,
) : TransactionalService<Project>(projectRepository)
