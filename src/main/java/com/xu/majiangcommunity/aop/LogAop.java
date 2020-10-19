package com.xu.majiangcommunity.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import com.xu.majiangcommunity.domain.WebLog;
import com.xu.majiangcommunity.service.impl.WebLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Slf4j
@Component
public class LogAop {
    @Autowired
    private WebLogService webLogService;

    @Pointcut("execution(* com.xu.majiangcommunity.controller.*.*(..))")
    public void logPointcut() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAop.class);

    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
        long startTime = DateUtil.current(false);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        WebLog webLog = new WebLog();
        Object proceed = joinPoint.proceed();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String desc = method.getName();
        if ("getLogList".equals(desc)) {
            return proceed;
        }
        long endTime = System.currentTimeMillis();
//        stopWatch.stop();
//        double totalTimeSeconds = stopWatch.getTotalTimeSeconds();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String ip = request.getRemoteHost();
        webLog.setDescription(desc);
        webLog.setIp(ip);
        webLog.setSpendTime(endTime - startTime);
        webLog.setStartTime(startTime);
        webLog.setUri(uri);
        webLog.setUrl(url);
        LOGGER.info(webLog.toString());
        return proceed;
    }
}
