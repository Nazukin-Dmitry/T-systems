package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.model.dao.interfaces.CityDao;
import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.service.api.CityServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service that provides functionality for working with {@link CityEntity}, that representing city's data.
 */
@Service
@Transactional
public class CityService implements CityServiceApi {

    /**
     * Data access object for city's data.
     */
    private CityDao cityDao;

    /**
     * Constructs service.
     */
    @Autowired
    public CityService(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    /**
     * @inheriDoc
     */
    @Override
    public void saveOrUpdate(final CityEntity entity) {
        cityDao.merge(entity);
    }

    /**
     * @inheriDoc
     */
    @Override
    public List<CityEntity> getAll() {
        List<CityEntity> result;
        result = cityDao.findAll();
        return result;
    }

    /**
     * @inheriDoc
     */
    @Override
    public CityEntity find(Integer id) {
        CityEntity result;
        result = cityDao.findById(CityEntity.class, id);
        return result;
    }
}
