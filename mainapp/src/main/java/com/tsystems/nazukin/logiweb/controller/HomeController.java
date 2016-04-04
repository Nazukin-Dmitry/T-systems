package com.tsystems.nazukin.logiweb.controller;

import com.tsystems.nazukin.logiweb.model.entity.CustomUserDetails;
import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 1 on 11.02.2016.
 */
@Controller
public class HomeController{

    @RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
    public String home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        EmployeeEntity employeeEntity = ((CustomUserDetails) auth.getPrincipal()).getEmployee();
        switch (employeeEntity.getEmployeeType()) {
            case MANAGER:
                return "redirect:/manager/startPage";
            case DRIVER:
                return "redirect:/driver/order";
            case NEW:
                return "redirect:/login?type=new";
            default:
                return "redirect:/login?type=new";
        }
    }

    @RequestMapping(value = "/manager/startPage", method = RequestMethod.GET)
    public String startPage() {
        return "manager/startPage";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(@RequestParam(value = "type", required = false) String typeOfPage,
                            Model model) {
        String error;
        if (typeOfPage != null) {
            switch (typeOfPage) {
                case "normal":
                    error = "";
                    break;
                case "new":
                    error = "Administrator didn't register you. Please try later";
                    break;
                case "notRegistr":
                    error = "Wrong login/passsword.";
                    break;
                default:
                    error = "";
            }
        } else {
            error = "";
        }
        model.addAttribute("error", error);
        return "login";
    }
}