package com.example.gestionAlumni.Config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Intercepte toute méthode dans le package Services
    @Before("execution(* com.example.gestionAlumni.Services.*.*(..))")
    public void logBeforeMethods(JoinPoint joinPoint) {
        logger.info("Appel de la méthode : " + joinPoint.getSignature().toShortString());
    }

    @Before("execution(* com.example.gestionAlumni.Services.OfferService.createOffer(..))")
    public void logBeforeCreateOffer(JoinPoint joinPoint) {
        logger.info("Méthode appelée : " + joinPoint.getSignature().toShortString());
        logger.info("Arguments : " + Arrays.toString(joinPoint.getArgs()));
    }
}
