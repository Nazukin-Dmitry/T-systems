package com.tsystems.nazukin.logiweb.validators;

import com.tsystems.nazukin.logiweb.model.entity.TruckEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 1 on 02.04.2016.
 */
@Component
public class TruckValidator implements Validator {

    private static final String REG_NUMBER_PATTERN = "[A-Z]{2}[0-9]{5}";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(TruckEntity.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TruckEntity  truck = (TruckEntity) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "regNumber", "truck.regNumber.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state", "truck.state.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "capacity", "truck.capacity.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "driverCount", "truck.driverCount.empty");

        pattern = Pattern.compile(REG_NUMBER_PATTERN);
        matcher = pattern.matcher(truck.getRegNumber());
        if (!matcher.matches()){
            errors.rejectValue("regNumber", "truck.regNumber.incorrect");
        }

    }
}
