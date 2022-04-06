package cn.bugstack.springframework.beans.context.event;

/**
 * @author hongxingyi
 * @description TODO 内置上下文刷新事件
 * @date 2022/3/28 8:52
 */
public class ContextRefreshedEvent extends ApplicationContextEvent{

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextRefreshedEvent(Object source) {
        super(source);
    }


}
