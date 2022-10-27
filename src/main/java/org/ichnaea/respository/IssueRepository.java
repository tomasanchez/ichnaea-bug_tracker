package org.ichnaea.respository;

import org.ichnaea.model.Issue;

import java.util.List;

public interface IssueRepository extends PersistentEntityRepository<Issue> {

    List<Issue> findByProject(Long projectId);
}
