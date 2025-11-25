package com.hoodgo.aspect;

import com.hoodgo.annotation.AutoFill;
import com.hoodgo.constant.AutoFillConstant;
import com.hoodgo.context.BaseContext;
import com.hoodgo.enumeration.OperationType;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("@annotation(com.hoodgo.annotation.AutoFill)")
    public void autoFillPointcut(){}

    @Before("autoFillPointcut()")
    public void autoFillBefore(JoinPoint joinPoint){
        log.info("开始自动填充的操作");

        MethodSignature sig = (MethodSignature)joinPoint.getSignature();
        AutoFill autoFill = sig.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        Object[] args = joinPoint.getArgs();
        if(args == null)
            return;
        Object arg = args[0];

        LocalDateTime currentTime = LocalDateTime.now();
        Long currentUserId = BaseContext.getCurrentId();
        try {
            Method setUpdataTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdataUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdataUser.invoke(arg, currentUserId);
            setUpdataTime.invoke(arg, currentTime);
        }catch (Exception e){e.printStackTrace();}

        if(operationType==OperationType.INSERT)
            try {
                Method setCreateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                setCreateUser.invoke(arg, currentUserId);
                setCreateTime.invoke(arg, currentTime);
            }catch (Exception e){e.printStackTrace();}
    }
}
