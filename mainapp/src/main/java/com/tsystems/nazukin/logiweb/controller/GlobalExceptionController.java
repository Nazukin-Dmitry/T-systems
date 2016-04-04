package com.tsystems.nazukin.logiweb.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by 1 on 02.04.2016.
 */
@ControllerAdvice
public class GlobalExceptionController {

    private static final Logger logger = Logger.getLogger(GlobalExceptionController.class);

    @ExceptionHandler(Exception.class)
    public String handleAllException(Exception ex) {
        logger.error(ex);
        return "500";
    }
}
