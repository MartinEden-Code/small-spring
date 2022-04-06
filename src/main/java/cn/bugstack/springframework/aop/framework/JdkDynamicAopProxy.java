package cn.bugstack.springframework.aop.framework;

import cn.bugstack.springframework.aop.AdvicedSupport;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 20:41
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvicedSupport advised;

    public JdkDynamicAopProxy(AdvicedSupport advised) {
        this.advised = advised;
    }


    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), advised.getTargetSource().getTargetClass(), this);//this指的是实现
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {

            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method, args));

        }
        return method.invoke(advised.getTargetSource().getTarget(), args);
    }
}
