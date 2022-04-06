package cn.bugstack.springframework.beans.factory.config;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValues;

/**
 * @author hongxingyi
 * @description TODO AOP代理对象处理，动态代理融入bean生命周期（继承beanPostProcessor，在注册之前判断是否是否是代理对象，如果是，则执行下面方法返回代理对象），如果判断该对象被代理则返回代理对象
 * @date 2022/3/30 18:12
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

    //在目标Bean对象执行初始化方法之前（对象注册之前），执行此方法，以返回代理对象
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws Exception;

    /**
     * Perform operations after the bean has been instantiated, via a constructor or factory method,
     * but before Spring property population (from explicit properties or autowiring) occurs.
     * <p>This is the ideal callback for performing field injection on the given bean instance.
     * See Spring's own {@link cn.bugstack.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor}
     * for a typical example.
     * <p>
     * 在 Bean 对象执行初始化方法之后，执行此方法
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

    //自定义注解 属性填充
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws Exception;
}
