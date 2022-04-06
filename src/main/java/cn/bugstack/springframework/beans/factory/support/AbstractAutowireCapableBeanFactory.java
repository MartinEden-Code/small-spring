package cn.bugstack.springframework.beans.factory.support;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.factory.*;
import cn.bugstack.springframework.beans.factory.config.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author hongxingyi
 * @description TODO 最终要的抽象实现类之一（实例化,初始化，属性填充，Aware感知对象设置等）
 * @date 2022/3/16 11:34
 *
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory{

    //实例化bean类

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();




    /**
     *
     * 注册bean核心方法  todo 步骤 实例化createBeanInstance -> 属性填充 applyPropertyVaules -> 初始化 initilizeBean
     * @param beanName
     * @param beanDefinition
     * @param args
     * @return
     * @throws BeansException
     */
    //注册bean，适用于有参构造函数，以及属性填充、初始化、上下文实例化前后属性修改、bean获取Aware感知对象（判断是否instanceof Aware接口，然后在初始化时设置相关属性）、AOP代理处理等
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;

        try {
            // 判断是否返回AOP代理 Bean 对象,如果是，则不需要初始化，返回代理对象
            bean = resolveBeforeInstantiation(beanName, beanDefinition);
            if (null != bean) {
                return bean;
            }

            // step1.实例化 Bean
            bean = createBeanInstance(beanDefinition, beanName, args);

            // 实例化后判断 Bean 实例化后对于返回 false 的对象，不在执行后续设置 Bean 对象属性的操作
            // (默认给DefaultAdvisorAutoProxyCreator 、 AutowiredAnnotationBeanPostProcessor开发属性修改权限)
            boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, bean);
            if (!continueWithPropertyPopulation) {
                return bean;
            }

            // step2.给 Bean 填充属性

            // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值（目前只有注解注入属性）
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);

            // (通过xml手动配置bean标签属性值，或者某属性值类里取值，然后填充属性)
            applyPropertyValues(beanName, bean, beanDefinition);

            //初始化前后修改bean属性;执行bean的初始化方法;感知Aware对象
            //setp3.执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
            bean = initializeBean(beanName,bean,beanDefinition);

        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        //注册实现了 DisposableBean 接口的bean对象（定理了销毁方法）
        registerDisposableBeanIfNecessary(beanName,bean,beanDefinition);

        //判断Scope_Singleton、Scope_Prototype
        if(beanDefinition.isSingleton()) {
            addSingleton(beanName, bean);//单例模式和原型模式的区别就在于是否存放到内存中，单例模式不妨到内存中
        }
        return bean;

    }


    //**********************************AOP代理融入bean生命周期部分代码******************************************
    //判断是否是代理返回的对象
    protected  Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) throws Exception {

        Object bean  = applyBeanPostProcessorsBeforeInitialization(beanDefinition.getBeanClass(),beanName);
        if(null!=bean){
            bean=applyBeanPostProcessorsAfterInitialization(bean,beanName);
        }
        return bean;
    }

    //处理AOP动态代理,返回动态代理对象
    public Object applyBeanPostProcessorsBeforeInitialization(Class<?> beanClass, String beanName) throws Exception {

        for(BeanPostProcessor beanPostProcessor : getBeanPostProcessors()){
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass,beanName);
                if(null!=result){
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Bean 实例化后对于返回 false 的对象，不在执行后续设置 Bean 对象属性的操作
     *
     * @param beanName
     * @param bean
     * @return
     */
    private boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
        boolean continueWithPropertyPopulation = true;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor = (InstantiationAwareBeanPostProcessor) beanPostProcessor;
                if (!instantiationAwareBeanPostProcessor.postProcessAfterInstantiation(bean, beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
        return continueWithPropertyPopulation;
    }

    //****************************注解属性注入*******************************
    protected  void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {

        for(BeanPostProcessor beanPostProcessor : getBeanPostProcessors()){
            /*if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(),bean,beanName);
            }*/
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                //这里再次添加有什么用？上一步已经注入好属性值了
                if (null != pvs) {
                    for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }
            }
        }

    }





    //注册bean，只使用于无参构造函数
    public Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException{
        Object bean = null;
        try {
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        addSingleton(beanName, bean);
        return bean;

    }

    //实例化前后修改bean属性，对目标bean做前后置处理等
    private Object initializeBean(String beanName,Object bean,BeanDefinition beanDefinition) throws Exception {

        //Aware感知对象判断并设置
        // invokeAwareMethods
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }

        // 1. 执行 BeanPostProcessor Before 处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // 执行bean对象的初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        }catch (Exception e){
            throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", e);
        }

        // 2. 执行 BeanPostProcessor After 处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        return wrappedBean;

    }


    //初始化方法

    /**
     *
     * @param beanName
     * @param wrappedBean 实例化后的bean对象
     * @param beanDefinition bean对应的beanDefinition，
     * @throws Exception
     */
    protected  void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) throws Exception{

        //下面两种初始化方法也可也写成一个适配器整合起来
        // setp1. 实现接口 的初始化方法
        if (wrappedBean instanceof InitializingBean) {
            ((InitializingBean) wrappedBean).afterPropertiesSet();
        }

        //setp2 实现配置init-method 的初始化方法（一些属性的初始化值设置等）
        String initMethodName = beanDefinition.getInitMethodName();
        if(StrUtil.isNotEmpty(initMethodName)){
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            if(null==initMethod){
                throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
            }
            initMethod.invoke(wrappedBean);
        }
    }

    /**
     *
     * 注册对象（实现了DisposableBean接口的bean对象）
     * 这里还有一段适配器的使用，因为反射调用和接口直接调用，是两种方式。所以需要使用适配器进行包装
     * 在创建  Bean  对象的实例的时候，需要把销毁方法保存起来，方便后续执行销毁动作进行调用。
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        //非单例singleton类型的bean不执行销毁方法
        if(!beanDefinition.isSingleton()) return;

        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            //继承的AbstractBeanFactory 继承了 DefaultSingletonBeanRegistry中的 registerDisPosableBean方法
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }


    //bean实例化前置处理
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
    }

    //bean实例化后置处理
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws Exception {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
    }


    //实例化bean 在这一步可以添加前置处理和后置处理 (加载 注册  前置处理（修改bean信息）实例化 后置处理（扩展bean信息） )
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        Constructor constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor ctor : declaredConstructors) {
            if (null != args && ctor.getParameterTypes().length == args.length) {
                constructorToUse = ctor;
                break;
            }
        }
        return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
    }

    /**
     * Bean 属性填充
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {

                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                if (value instanceof BeanReference) {
                    // A 依赖 B，获取 B 的实例化
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                // 属性填充
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values：" + beanName);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }






}
