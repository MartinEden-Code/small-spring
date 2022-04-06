package cn.bugstack.springframework.test.event;

import cn.bugstack.springframework.beans.context.ApplicationListener;

import java.util.Date;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/25 17:08
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("收到：" + event.getSource() + "消息;时间：" + new Date());
        System.out.println("消息：" + event.getId() + ":" + event.getMessage());
    }
}
