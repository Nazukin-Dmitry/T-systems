package com.tsystems.nazukin.logiweb.restclient;

import com.tsystems.nazukin.logiweb.restclient.entity.*;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 26.03.2016.
 */
@ManagedBean
@SessionScoped
public class DriverBean {

    private static final Logger logger = Logger.getLogger(DriverBean.class);

    private static final String REST_SERVICE = "http://localhost:8080/rest-service";

    private ResteasyClient client;

    private Integer serialNumber;
    private String errorMessage = "";

    private Integer cargoId;
    private CargoStatus cargoStatus;

    private DriverEntity driverInfo;
    private OrderEntity orderInfo;
    private List<CargoEntity> cargos;

    public String getInformation(){
        client = new ResteasyClientBuilder().build();
        if (serialNumber == null) {
            errorMessage = "No data. Try again please.";
            return "index";
        }
        ResteasyWebTarget target = client.target(REST_SERVICE + "/checkSerial").queryParam("serialNumber", serialNumber);
        Response response = target.request().get();

        if (response.getStatus() == 200){
            response.close();
            errorMessage = "";

            target = client.target(REST_SERVICE + "/order").queryParam("serialNumber", serialNumber);
            response = target.request().get();
            if (response.getStatus() != 200){
                response.close();
                logger.warn("Some problems with server.");
                errorMessage = "Some problems with server.";
                return "index";
            }
            orderInfo = response.readEntity(new GenericType<OrderEntity>(){});
            orderInfo.getDrivers().sort((o1, o2) -> Integer.compare(o1.getSerialNumber(), o2.getSerialNumber()));
            cargos = new ArrayList<>();
            for (OrderItemEntity item : orderInfo.getOrderItems()){
                if (item.getType() == OrderItemType.LOADING){
                    cargos.add(item.getCargo());
                }
            }
            for(DriverEntity driver : orderInfo.getDrivers()){
                if (driver.getSerialNumber() == serialNumber){
                    driverInfo = driver;
                }
            }
            response.close();
            return "driver";
        } else if (response.getStatus() == 400){
            response.close();
            logger.warn("Wrong serial number");
            errorMessage = "Wrong serial number or you don't have any order now. Try again.";
            return "index";
        } else {
            response.close();
            logger.warn("Some problems with server.");
            errorMessage = "Some problems with server.";
            return "index";
        }
    }

    public String startOrder(){
        ResteasyWebTarget target = client.target(REST_SERVICE + "/startOrder");
        Response response = target.request().post(Entity.entity(driverInfo,
                "application/json"));
        if (response.getStatus() != 200){
            response.close();
            logger.warn("Some problems with server.");
            errorMessage = "Some problems with server.";
            return "index";
        }
        response.close();
        return getInformation();
    }

    public String changeCargoStatus(){
        ResteasyWebTarget target = client.target(REST_SERVICE + "/changeCargoStatus");
        CargoEntity cargo = new CargoEntity();
        cargo.setId(cargoId);
        cargo.setStatus(cargoStatus);
        Response response = target.request().post(Entity.entity(cargo, "application/json"));
        if (response.getStatus() != 200){
            response.close();
            logger.warn("Some problems with server.");
            errorMessage = "Some problems with server.";
            return "index";
        }
        response.close();
        return getInformation();
    }

    public String changeDriverStatus(){
        ResteasyWebTarget target = client.target(REST_SERVICE + "/changeStatus");
        if (driverInfo.getStatus() == DriverStatus.DRIVE) {
            driverInfo.setStatus(DriverStatus.ORDER);
        } else  {
            driverInfo.setStatus(DriverStatus.DRIVE);
        }
        Response response = target.request().post(Entity.entity(driverInfo, "application/json"));
        if (response.getStatus() != 200){
            response.close();
            logger.warn("Some problems with server.");
            errorMessage = "Some problems with server.";
            return "index";
        }
        response.close();
        return getInformation();
    }

    public boolean isAllDelivered(){
        for (CargoEntity cargoEntity : cargos){
            if (cargoEntity.getStatus() != CargoStatus.DELIVERED){
                return false;
            }
        }
        return true;
    }

    public String finishOrder(){
        ResteasyWebTarget target = client.target(REST_SERVICE + "/finishOrder");
        Response response = target.request().post(Entity.entity(driverInfo, "application/json"));
        if (response.getStatus() != 200){
            response.close();
            logger.warn("Some problems with server.");
            errorMessage = "Some problems with server.";
            return "index";
        }
        response.close();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index";
    }

    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<CargoEntity> getCargos() {
        return cargos;
    }

    public void setCargos(List<CargoEntity> cargos) {
        this.cargos = cargos;
    }

    public ResteasyClient getClient() {
        return client;
    }

    public void setClient(ResteasyClient client) {
        this.client = client;
    }

    public Integer getCargoId() {
        return cargoId;
    }

    public void setCargoId(Integer cargoId) {
        this.cargoId = cargoId;
    }

    public CargoStatus getCargoStatus() {
        return cargoStatus;
    }

    public void setCargoStatus(CargoStatus cargoStatus) {
        this.cargoStatus = cargoStatus;
    }

    public DriverEntity getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverEntity driverInfo) {
        this.driverInfo = driverInfo;
    }

    public OrderEntity getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderEntity orderInfo) {
        this.orderInfo = orderInfo;
    }
}
