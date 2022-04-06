package cn.bugstack.springframework.beans.factory.config;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/16 11:04
 */
public interface SingletonBeanRegistory {

    Object getSingleleton(String beanName);

    void registerSingleton(String beanName,Object singletonObject);

}
