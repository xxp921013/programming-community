package com.xu.majiangcommunity.advice;

import ch.qos.logback.core.status.StatusUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ModelAndView handlerExcetion(HttpServletRequest req, Exception e) {
        System.out.println(e.getMessage());
        return new ModelAndView("/error/404");
    }
}
