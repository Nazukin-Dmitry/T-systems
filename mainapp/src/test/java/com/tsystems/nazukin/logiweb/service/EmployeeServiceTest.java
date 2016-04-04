package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.model.dao.implementations.EmployeeDaoImpl;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.EmployeeDao;
import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;
import com.tsystems.nazukin.logiweb.model.enums.EmployeeType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by 1 on 02.04.2016.
 */
public class EmployeeServiceTest {

    EmployeeEntity employee = new EmployeeEntity();
    private EmployeeDao employeeDao;
    private EmployeeService employeeService;

    @Before
    public void init() throws Exception {
        employeeDao = mock(EmployeeDaoImpl.class);
        employeeService = new EmployeeService(employeeDao);
        employee.setFirstName("Vasya");
        employee.setSecondName("Vasin");
        employee.setEmployeeType(EmployeeType.NEW);
        employee.setLogin("vasya");
        employee.setPassword("vasya");
        employee.setId(1);
    }

    @Test
    public void testGetAllNew() throws Exception {
        List<EmployeeEntity> employees = new ArrayList<>();
        employees.add(employee);
        when(employeeDao.findAllNew()).thenReturn(employees);
        assertEquals(employees, employeeService.getAllNew());
    }

    @Test
    public void testNewToManager() throws Exception {
        when(employeeDao.findById(EmployeeEntity.class, 1)).thenReturn(employee);
        employeeService.newToManager(1);
        assertEquals(EmployeeType.MANAGER, employee.getEmployeeType());
    }

}