package com.tsystems.nazukin.logiweb.validators;

import com.tsystems.nazukin.logiweb.model.entity.EmployeeEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by 1 on 20.03.2016.
 */
@Component
public class EmployeeValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(EmployeeEntity.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "employee.first.name.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "secondName", "employee.second.name.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "employee.login.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "employee.password.empty");
    }
}
