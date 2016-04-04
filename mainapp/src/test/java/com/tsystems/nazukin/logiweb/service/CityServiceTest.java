package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.model.dao.implementations.CityDaoImpl;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.CityDao;
import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by 1 on 03.04.2016.
 */
public class CityServiceTest {

    CityEntity city1 = new CityEntity();
    private CityDao cityDao;
    private CityService cityService;

    @Before
    public void init() throws Exception {
        cityDao = mock(CityDaoImpl.class);

        cityService = new CityService(cityDao);
        city1.setId(1);
        city1.setName("Moscow");
    }

    @Test
    public void testGetAll() throws Exception {
        List<CityEntity> cities = new ArrayList<>();
        cities.add(city1);
        when(cityDao.findAll()).thenReturn(cities);
        Assert.assertEquals(cities, cityService.getAll());
    }

    @Test
    public void testFind() throws Exception {
        when(cityDao.findById(CityEntity.class, 1)).thenReturn(city1);
        assertEquals(city1, cityService.find(1));
    }


}