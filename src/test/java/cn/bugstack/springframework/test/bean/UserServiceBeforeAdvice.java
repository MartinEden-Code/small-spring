package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/30 17:15
 */
public class UserServiceBeforeAdvice implements MethodBeforeAdvice {


    @Override
    public void before(Method method, Object[] args, Object target) {

        System.out.println("拦截方法-before： "+method.getName());

    }
}
