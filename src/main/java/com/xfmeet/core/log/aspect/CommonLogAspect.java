package com.xfmeet.core.log.aspect;

import com.xfmeet.core.log.annotation.CommonLog;
import com.xfmeet.core.log.common.CommonLogUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author meetzy
 */
@Aspect
@Component
@Order(Integer.MIN_VALUE+1)
public class CommonLogAspect {


    @Pointcut("@annotation(com.xfmeet.core.log.annotation.CommonLog)")
    public void serviceLog() {
    }

    @Around("serviceLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CommonLog log = method.getAnnotation(CommonLog.class);
        String className = CommonLogUtils.getClassName(joinPoint.getTarget().getClass(),log.type());
        String methodName = method.getName();
        CommonLogUtils.log(log.value(), CommonLogUtils.getLogString(className, methodName, joinPoint.getArgs()));
        Object obj = joinPoint.proceed();
        if (method.getReturnType() != Void.TYPE) {
            CommonLogUtils.log(log.value(), CommonLogUtils.getLogString(className, methodName, obj));
        }
        return obj;
    }
}
