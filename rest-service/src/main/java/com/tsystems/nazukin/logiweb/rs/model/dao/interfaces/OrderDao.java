package com.tsystems.nazukin.logiweb.rs.model.dao.interfaces;

import com.tsystems.nazukin.logiweb.rs.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.rs.model.entity.OrderEntity;
import com.tsystems.nazukin.logiweb.rs.model.entity.OrderItemEntity;

import java.util.List;

/**
 * Created by 1 on 20.02.2016.
 */
public interface OrderDao extends GenericDAO<OrderEntity, Integer> {

    List<OrderItemEntity> findEntitiesByOrderId(Integer orderId);

    CityEntity findLastCity(Integer orderId);

}
