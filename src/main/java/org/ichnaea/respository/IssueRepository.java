package org.ichnaea.respository;

import org.ichnaea.model.Issue;
import org.ichnaea.model.IssueStatus;

import java.util.List;

public interface IssueRepository extends PersistentEntityRepository<Issue> {

    /**
     * List all issues for a given project.
     *
     * @param projectId the project id
     * @return the list of issues associated
     */
    List<Issue> findByProject(Long projectId);

    /**
     * Sets assignee to null
     *
     * @param issueId issue to be unassigned
     */
    void unAssign(Long issueId);

    /**
     * Updates an issue status to the given value.
     *
     * @param issueId primary key of an issue
     * @param status  to be set
     */
    void setStatus(Long issueId, IssueStatus status);
}
