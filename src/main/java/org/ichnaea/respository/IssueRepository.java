package org.ichnaea.respository;

import org.ichnaea.model.Issue;

import java.util.List;

public interface IssueRepository extends PersistentEntityRepository<Issue> {

    List<Issue> findByProject(Long projectId);

    /**
     * Sets assignee to null
     *
     * @param issueId issue to be unassigned
     */
    void unAssign(Long issueId);
}
