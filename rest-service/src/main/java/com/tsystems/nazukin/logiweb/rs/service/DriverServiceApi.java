package com.tsystems.nazukin.logiweb.rs.service;

import com.tsystems.nazukin.logiweb.rs.model.entity.OrderEntity;
import com.tsystems.nazukin.logiweb.rs.model.enums.CargoStatus;
import com.tsystems.nazukin.logiweb.rs.model.enums.DriverStatus;

/**
 * Created by 1 on 02.04.2016.
 */
public interface DriverServiceApi {
    boolean fromOrderToDrive(Integer serialNumber);

    boolean fromDriveToOrder(Integer serialNumber);

    boolean fromFreeToDrive(Integer serialNumber);

    boolean fromFreeToOrder(Integer serialNumber);

    DriverStatus getStatus(Integer serialNumber);

    boolean finishOrder(Integer serialNumber);

    boolean changeCargoStatus(Integer cargoId, CargoStatus cargoStatus);

    OrderEntity getOrder(Integer serialNumber);

    boolean checkSerial(Integer serialNumber);
}
