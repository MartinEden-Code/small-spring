package cn.bugstack.springframework.beans.factory;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/23 15:56
 */
public interface BeanNameAware extends Aware{
    void setBeanName(String name);
}
