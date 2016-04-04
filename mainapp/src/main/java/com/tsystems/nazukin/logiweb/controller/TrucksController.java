package com.tsystems.nazukin.logiweb.controller;

import com.tsystems.nazukin.logiweb.customexceptions.TruckAllreadyUsedException;
import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.model.entity.TruckEntity;
import com.tsystems.nazukin.logiweb.model.enums.TruckState;
import com.tsystems.nazukin.logiweb.service.api.CityServiceApi;
import com.tsystems.nazukin.logiweb.service.api.TruckServiceApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by 1 on 16.02.2016.
 */
@Controller
@RequestMapping("/manager/trucks")
public class TrucksController {

    public static final String TYPE_ERROR = "error";
    public static final String TYPE_ERROR_DELETE = "errorDelete";

    private static final Logger logger = Logger.getLogger(TrucksController.class);

    @Autowired
    private TruckServiceApi truckService;
    @Autowired
    private CityServiceApi cityService;

    @RequestMapping(method = RequestMethod.GET)
    public String getTrucksPage(@RequestParam(value = "cityId", required = false) String cityId,
                              Model model) {

        CityEntity currentCity;
        List<CityEntity> cities = cityService.getAll();
        List<TruckEntity> trucks;
        if (cityId != null) {
            if (cityId.equals("all")) {
                currentCity = new CityEntity();
                currentCity.setName("All cities");
                model.addAttribute("currentCity", currentCity);
                trucks = truckService.findAll();
            } else {
                currentCity = cityService.find(Integer.parseInt(cityId));
                model.addAttribute("currentCity", currentCity);
                trucks = truckService.findAllByCityId(Integer.parseInt(cityId));
            }
        } else {
            trucks = Collections.emptyList();
        }

        model.addAttribute("cityList", cities);
        model.addAttribute("truckList", trucks);

        return "manager/trucks";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getEditPage(@RequestParam(value = "id", required = true) Integer truckId,
                        @RequestParam(value = "type", required = false) String type,
                        Model model) {
        TruckEntity truck = truckService.find(truckId);
        List<CityEntity> cityList = cityService.getAll();

        model.addAttribute("truck", truck);
        model.addAttribute("cityList", cityList);
        model.addAttribute("stateList", Arrays.asList(TruckState.values()));

        String error = "";
        if (type != null) {
            switch (type) {
                case TYPE_ERROR:
                    error = "Registration number exists!!!";
                    break;
                case TYPE_ERROR_DELETE:
                    error = "Unable to remove the truck, it is used in order.";
                    break;
                default:
                    error = "";
            }
        }

        model.addAttribute("error", error);
        return "manager/trucksEdit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(TruckEntity truckEntity,
                       HttpServletRequest request) {

        String currentCityId = request.getParameter("currentCityId");
        String pastCurrentCityId = request.getParameter("pastCurrentCityId");

        switch (request.getParameter("action")) {
            case "update":
                try {
                    truckService.saveOrUpdate(truckEntity, currentCityId);
                    return "redirect:/manager/trucks/edit?id=" + truckEntity.getId();
                } catch (RuntimeException e) {
                    logger.warn("Registration number exception", e);
                    return "redirect:/manager/trucks/edit?id=" + truckEntity.getId() + "&type=" + "error";
                }
            case "delete":
                Integer id = truckEntity.getId();
                try {
                    truckService.delete(id);
                    return "redirect:/manager/trucks?cityId=" + pastCurrentCityId;
                } catch (TruckAllreadyUsedException e) {
                    logger.warn("Registration number exception", e);
                    return "redirect:/manager/trucks/edit?id=" + id + "&type=" + "errorDelete";
                }
            default:
                return "redirect:/manager/startPage";
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createTruck(@ModelAttribute("truckEntity") @Valid TruckEntity truckEntity,
                              BindingResult bindingResult,
                              @RequestParam(value = "currentCityId") String currentCityId,
                              RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()){
                redirectAttributes.addFlashAttribute("truckEntity", truckEntity);
                return "redirect:/manager/trucks/create";
            }
            truckService.saveOrUpdate(truckEntity, currentCityId);
            return "redirect:/manager/trucks";
        } catch (RuntimeException e) {
            logger.warn("Registration number exception", e);
            redirectAttributes.addFlashAttribute("truckEntity", truckEntity);
            return "redirect:/manager/trucks/create?type=error";
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getCreatePage(@RequestParam(value = "type", required = false) String type,
                                Model model,
                                @ModelAttribute("truckEntity") TruckEntity truckEntity) {
        List<CityEntity> cities = cityService.getAll();
        model.addAttribute("cityList", cities);
        model.addAttribute("stateList", Arrays.asList(TruckState.values()));

        String error = "";
        if (type != null) {
            switch (type) {
                case TYPE_ERROR:
                    error = "Registration number exists!!!";
                    break;
                default:
                    error = "";
            }
        }
        model.addAttribute("error", error);
        return "manager/trucksCreate";
    }
}
