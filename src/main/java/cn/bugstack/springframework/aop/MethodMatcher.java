package cn.bugstack.springframework.aop;

import java.lang.reflect.Method;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 19:53
 */
public interface MethodMatcher {

    //方法匹配，找到表达式范围内匹配下的目标类和方法
    boolean matches(Method method, Class<?> targetClass);

}
