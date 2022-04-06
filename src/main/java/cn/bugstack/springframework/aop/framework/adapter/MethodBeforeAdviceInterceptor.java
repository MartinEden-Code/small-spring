package cn.bugstack.springframework.aop.framework.adapter;

import cn.bugstack.springframework.aop.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/30 17:30
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    //用户自定义前置切点
    private MethodBeforeAdvice advice;


    public MethodBeforeAdviceInterceptor(){}


    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }


    /***
     * 改造拦截发方法 前后置处理
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //拦截方法前置方法
        advice.before(invocation.getMethod(),invocation.getArguments(),invocation.getThis());

        return invocation.proceed();

    }
}
