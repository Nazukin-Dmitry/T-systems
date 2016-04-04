package com.tsystems.nazukin.logiweb.model.dao.implementations;

import com.tsystems.nazukin.logiweb.model.dao.interfaces.CargoDao;
import com.tsystems.nazukin.logiweb.model.entity.CargoEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by 1 on 25.02.2016.
 */
@Repository
public class CargoDaoImpl extends GenericDAOImpl<CargoEntity, Integer> implements CargoDao {

    @Override
    public List<CargoEntity> findByOrderId(Integer orderId) {
        TypedQuery<CargoEntity> query = em.createNamedQuery("CargoEntity.findByOrderId", CargoEntity.class);
        query.setParameter("id", orderId);
        List<CargoEntity> results = query.getResultList();
        return results;
    }
}
