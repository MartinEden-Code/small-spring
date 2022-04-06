package cn.bugstack.springframework.beans.context;

import java.util.EventListener;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 9:56
 */
public interface ApplicationListener<E extends ApplicationEvent>  extends EventListener {
    
    void onApplicationEvent(E event);
}
