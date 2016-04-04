package com.tsystems.nazukin.logiweb.controller;

import com.tsystems.nazukin.logiweb.customexceptions.DriverAllreadyUsedException;
import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.model.entity.DriverEntity;
import com.tsystems.nazukin.logiweb.model.enums.DriverStatus;
import com.tsystems.nazukin.logiweb.service.api.CityServiceApi;
import com.tsystems.nazukin.logiweb.service.api.DriverServiceApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by 1 on 18.02.2016.
 * GET /page/manager/drivers
 */
@Controller
@RequestMapping("/manager/drivers")
public class DriversController {

    public static final String TYPE_ERROR_DELETE = "errorDelete";

    private static final Logger logger = Logger.getLogger(DriversController.class);

    @Autowired
    private CityServiceApi cityService;
    @Autowired
    private DriverServiceApi driverService;

    @RequestMapping(method = RequestMethod.GET)
    public String getDriversPage(@RequestParam(value = "cityId", required = false) String cityId,
                               Model model) {
        List<CityEntity> cities = cityService.getAll();
        List<DriverEntity> drivers;
        if (cityId != null) {
            if ("all".equals(cityId)){
                CityEntity currentCity = new CityEntity();
                currentCity.setName("All cities");
                model.addAttribute("currentCity", currentCity);
                drivers = driverService.findAll();
            } else {
                CityEntity currentCity;
                currentCity = cityService.find(Integer.parseInt(cityId));
                model.addAttribute("currentCity", currentCity);
                drivers = driverService.findAllByCityId(Integer.parseInt(cityId));
            }
        } else {
            drivers = Collections.emptyList();
        }
        model.addAttribute("cityList", cities);
        model.addAttribute("driverList", drivers);
        return "/manager/drivers";
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public String editDriver(HttpServletRequest request) {
        String id = request.getParameter("id");
        String firstName = request.getParameter("firstName");
        String secondName = request.getParameter("secondName");
        String workTime = request.getParameter("workTime");
        String cityId = request.getParameter("currentCity");
        String pastCurrentCityID = request.getParameter("pastCurrentCity");

        switch (request.getParameter("action")) {
            case "delete":
                try {
                    driverService.delete(Integer.parseInt(id));
                    return "redirect:/manager/drivers?cityId=" + pastCurrentCityID;
                } catch (DriverAllreadyUsedException e) {
                    logger.warn("Try to delete driver, that used in order.", e);
                    return "redirect:/manager/drivers/edit?id=" + id +
                            "&type=" + TYPE_ERROR_DELETE;
                }
            case "update":
                default:
                driverService.update(id, firstName, secondName, workTime, cityId);
                return "redirect:/manager/drivers/edit?id=" + id;
        }
    }

    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String getDriverEditPage(@RequestParam(value = "id", required = true) Integer driverId,
                        @RequestParam(value = "type", required = false) String type,
                        Model model) {
        DriverEntity driver = driverService.find(driverId);
        List<CityEntity> cityList = cityService.getAll();
        model.addAttribute("driver", driver);
        model.addAttribute("cityList", cityList);
        model.addAttribute("statusList", Arrays.asList(DriverStatus.values()));

        String error = "";
        if (type != null) {
            switch (type) {
                case TYPE_ERROR_DELETE:
                    error = "Unable to remove the driver, it is used in order.";
                    break;
                default:
                    error = "";
            }
        }

        model.addAttribute("error", error);
        return "manager/driversEdit";
    }
}
