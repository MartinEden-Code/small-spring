package cn.bugstack.springframework.beans.factory.config;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/18 16:45
 * 目标对象初始化前后执行相应方法
 */
public interface BeanPostProcessor {

    //在 Bean 对象执行 初始化 方法之前，执行此方法

    Object postProcessBeforeInitialization(Object bean,String beanName);

    //在 Bean 对象执行 初始化 方法之后，执行此方法
    Object postProcessAfterInitialization(Object bean,String beanName) throws Exception;

}
