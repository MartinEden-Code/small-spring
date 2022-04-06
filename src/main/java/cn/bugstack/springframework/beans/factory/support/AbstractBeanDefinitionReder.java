package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.core.io.DefaultRecourceLoader;
import cn.bugstack.springframework.beans.core.io.RecourceLoader;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/17 13:22
 */
public abstract class AbstractBeanDefinitionReder implements BeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private RecourceLoader recourceLoader;


    protected AbstractBeanDefinitionReder(BeanDefinitionRegistry registry) {
        this(registry, new DefaultRecourceLoader());//使用默认的资源加载器
    }



    public AbstractBeanDefinitionReder(BeanDefinitionRegistry registry, RecourceLoader recourceLoader) {
        this.registry = registry;
        this.recourceLoader = recourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public RecourceLoader getRecourceLoader() {
        return recourceLoader;
    }
}
