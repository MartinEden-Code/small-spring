package cn.bugstack.springframework.aop.framework.autoproxy;

import cn.bugstack.springframework.aop.AdvicedSupport;
import cn.bugstack.springframework.aop.Advisor;
import cn.bugstack.springframework.aop.ClassFilter;
import cn.bugstack.springframework.aop.TargetSource;
import cn.bugstack.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.bugstack.springframework.aop.framework.ProxyFactory;
import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.beans.factory.BeanFactoryAware;
import cn.bugstack.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;

import java.util.Collection;

/**
 * @author hongxingyi
 * @description TODO 融入Bean生命周期的自动代理创建者 核心类
 * todo 继承了beanPostProcessor，再目标对象初始化之前实现拦截方法 整合了顾问包装器、动态代理包装器等，用以实现动态代理
 * @date 2022/3/30 18:08
 */

public class DefaultAdvisorAutoProxyCreator implements BeanFactoryAware, InstantiationAwareBeanPostProcessor {


    private DefaultListableBeanFactory beanFactory;


    //实现此接口，既能感知到所属的 BeanFactory，在bean初始化前后initializeBean 方法中判断 此bean是否实现了BeanFacooryAware类，将beanFactory赋值过来
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory= (DefaultListableBeanFactory) beanFactory;
    }

    /**
     * todo 动态代理对象创建具体实现方法，在bean生命周期注册前判断为代理对象时调用
     * @param beanClass
     * @param beanName
     * @return AOP动态代理对象
     * @throws Exception
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws Exception {

        /*//判断是不是代理类
        if (isInfrastructureClass(beanClass)) return null;

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(beanClass)) continue;

            AdvicedSupport advisedSupport = new AdvicedSupport();

            TargetSource targetSource = null;
            try {
                targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //代理类
            advisedSupport.setTargetSource(targetSource);

            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getBeforeAdvice());//注入前置切面后置拦截方法
            //advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAfterAdvice());

            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(false);
            return new ProxyFactory(advisedSupport).getProxy();

        }*/

        return null;
    }

    /**
     * Bean 实例化后对于返回 false 的对象，不在执行后续设置 Bean 对象属性的操作，默认给代理对象设置属性的权限
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws Exception {
        return pvs;
    }


    //判断beanClass是否为下面几个类的子类
    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    //aop代理属性注入需要用到这个方法
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {

        //判断是不是代理类
        if (isInfrastructureClass(bean.getClass())) return bean;

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(bean.getClass())) continue;

            AdvicedSupport advisedSupport = new AdvicedSupport();

            TargetSource targetSource = null;
            try {
                targetSource = new TargetSource(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //代理类
            advisedSupport.setTargetSource(targetSource);

            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getBeforeAdvice());//注入前置切面后置拦截方法
            //advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAfterAdvice());

            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(false);

            //返回代理对象
            return new ProxyFactory(advisedSupport).getProxy();

        }

        return bean;
    }


}
