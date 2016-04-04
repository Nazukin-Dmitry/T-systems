package com.tsystems.nazukin.logiweb.service.api;

import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.model.entity.MapEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderItemEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 02.04.2016.
 */
public interface MapServiceApi {
    void saveOrUpdate(Integer id1, Integer id2, Integer interval);

    void delete(Integer id);

    Map<CityEntity, MapEntity> getCityIntervals(Integer cityId);

    Integer getInterval(Integer cityId1, Integer cityId2);

    CityEntity save(Integer idCityFrom, String nameCityTo, Integer interval);

    Integer duration(List<OrderItemEntity> orderItems);
}
