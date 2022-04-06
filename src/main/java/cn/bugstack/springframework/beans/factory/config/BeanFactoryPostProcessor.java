package cn.bugstack.springframework.beans.factory.config;

import cn.bugstack.springframework.beans.factory.ConfigurableListableBeanFactory;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/18 16:45
 */
public interface BeanFactoryPostProcessor  {

    //可以通过beanFactory获取bean定义信息并修改
    // BeanDefinition 加载完成 & Bean 实例化之前，修改 BeanDefinition 的属性值
    /**
     * 在所有的 BeanDefinition 加载完成后，实例化 Bean 对象之前，提供修
     改 BeanDefinition 属性的机制
     **/
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);

}
