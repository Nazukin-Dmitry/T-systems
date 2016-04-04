package com.tsystems.nazukin.logiweb.rs.model.dao.interfaces;

import com.tsystems.nazukin.logiweb.rs.model.entity.CargoEntity;

import java.util.List;

/**
 * Created by 1 on 25.02.2016.
 */
public interface CargoDao extends GenericDAO<CargoEntity, Integer> {
    List<CargoEntity> findByOrderId(Integer orderId);
}
