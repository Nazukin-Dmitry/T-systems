package com.tsystems.nazukin.logiweb.service.api;

import com.tsystems.nazukin.logiweb.model.entity.CityEntity;

import java.util.List;

/**
 * Created by 1 on 02.04.2016.
 */
public interface CityServiceApi {
    void saveOrUpdate(CityEntity entity);

    List<CityEntity> getAll();

    CityEntity find(Integer id);
}
