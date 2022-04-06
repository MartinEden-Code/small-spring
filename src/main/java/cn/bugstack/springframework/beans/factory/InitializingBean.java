package cn.bugstack.springframework.beans.factory;

/**
 * @author hongxingyi
 * @description TODO 初始化
 * @date 2022/3/22 18:20
 */
public interface InitializingBean {
    //bean填充属性后调用
    void afterPropertiesSet() throws Exception;
}
