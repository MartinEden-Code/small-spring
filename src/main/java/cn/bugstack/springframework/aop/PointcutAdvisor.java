package cn.bugstack.springframework.aop;

/**
 * @author hongxingyi
 * @description TODO 把切面pointcut、拦截方法  advice 包装在一起
 * @date 2022/3/30 18:16
 */
public interface PointcutAdvisor extends Advisor{

    PointCut getPointcut();

}
