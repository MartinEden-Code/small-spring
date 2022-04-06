package cn.bugstack.springframework.beans.context.support;

import cn.bugstack.springframework.beans.BeansException;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/20 23:08
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{


    private String[] configLocations;

    public  ClassPathXmlApplicationContext(){

    }

    //从XML中加载 BeanDefinition。并刷新上下文
    public ClassPathXmlApplicationContext(String configLocations){
        this(new String[]{configLocations});
    }

    //从XML中加载 BeanDefinition。并刷新上下文
    public ClassPathXmlApplicationContext(String[] configLocations){
        this.configLocations=configLocations;
        reflush();
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }


    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return null;
    }
}
