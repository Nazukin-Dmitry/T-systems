package com.tsystems.nazukin.logiweb.service.api;

import com.tsystems.nazukin.logiweb.customexceptions.DriverAllreadyUsedException;
import com.tsystems.nazukin.logiweb.customexceptions.WrongSerialNumberException;
import com.tsystems.nazukin.logiweb.model.entity.DriverEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderEntity;

import java.util.List;

/**
 * Service that provides functionality for working with {@link DriverEntity}, that representing driver's data.
 */
public interface DriverServiceApi {

    /**
     * Make and saves driver from specified employee. Sets driver's work time to 0, status to 'FREE'.
     * Generates driver's serial number.
     *
     * @param employeeId employee's id
     * @param cityId     city's id
     */
    void save(Integer employeeId, Integer cityId);

    /**
     * Finds all drivers from specified city.
     *
     * @param id city's id
     * @return a list of drivers from specified city
     */
    List<DriverEntity> findAllByCityId(Integer id);

    /**
     * Finds {@link DriverEntity} with specified id.
     *
     * @param id driver's  id
     * @return driver with specified id
     */
    DriverEntity find(Integer id);

    /**
     * Updates the specified driver.
     *
     * @param id         driver's id
     * @param firstName  driver's new first name
     * @param secondName driver's new second name
     * @param workTime   driver's new work time
     * @param cityId     driver's new city
     */
    void update(String id, String firstName, String secondName, String workTime, String cityId);

    /**
     * Deletes the specified driver.
     *
     * @param id driver's id
     * @throws DriverAllreadyUsedException if we want to delete driver, which used in order
     */
    void delete(Integer id);

    /**
     * Selects drivers for the order. The driver does not perform other orders at a the time
     * and working hours should not exceed 176 hours at the end of the order.
     *
     * @param orderEntity order's data
     * @return a list of suitable drivers for order
     */
    List<DriverEntity> findForOrder(OrderEntity orderEntity);

    /**
     * Finds driver with specified emplooyee's id and checks, that correct serial number has been entered.
     *
     * @param employeeId   employee's id
     * @param serialNumber driver's serial number
     * @return driver's data, if serial number was correct
     * @throws WrongSerialNumberException if serial number was wrong
     */
    DriverEntity findByEmployeeIdAndSerial(Integer employeeId, Integer serialNumber);

    /**
     * Finds all drivers.
     *
     * @return a list of drivers
     */
    List<DriverEntity> findAll();

    /**
     * Finds drivers with specified order.
     *
     * @param orderId   order's id
     *
     * @return a list of drivers
     */
    List<DriverEntity> findByOrderId(Integer orderId);
}
