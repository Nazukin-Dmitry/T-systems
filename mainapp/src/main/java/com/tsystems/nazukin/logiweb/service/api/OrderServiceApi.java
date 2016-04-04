package com.tsystems.nazukin.logiweb.service.api;

import com.tsystems.nazukin.logiweb.customexceptions.DriverAllreadyUsedException;
import com.tsystems.nazukin.logiweb.customexceptions.TruckAllreadyUsedException;
import com.tsystems.nazukin.logiweb.model.entity.CargoEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderItemEntity;

import java.util.List;

/**
 * Service that provides functionality for working with {@link OrderEntity}, that representing order's data.
 */
public interface OrderServiceApi {
    /**
     * Saves order, order items in order and cargos. Sets drivers with specified ids to the order.
     *
     * @param orderEntity order's data
     * @param driversIds  drivers ids
     * @return saved order
     * @throws TruckAllreadyUsedException  if order's truck has been used by other order yet
     * @throws DriverAllreadyUsedException if order's driver has been used by other order yet
     */
    OrderEntity save(OrderEntity orderEntity, String[] driversIds);

    /**
     * Finds all orders, which has start city with specified id.
     *
     * @param cityId city's id
     * @return list of orders with specified start city
     */
    List<OrderEntity> findByStartCityId(Integer cityId);

    /**
     * Finds all cargos in specified order.
     *
     * @param orderId order's id
     * @return list of cargos in specified order
     */
    List<CargoEntity> findCargosByOrderId(Integer orderId);

    /**
     * Finds all orders.
     *
     * @return list of all orders
     */
    List<OrderEntity> findAll();

    /**
     * Finds order's points.
     *
     * @param orderId order's id
     *
     * @return list of order's points
     */
    List<OrderItemEntity> findItemsByOrderId(Integer orderId);

    /**
     * Find order with specified id.
     *
     * @param orderId order's id
     *
     * @return order
     */
    OrderEntity findById(Integer orderId);

    /**
     * Delete order
     *
     * @param orderId order's id
     */
    void delete(Integer orderId);
}
