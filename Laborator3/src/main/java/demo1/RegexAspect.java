package demo1;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.regex.Pattern;

@Aspect
@Component
public class RegexAspect {
    @Before("execution (* set*(..)) && args(value)")
    public void beforeSetter(JoinPoint joinPoint, Object value) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Parameter parameter = methodSignature.getMethod().getParameters()[0];
        Regex annotation = parameter.getAnnotation(Regex.class);
        if(annotation != null && (value == null || !Pattern.matches(annotation.value(), value.toString()))) {
            throw new IllegalArgumentException(String.format("Illegal argument %s for %s", value, parameter.getName()));
        }
    }
}
