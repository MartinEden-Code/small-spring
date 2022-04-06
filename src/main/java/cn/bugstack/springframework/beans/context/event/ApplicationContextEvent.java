package cn.bugstack.springframework.beans.context.event;

import cn.bugstack.springframework.beans.context.ApplicationContext;
import cn.bugstack.springframework.beans.context.ApplicationEvent;

/**
 * @author hongxingyi
 * @description TODO
 * 定义的事件包括关闭、刷新，以及用户自己实现的事件，都需要继承这个类
 * @date 2022/3/28 8:48
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationContextEvent(Object source) {
        super(source);
    }


    public final ApplicationContext getApplicationContext(){
        return (ApplicationContext) getSource();
    }
}
