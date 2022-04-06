package cn.bugstack.springframework.test.event;

import cn.bugstack.springframework.beans.context.ApplicationListener;
import cn.bugstack.springframework.beans.context.event.ContextClosedEvent;

/**
 * @author hongxingyi
 * @description TODO 自定义上下文关闭事件监听器
 * @date 2022/3/25 17:09
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件： "+this.getClass().getName());
    }
}
