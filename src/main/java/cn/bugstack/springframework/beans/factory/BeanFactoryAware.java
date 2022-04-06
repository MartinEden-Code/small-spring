package cn.bugstack.springframework.beans.factory;

/**
 * @author hongxingyi
 * @description TODO 实现此接口，既能感知到所属的 BeanFactory
 * @date 2022/3/23 15:58
 */
public interface BeanFactoryAware extends Aware{
    void setBeanFactory(BeanFactory beanFactory);
}
