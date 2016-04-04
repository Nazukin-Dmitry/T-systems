package com.tsystems.nazukin.logiweb.model.entity;

import com.tsystems.nazukin.logiweb.model.enums.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by 1 on 15.02.2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "OrderEntity.findByTruckId", query = "SELECT c FROM OrderEntity c WHERE c.truck.id=:id"),
        @NamedQuery(name = "OrderEntity.findByCityIdWithoutDrivers", query = "SELECT c FROM OrderEntity c " +
                "WHERE c.startCity.id=:id AND size(c.drivers)=0"),
        @NamedQuery(name = "OrderEntity.findByCityId", query = "SELECT c FROM OrderEntity c " +
                "WHERE c.startCity.id=:id"),
        @NamedQuery(name = "OrderEntity.findAll", query = "SELECT c FROM OrderEntity c")
})
@Table(name = "orders", schema = "logiwebdb", catalog = "")
public class OrderEntity implements Serializable {
    private Integer id;
    private OrderStatus status;
    private Integer duration;
    private Date startTime;

    private Set<OrderItemEntity> orderItems;
    private TruckEntity truck;
    private CityEntity startCity;
    private Set<DriverEntity> drivers;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "enum('NEW', 'IN_PROGRESS', 'DONE')")
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Basic
    @NotNull
    @Min(0)
    @Column(name = "duration", nullable = false)
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Basic
    @NotNull
    @Column(name = "start_time", nullable = false)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id")
    public TruckEntity getTruck() {
        return truck;
    }

    public void setTruck(TruckEntity truck) {
        this.truck = truck;
    }


    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "start_city_id")
    public CityEntity getStartCity() {
        return startCity;
    }

    public void setStartCity(CityEntity startCity) {
        this.startCity = startCity;
    }


    @OneToMany(mappedBy = "currentOrder", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    public Set<DriverEntity> getDrivers() {
        return drivers;
    }

    public void setDrivers(Set<DriverEntity> drivers) {
        this.drivers = drivers;
    }
}
