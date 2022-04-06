package cn.bugstack.springframework.aop.framework;


import cn.bugstack.springframework.aop.AdvicedSupport;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author hongxingyi
 * @description TODO cglib动态代理和JDK动态代理的区别
 * todo 1、JDK动态代理只能对实现了接口的类生成代理，而不能针对类（默认对接口进行代理）
 * todo 2、Cglib是针对类实现代理,使用ASM字节码生成代理类，主要是对指定的类生成一个子类，覆盖其中的方法，并覆盖其中方法的增强，但是因为采用的是继承，所以该类或方法最好不要生成final，对于final类或方法，是无法继承的
 *  //todo JDK和cglib创建的类区别：
 * @date 2022/3/28 20:41
 */
public class Cglib2AopProxy implements AopProxy {

    private final AdvicedSupport advised;

    public Cglib2AopProxy(AdvicedSupport adviced) {
        this.advised = adviced;
    }


    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        //设置父类,因为Cglib是针对指定的类生成一个子类，所以需要指定父类
        enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));//设置回调
        return enhancer.create();//创建代理并返回
    }


    private static class DynamicAdvisedInterceptor implements MethodInterceptor {

        private final AdvicedSupport advised;

        public DynamicAdvisedInterceptor(AdvicedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, objects, methodProxy);
            if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
                return advised.getMethodInterceptor().invoke(methodInvocation);
            }
            return methodInvocation.proceed();
        }
    }

    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

        private final MethodProxy methodProxy;

        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }

        @Override
        public Object proceed() throws Throwable {
            return this.methodProxy.invoke(this.target, this.arguments);
        }

    }




}
