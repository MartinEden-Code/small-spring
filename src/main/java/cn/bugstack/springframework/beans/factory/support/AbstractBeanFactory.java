package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanPostProcessor;
import cn.bugstack.springframework.beans.factory.config.ConfigurableBeanFactory;
import cn.bugstack.springframework.beans.factory.config.FactoryBean;
import cn.bugstack.springframework.beans.utils.ClassUtils;
import cn.bugstack.springframework.beans.utils.StringValueResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongxingyi
 * @description todo 主要是实现breanFactory 的getBean方法，扩展了对象作用域（单例、原型）和FactoryBean扩展（主要是getObject()）
 *
 * @date 2022/3/16 11:06
 * BeanDefinition 注册表接口
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    /** BeanPostProcessors to apply in createBean */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    // 添加字符串解析器，供解析@Value注解使用
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();


    //方式1.通过单例缓存获取bean

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }


    protected <T> T doGetBean(final String name, final Object[] args) {
        Object sharedInstance = getSingleleton(name);//单例模式将bean对象放到内存中，下次获取直接从内存中获取，原型模式则每次都重新创建新的对象
        if (sharedInstance != null) {
            // 如果是 FactoryBean，则需要调用 FactoryBean，否则直接返回bean对象
            return (T) getObjectForBeanInstance(sharedInstance,name);
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object bean = createBean(name, beanDefinition, args);
        return (T) getObjectForBeanInstance(bean,name);
    }


    //如果是 FactoryBean，则需要调用 FactoryBean#getObject
    private Object getObjectForBeanInstance(Object beanInstance,String beanName){

        if(!(beanInstance instanceof  FactoryBean)){
            return beanInstance;
        }

        //
        Object object = getCachedObjectForFactoryBean(beanName);

        if(object==null){
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object=getObjectFromFactoryBean(factoryBean,beanName);
        }

        return object;
    }


    //方式2.通过注册实例化bean

    //获取注册表信息
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    //抽象方法必须被某个子类（如果子类还是抽象方法可以实现，再留给子类的子类实现）实现
    public abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;



    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * Return the list of BeanPostProcessors that will get applied
     * to beans created with this factory.
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }


    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }


    /**
     * 添加字符串解析器，供解析@Value注解使用
     * @param valueResolver the String resolver to apply to embedded values
     */
    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    /**
     * 字符串解析
     * @param value the value to resolve
     * @return
     */
    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }

}
