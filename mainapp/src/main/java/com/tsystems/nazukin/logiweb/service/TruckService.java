package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.customexceptions.TruckAllreadyUsedException;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.CityDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.OrderDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.TruckDao;
import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderEntity;
import com.tsystems.nazukin.logiweb.model.entity.TruckEntity;
import com.tsystems.nazukin.logiweb.service.api.TruckServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 1 on 15.02.2016.
 */
@Service
@Transactional
public class TruckService implements TruckServiceApi {

    private CityDao cityDao;
    private TruckDao truckDao;
    private OrderDao orderDao;

    @Autowired
    public TruckService(CityDao cityDao, TruckDao truckDao, OrderDao orderDao) {
        this.cityDao = cityDao;
        this.truckDao = truckDao;
        this.orderDao = orderDao;
    }

    @Override
    public List<TruckEntity> findAllByCityId(Integer id) {
        List<TruckEntity> result;
        result = truckDao.findByCityId(id);
        return result;
    }

    @Override
    public void saveOrUpdate(TruckEntity entity, String cityId) {
        entity.setCurrentCity(cityDao.findById(CityEntity.class, Integer.parseInt(cityId)));
        truckDao.merge(entity);
    }

    @Override
    public void delete(Integer id) {
        TruckEntity entity = truckDao.findById(TruckEntity.class, id);
        ///check that truck isn't used now
        OrderEntity orderEntity = orderDao.findByTruckId(entity.getId());
        if (orderEntity != null) {
            throw new TruckAllreadyUsedException();
        }
        truckDao.delete(entity);
    }

    @Override
    public TruckEntity find(Integer id) {
        TruckEntity entity;
        entity = truckDao.findById(TruckEntity.class, id);
        return entity;
    }

    @Override
    public List<TruckEntity> findAllForOrder(Integer cityId) {
        List<TruckEntity> result;
        result = truckDao.findForOrder(cityId);
        return result;
    }

    @Override
    public List<TruckEntity> findAll() {
        List<TruckEntity> result;
        result = truckDao.findAll();
        return result;
    }

    @Override
    public TruckEntity findByOrderId(Integer orderId) {
        return truckDao.findByOrderId(orderId);
    }

}
