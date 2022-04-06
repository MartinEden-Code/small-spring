package cn.bugstack.springframework.aop;


import org.aopalliance.aop.Advice;

/**
 * @author hongxingyi
 * @description TODO 访问者
 * @date 2022/3/30 16:55
 */
public interface Advisor {

    Advice getBeforeAdvice();

    Advice getAfterAdvice();

}
