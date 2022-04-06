package cn.bugstack.springframework.aop.framework.adapter;

import cn.bugstack.springframework.aop.AfterAdvice;
import cn.bugstack.springframework.aop.MethodAfterAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/31 15:41
 */
public class AfterReturningAdviceInterceptor implements MethodInterceptor {

    MethodAfterAdvice advice;

    public AfterReturningAdviceInterceptor(){}

    public AfterReturningAdviceInterceptor(MethodAfterAdvice advice){
        this.advice=advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object result = invocation.proceed();//执行反射方法返回的结果
        advice.after(result,invocation.getMethod(),invocation.getArguments(),invocation.getThis());
        return result;
    }
}
