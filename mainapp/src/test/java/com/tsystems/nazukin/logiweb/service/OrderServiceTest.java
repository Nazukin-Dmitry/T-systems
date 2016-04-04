package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.customexceptions.DriverAllreadyUsedException;
import com.tsystems.nazukin.logiweb.customexceptions.OrderIsInProgressException;
import com.tsystems.nazukin.logiweb.customexceptions.TruckAllreadyUsedException;
import com.tsystems.nazukin.logiweb.model.dao.implementations.CargoDaoImpl;
import com.tsystems.nazukin.logiweb.model.dao.implementations.DriverDaoImpl;
import com.tsystems.nazukin.logiweb.model.dao.implementations.OrderDaoImpl;
import com.tsystems.nazukin.logiweb.model.dao.implementations.OrderItemDaoImpl;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.CargoDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.DriverDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.OrderDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.OrderItemDao;
import com.tsystems.nazukin.logiweb.model.entity.*;
import com.tsystems.nazukin.logiweb.model.enums.OrderStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by 1 on 01.03.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    private OrderDao orderDao;
    private DriverDao driverDao;
    private CargoDao cargoDao;
    private OrderItemDao orderItemDao;

    private OrderService orderService;

    @Before
    public void init() throws Exception {
        orderDao = mock(OrderDaoImpl.class);
        driverDao = mock(DriverDaoImpl.class);
        cargoDao = mock(CargoDaoImpl.class);
        orderItemDao = mock(OrderItemDaoImpl.class);

        orderService = new OrderService(orderDao, driverDao, cargoDao, orderItemDao);
    }

    @Test(expected = TruckAllreadyUsedException.class)
    public void testSaveExceptionTruck() throws Exception {
        OrderEntity orderEntityWithTruck = new OrderEntity();
        when(orderDao.findByTruckId(Mockito.anyInt())).thenReturn(orderEntityWithTruck);
        when(orderDao.merge(Mockito.any(OrderEntity.class))).thenReturn(new OrderEntity());

        OrderEntity orderEntityToSave = new OrderEntity();
        orderEntityToSave.setOrderItems(new HashSet<>());
        TruckEntity truckEntity = new TruckEntity();
        truckEntity.setId(1);
        orderEntityToSave.setTruck(truckEntity);
        orderService.save(orderEntityToSave, new String[]{"1", "2"});
    }

    @Test(expected = DriverAllreadyUsedException.class)
    public void testSaveExceptionDriver() throws Exception {
        when(orderDao.findByTruckId(Mockito.anyInt())).thenReturn(null);

        DriverEntity driverEntity = new DriverEntity();
        driverEntity.setCurrentOrder(new OrderEntity());
        when(driverDao.findById(DriverEntity.class, 1)).thenReturn(driverEntity);
        when(orderDao.merge(Mockito.any(OrderEntity.class))).thenReturn(new OrderEntity());

        OrderEntity orderEntityToSave = new OrderEntity();
        orderEntityToSave.setOrderItems(new HashSet<>());
        TruckEntity truckEntity = new TruckEntity();
        truckEntity.setId(1);
        orderEntityToSave.setTruck(truckEntity);

        orderService.save(orderEntityToSave, new String[]{"1", "2"});
    }


    @Test
    public void testFindAll() throws Exception {
        List<OrderEntity> orders = new ArrayList<>();
        OrderEntity order1 = new OrderEntity();
        OrderEntity order2 = new OrderEntity();
        orders.add(order1);
        orders.add(order2);
        when(orderDao.findAll()).thenReturn(orders);
        assertEquals(orders, orderService.findAll());

    }

    @Test
    public void testFindById() throws Exception {
        OrderEntity order = new OrderEntity();
        order.setId(1);
        when(orderDao.findById(OrderEntity.class, 1)).thenReturn(order);
        assertEquals(order, orderService.findById(1));
    }


    @Test(expected = OrderIsInProgressException.class)
    public void testSaveInProgress() throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1);
        orderEntity.setStatus(OrderStatus.IN_PROGRESS);
        when(orderDao.findById(OrderEntity.class, 1)).thenReturn(orderEntity);
        orderService.save(orderEntity, new String[]{"1","2"});
    }

    @Test
    public void testFindCargosByOrderId() throws Exception {
        CargoEntity cargo1 = new CargoEntity();
        cargo1.setName("Apples");
        CargoEntity cargo2 = new CargoEntity();
        cargo1.setName("Oranges");
        List<CargoEntity> cargos = new ArrayList<>();
        cargos.add(cargo1);
        cargos.add(cargo2);
        when(cargoDao.findByOrderId(1)).thenReturn(cargos);
        assertEquals(cargos, orderService.findCargosByOrderId(1));
    }

    @Test
    public void testFindItemsByOrderId() throws Exception {
        OrderItemEntity item = new OrderItemEntity();
        List<OrderItemEntity> items = new ArrayList<>();
        items.add(item);
        when(orderDao.findEntitiesByOrderId(1)).thenReturn(items);
        assertEquals(items, orderService.findItemsByOrderId(1));
    }
}