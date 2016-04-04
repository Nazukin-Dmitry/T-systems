package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.customexceptions.DriverAllreadyUsedException;
import com.tsystems.nazukin.logiweb.customexceptions.OrderIsInProgressException;
import com.tsystems.nazukin.logiweb.customexceptions.TruckAllreadyUsedException;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.CargoDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.DriverDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.OrderDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.OrderItemDao;
import com.tsystems.nazukin.logiweb.model.entity.CargoEntity;
import com.tsystems.nazukin.logiweb.model.entity.DriverEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderItemEntity;
import com.tsystems.nazukin.logiweb.model.enums.OrderStatus;
import com.tsystems.nazukin.logiweb.service.api.OrderServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service that provides functionality for working with {@link OrderEntity}, that representing order's data.
 */
@Service
@Transactional
public class OrderService implements OrderServiceApi {

    /**
     * Data access object for order's data.
     */
    private OrderDao orderDao;

    /**
     * Data access object for driver's data.
     */
    private DriverDao driverDao;

    /**
     * Data access object for cargo's data.
     */
    private CargoDao cargoDao;

    /**
     * Data access object for order item's data.
     */
    private OrderItemDao orderItemDao;

    /**
     * Constructs service.
     */
    @Autowired
    public OrderService(OrderDao orderDao, DriverDao driverDao, CargoDao cargoDao, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.driverDao = driverDao;
        this.cargoDao = cargoDao;
        this.orderItemDao = orderItemDao;
    }

    /**
     * @inheritDoc
     */
    @Override
    public OrderEntity save(OrderEntity orderEntity, String[] driversIds) {
        //if it is editing of order, so firstly we delete order and then save it
        if (orderEntity.getId() != null){
            Integer orderId =  orderEntity.getId();
            OrderEntity order = orderDao.findById(OrderEntity.class, orderId);
            order.setTruck(null);
            //if order started while we edited it
            if (order.getStatus() == OrderStatus.IN_PROGRESS){
                throw new OrderIsInProgressException();
            }
            List<DriverEntity> drivers = driverDao.findByOrderId(orderId);
            for (DriverEntity driverEntity : drivers){
                driverEntity.setCurrentOrder(null);
            }
            List<OrderItemEntity> items = orderDao.findEntitiesByOrderId(orderId);
            for(OrderItemEntity item : items){
                orderItemDao.delete(item);
            }
        }

        orderEntity.setStatus(OrderStatus.NEW);
        Set<OrderItemEntity> orderItems = orderEntity.getOrderItems();

        for (OrderItemEntity item : orderItems) {
            item.setOrder(orderEntity);
        }

        //check that truck isn't used
        OrderEntity entity = orderDao.findByTruckId(orderEntity.getTruck().getId());
        if (entity != null) {
            throw new TruckAllreadyUsedException();
        }
        //add drivers
        Set<DriverEntity> drivers = new HashSet<>();
        for (int i = 0; i < driversIds.length; i++) {
            Integer driverId = Integer.parseInt(driversIds[i]);
            DriverEntity driverEntity = driverDao.findById(DriverEntity.class, driverId);
            ///if driver is selected yet
            if (driverEntity.getCurrentOrder() != null) {
                throw new DriverAllreadyUsedException();
            }
            driverEntity.setCurrentOrder(orderEntity);
            drivers.add(driverEntity);
        }
        orderEntity.setDrivers(drivers);
        OrderEntity order = orderDao.merge(orderEntity);
        return order;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<OrderEntity> findByStartCityId(Integer cityId) {
        List<OrderEntity> result;
        result = orderDao.findByStartCityId(cityId);
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<CargoEntity> findCargosByOrderId(Integer orderId) {
        List<CargoEntity> result;
        result = cargoDao.findByOrderId(orderId);
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<OrderEntity> findAll() {
        List<OrderEntity> result;
        result = orderDao.findAll();
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<OrderItemEntity> findItemsByOrderId(Integer orderId){
        List<OrderItemEntity> result = orderDao.findEntitiesByOrderId(orderId);
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public OrderEntity findById(Integer orderId){
        return orderDao.findById(OrderEntity.class, orderId);
    }


    /**
     * @inheritDoc
     */
    @Override
    public void delete(Integer orderId){
        OrderEntity order = orderDao.findById(OrderEntity.class, orderId);
        if (order.getStatus() == OrderStatus.IN_PROGRESS){
            throw new OrderIsInProgressException();
        }
        List<DriverEntity> drivers = driverDao.findByOrderId(orderId);
        for (DriverEntity driverEntity : drivers){
            driverEntity.setCurrentOrder(null);
        }
        orderDao.delete(order);
    }


}
