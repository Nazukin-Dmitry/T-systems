package com.tsystems.nazukin.logiweb.model.dao.implementations;

import com.tsystems.nazukin.logiweb.model.dao.interfaces.DriverDao;
import com.tsystems.nazukin.logiweb.model.entity.DriverEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by 1 on 17.02.2016.
 */
@Repository
public class DriverDaoImpl extends GenericDAOImpl<DriverEntity, Integer> implements DriverDao {
    @Override
    public Integer maxSerialNumber() {
        TypedQuery<Integer> query = em.createNamedQuery("DriverEntity.findMaxSerial", Integer.class);
        Integer result = query.getSingleResult();
        return result;
    }

    @Override
    public List<DriverEntity> findByCityId(Integer id) {
        TypedQuery<DriverEntity> query = em.createNamedQuery("DriverEntity.findByCityId", DriverEntity.class);
        query.setParameter("id", id);
        List<DriverEntity> results = query.getResultList();
        return results;
    }

    @Override
    public List<DriverEntity> findForOrder(Integer cityId) {
        TypedQuery<DriverEntity> query = em.createNamedQuery("DriverEntity.findForOrder", DriverEntity.class);
        query.setParameter("id", cityId);
        List<DriverEntity> results = query.getResultList();
        return results;
    }

    @Override
    public DriverEntity findByEmployeeId(Integer employeeId) {
        TypedQuery<DriverEntity> query = em.createNamedQuery("DriverEntity.findByEmployeeId", DriverEntity.class);
        query.setParameter("id", employeeId);
        List<DriverEntity> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Override
    public List<DriverEntity> findAll() {
        TypedQuery<DriverEntity> query = em.createNamedQuery("DriverEntity.findAll", DriverEntity.class);
        List<DriverEntity> results = query.getResultList();
        return results;
    }

    @Override
    public List<DriverEntity> findByOrderId(Integer orderId) {
        TypedQuery<DriverEntity> query = em.createNamedQuery("DriverEntity.findByOrderId", DriverEntity.class);
        query.setParameter("id", orderId);
        List<DriverEntity> results = query.getResultList();
        return results;
    }
}
