package cn.bugstack.springframework.beans.context.support;

import cn.bugstack.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/18 16:30
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    DefaultListableBeanFactory beanFactory;

    @Override
    public  void refreshBeanFactory(){

        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;

    }

    private DefaultListableBeanFactory createBeanFactory(){
        return new DefaultListableBeanFactory();
    }


    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);


    @Override
    protected ConfigurableListableBeanFactory getBeanFactory(){
        return beanFactory;
    }




}
