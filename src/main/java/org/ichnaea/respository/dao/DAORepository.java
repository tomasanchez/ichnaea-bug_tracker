package org.ichnaea.respository.dao;

import org.ichnaea.dao.PersistentEntityDAO;
import org.ichnaea.model.PersistentEntity;
import org.ichnaea.respository.PersistentEntityRepository;

import java.util.List;
import java.util.Optional;

public abstract class DAORepository<T extends PersistentEntity> implements PersistentEntityRepository<T> {

    abstract protected PersistentEntityDAO<T> getDAO();

    @Override
    public T save(T entity) {
        return getDAO().save(entity);
    }

    @Override
    public List<T> findAll() {
        return getDAO().findAll();
    }

    @Override
    public void delete(Long id) {
        getDAO().delete(id);
    }

    @Override
    public Optional<T> findById(Long id) {
        return getDAO().findById(id);
    }

    @Override
    public int count() {
        return getDAO().count();
    }

}
