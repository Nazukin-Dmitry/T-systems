package com.tsystems.nazukin.logiweb.rs.model.dao.implementations;

import com.tsystems.nazukin.logiweb.rs.model.dao.interfaces.OrderDao;
import com.tsystems.nazukin.logiweb.rs.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.rs.model.entity.OrderEntity;
import com.tsystems.nazukin.logiweb.rs.model.entity.OrderItemEntity;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by 1 on 20.02.2016.
 */
@Stateless
public class OrderDaoImpl extends GenericDAOImpl<OrderEntity, Integer> implements OrderDao {

    @Override
    public List<OrderItemEntity> findEntitiesByOrderId(Integer orderId) {
        TypedQuery<OrderItemEntity> query = em.createNamedQuery("OrderItemEntity.findByOrderId", OrderItemEntity.class);
        query.setParameter("id", orderId);
        List<OrderItemEntity> results = query.getResultList();
        return results;
    }

    @Override
    public CityEntity findLastCity(Integer orderId) {
        TypedQuery<CityEntity> query = em.createNamedQuery("OrderItemEntity.findLastCity", CityEntity.class);
        query.setParameter("id", orderId);
        query.setMaxResults(1);
        CityEntity result = query.getResultList().get(0);
        return result;
    }
}
