package cn.bugstack.springframework.beans.context.event;

import cn.bugstack.springframework.beans.BeansException;
import cn.bugstack.springframework.beans.context.ApplicationEvent;
import cn.bugstack.springframework.beans.context.ApplicationListener;
import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.beans.factory.BeanFactoryAware;
import cn.bugstack.springframework.beans.utils.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 10:38
 */
public abstract class AbstractApplicationEventMulticast implements ApplicationEventMulticaster , BeanFactoryAware {

    //所有的监听器容器
    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    private BeanFactory beanFactory;

    public void addApplicationListener(ApplicationListener<?> listener){
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    public void removeApplicationListener(ApplicationListener<?> listener){
        applicationListeners.remove(listener);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory=beanFactory;
    }


    //获取对某事件感兴趣的监听器
    protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event){
        LinkedList<ApplicationListener> allListeners_forThisEvent= new LinkedList<ApplicationListener>();

        for(ApplicationListener<ApplicationEvent> listener : applicationListeners){
            if(supportsEvent(listener,event)) allListeners_forThisEvent.add(listener);
        }
        return allListeners_forThisEvent;
    }

    //判断该监听器是否对该事件感兴趣
    protected  boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event){
        Class<? extends ApplicationListener> listenerClass =  applicationListener.getClass();

        // 按照 CglibSubclassingInstantiationStrategy、SimpleInstantiationStrategy 不同的实例化类型，需要判断后获取目标 class
        Class<?> targetClass = ClassUtils.isCglibProxyClass(listenerClass) ? listenerClass.getSuperclass() : listenerClass;
        Type genericInterface = targetClass.getGenericInterfaces()[0];

        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        String className = actualTypeArgument.getTypeName();
        Class<?> eventClassName;
        try {
            eventClassName = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + className);
        }
        // 判定此 eventClassName 对象所表示的类或接口与指定的 event.getClass() 参数所表示的类或接口是否相同，或是否是其超类或超接口。
        // isAssignableFrom 是用来判断子类和父类的关系的，或者接口的实现类和接口的关系的，默认所有的类的终极父类都是 Object。
        // 如果 A.isAssignableFrom(B)结果是 true，证明 B 可以转换成为 A,也就是 A 可以由 B 转换而来。
        return eventClassName.isAssignableFrom(event.getClass());
    }


}