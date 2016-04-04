package com.tsystems.nazukin.logiweb.rs.service;

import com.tsystems.nazukin.logiweb.rs.model.dao.interfaces.CargoDao;
import com.tsystems.nazukin.logiweb.rs.model.dao.interfaces.DriverDao;
import com.tsystems.nazukin.logiweb.rs.model.dao.interfaces.OrderDao;
import com.tsystems.nazukin.logiweb.rs.model.entity.*;
import com.tsystems.nazukin.logiweb.rs.model.enums.CargoStatus;
import com.tsystems.nazukin.logiweb.rs.model.enums.DriverStatus;
import com.tsystems.nazukin.logiweb.rs.model.enums.OrderStatus;

import javax.ejb.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1 on 23.03.2016.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DriverService implements DriverServiceApi {
    @EJB
    private DriverDao driverDao;
    @EJB
    private CargoDao cargoDao;
    @EJB
    private OrderDao orderDao;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean fromOrderToDrive(Integer serialNumber) {
        DriverEntity driverEntity = driverDao.findBySerialNumber(serialNumber);
        if (driverEntity.getStatus() != DriverStatus.ORDER) {
            return false;
        }
        OrderEntity orderEntity = driverEntity.getCurrentOrder();
        Set<DriverEntity> orderDrivers = orderEntity.getDrivers();
        for (DriverEntity driver : orderDrivers) {
            if (driver.getStatus() == DriverStatus.DRIVE) {
                driver.setStatus(DriverStatus.ORDER);
            }
        }
        driverEntity.setStatus(DriverStatus.DRIVE);
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean fromDriveToOrder(Integer serialNumber) {
        DriverEntity driverEntity = driverDao.findBySerialNumber(serialNumber);
        if (driverEntity.getStatus() != DriverStatus.DRIVE) {
            return false;
        }
        driverEntity.setStatus(DriverStatus.ORDER);
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean fromFreeToDrive(Integer serialNumber) {
        DriverEntity driverEntity = driverDao.findBySerialNumber(serialNumber);
        if (driverEntity.getStatus() != DriverStatus.FREE) {
            return false;
        }
        OrderEntity orderEntity = driverEntity.getCurrentOrder();
        orderEntity.setStatus(OrderStatus.IN_PROGRESS);
        Set<DriverEntity> orderDrivers = orderEntity.getDrivers();
        for (DriverEntity driver : orderDrivers) {
            if (driver.getSerialNumber() != serialNumber) {
                driver.setStatus(DriverStatus.ORDER);
            } else {
                driver.setStatus(DriverStatus.DRIVE);
            }
        }
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean fromFreeToOrder(Integer serialNumber) {
        DriverEntity driverEntity = driverDao.findBySerialNumber(serialNumber);
        if (driverEntity.getStatus() != DriverStatus.FREE) {
            return false;
        }
        OrderEntity orderEntity = driverEntity.getCurrentOrder();
        orderEntity.setStatus(OrderStatus.IN_PROGRESS);
        for (DriverEntity driver : orderEntity.getDrivers()) {
            driver.setStatus(DriverStatus.ORDER);
        }
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public DriverStatus getStatus(Integer serialNumber) {
        DriverEntity driverEntity = driverDao.findBySerialNumber(serialNumber);
        return driverEntity.getStatus();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean finishOrder(Integer serialNumber) {
        DriverEntity driverEntity = driverDao.findBySerialNumber(serialNumber);
        OrderEntity orderEntity = driverEntity.getCurrentOrder();
        Integer duration = orderEntity.getDuration();
        List<CargoEntity> cargos = cargoDao.findByOrderId(orderEntity.getId());
        for (CargoEntity cargoEntity : cargos) {
            if (cargoEntity.getStatus() != CargoStatus.DELIVERED) {
                return false;
            }
        }
        CityEntity lastCity = orderDao.findLastCity(orderEntity.getId());
        Set<DriverEntity> orderDrivers = orderEntity.getDrivers();
        TruckEntity truck = orderEntity.getTruck();

        for (DriverEntity driver : orderDrivers) {
            driver.setStatus(DriverStatus.FREE);
            driver.setCurrentCity(lastCity);
            driver.setCurrentOrder(null);
            driver.setWorkTime(countWorkTime(driver.getWorkTime(), orderEntity));
        }
        truck.setCurrentCity(lastCity);
        orderEntity.setStatus(OrderStatus.DONE);
        orderEntity.setTruck(null);
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean changeCargoStatus(Integer cargoId, CargoStatus cargoStatus) {
        CargoEntity cargoEntity = cargoDao.findById(CargoEntity.class, cargoId);
        cargoEntity.setStatus(cargoStatus);
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public OrderEntity getOrder(Integer serialNumber){
        DriverEntity driverEntity = driverDao.findBySerialNumber(serialNumber);
        OrderEntity order = driverEntity.getCurrentOrder();
        List<OrderItemEntity> items = orderDao.findEntitiesByOrderId(order.getId());
        order.setOrderItems(new LinkedHashSet<OrderItemEntity>(items));
        return order;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean checkSerial(Integer serialNumber){
        DriverEntity driverEntity = driverDao.findBySerialNumber(serialNumber);
        if (driverEntity == null){
            return false;
        }
        OrderEntity orderEntity = driverEntity.getCurrentOrder();
        if (orderEntity == null) {
            return false;
        }
        return true;
    }

    private Integer countWorkTime(Integer workTime, OrderEntity order) {
        Date startTime = order.getStartTime();
        Integer duration = order.getDuration();

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(startTime);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(startTime.getTime() + TimeUnit.HOURS.toMillis(duration));

        //if startTime of order is not in next month
        if (Calendar.getInstance().get(Calendar.MONTH) == calendarStart.get(Calendar.MONTH)) {
            //if end of order in the next month
            long diffHours = duration;
            if (calendarStart.get(Calendar.MONTH) != calendarEnd.get(Calendar.MONTH)) {
                //set time to start of neext month
                calendarStart.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMinimum(Calendar.DAY_OF_MONTH));
                calendarStart.set(Calendar.HOUR_OF_DAY, calendarEnd.getActualMinimum(Calendar.HOUR_OF_DAY));
                calendarStart.set(Calendar.MINUTE, calendarEnd.getActualMinimum(Calendar.MINUTE));
                calendarStart.set(Calendar.SECOND, calendarEnd.getActualMinimum(Calendar.SECOND));

                long diff = calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis();
                diffHours = TimeUnit.MILLISECONDS.toHours(diff);
                return (int) diffHours;
            } else {
                return ((int) diffHours + workTime);
            }
        } else {
            return duration + workTime;
        }
    }
}
