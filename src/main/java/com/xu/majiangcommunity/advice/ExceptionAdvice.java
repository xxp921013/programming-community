package com.xu.majiangcommunity.advice;

import com.xu.majiangcommunity.GlobalException;
import com.xu.majiangcommunity.UserException;
import com.xu.majiangcommunity.dto.BaseResponseBody;
import com.xu.majiangcommunity.enums.ExcetionEnmu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public BaseResponseBody<Void> handlerGlobalException(GlobalException e) {
        ExcetionEnmu excetionEnmu = e.getExcetionEnmu();
        return new BaseResponseBody<>(excetionEnmu.getCode(), excetionEnmu.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public ModelAndView handlerExcetion(HttpServletRequest req, Exception e) {
//        System.out.println(e.getMessage());
//        return new ModelAndView("/error/404");
//    }

    @ExceptionHandler(UserException.class)
    public String handlderUserException(UserException e) {
        ExcetionEnmu excetionEnmu = e.getExcetionEnmu();
        if (excetionEnmu.getCode() == 10001) {
            return "redirect:/localUser/login";
        }
        return "redirect:/";
    }
}
