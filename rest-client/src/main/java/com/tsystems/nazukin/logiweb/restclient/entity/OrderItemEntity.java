package com.tsystems.nazukin.logiweb.restclient.entity;

/**
 * Created by 1 on 27.03.2016.
 */
public class OrderItemEntity {
    private Integer sequenceNumber;
    private OrderItemType type;

    private CargoEntity cargo;
    private CityEntity city;

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public OrderItemType getType() {
        return type;
    }

    public void setType(OrderItemType type) {
        this.type = type;
    }

    public CargoEntity getCargo() {
        return cargo;
    }

    public void setCargo(CargoEntity cargo) {
        this.cargo = cargo;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

}
