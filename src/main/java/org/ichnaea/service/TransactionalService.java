package org.ichnaea.service;

import lombok.Getter;
import org.ichnaea.model.PersistentEntity;
import org.ichnaea.respository.PersistentEntityRepository;

import java.util.List;
import java.util.Optional;

@Getter
public abstract class TransactionalService<T extends PersistentEntity> {

    protected final PersistentEntityRepository<T> repository;

    public TransactionalService(PersistentEntityRepository<T> repository) {
        this.repository = repository;
    }

    /**
     * Does a transactional persistence of an entity.
     *
     * @param entity to be persisted
     * @return the persisted entity
     */
    public T save(T entity) {
        return repository.save(entity);
    }

    /**
     * Removes an entity from the persistence unit.
     *
     * @param id unique identifier of the entity
     */
    public void delete(Long id) {
        repository.delete(id);
    }

    /**
     * Lists all entities.
     *
     * @return an entity list
     */
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id to be searched
     * @return the found entity
     */
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

}
