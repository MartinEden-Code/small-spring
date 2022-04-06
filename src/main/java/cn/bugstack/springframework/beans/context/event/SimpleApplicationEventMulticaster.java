package cn.bugstack.springframework.beans.context.event;

import cn.bugstack.springframework.beans.context.ApplicationEvent;
import cn.bugstack.springframework.beans.context.ApplicationListener;
import cn.bugstack.springframework.beans.factory.BeanFactory;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 11:24
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticast{
    public SimpleApplicationEventMulticaster(BeanFactory beanFactory){
        setBeanFactory(beanFactory);
    }

    //获取所有与该事件有关的监听器->执行该监听器的方法
    @Override
    public void muticastEvent(final ApplicationEvent event) {
        for(final ApplicationListener listener: getApplicationListeners(event)){//getApplicationListeners方法是获取对某事件感兴趣的监听器
            listener.onApplicationEvent(event);
        }
    }
}
