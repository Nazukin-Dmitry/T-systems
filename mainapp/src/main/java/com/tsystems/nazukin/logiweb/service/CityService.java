package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.model.dao.interfaces.CityDao;
import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.service.api.CityServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 1 on 14.02.2016.
 */
@Service
@Transactional
public class CityService implements CityServiceApi {

    private CityDao cityDao;

    @Autowired
    public CityService(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    public void saveOrUpdate(final CityEntity entity) {
        cityDao.merge(entity);
    }

    @Override
    public List<CityEntity> getAll() {
        List<CityEntity> result;
        result = cityDao.findAll();
        return result;
    }

    @Override
    public CityEntity find(Integer id) {
        CityEntity result;
        result = cityDao.findById(CityEntity.class, id);
        return result;
    }
}
