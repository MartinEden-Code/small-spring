package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.core.io.Recource;
import cn.bugstack.springframework.beans.core.io.RecourceLoader;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/17 10:29
 */

public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    RecourceLoader getRecourceLoader();

    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(Recource... resources) throws BeansException;

    void loadBeanDefinitions(Recource resource) throws BeansException;

    void loadBeanDefinitions(String... locations) throws BeansException;
}
