package com.tele.goldenkey.advice;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tele.goldenkey.interceptor.ServerApiParamHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jianlu.Yu
 * @Date: 2020/8/24
 * @Description: 全局打印 Controller 入参日志
 * @Copyright (c) 2020, rongcloud.cn All Rights Reserved
 */
@Aspect
@Configuration
@Slf4j
public class LogAspect {

    // 定义切点Pointcut
    @Pointcut("execution(* com.tele.goldenkey.controller..*Controller.*(..))")
    public void executeService() {
    }

    @Before("executeService()")
    public void doBefore(JoinPoint joinPoint) {
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

        Object[] args = joinPoint.getArgs();
        String[] paramsName = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        Map<String, Object> paramMap = new HashMap<>();
        if (args != null && paramsName != null && args.length > 0 && paramsName.length > 0) {
            for (int i = 0; i < paramsName.length; i++) {
                String paramName = paramsName[i];
                Object paramVal = args[i];
                if (!(paramVal instanceof HttpServletResponse) && !(paramVal instanceof HttpServletRequest)) {
                    paramMap.put(paramName, args[i]);
                }
            }
        }
        String uri = ServerApiParamHolder.getURI();
        String traceId = ServerApiParamHolder.getTraceId();
        String uid = ServerApiParamHolder.getEncodedCurrentUserId();
        log.info("请求: traceId={},uri={},target={},params=[{}],uid={}", traceId, uri, target, JSON.toJSONString(paramMap), uid);
    }

    @AfterReturning(value = "executeService()", returning = "returnValue")
    public void doAfter(Object returnValue) {
        log.info("响应: traceId={},uri={},uid={},return value={}",
                ServerApiParamHolder.getTraceId(),
                ServerApiParamHolder.getURI(),
                ServerApiParamHolder.getEncodedCurrentUserId(),
                JSON.toJSONString(returnValue));
    }
}
