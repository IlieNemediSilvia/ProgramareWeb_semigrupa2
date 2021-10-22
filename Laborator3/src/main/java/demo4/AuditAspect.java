package demo4;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class AuditAspect {
    @After("execution (* *(..))")
    public void afterExecution(JoinPoint joinPoint) {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        System.out.println(String.format("Class %s, method %s, thread %s, user %s",
                method.getDeclaringClass().getName(),
                method.getName(),
                Thread.currentThread().getName(),
                System.getProperty("user.name")));
    }
}
