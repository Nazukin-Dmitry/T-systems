package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.customexceptions.UserAllreadyExistException;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.EmployeeDao;
import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;
import com.tsystems.nazukin.logiweb.model.enums.EmployeeType;
import com.tsystems.nazukin.logiweb.service.api.EmployeeServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 1 on 06.02.2016.
 */
@Service
@Transactional
public class EmployeeService implements EmployeeServiceApi {

    private EmployeeDao employeeDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public List<EmployeeEntity> getAllNew() {
        List<EmployeeEntity> result;
        result = employeeDao.findAllNew();
        return result;
    }

    @Override
    public void newToManager(Integer id) {
        EmployeeEntity entity;
        entity = employeeDao.findById(EmployeeEntity.class, id);
        entity.setEmployeeType(EmployeeType.MANAGER);
    }

    @Override
    public void save(EmployeeEntity entity) {
        EmployeeEntity userWithSameLogin = employeeDao.findByLogin(entity.getLogin());
        if (userWithSameLogin != null) {
            throw new UserAllreadyExistException("This login has already used");
        }
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        employeeDao.save(entity);
    }


}
