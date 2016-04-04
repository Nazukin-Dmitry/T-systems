package com.tsystems.nazukin.logiweb.rs.controller;

import com.tsystems.nazukin.logiweb.rs.model.entity.CargoEntity;
import com.tsystems.nazukin.logiweb.rs.model.entity.DriverEntity;
import com.tsystems.nazukin.logiweb.rs.model.entity.OrderEntity;
import com.tsystems.nazukin.logiweb.rs.model.enums.DriverStatus;
import com.tsystems.nazukin.logiweb.rs.service.DriverServiceApi;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by 1 on 25.03.2016.
 */
@Stateless
@Path("/")
@Produces({ "application/json" })
public class RestController {

    private static final Logger logger = Logger.getLogger(RestController.class);

    @EJB
    private DriverServiceApi driverService;

    @POST
    @Path("/startOrder")
    @Consumes({ "application/json" })
    public Response startOrder(DriverEntity driverEntity){
        try {
            if (driverEntity.getStatus() == DriverStatus.DRIVE) {
                if (driverService.fromFreeToDrive(driverEntity.getSerialNumber())) {
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            } else {
                if (driverService.fromFreeToOrder(driverEntity.getSerialNumber())) {
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }
        } catch (Exception e){
            logger.error(e);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/changeStatus")
    @Consumes({ "application/json" })
    public Response changeDriverStatus(DriverEntity driverEntity){
        try {
            if (driverEntity.getStatus() == DriverStatus.DRIVE) {
                if (driverService.fromOrderToDrive(driverEntity.getSerialNumber())) {
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            } else {
                if (driverService.fromDriveToOrder(driverEntity.getSerialNumber())) {
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }
        } catch (Exception e){
            logger.error(e);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/finishOrder")
    @Consumes({ "application/json" })
    public Response finishOrder(DriverEntity driverEntity){
        try {
            driverService.finishOrder(driverEntity.getSerialNumber());
            return Response.ok().build();
        } catch (Exception e){
            logger.error(e);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/changeCargoStatus")
    @Consumes({ "application/json" })
    public Response changeCargoStatus(CargoEntity cargoEntity){
        try {
            driverService.changeCargoStatus(cargoEntity.getId(), cargoEntity.getStatus());
            return Response.ok().build();
        } catch (Exception e){
            logger.error(e);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/order")
    public Response orderInformation(@QueryParam("serialNumber") Integer serialNumber){
        try {
            OrderEntity order = driverService.getOrder(serialNumber);
            return Response.ok().entity(order).build();
        } catch (Exception e){
            logger.error(e);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/checkSerial")
    public Response checkSerial(@QueryParam("serialNumber") Integer serialNumber){
        try {
            boolean ok = driverService.checkSerial(serialNumber);
            if (ok) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e){
            logger.error(e);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/status")
    public Response getStatus(@QueryParam("serialNumber") Integer serialNumber){
        try {
            DriverStatus status = driverService.getStatus(serialNumber);
            return Response.ok().entity(status).build();
        } catch (Exception e){
            logger.error(e);
            return Response.serverError().build();
        }
    }
}
