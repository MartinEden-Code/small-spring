package cn.bugstack.springframework.beans.context;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 10:16
 */
public interface ApplicationEventPublicsher {

    void publishEvent(ApplicationEvent event);

}
