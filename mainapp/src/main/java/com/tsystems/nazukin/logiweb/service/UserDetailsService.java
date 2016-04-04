package com.tsystems.nazukin.logiweb.service;

import com.tsystems.nazukin.logiweb.model.dao.interfaces.EmployeeDao;
import com.tsystems.nazukin.logiweb.model.entity.CustomUserDetails;
import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 1 on 17.03.2016.
 */
@Service
@Transactional
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private EmployeeDao dao;

    @Autowired
    public UserDetailsService(EmployeeDao dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        EmployeeEntity entity = dao.findByLogin(login);
        if (entity == null){
            throw new UsernameNotFoundException("No user found with login: " + login);
        }
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setEmployee(entity);
        return userDetails;
    }
}
