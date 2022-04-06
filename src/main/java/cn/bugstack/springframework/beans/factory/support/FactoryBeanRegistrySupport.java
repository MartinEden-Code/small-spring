package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.config.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongxingyi
 * @description TODO DefaultSingletonBeanRegistry 默认是取单例对象，FactoryBeanRegistrySupport 扩展成取多种类型对象，比如原型等
 *  此抽象类依赖FactoryBean接口
 * @date 2022/3/24 11:36
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

    //和DefaultSingletonBeanRegistry 里的singletonObjects作用类似，如果一个bean对象是单例的话，则singletonObjects和factoryBeanObjectCache都会存这个bean对象
    //如果是单例对象，是先添加到singletonObjects容器里，再添加到factoryBeanObjectCache容器里
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>();

    /**
     * 从缓存中获取对象
     * @param beanName
     * @return
     */
    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object object = this.factoryBeanObjectCache.get(beanName);
        return (object != NULL_OBJECT ? object : null);

    }

    /**
     * 判断是否是单例，从缓存中获取对象，如果不存在则创建存入
     * @param factory
     * @param beanName
     * @return
     */
    protected Object getObjectFromFactoryBean(FactoryBean factory, String beanName) {
        if (factory.isSingleton()) {
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object == null) {
                object = doGetObjectFromFactoryBean(factory, beanName);
                this.factoryBeanObjectCache.put(beanName, (object != null ? object : NULL_OBJECT));
            }
            return (object != NULL_OBJECT ? object : null);
        } else {
            return doGetObjectFromFactoryBean(factory, beanName);
        }
    }

    /**
     * 依赖了FactoryBean的getObject方法，返回Bean实例对象外，实现了FactoryBean的子类中可以在getObejct方法中添加相应处理
     *  比如getObject中提供的就是一个  InvocationHandler  的代理对象，当有方法调用的时候，则执行代理对象的功能。
     * @param factory
     * @param beanName
     * @return
     */
    private Object doGetObjectFromFactoryBean(final FactoryBean factory, final String beanName){
        try {
            return factory.getObject();//getObject
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
        }
    }
}
