package com.tsystems.nazukin.logiweb.controller;

import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;
import com.tsystems.nazukin.logiweb.service.api.CityServiceApi;
import com.tsystems.nazukin.logiweb.service.api.DriverServiceApi;
import com.tsystems.nazukin.logiweb.service.api.EmployeeServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 1 on 17.02.2016.
 * /page/manager/newUsers get
 */
@Controller
@RequestMapping("/manager/newUsers")
public class NewUsersController {
    @Autowired
    private EmployeeServiceApi employeeService;
    @Autowired
    private CityServiceApi cityService;
    @Autowired
    private DriverServiceApi driverService;

    @RequestMapping(method = RequestMethod.GET)
    public String getNewUsersPage(Model model){
        List<EmployeeEntity> newEmployees = employeeService.getAllNew();
        model.addAttribute("userList", newEmployees);
        return "/manager/driversNew";
    }

    @RequestMapping(value = "/createManager",method = RequestMethod.POST)
    public String createManager(HttpServletRequest request) {
        String employeeId = request.getParameter("id");
        employeeService.newToManager(Integer.parseInt(employeeId));
        return "redirect:/manager/newUsers";
    }

    @RequestMapping(value = "/createDriver",method = RequestMethod.GET)
    public String getCreateDriverPage(@RequestParam(value = "id", required = true) String employeeId,
                                        Model model) {
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("cityList", cityService.getAll());
        return "manager/driversCreate";
    }

    @RequestMapping(value = "/createDriver",method = RequestMethod.POST)
    public String createDriver(HttpServletRequest request) {
        String employeeId = request.getParameter("employeeId");
        String cityId = request.getParameter("currentCity");
        driverService.save(Integer.parseInt(employeeId), Integer.parseInt(cityId));
        return "redirect:/manager/newUsers";
    }
}
