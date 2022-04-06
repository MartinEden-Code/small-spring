package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.aop.MethodAfterAdvice;
import cn.bugstack.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/31 15:51
 */
public class UserServiceAfterAdvice implements MethodAfterAdvice {


    @Override
    public void after(Object returnValue, Method method, Object[] args, Object target) {
        System.out.println("拦截前返回结果："+returnValue);

        System.out.println("后置拦截方法-after:"+method.getName());


    }
}
