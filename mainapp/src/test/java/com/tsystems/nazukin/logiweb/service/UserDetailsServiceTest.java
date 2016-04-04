package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.model.dao.implementations.EmployeeDaoImpl;
import com.tsystems.nazukin.logiweb.model.dao.interfaces.EmployeeDao;
import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;
import com.tsystems.nazukin.logiweb.model.enums.EmployeeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by 1 on 04.04.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceTest {

    private EmployeeDao dao;

    private UserDetailsService service;

    @Before
    public void init() throws Exception {
        dao = mock(EmployeeDaoImpl.class);

        service = new UserDetailsService(dao);
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setLogin("vasya");
        employee.setEmployeeType(EmployeeType.MANAGER);
        when(dao.findByLogin("vasya")).thenReturn(employee);
        UserDetails details = service.loadUserByUsername("vasya");
        assertTrue(details.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER")));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameEx() throws Exception {
        when(dao.findByLogin("vasya")).thenReturn(null);
        UserDetails details = service.loadUserByUsername("vasya");
    }
}