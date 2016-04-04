package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.model.dao.interfaces.CityDao;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.MapDao;
import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.model.entity.MapEntity;
import com.tsystems.nazukin.logiweb.model.entity.OrderItemEntity;
import com.tsystems.nazukin.logiweb.service.api.MapServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service that provides functionality for working with {@link MapEntity}, that representing map's data.
 */
@Service
public class MapService implements MapServiceApi {

    /**
     * Data access object for map data.
     */
    private MapDao mapDao;

    /**
     * Data access object for city's data.
     */
    private CityDao cityDao;


    /**
    * Constructs service.
     */
    @Autowired
    public MapService(MapDao mapDao, CityDao cityDao) {
        this.mapDao = mapDao;
        this.cityDao = cityDao;
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional
    public void saveOrUpdate(Integer id1, Integer id2, Integer interval) {
        MapEntity mapEntity = mapDao.findByIds(id1, id2);
        //if interval exists update it
        if (mapEntity != null) {
            //update interval
            mapEntity.setIntervalValue(interval);
        } else {
            //create interval
            CityEntity city1 = cityDao.findById(CityEntity.class, id1);
            CityEntity city2 = cityDao.findById(CityEntity.class, id2);
            mapEntity = new MapEntity();
            mapEntity.setIntervalValue(interval);
            mapEntity.setCity1(city1);
            mapEntity.setCity2(city2);
            mapDao.save(mapEntity);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        MapEntity entity = mapDao.findById(MapEntity.class, id);
        mapDao.delete(entity);
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional
    public Map<CityEntity, MapEntity> getCityIntervals(Integer cityId) {
        //list for first piece of MapEntities where our city on first position
        List<MapEntity> first;
        //list for first piece of MapEntities where our city on second position
        List<MapEntity> second;
        first = mapDao.findAllByCity1(cityId);
        second = mapDao.findAllByCity2(cityId);

        Map<CityEntity, MapEntity> result = new HashMap<>();

        for (MapEntity entity : first) {
            result.put(entity.getCity2(), entity);
        }
        for (MapEntity entity : second) {
            result.put(entity.getCity1(), entity);
        }

        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional
    public Integer getInterval(Integer cityId1, Integer cityId2) {
        MapEntity result;
        if (cityId1 == cityId2) {
            return 0;
        }
        result = mapDao.findByIds(cityId1, cityId2);
        if (result != null) {
            return result.getIntervalValue();
        } else {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional
    public CityEntity save(Integer idCityFrom, String nameCityTo, Integer interval) {
        CityEntity cityTo = new CityEntity();
        CityEntity city1 = cityDao.findById(CityEntity.class, idCityFrom);
        cityTo.setName(nameCityTo);
        cityTo = cityDao.merge(cityTo);

        MapEntity mapEntity = new MapEntity();
        mapEntity.setCity1(city1);
        mapEntity.setCity2(cityTo);
        mapEntity.setIntervalValue(interval);
        mapDao.merge(mapEntity);
        return cityTo;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Integer duration(List<OrderItemEntity> orderItems) {
        Integer duration = 0;
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItemEntity item = orderItems.get(i);
            switch (item.getType()) {
                //2 hour for loading or unloading
                case TRANSIT:
                    duration += 0;
                    break;
                case LOADING:
                    duration += 2;
                    break;
                case UNLOADING:
                    duration += 2;
                    break;
                default:
                    break;
            }
            if (i > 0) {
                duration += getInterval(item.getCity().getId(), orderItems.get(i - 1).getCity().getId());
            }
        }
        return duration;
    }
}
