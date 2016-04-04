package com.tsystems.nazukin.logiweb.controller;

import com.tsystems.nazukin.logiweb.customexceptions.WrongSerialNumberException;
import com.tsystems.nazukin.logiweb.model.entity.*;
import com.tsystems.nazukin.logiweb.service.api.DriverServiceApi;
import com.tsystems.nazukin.logiweb.service.api.OrderServiceApi;
import com.tsystems.nazukin.logiweb.service.api.TruckServiceApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by 1 on 25.02.2016.
 * GET /page/driver/order
 */
@Controller
@RequestMapping("/driver/order")
public class DriverPageController {

    private static final Logger logger = Logger.getLogger(DriverPageController.class);

    @Autowired
    private DriverServiceApi driverService;
    @Autowired
    private OrderServiceApi orderService;
    @Autowired
    private TruckServiceApi truckService;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrderPage(@RequestParam( value = "serialNumber", required = false) String serialNumber,
                               Model model) {
        if (serialNumber == null) {
            return "driver/startPage";
        } else {
            Integer serial = Integer.parseInt(serialNumber);
            EmployeeEntity employeeEntity = ((CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getEmployee();
            DriverEntity driverEntity;

            //check right serial number
            try {
                driverEntity = driverService.findByEmployeeIdAndSerial(employeeEntity.getId(), serial);
            } catch (WrongSerialNumberException e) {
                logger.warn("Wrong serial number!!!", e);
                model.addAttribute("error", "Wrong serial number!!! Try again.");
                return "driver/startPage";
            }
            OrderEntity order = driverEntity.getCurrentOrder();
            if (order == null) {
                model.addAttribute("type", "noorder");
            } else {
                model.addAttribute("type", "order");
                List<OrderItemEntity> items = orderService.findItemsByOrderId(order.getId());
                TruckEntity truck = truckService.findByOrderId(order.getId());
                model.addAttribute("orderItemList", items);
                model.addAttribute("order", order);
                model.addAttribute("truck", truck);
                model.addAttribute("codriverList", driverService.findByOrderId(order.getId()));
                model.addAttribute("serialNumber", driverEntity.getSerialNumber());
            }
            return "driver/startPage";
        }
    }
}
