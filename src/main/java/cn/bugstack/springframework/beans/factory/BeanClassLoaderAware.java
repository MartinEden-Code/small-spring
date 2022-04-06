package cn.bugstack.springframework.beans.factory;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/23 15:57
 */
public interface BeanClassLoaderAware extends Aware{
    void setBeanClassLoader(ClassLoader classLoader);
}
