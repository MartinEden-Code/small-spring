package cn.bugstack.springframework.beans.context.support;

import cn.bugstack.springframework.beans.context.ApplicationContext;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/23 16:10
 */
public interface ApplicationConextAware {
    void setApplicationContext(ApplicationContext applicationContext);
}
