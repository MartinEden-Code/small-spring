package cn.bugstack.springframework.beans.context;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/18 16:18
 */
public interface ConfigurableApplicationContext extends ApplicationContext{

    void reflush();

    void registerShutdownHook();

    void close();

}
