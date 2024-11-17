package ru.ivanems.task.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.ivanems.task.entity.Task;

@Aspect
@Component
public class TaskAspect {

    private static final Logger logger = LoggerFactory.getLogger(TaskAspect.class);

    @Before(
            "@within(org.springframework.web.bind.annotation.RestController)"
    )
    public void logRequestIP(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        logger.info("Request send from IP: {}, method: {}", request.getRemoteAddr(), joinPoint.getSignature());
    }

    @AfterReturning(
            pointcut = "@annotation(ru.ivanems.task.aspect.LogTaskUpdate)",
            returning = "task"
    )
    public void logCreateTask(JoinPoint joinPoint, Task task) {
        logger.info("Task with ID {} was created or updated: {}", task.getId(), task);
    }

    @AfterThrowing(
            pointcut = "execution(* ru.ivanems.task.service.*.*(..))",
            throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Exception exception) {
        logger.error("Exception was thrown: {}, message: {}", joinPoint.getSignature(), exception.getMessage());
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
            logger.info("Method {} execution time: {} ms", joinPoint.getSignature(), executionTime);
        }
    }

}
