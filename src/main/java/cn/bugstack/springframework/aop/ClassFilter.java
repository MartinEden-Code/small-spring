package cn.bugstack.springframework.aop;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 19:53
 */
public interface ClassFilter {

    //用于切点找到给定的接口和目标类。
    boolean matches(Class<?> clazz);

}
