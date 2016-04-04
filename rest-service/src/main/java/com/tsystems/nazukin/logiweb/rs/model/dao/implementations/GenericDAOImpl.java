package com.tsystems.nazukin.logiweb.rs.model.dao.implementations;

import com.tsystems.nazukin.logiweb.rs.model.dao.interfaces.GenericDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;


/**
 * Created by 1 on 11.02.2016.
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    @PersistenceContext(unitName = "DataSource")
    protected EntityManager em;
    @Override
    public void save(T entity) {
        em.persist(entity);
    }
    @Override
    public T merge(T entity) {
        return em.merge(entity);
    }
    @Override
    public void delete(T entity) {
        em.remove(entity);
    }
    @Override
    public T findById(Class<T> clazz, ID id) {
        return em.find(clazz, id);
    }
}
