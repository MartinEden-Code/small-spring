package cn.bugstack.springframework.aop;

import cn.bugstack.springframework.beans.utils.ClassUtils;

/**
 * @author hongxingyi
 * @description TODO 被代理的目标对象
 * @date 2022/3/28 20:30
 */
public class TargetSource {

    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    /**
     * 获取此对象的所有接口方法
     * @return the type of targets returned by this {@link TargetSource}
     */
    public Class<?>[] getTargetClass(){
        Class<?> clazz = this.target.getClass();
        //个  target  可能是  JDK 代理  创建也可能是  CGlib 创建，为了保证都能正确的获取到结果，这里需要增加判读
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        return clazz.getInterfaces();//获取此对象的所有接口方法
    }

    /**
     * 获取目标对象
     */
    public Object getTarget(){
        return this.target;
    }
}
