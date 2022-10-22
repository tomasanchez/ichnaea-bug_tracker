package org.ichnaea.respository;

import org.ichnaea.model.PersistentEntity;

import java.util.List;
import java.util.Optional;

public interface PersistentEntityRepository<T extends PersistentEntity> {

    /**
     * Creates a new entry for an entity.
     *
     * @param entity to be persisted
     * @return the persisted entity with a new id
     */
    T save(T entity);

    /**
     * Lists all entities.
     *
     * @return an entity list
     */
    List<T> findAll();

    /**
     * Removes an entity of the persistence unit.
     *
     * @param id unique identifier of the entity
     */
    void delete(Long id);

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id to be searched
     * @return the entity if found
     */
    Optional<T> findById(Long id);

    /**
     * Counts the number of persisted entities.
     *
     * @return how many entities are currently persisted
     */
    int count();
}
