package cn.bugstack.springframework.beans.context.event;

/**
 * @author hongxingyi
 * @description TODO 内置上下文关闭事件
 * @date 2022/3/28 8:53
 */
public class ContextClosedEvent extends ApplicationContextEvent{
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextClosedEvent(Object source) {
        super(source);
    }


}
