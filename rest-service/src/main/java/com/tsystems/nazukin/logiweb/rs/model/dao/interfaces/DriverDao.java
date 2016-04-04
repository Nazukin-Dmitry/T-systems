package com.tsystems.nazukin.logiweb.rs.model.dao.interfaces;

import com.tsystems.nazukin.logiweb.rs.model.entity.DriverEntity;

/**
 * Created by 1 on 17.02.2016.
 */
public interface DriverDao extends GenericDAO<DriverEntity, Integer> {

    DriverEntity findBySerialNumber(Integer serialNumber);
}
