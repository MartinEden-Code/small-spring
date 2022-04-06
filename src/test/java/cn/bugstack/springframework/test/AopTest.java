package cn.bugstack.springframework.test;

import cn.bugstack.springframework.aop.AdvicedSupport;
import cn.bugstack.springframework.aop.ClassFilter;
import cn.bugstack.springframework.aop.MethodMatcher;
import cn.bugstack.springframework.aop.TargetSource;
import cn.bugstack.springframework.aop.aspectj.AspectJExpressionPointcut;
import cn.bugstack.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.bugstack.springframework.aop.framework.Cglib2AopProxy;
import cn.bugstack.springframework.aop.framework.JdkDynamicAopProxy;
import cn.bugstack.springframework.aop.framework.ProxyFactory;
import cn.bugstack.springframework.aop.framework.ReflectiveMethodInvocation;
import cn.bugstack.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;
import cn.bugstack.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import cn.bugstack.springframework.beans.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.test.bean.*;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author hongxingyi
 * @description TODO 通过预编译的方式和运行期间(动态代理)实现程序功能功能的统一维护,
 * todo 重点是动态代理,方法拦截，给A代理添加一些自定义处理，比如日志打印，记录耗时，监控异常
 * //todo
 * @date 2022/3/28 19:35
 */
public class AopTest {



    //Chapters 1 AOP
    @Test
    public void test_proxy_method() {

        // 目标对象(可以替换成任何的目标对象)
        Object targetObj = new UserService();

        // AOP 代理
        IUserService proxy = (IUserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), targetObj.getClass().getInterfaces(), new InvocationHandler() {
            // 方法匹配器
            MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* cn.bugstack.springframework.test.bean.IUserService.*(..))");

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (methodMatcher.matches(method, targetObj.getClass())) {
                    // 方法拦截器（可自定义方法拦截器，）
                    MethodInterceptor methodInterceptor = invocation -> {
                        long start = System.currentTimeMillis();
                        try {
                            return invocation.proceed();
                        } finally {
                            System.out.println("监控 - Begin By AOP");
                            System.out.println("方法名称：" + invocation.getMethod().getName());
                            System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
                            System.out.println("监控 - End\r\n");
                        }
                    };
                    // 方法拦截器反射调用
                    return methodInterceptor.invoke(new ReflectiveMethodInvocation(targetObj, method, args));
                }
                //代理的反射调用
                return method.invoke(targetObj, args);
            }

        });

        String result = proxy.queryUserInfo();
        System.out.println(proxy.register("江小白"));
        System.out.println("测试结果：" + result);

    }

    //动态代理
    @Test
    public void test_dynamic() {
        // 目标对象
        IUserService userService = new UserService();
        // 组装动态代理信息
        AdvicedSupport advisedSupport = new AdvicedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodInterceptor(new UserServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* cn.bugstack.springframework.test.bean.IUserService.*(..))"));
        //todo 1、JDK动态代理只能对实现了接口的类生成代理，而不能针对类（默认对接口进行代理）
        //todo 2、Cglib是针对类实现代理，主要是对指定的类生成一个子类，覆盖其中的方法，并覆盖其中方法的增强，但是因为采用的是继承，所以该类或方法最好不要生成final，对于final类或方法，是无法继承的

        // 代理对象(JdkDynamicAopProxy)
        IUserService proxy_jdk = (IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        // 测试调用
        System.out.println("测试结果：" + proxy_jdk.queryUserInfo());

        // 代理对象(Cglib2AopProxy)
        IUserService proxy_cglib = (IUserService) new Cglib2AopProxy(advisedSupport).getProxy();

        // 测试调用
        System.out.println("测试结果：" + proxy_cglib.register("花花"));
    }

    //chapter 2 将AOP整合到 bean的生命周期

    public AdvicedSupport init_AdvicedSupport(){
        IUserService userService = new UserService();
        AdvicedSupport advisedSupport = new AdvicedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodInterceptor(new UserServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* cn.bugstack.springframework.test.bean.IUserService.*(..))"));
        advisedSupport.setProxyTargetClass(false); //默认JDK动态代理
        return advisedSupport;
    }
    /**
     * 代理工厂，集成了JDK动态代理 和 cglib代理两种方式
     * @throws Exception
     */
    @Test
    public void test_ProxyFactory() throws Exception {

        IUserService proxy = (IUserService) new ProxyFactory(init_AdvicedSupport()).getProxy();
        System.out.println(proxy.queryUserInfo());
    }

    /**
     * 测试拦截器前置处理方法
     * @throws Exception
     */
    @Test
    public void test_advice() throws Exception {
        //初始化动态代理包装类
        AdvicedSupport advicedSupport = init_AdvicedSupport();

        //自定义拦截器 前置方法
        UserServiceBeforeAdvice beforeAdvice = new UserServiceBeforeAdvice();

        UserServiceAfterAdvice afterAdvice = new UserServiceAfterAdvice();

        //讲自定义拦截器前置方法嵌入 动态代理中的反射方法中
        //前置拦截
        MethodBeforeAdviceInterceptor beforeAdviceInterceptor = new MethodBeforeAdviceInterceptor(beforeAdvice);

        //后置拦截
        AfterReturningAdviceInterceptor adviceInterceptor = new AfterReturningAdviceInterceptor(afterAdvice);
        //将含有前置处理方法的拦截器方法放入 动态代理包装类中
        advicedSupport.setMethodInterceptor(adviceInterceptor);

        //获取代理类
        IUserService proxy = (IUserService) new ProxyFactory(advicedSupport).getProxy();

        System.out.println(proxy.queryUserInfo());


    }

    //advisor顾问，包装通知advice
    @Test
    public void test_advisor() throws Exception {

        //目标对象
        IUserService userService = new UserService();

        //顾问包装器，将advice通知、PointCut(默认AspectJExpressionPointcut),切点表达式等包装起来
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression("execution(* cn.bugstack.springframework.test.bean.IUserService.*(..))");
        advisor.setBeforeadvice(new MethodBeforeAdviceInterceptor(new UserServiceBeforeAdvice()));
        advisor.setAfteradvice(new AfterReturningAdviceInterceptor(new UserServiceAfterAdvice()));

        ClassFilter classFilter = advisor.getPointcut().getClassFilter();
        if(classFilter.matches(userService.getClass())){//判断目标对象是否被代理
            AdvicedSupport advisedSupport = new AdvicedSupport();
            TargetSource targetSource = new TargetSource(userService);
            advisedSupport.setTargetSource(targetSource);
            //advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getBeforeAdvice());
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAfterAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(false); // false/true，JDK动态代理、CGlib动态代理

            IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
            System.out.println("测试结果：" + proxy.queryUserInfo());
        }
    }

    //将AOP融入spring Bean周期 核心实现类 DefaultAdvisorAutoProxyCreator，继承了beanPostProcessor，再目标对象初始化之前实现拦截方法 整合了顾问包装器、动态代理包装器等，用以实现动态代理
    @Test
    public void test_aop() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");

        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());//未做属性注入，userService属性company输出未null
    }


    //给代理对象设置属性注入  在DefaultAdvisorAutoProxyCreator 的 postProcessAfterInitialization方法中（初始化后）进行处理 ，
    // 因为已经初始化了，所以属性自然已经注入了（原来是在postProcessBeforeInstantiation中处理，在初始化之前进行代理，次数属性暂未注入）
    @Test
    public void autoProxy(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }





}
