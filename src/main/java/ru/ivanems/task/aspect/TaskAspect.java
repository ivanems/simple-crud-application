package ru.ivanems.task.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.ivanems.task.entity.Task;

@Aspect
@Component
@Slf4j
public class TaskAspect {

    @Before(
            "@within(org.springframework.web.bind.annotation.RestController)"
    )
    public void logRequestIP(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("Request send from IP: {}, method: {}", request.getRemoteAddr(), joinPoint.getSignature());
    }

    @AfterReturning(
            pointcut = "@annotation(ru.ivanems.task.aspect.LogTaskUpdate)",
            returning = "task"
    )
    public void logCreateTask(JoinPoint joinPoint, Task task) {
        log.info("Task with ID {} was created or updated: {}", task.getId(), task);
    }

    @AfterThrowing(
            pointcut = "execution(* ru.ivanems.task.service.*.*(..))",
            throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Exception exception) {
        log.error("Exception was thrown: {}, message: {}", joinPoint.getSignature(), exception.getMessage());
    }

    @Around(
            "@annotation(ru.ivanems.task.aspect.LogExecutionTime)"
    )
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            log.info("Method {} execution time: {} ms", joinPoint.getSignature(), executionTime);
        }
    }

}
