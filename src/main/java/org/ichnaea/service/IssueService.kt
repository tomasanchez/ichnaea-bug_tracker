package org.ichnaea.service

import org.ichnaea.model.Issue
import org.ichnaea.respository.IssueRepository
import org.ichnaea.respository.PersistentEntityRepository
import org.ichnaea.respository.dao.IssueDAORepository

class IssueService(
    repository: PersistentEntityRepository<Issue> = IssueDAORepository(),
) : TransactionalService<Issue>(repository) {

    fun findByProject(projectId: Long): List<Issue> {
        return (repository as IssueRepository).findByProject(projectId)
    }
}