package cn.bugstack.springframework.beans.factory.config;

/**
 * @author hongxingyi
 * 工厂方法
 * @description TODO MyBatis 就是实现了一个 MapperFactoryBean 类，在 getObject 方法中提供 SqlSession 对执行CRUD 方法的操作
 * @date 2022/3/24 11:39
 */
public interface FactoryBean<T> {
    //getObject()  中提供的就是一个  InvocationHandler  的代理对象，当有方法调用的时候，则执行代理对象的功能。
    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();
}
