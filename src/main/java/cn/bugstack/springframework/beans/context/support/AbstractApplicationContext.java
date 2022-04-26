package cn.bugstack.springframework.beans.context.support;


import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.context.ApplicationEvent;
import cn.bugstack.springframework.beans.context.ApplicationListener;
import cn.bugstack.springframework.beans.context.ConfigurableApplicationContext;
import cn.bugstack.springframework.beans.context.event.ApplicationEventMulticaster;
import cn.bugstack.springframework.beans.context.event.ContextRefreshedEvent;
import cn.bugstack.springframework.beans.context.event.SimpleApplicationEventMulticaster;
import cn.bugstack.springframework.beans.core.io.DefaultRecourceLoader;
import cn.bugstack.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.bugstack.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.bugstack.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.event.ApplicationContextEvent;

import java.util.Collection;
import java.util.Map;

/**
 * @author hongxingyi
 * @description TODO 最重要的抽象实现类之一（应用上下文，刷新并加载xml实例化bean，执行销毁方法，事件监听）
 * 面向用户，用户可以自定义相关类继承实现此类扩展功能
 * @date 2022/3/18 16:19
 * 上下文核心类
 */
public abstract class AbstractApplicationContext extends DefaultRecourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    //刷新容器
    @Override
    public void reflush(){

        // 1. 创建 BeanFactory，并xml加载 BeanDefinition（加载资源器）
        refreshBeanFactory();

        // 2. 获取 BeanFactory  (在refreshBeanFactory方法中已经创建过了)
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        /* 另外由于ApplicationContext并不是在AbstractAutowireCapableBeanFactory中createBean方法下的内容，所以需要像容器中注册addBeanPostProcessor  ，
         再由  createBean  统一调用applyBeanPostProcessorsBeforeInitialization时进行操作。*/
        //3. 让所有继承ApplicationConextAware 的Bean对象都能感知到所属的ApplicationContext(如何联通上下文和beanFactory实例化bean?->通过beanPostProcessor,再实例化或者初始化前后做一些操作)
        ApplicationContextAwarePorcessor applicationContextAwarePorcessor = new ApplicationContextAwarePorcessor(this);//将applicationContext赋值给自定义的后置处理器
        beanFactory.addBeanPostProcessor(applicationContextAwarePorcessor);//设置上下文自定义处理器

        // 4. 在 Bean 实例化之前，从 bean中找到beanPostFactoryProcessor定义的相关类,并修改bean信息
        //BeanFactoryPostProcessor是在spring容器加载了bean的定义文件之后，在bean实例化之前执行的。在整个过程中，只调用一次这个接口的实现类。
        invokeBeanFactoryPostProcessors(beanFactory);

        // 5. BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
        //BeanPostProcessor 是spring容器加载bean的定义文件完，且实例化了bean的定义的对象之时执行的
        //todo 比如这一步注册了AutowiredAnnotationBeanPostProcessor（用来注解融入bean生命周期，解析、注入注解） DefaultAdvisorAutoProxyCreator（AOP融入bean生命周期）
        registerBeanPostProcessors(beanFactory);//此方法里也是beanFactory.addBeanPostProcessor(beanPostProcessor);

        // 6. 初始化事件发布者(建立一个简单事件广播器)
        initApplicationEventMulticaster(beanFactory);

        // 7. 注册所有的事件监听器（getBeanType,从简单工厂中找到所有的监听器）
        registerListeners();


        // 8. 提前实例化单例 Bean 对象 注意，3，4步会先实例化 BeanPostProcessor 和 BeanPostFactoryProcessor实例对象，用来修改bean属性，之后再去实例化其余非beanPostProcessor对象
        //这样继承了beanPostProcessor类的自定义对象（比如InstantiaionAwareBeanPostProcessor，用于处理AOP的拦截方法）、就能在 目标对象实例化之时执行处理
        beanFactory.preInstantiateSingletons();

        // 9. 发布容器刷新完成事件
        finishRefresh();
    }



    //通过 AbstractRefreshableApplicationContext 抽象类 实现1、2这两个方法

    protected abstract void refreshBeanFactory();


    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    protected  void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory){
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            //在所有的 BeanDefinition 加载完成后，实例化 Bean 对象之前，提供修改 BeanDefinition 属性的机制
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    protected  void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory){
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }


    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }


    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }


    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }


    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }


    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }
    //*****************************************事件监听**********************************************************


    /**
     * 初始化事件发布者
     */
    protected  void initApplicationEventMulticaster(ConfigurableListableBeanFactory beanFactory){
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);//建立一个简单的事件广播器
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME,applicationEventMulticaster);
    }

    /**
     * 注册事件监听器
     */
    protected  void registerListeners(){
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();//从简单工厂中获取
        for(ApplicationListener listener: applicationListeners){
            applicationEventMulticaster.addApplicationListener(listener);//获取所有的监听器，并加入广播器中
        }
    }

    /**
     * 发布容器刷新完成事件
     */
    protected  void finishRefresh(){
        publishEvent(new ContextRefreshedEvent(this));
    }


    /**
     * 发布事件->向所有监听器广播该事件->找到和该事件有关的监听器->执行监听方法
     * @param event
     */
    @Override
    public void publishEvent(ApplicationEvent event){
        applicationEventMulticaster.muticastEvent(event);//广播所有的监听器，对这个事件event感兴趣的事件则执行
    }

    //******************************************销毁方法**********************************************************
    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        getBeanFactory().destroySingletons();
    }


}
