package cn.bugstack.springframework.test.event;

import cn.bugstack.springframework.beans.context.ApplicationListener;
import cn.bugstack.springframework.beans.context.event.ContextRefreshedEvent;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/25 17:08
 */
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("刷新事件："+this.getClass().getName());
    }
}
