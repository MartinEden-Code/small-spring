package cn.bugstack.springframework.aop;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/28 19:53
 */
public interface PointCut {

    //获取切点表达式提供的内容
    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();


}
