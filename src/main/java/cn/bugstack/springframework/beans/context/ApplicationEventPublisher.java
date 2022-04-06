package cn.bugstack.springframework.beans.context;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 13:18
 */
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
