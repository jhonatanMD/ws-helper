package com.helper.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class AppAOP {

    //@Around("execution(*  com.ws.controller.*.*(..))")
    @Around("@annotation(org.springframework.web.bind.annotation.RestController)")
    //@Around(${""})
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String metodo = joinPoint.getSignature().getName();

        Arrays.stream(joinPoint.getArgs())
                .forEach(rq -> {
                    try {

                        logger.info("request " + metodo  + " ==> " + new JsonMapper().writeValueAsString(rq));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
        Long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        Long executionTime = System.currentTimeMillis() - start;
        logger.info(metodo + "() executed in: " + executionTime + " ms");

        try {

            logger.info("response ==> " + metodo  + " ==> " + new JsonMapper().writeValueAsString(proceed));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return proceed;
    }

    /*
    @Around("execution(* com.ws.*.*.*(..))")
    public Object businessLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String metodo = joinPoint.getSignature().getName();

        Arrays.stream(joinPoint.getArgs())
                .forEach(rq -> {
                    try {

                        logger.info("request " + metodo  + " ==> " + new JsonMapper().writeValueAsString(rq));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
        Long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        Long executionTime = System.currentTimeMillis() - start;
        logger.info(metodo + "() executed in: " + executionTime + " ms");

        try {

            logger.info("response ==> " + metodo  + " ==> " + new JsonMapper().writeValueAsString(proceed));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return proceed;
    }*/
}
