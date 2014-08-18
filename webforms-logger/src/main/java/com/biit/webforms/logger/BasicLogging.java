package com.biit.webforms.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

@Aspect
public class BasicLogging extends AbstractLogging {

	// @Before("execution(* com.biit.abcd.persistence.HibernateBasicExample.testCrud())")
	// @Before(value = "@annotation(loggable)")
	// @Before("execution(* com.biit.abcd..*.*(..))")
	public void logBefore(JoinPoint joinPoint) throws Throwable {
		log(joinPoint);
	}

	@Around("execution(* com.biit.webforms..*.*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		Object returnValue = null;
		stopWatch.start();
		returnValue = joinPoint.proceed();
		stopWatch.stop();
		log(stopWatch.getTotalTimeMillis(), joinPoint);
		return returnValue;
	}

}
