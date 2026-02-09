package com.assessment.config;

//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class LoggingFilter extends OncePerRequestFilter {
//
//    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        log.info("REQUEST  {} {}", request.getMethod(), request.getRequestURI());
//        filterChain.doFilter(request, response);
//        log.info("RESPONSE {} {}", response.getStatus(), request.getRequestURI());
//    }
//}


import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggingFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private final ObjectMapper objectMapper;

    public LoggingFilter() {
        this.objectMapper = new ObjectMapper();
        // Register Java 8 Date/Time module
        this.objectMapper.registerModule(new JavaTimeModule());
        // Disable WRITE_DATES_AS_TIMESTAMPS to get readable dates
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Around("execution(* com.assessment.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // Log REQUEST
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                try {
                    String argJson = objectMapper.writeValueAsString(args[i]);
                    logger.info("REQUEST - {}.{}() - Arg[{}]: {}",
                            className, methodName, i, argJson);
                } catch (Exception e) {
                    // Log basic info if serialization fails
                    logger.info("REQUEST - {}.{}() - Arg[{}]: {} (Type: {})",
                            className, methodName, i, args[i],
                            args[i] != null ? args[i].getClass().getSimpleName() : "null");
                }
            }
        } else {
            logger.info("REQUEST - {}.{}() - No arguments", className, methodName);
        }

        // Execute method
        Object result;
        long startTime = System.currentTimeMillis();

        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            // Log exception
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("ERROR - {}: {} - Exception: {} (Execution time: {}ms)",
                    className, methodName, e.getMessage(), executionTime);
            throw e;
        }

        long executionTime = System.currentTimeMillis() - startTime;

        // Log RESPONSE
        try {
            if (result != null) {
                String responseJson = objectMapper.writeValueAsString(result);
                logger.info("RESPONSE - {}.{}() - Result: {} (Execution time: {}ms)",
                        className, methodName, responseJson, executionTime);
            } else {
                logger.info("RESPONSE - {}.{}() - Result: null (Execution time: {}ms)",
                        className, methodName, executionTime);
            }
        } catch (Exception e) {
            // Fallback logging if serialization fails
            logger.info("RESPONSE - {}.{}() - Result: {} (Type: {}) (Execution time: {}ms)",
                    className, methodName, result,
                    result != null ? result.getClass().getSimpleName() : "null", executionTime);
        }

        return result;
    }
}