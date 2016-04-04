package com.tsystems.nazukin.logiweb.model.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by 1 on 14.02.2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "MapEntity.findAllByCity1", query = "SELECT c FROM MapEntity c WHERE c.city1.id=:city_id"),
        @NamedQuery(name = "MapEntity.findAllByCity2", query = "SELECT c FROM MapEntity c WHERE c.city2.id=:city_id"),
        @NamedQuery(name = "MapEntity.findByIds", query = "SELECT c FROM MapEntity c " +
                "WHERE (c.city1.id=:id1 AND c.city2.id=:id2) OR (c.city1.id=:id2 AND c.city2.id=:id1)")
})
@Table(name = "map", schema = "logiwebdb", catalog = "")
public class MapEntity implements Serializable {
    private Integer id;
    private Integer intervalValue;
    private CityEntity city1;
    private CityEntity city2;

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
    @NotNull
    @Min(value = 1, message = "interval should be not negative")
    @Column(name = "intervalvalue", nullable = false)
    public Integer getIntervalValue() {
        return intervalValue;
    }

    public void setIntervalValue(Integer interval) {
        this.intervalValue = interval;
    }

    @OneToOne()
    @JoinColumn(name = "city1_id", nullable = false)
    public CityEntity getCity1() {
        return city1;
    }

    public void setCity1(CityEntity c1) {
        this.city1 = c1;
    }

    @OneToOne()
    @JoinColumn(name = "city2_id", nullable = false)
    public CityEntity getCity2() {
        return city2;
    }

    public void setCity2(CityEntity c2) {
        this.city2 = c2;
    }
}
