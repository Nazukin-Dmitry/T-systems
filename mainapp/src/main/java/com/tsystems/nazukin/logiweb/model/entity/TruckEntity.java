package com.tsystems.nazukin.logiweb.model.entity;

import com.tsystems.nazukin.logiweb.model.enums.TruckState;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by 1 on 15.02.2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "TruckEntity.findByRegNumber", query = "SELECT c FROM TruckEntity c WHERE c.regNumber=:regNumber"),
        @NamedQuery(name = "TruckEntity.findByCityId", query = "SELECT c FROM TruckEntity c WHERE c.currentCity.id=:id"),
        @NamedQuery(name = "TruckEntity.findByOrderId", query = "SELECT c FROM TruckEntity c WHERE c.currentOrder.id=:id"),
        @NamedQuery(name = "TruckEntity.findForOrder", query = "SELECT c FROM TruckEntity c LEFT JOIN c.currentOrder d " +
                "WHERE c.currentCity.id=:id AND c.state = 'WORK' AND (d.truck IS NULL)"),
        @NamedQuery(name = "TruckEntity.findAll", query = "SELECT c FROM TruckEntity c")
})
@Table(name = "trucks", schema = "logiwebdb", catalog = "")
public class TruckEntity implements Serializable {
    private Integer id;
    private String regNumber;
    private TruckState state;
    private Integer capacity;
    private Integer driverCount;

    private OrderEntity currentOrder;
    private CityEntity currentCity;

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
    @Pattern(regexp = "[a-zA-Z]{2}[0-9]{5}", message = "Wrong format. Example: AB12345")
    @Column(name = "reg_number", nullable = false, length = 45)
    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    @Basic
    @NotNull
    @Column(name = "state", nullable = false, columnDefinition = "enum('WORK', 'REPAIR')")
    @Enumerated(EnumType.STRING)
    public TruckState getState() {
        return state;
    }

    public void setState(TruckState state) {
        this.state = state;
    }

    @Basic
    @NotNull
    @Column(name = "capacity", nullable = false)
    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Basic
    @NotNull
    @Min(1)
    @Column(name = "driver_count", nullable = false)
    public Integer getDriverCount() {
        return driverCount;
    }

    public void setDriverCount(Integer driverCount) {
        this.driverCount = driverCount;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    public CityEntity getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(CityEntity currentCity) {
        this.currentCity = currentCity;
    }


    @OneToOne(mappedBy = "truck", fetch = FetchType.LAZY)
    public OrderEntity getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(OrderEntity currentOrder) {
        this.currentOrder = currentOrder;
    }
}
