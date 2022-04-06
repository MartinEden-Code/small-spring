package cn.bugstack.springframework.aop.aspectj;

import cn.bugstack.springframework.aop.PointCut;
import cn.bugstack.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;


/**
 * @author hongxingyi
 * @description TODO 把切面pointcut、拦截方法advice  和具体的拦截表达式包装在一起
 * @date 2022/3/30 18:18
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    //切面
    private AspectJExpressionPointcut pointcut;

    //具体的前置拦截方法
    private Advice beforeadvice;

    //具体的前置拦截方法
    private Advice afteradvice;

    //表达式
    private String expression;

    public Advice getBeforeadvice() {
        return beforeadvice;
    }

    public void setBeforeadvice(Advice beforeadvice) {
        this.beforeadvice = beforeadvice;
    }

    public Advice getAfteradvice() {
        return afteradvice;
    }

    public void setAfteradvice(Advice afteradvice) {
        this.afteradvice = afteradvice;
    }

    @Override
    public PointCut getPointcut() {
        if(null==pointcut){
            pointcut=new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }



    public void setExpression(String expression) {
        this.expression = expression;
    }



    @Override
    public Advice getBeforeAdvice() {
        return beforeadvice;
    }

    @Override
    public Advice getAfterAdvice() {
        return afteradvice;
    }
}
