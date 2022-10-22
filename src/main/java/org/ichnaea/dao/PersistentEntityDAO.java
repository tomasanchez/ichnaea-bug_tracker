package org.ichnaea.dao;

import org.ichnaea.model.PersistentEntity;

import java.util.Optional;

public interface PersistentEntityDAO<T extends PersistentEntity> {

    /**
     * Creates a new entry for an entity.
     *
     * @param entity to be persisted
     */
    T save(T entity);

    /**
     * Lists all entities.
     *
     * @return an entity list
     */
    Iterable<T> findAll();

    /**
     * Removes an entity of the persistence unit.
     *
     * @param id unique identifier of the entity
     */
    void delete(long id);

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id to be searched
     * @return the entity with the given id
     */
    Optional<T> findById(long id);

    /**
     * Counts how many items are in the DAO.
     *
     * @return the number of entities
     */
    int count();
}
