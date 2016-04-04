package com.tsystems.nazukin.logiweb.rs.model.dao.implementations;

import com.tsystems.nazukin.logiweb.rs.model.dao.interfaces.CargoDao;
import com.tsystems.nazukin.logiweb.rs.model.entity.CargoEntity;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by 1 on 25.02.2016.
 */
@Stateless
public class CargoDaoImpl extends GenericDAOImpl<CargoEntity, Integer> implements CargoDao {

    @Override
    public List<CargoEntity> findByOrderId(Integer orderId) {
        TypedQuery<CargoEntity> query = em.createNamedQuery("CargoEntity.findByOrderId", CargoEntity.class);
        query.setParameter("id", orderId);
        List<CargoEntity> results = query.getResultList();
        return results;
    }
}
