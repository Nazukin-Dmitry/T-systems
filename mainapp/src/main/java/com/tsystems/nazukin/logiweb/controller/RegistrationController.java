package com.tsystems.nazukin.logiweb.controller;

import com.tsystems.nazukin.logiweb.customexceptions.UserAllreadyExistException;
import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;
import com.tsystems.nazukin.logiweb.model.enums.EmployeeType;
import com.tsystems.nazukin.logiweb.service.api.EmployeeServiceApi;
import com.tsystems.nazukin.logiweb.validators.EmployeeValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by 1 on 13.02.2016.
 */
@Controller
@RequestMapping("/registration")
public class RegistrationController {
    public final static String WITHOUT_ERROR = "normal";
    public final static String PASSWORD_ERROR = "pass";
    public final static String LOGIN_EXIST_ERROR = "loginExist";

    private static final Logger logger = Logger.getLogger(RegistrationController.class);

    @Autowired
    private EmployeeServiceApi employeeService;

    @Autowired
    private EmployeeValidator employeeValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(employeeValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getRegistrationPage(@RequestParam(value = "type", required = false) String typeOfPage,
                                      Model model,
                                      @ModelAttribute("employeeEntity") EmployeeEntity employeeEntity) {
        String error;
        if (typeOfPage != null) {
            switch (typeOfPage) {
                case WITHOUT_ERROR:
                    error = "";
                    break;
                case PASSWORD_ERROR:
                    error = "Passwords are not equals!!!";
                    break;
                case LOGIN_EXIST_ERROR:
                    error = "User with that login has already registered!!!";
                    break;
                default:
                    error = "";
            }
        } else {
            error = "";
        }
        model.addAttribute("error", error);
        return "registration";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registrate(@ModelAttribute("employeeEntity") @Valid EmployeeEntity employeeEntity,
                          BindingResult bindingResult,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            return "registration";
        }

        String password = employeeEntity.getPassword();
        String password2 = request.getParameter("password2");
        employeeEntity.setEmployeeType(EmployeeType.NEW);

        String resultPage;
        if (!password.equals(password2)) {
            resultPage = "/registration?type=" + RegistrationController.PASSWORD_ERROR;
            redirectAttributes.addFlashAttribute("employeeEntity", employeeEntity);
        } else {
            try {
                employeeService.save(employeeEntity);
                resultPage = "/login?type=normal";
            } catch (UserAllreadyExistException ex) {
                logger.warn("Try to save employee with existing login.", ex);
                redirectAttributes.addFlashAttribute("employeeEntity", employeeEntity);
                resultPage = "/registration?type=" + RegistrationController.LOGIN_EXIST_ERROR;
            }
        }
        return "redirect:" + resultPage;
    }
}
