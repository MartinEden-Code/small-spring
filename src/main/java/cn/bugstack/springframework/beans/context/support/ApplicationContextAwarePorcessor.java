package cn.bugstack.springframework.beans.context.support;

import cn.bugstack.springframework.beans.context.ApplicationContext;
import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author hongxingyi
 * @description TODO 上下文包装感知容器处理器
 * @date 2022/3/23 16:12
 */
public class ApplicationContextAwarePorcessor implements BeanPostProcessor{

    private final ApplicationContext applicationContext;

    public ApplicationContextAwarePorcessor(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if(bean instanceof ApplicationConextAware){
            ((ApplicationConextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return null;
    }
}
