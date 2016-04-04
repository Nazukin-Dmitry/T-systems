package com.tsystems.nazukin.logiweb.service.api;

import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.model.entity.MapEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderItemEntity;

import java.util.List;
import java.util.Map;

/**
 * Service that provides functionality for working with {@link MapEntity}, that representing map's data.
 */
public interface MapServiceApi {
    /**
     * Saves interval between cities.
     * @param id1 first city's id
     * @param id2 second city's id
     * @param interval interval between cities
     */
    void saveOrUpdate(Integer id1, Integer id2, Integer interval);

    /**
     * Deletes interval with specified id.
     *
     * @param id specified id
     */
    void delete(Integer id);

    /**
     * Finds all intervals with specified city.
     *
     * @param cityId citie's id
     * @return map of intervals
     */
    Map<CityEntity, MapEntity> getCityIntervals(Integer cityId);

    /**
     * Finds interval between two cities.
     *
     * @param cityId1 first city's id
     * @param cityId2  second city's id
     * @return interval
     */
    Integer getInterval(Integer cityId1, Integer cityId2);

    /**
     * Saves  interval
     *
     * @param idCityFrom first city's id
     * @param nameCityTo second city's id
     * @param interval interval
     * @return result of  saving
     */
    CityEntity save(Integer idCityFrom, String nameCityTo, Integer interval);

    /**
     * Counts duration of order's items.
     *
     * @param orderItems list of order's items
     * @return duration
     */
    Integer duration(List<OrderItemEntity> orderItems);
}
