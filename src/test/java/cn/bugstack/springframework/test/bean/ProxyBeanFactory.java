package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.beans.factory.config.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hongxingyi
 * @description TODO 代理类 主要是模拟了UserDao  的原有功能，类似于  MyBatis  框架中的代理操作
 * @date 2022/3/24 17:10
 */
public class ProxyBeanFactory implements FactoryBean<IUserDao> {

    //getObject()  中提供的就是一个  InvocationHandler  的代理对象，当有方法调用的时候，则执行代理对象的功能。
    @Override
    public IUserDao getObject() throws Exception {
        InvocationHandler handler = (proxy,method,args)->{
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("10001", "小傅哥");
            hashMap.put("10002", "八杯水");
            hashMap.put("10003", "阿毛");

            return "你被代理了 " + method.getName() + "： " + hashMap.get(args[0].toString());
        };
        //todo 代理方法作用原理
        return (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{IUserDao.class},handler);

    }

    @Override
    public Class<?> getObjectType() {
        return IUserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
