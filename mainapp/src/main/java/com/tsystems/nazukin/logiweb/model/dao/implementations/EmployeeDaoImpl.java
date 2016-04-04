package com.tsystems.nazukin.logiweb.model.dao.implementations;

import com.tsystems.nazukin.logiweb.model.dao.interfaces.EmployeeDao;
import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;
import com.tsystems.nazukin.logiweb.model.enums.EmployeeType;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by 1 on 06.02.2016.
 */
@Repository
public class EmployeeDaoImpl extends GenericDAOImpl<EmployeeEntity, Integer> implements EmployeeDao {

    @Override
    public List<EmployeeEntity> findAll() {
        TypedQuery<EmployeeEntity> query = em.createNamedQuery("EmployeeEntity.findAll", EmployeeEntity.class);
        List<EmployeeEntity> results = query.getResultList();
        return results;
    }

    @Override
    public EmployeeEntity findByLoginAndPassword(String login, String password) {
        TypedQuery<EmployeeEntity> query = em.createNamedQuery("EmployeeEntity.findByLoginAndPassword",
                EmployeeEntity.class);
        query.setParameter("login", login);
        query.setParameter("password", password);
        List<EmployeeEntity> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Override
    public EmployeeEntity findByLogin(String login) {
        TypedQuery<EmployeeEntity> query = em.createNamedQuery("EmployeeEntity.findByLogin",
                EmployeeEntity.class);
        query.setParameter("login", login);
        List<EmployeeEntity> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Override
    public List<EmployeeEntity> findAllNew() {
        TypedQuery<EmployeeEntity> query = em.createNamedQuery("EmployeeEntity.findAllNew", EmployeeEntity.class);
        query.setParameter("type", EmployeeType.NEW);
        List<EmployeeEntity> results = query.getResultList();
        return results;
    }
}
