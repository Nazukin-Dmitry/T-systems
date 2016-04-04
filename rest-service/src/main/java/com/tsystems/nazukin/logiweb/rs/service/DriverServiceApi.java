package com.tsystems.nazukin.logiweb.rs.service;

import com.tsystems.nazukin.logiweb.rs.model.entity.OrderEntity;
import com.tsystems.nazukin.logiweb.rs.model.enums.CargoStatus;
import com.tsystems.nazukin.logiweb.rs.model.enums.DriverStatus;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Created by 1 on 02.04.2016.
 */
public interface DriverServiceApi {
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    boolean fromOrderToDrive(Integer serialNumber);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    boolean fromDriveToOrder(Integer serialNumber);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    boolean fromFreeToDrive(Integer serialNumber);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    boolean fromFreeToOrder(Integer serialNumber);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    DriverStatus getStatus(Integer serialNumber);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    boolean finishOrder(Integer serialNumber);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    boolean changeCargoStatus(Integer cargoId, CargoStatus cargoStatus);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    OrderEntity getOrder(Integer serialNumber);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    boolean checkSerial(Integer serialNumber);
}
