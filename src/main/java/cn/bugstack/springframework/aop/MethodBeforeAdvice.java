package cn.bugstack.springframework.aop;

import java.lang.reflect.Method;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/30 16:58
 */
public interface MethodBeforeAdvice extends BeforeAdvice{

    /**
     * 拦截器 前置方法
     * @param method
     * @param args
     * @param target
     */
    void before(Method method, Object[] args, Object target);


}
