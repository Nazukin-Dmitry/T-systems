package com.tsystems.nazukin.logiweb.service.api;

import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;

import java.util.List;

/**
 * Created by 1 on 02.04.2016.
 */
public interface EmployeeServiceApi {
    List<EmployeeEntity> getAllNew();

    void newToManager(Integer id);

    void save(EmployeeEntity entity);
}
