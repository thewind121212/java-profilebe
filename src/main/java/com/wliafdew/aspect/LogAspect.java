package com.wliafdew.aspect;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wliafdew.model.ApiLog;
import com.wliafdew.repo.ApiLogRepository;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private ApiLogRepository apiLogRepository;

    @Around("execution(* com.wliafdew.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        System.out.println("⏱ Start: " + className + "." + methodName);
        
        LocalDateTime startTime = LocalDateTime.now();
        ApiLog apiLog = new ApiLog();
        apiLog.setClassName(className);
        apiLog.setMethodName(methodName);
        apiLog.setStartTime(startTime);

        try {
            Object result = joinPoint.proceed();
            LocalDateTime endTime = LocalDateTime.now();
            
            apiLog.setEndTime(endTime);
            apiLog.setExecutionTime(java.time.Duration.between(startTime, endTime).toMillis());
            apiLog.setStatus("SUCCESS");
            
            System.out.println("✅ End: " + className + "." + methodName);
            System.out.println("⏳ Execution time: " + apiLog.getExecutionTime() + "ms\n");
            
            apiLogRepository.save(apiLog);
            return result;
        } catch (Exception e) {
            LocalDateTime endTime = LocalDateTime.now();
            
            apiLog.setEndTime(endTime);
            apiLog.setExecutionTime(java.time.Duration.between(startTime, endTime).toMillis());
            apiLog.setStatus("ERROR");
            apiLog.setErrorMessage(e.getMessage());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            apiLog.setStackTrace(sw.toString());

            
            System.out.println("❌ Error in " + className + "." + methodName);
            System.out.println("⏳ Failed after: " + apiLog.getExecutionTime() + "ms");
            System.out.println("Error: " + e.getMessage());
            System.out.println("Stack trace:");
            e.printStackTrace();
            System.out.println();
            
            apiLogRepository.save(apiLog);
            throw e;
        }
    }
} 