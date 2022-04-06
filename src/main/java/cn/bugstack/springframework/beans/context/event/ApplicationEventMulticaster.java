package cn.bugstack.springframework.beans.context.event;

import cn.bugstack.springframework.beans.context.ApplicationEvent;
import cn.bugstack.springframework.beans.context.ApplicationListener;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 10:35
 */
public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);

    void removeApplicationListener(ApplicationListener<?> listener);

    //事件广播
    void muticastEvent(ApplicationEvent event);
}
