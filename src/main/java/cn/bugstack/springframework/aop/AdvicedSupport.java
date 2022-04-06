package cn.bugstack.springframework.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author hongxingyi
 * @description TODO 包装动态代理信息
 * @date 2022/3/28 19:51
 */
public class AdvicedSupport {

    // ProxyConfig
    private boolean proxyTargetClass = false;//默认是JDK动态代理

    //被代理的目标对象
    private TargetSource targetSource;

    //方法拦截器
    private MethodInterceptor methodInterceptor;

    //方法匹配器（检查目标方法是否符合通知条件）
    private MethodMatcher methodMatcher;


    public boolean isProxyTargetClass(){
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }
}
