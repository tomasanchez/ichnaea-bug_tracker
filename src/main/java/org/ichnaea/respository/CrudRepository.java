package org.ichnaea.respository;

import org.ichnaea.model.PersistentEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class CrudRepository<T extends PersistentEntity> {

    private final List<T> list = new ArrayList<>();

    private long lastId = 0;

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

    public List<T> findAll() {
        return list;
    }

    public void delete(Long id) {
        list.removeIf(entity -> Objects.equals(entity.getId(), id));
    }

    public Optional<T> findById(Long id) {
        return list.stream()
                .filter(entity -> Objects.equals(entity.getId(), id))
                .findFirst();
    }

}
