package com.tsystems.nazukin.logiweb.rs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsystems.nazukin.logiweb.rs.model.enums.EmployeeType;

import javax.persistence.*;

/**
 * Created by 1 on 11.02.2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "EmployeeEntity.findAll", query = "SELECT c FROM EmployeeEntity c"),
        @NamedQuery(name = "EmployeeEntity.findByLoginAndPassword", query = "SELECT c FROM EmployeeEntity c " +
                "WHERE c.login=:login AND c.password=:password"),
        @NamedQuery(name = "EmployeeEntity.findByLogin", query = "SELECT c FROM EmployeeEntity c " +
                "WHERE c.login=:login"),
        @NamedQuery(name = "EmployeeEntity.findAllNew", query = "SELECT c FROM EmployeeEntity c " +
                "WHERE c.employeeType=:type")
})

@Table(name = "employees", schema = "logiwebdb", catalog = "")
public class EmployeeEntity  {
    @JsonIgnore
    private int id;
    private String firstName;
    private String secondName;
    @JsonIgnore
    private String login;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private EmployeeType employeeType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "second_name")
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "employee_type", columnDefinition = "enum('DRIVER', 'MANAGER', 'NEW')")
    @Enumerated(EnumType.STRING)
    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

}
