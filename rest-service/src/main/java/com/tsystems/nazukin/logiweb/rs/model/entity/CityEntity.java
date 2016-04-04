package com.tsystems.nazukin.logiweb.rs.model.entity;

import javax.persistence.*;

/**
 * Created by 1 on 14.02.2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "CityEntity.findAll", query = "SELECT c FROM CityEntity c ORDER BY c.name"),
        @NamedQuery(name = "CityEntity.findAllWithout", query = "SELECT c FROM CityEntity c WHERE c.id<>:city_id")
})
@Table(name = "cities", schema = "logiwebdb", catalog = "")
public class CityEntity {
    private Integer id;
    private String name;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
