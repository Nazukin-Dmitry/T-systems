package com.tsystems.nazukin.logiweb.service.api;

import com.tsystems.nazukin.logiweb.model.entity.TruckEntity;

import java.util.List;

/**
 * Created by 1 on 02.04.2016.
 */
public interface TruckServiceApi {
    List<TruckEntity> findAllByCityId(Integer id);

    void saveOrUpdate(TruckEntity entity, String cityId);

    void delete(Integer id);

    TruckEntity find(Integer id);

    List<TruckEntity> findAllForOrder(Integer cityId);

    List<TruckEntity> findAll();

    TruckEntity findByOrderId(Integer orderId);
}
