package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.model.dao.implementations.CityDaoImpl;
import com.tsystems.nazukin.logiweb.model.dao.implementations.OrderDaoImpl;
import com.tsystems.nazukin.logiweb.model.dao.implementations.TruckDaoImpl;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.CityDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.OrderDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.TruckDao;
import com.tsystems.nazukin.logiweb.model.entity.TruckEntity;
import com.tsystems.nazukin.logiweb.model.enums.TruckState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by 1 on 02.04.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class TruckServiceTest {

    TruckEntity truck = new TruckEntity();
    private CityDao cityDao;
    private TruckDao truckDao;
    private OrderDao orderDao;
    private TruckService truckService;

    @Before
    public void init() throws Exception {
        cityDao = mock(CityDaoImpl.class);
        truckDao = mock(TruckDaoImpl.class);
        orderDao = mock(OrderDaoImpl.class);

        truckService = new TruckService(cityDao, truckDao, orderDao);

        truck.setCapacity(10000);
        truck.setDriverCount(3);
        truck.setState(TruckState.WORK);
        truck.setRegNumber("AB12345");
    }

    @Test
    public void testFindAllByCityId() throws Exception {
        List<TruckEntity> trucks = new ArrayList<>();
        trucks.add(truck);
        when(truckDao.findByCityId(anyInt())).thenReturn(trucks);
        assertEquals(trucks, truckService.findAllByCityId(3));
    }


    @Test
    public void testFind() throws Exception {
        when(truckDao.findById(TruckEntity.class, 1)).thenReturn(truck);
        assertEquals(truck, truckService.find(1));
    }

    @Test
    public void testFindAllForOrder() throws Exception {
        List<TruckEntity> trucks = new ArrayList<>();
        trucks.add(truck);
        when(truckDao.findForOrder(anyInt())).thenReturn(trucks);
        assertEquals(trucks, truckService.findAllForOrder(3));
    }

    @Test
    public void testFindAll() throws Exception {
        List<TruckEntity> trucks = new ArrayList<>();
        trucks.add(truck);
        when(truckDao.findAll()).thenReturn(trucks);
        assertEquals(trucks, truckService.findAll());
    }

    @Test
    public void testFindByOrderId() throws Exception {
        List<TruckEntity> trucks = new ArrayList<>();
        trucks.add(truck);
        when(truckDao.findByOrderId(anyInt())).thenReturn(truck);
        assertEquals(truck, truckService.findByOrderId(3));
    }
}