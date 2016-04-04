package com.tsystems.nazukin.logiweb.controller;

import com.tsystems.nazukin.logiweb.model.entity.CityEntity;
import com.tsystems.nazukin.logiweb.model.entity.MapEntity;
import com.tsystems.nazukin.logiweb.service.api.CityServiceApi;
import com.tsystems.nazukin.logiweb.service.api.MapServiceApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 15.02.2016.
 */
@Controller
@RequestMapping("/manager/map")
public class MapController {
    public static final String TYPE_ERROR = "error";

    private static final Logger logger = Logger.getLogger(MapController.class);

    @Autowired
    private CityServiceApi cityService;
    @Autowired
    private MapServiceApi mapService;

    @RequestMapping(method = RequestMethod.GET)
    public String getMapPage(@RequestParam(value = "type", required = false) String typeOfPage,
                             Model model) {
        List<CityEntity> cities = cityService.getAll();
        String error = "";
        if (typeOfPage != null) {
            switch (typeOfPage) {
                case TYPE_ERROR:
                    error = "City with same name have created yet!!!";
                    break;
                default:
                    error = "";
            }
        }
        model.addAttribute("cityList", cities);
        model.addAttribute("error", error);
        return "manager/map";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getCityEditPage(@RequestParam(value = "id", required = true) Integer id,
                                  @RequestParam(value = "type", required = false) String type,
                                  Model model) {

        CityEntity entity = cityService.find(id);
        model.addAttribute("name", entity.getName());
        model.addAttribute("id", entity.getId());
        Map<CityEntity, MapEntity> intervalMap = mapService.getCityIntervals(id);
        model.addAttribute("intervalMap", intervalMap);

        List<CityEntity> cities = cityService.getAll();
        model.addAttribute("cityList", cities);

        String error = "";
        if (type != null) {
            switch (type) {
                case TYPE_ERROR:
                    error = "City with same name have created yet!!!";
                    break;
                default:
                    error = "";
            }
        }
        model.addAttribute("error", error);

        return "manager/mapEdit";
    }

    @RequestMapping(value = "/editInterval", method = RequestMethod.POST)
    public String editInterval(HttpServletRequest request) {
        Integer id1 = Integer.parseInt(request.getParameter("id1"));
        Integer id2 = Integer.parseInt(request.getParameter("id2"));
        Integer interval = Integer.parseInt(request.getParameter("interval"));
        mapService.saveOrUpdate(id1, id2, interval);
        return "redirect:/manager/map/edit?id=" + id1;

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createCity(HttpServletRequest request) {
        CityEntity entity = new CityEntity();
        entity.setName(request.getParameter("name"));

        try {
            cityService.saveOrUpdate(entity);
        } catch (RuntimeException e) {
            logger.warn("Save existing city", e);
            return "redirect:/manager/map?type=" + TYPE_ERROR;
        }
        return "redirect:/manager/map";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editCity(HttpServletRequest request) {

        Integer id = Integer.parseInt(request.getParameter("id"));
        String cityName = request.getParameter("name");

        CityEntity entity = new CityEntity();
        entity.setId(id);
        entity.setName(cityName);
        try {
            cityService.saveOrUpdate(entity);
            return "redirect:/manager/map/edit?id=" + id;
        } catch (RuntimeException e) {
            logger.warn("Try to update city to existing name", e);
            return "redirect:/manager/map/edit?id=" + id + "&type=" + TYPE_ERROR;
        }
    }

}
