package cn.bugstack.springframework.beans.context;

import java.util.EventObject;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 8:47
 */
public abstract class ApplicationEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationEvent(Object source) {
        super(source);
    }


}
