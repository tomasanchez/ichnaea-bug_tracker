package org.ichnaea.respository.list;

import org.ichnaea.model.PersistentEntity;
import org.ichnaea.respository.PersistentEntityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class ListRepository<T extends PersistentEntity> implements PersistentEntityRepository<T> {

    private final List<T> list = new ArrayList<>();

    private long lastId = 0;

    /**
     * Creates a new entry for an entity.
     *
     * @param entity to be persisted
     * @return the persisted entity with a new id
     */
    public T save(T entity) {

        if (Objects.isNull(entity.getId())) {
            entity.setId(++lastId);
            list.add(entity);
            return entity;
        } else {
            return findById(entity.getId())
                    .map(e -> {
                        delete(e.getId());
                        list.add(entity);
                        return entity;
                    })
                    .orElseThrow(() -> new IllegalArgumentException("Entity not found"));
        }
    }

    /**
     * Lists all entities.
     *
     * @return an entity list
     */
    public List<T> findAll() {
        return list;
    }

    /**
     * Removes an entity of the persistence unit.
     *
     * @param id unique identifier of the entity
     */
    public void delete(Long id) {
        list.removeIf(entity -> Objects.equals(entity.getId(), id));
    }

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id to be searched
     * @return Maybe an entity
     */
    public Optional<T> findById(Long id) {
        return list.stream()
                .filter(entity -> Objects.equals(entity.getId(), id))
                .findFirst();
    }

    /**
     * Counts the number of persisted entities.
     *
     * @return how many entities are currently persisted
     */
    public int count() {
        return list.size();
    }

}
