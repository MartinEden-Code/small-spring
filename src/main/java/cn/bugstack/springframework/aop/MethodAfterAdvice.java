package cn.bugstack.springframework.aop;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/31 15:47
 */
public interface MethodAfterAdvice extends AfterAdvice{

    void after(Object returnValue,Method method, Object[] args, Object target);
}
