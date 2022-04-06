package cn.bugstack.springframework.beans.factory;

import cn.bugstack.springframework.beans.BeansException;

/**
 * @author hongxingyi
 * @description TODO 简单工厂
 * @date 2022/3/16 11:01s
 */
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    //更具类属性类型（蔽日double float等）返回相应的bean
    <T> T getBean(Class<T> requiredType) throws BeansException;
}
