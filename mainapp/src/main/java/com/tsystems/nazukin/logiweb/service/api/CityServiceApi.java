package com.tsystems.nazukin.logiweb.service.api;

import com.tsystems.nazukin.logiweb.model.entity.CityEntity;

import java.util.List;

/**
 * Service that provides functionality for working with {@link CityEntity}, that representing city's data.
 */
public interface CityServiceApi {
    /**
     * This method saves city, if there isn't such city in database, otherwise it updates city.
     *
     * @param entity CityEntity
     */
    void saveOrUpdate(CityEntity entity);

    /**
     * This method returns list of  cities.
     *
     * @return list of all cities
     */
    List<CityEntity> getAll();

    /**
     * This method finds city with specified id.
     *
     * @param id city's id
     * @return city
     */
    CityEntity find(Integer id);
}
