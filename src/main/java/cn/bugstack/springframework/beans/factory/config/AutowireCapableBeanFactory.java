package cn.bugstack.springframework.beans.factory.config;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.BeanFactory;

/**
 * Extension of the {@link BeanFactory}
 * interface to be implemented by bean factories that are capable of
 * autowiring, provided that they want to expose this functionality for
 * existing bean instances.
 *
 * 上下文bean实例化前后处理
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    //上下文bean实例化前后处理

    /**
     * 执行 BeanPostProcessors 接口实现类的 postProcessBeforeInitialization 方法
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    /**
     * 执行 BeanPostProcessors 接口实现类的 postProcessorsAfterInitialization 方法
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws Exception;



}
