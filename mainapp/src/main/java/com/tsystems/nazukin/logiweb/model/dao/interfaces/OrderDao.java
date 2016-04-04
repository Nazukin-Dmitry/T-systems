package com.tsystems.nazukin.logiweb.model.dao.interfaces;

import com.tsystems.nazukin.logiweb.model.entity.OrderEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderItemEntity;

import java.util.List;

/**
 * Created by 1 on 20.02.2016.
 */
public interface OrderDao extends GenericDAO<OrderEntity, Integer> {
    OrderEntity findByTruckId(Integer truckId);

    List<OrderEntity> findByCityIdWithoutDrivers(Integer cityId);

    List<OrderEntity> findByStartCityId(Integer cityId);

    List<OrderEntity> findAll();

    List<OrderItemEntity> findEntitiesByOrderId(Integer orderId);



}
