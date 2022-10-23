package org.ichnaea.respository;

import org.ichnaea.model.Project;
import org.ichnaea.model.User;

import java.util.List;

public interface ProjectRepository extends PersistentEntityRepository<Project> {

    List<User> findMembers(Long id);

    List<Project> findProjects(Long id);

    void addMember(Long id, Long userId);

    void removeMember(Long id, Long userId);
}

