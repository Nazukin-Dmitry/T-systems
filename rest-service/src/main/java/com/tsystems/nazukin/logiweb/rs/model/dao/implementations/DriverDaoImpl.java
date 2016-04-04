package com.tsystems.nazukin.logiweb.rs.model.dao.implementations;

import com.tsystems.nazukin.logiweb.rs.model.dao.interfaces.DriverDao;
import com.tsystems.nazukin.logiweb.rs.model.entity.DriverEntity;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by 1 on 17.02.2016.
 */
@Stateless
public class DriverDaoImpl extends GenericDAOImpl<DriverEntity, Integer> implements DriverDao {

    @Override
    public DriverEntity findBySerialNumber(Integer serialNumber) {
        TypedQuery<DriverEntity> query = em.createNamedQuery("DriverEntity.findBySerialNumber", DriverEntity.class);
        query.setParameter("serialNumber", serialNumber);
        List<DriverEntity> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }
}
