package com.example.demo.core.aspect;
import com.yuantu.frontgateway.common.request.dto.BaseRequestDTO;
import com.yuantu.frontgateway.common.response.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.aspectj.lang.JoinPoint;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/2/21 14:14
 */


/**
 * 此类为一个切面类，主要作用就是对接口的请求进行拦截
 * 拦截的方式，只需要在指定接口方法上面加上@MonitorRequest注解即可
 */

/*1、@controller 控制器（注入服务）
用于标注控制层，相当于struts中的action层

2、@service 服务（注入dao）
用于标注服务层，主要用来进行业务的逻辑处理

3、@repository（实现dao访问）
用于标注数据访问层，也可以说用于标注数据访问组件，即DAO组件.

4、@component （把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>）
泛指各种组件，就是说当我们的类不属于各种归类的时候（不属于@Controller、@Services等的时候），我们就可以使用@Component来标注这个类。*/



@Aspect
@Component
public class RequestAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestAspect.class);

    /**
     * 在执行被MoniterRequest 方法之前，会执行doBefore方法
     * joinpoint 就是被拦截点
     * @param joinpoint
     */
    @Before(value = "@annotation(com.example.demo.annotation.MoniterRequest)") //@annotation 指定具体的类
    public void doBefore(JoinPoint joinpoint){
        //获取请求属性
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        //获取请求对象
        HttpServletRequest servletRequest = servletRequestAttributes.getRequest();

        //打印请求信息
        //URL：根据请求对象拿到访问的地址
        logger.info("url=" + servletRequest.getRequestURL());
        //获取请求的方法，是Get还是Post请求
        logger.info("method=" + servletRequest.getMethod());
        //ip：获取到访问
        logger.info("ip=" + servletRequest.getRemoteAddr());

        logger.info("类名：{}",joinpoint.getSignature().getName());

    }


    /**
     * 切点，controller包下的所有类,对参数进行校验
     * 切面表达式
     *     1 所有公有方法的执行 execution(public * *(..))
     *     2 所有以set开头的公有方法的执行 execution(* set*(..))
     *     3 AccountService接口下的所有方法的执行 execution(* com.xyz.service.AccountService.*(..))
     *     4 com.xyz.service包下的所有方法的执行 execution(* com.xyz.service.*.*(..))
     *     5 com.xyz.service包及其子包下的所有方法的执行 execution(* com.xyz.service..*.*(..))
     *     6 匹配com.xyz.service包下的所有类的所有方法（不含子包）within(com.xyz.service.*)
     *     7 com.xyz.service包和子包的所有方法 within(com.xyz.service..*)
     *     8 匹配AccountService的代理类（不支持通配符）this(com.xyz.service.AccountService)
     * @param requestDTO
     * @param <T>
     */
    @Pointcut("execution(public * com.example.demo.controller.*.*(..))")
    public <T extends BaseRequestDTO> void bizService() {

    }

    @Order(1)
    @Around(value = "bizService()") // @pointcut 可以指定具体的类，也可以指定某个包下的所有类
    public <T extends BaseRequestDTO> Result gatewayLog(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //判断请求方法类型，根据类型打印日志
        logger.info("入参：frontgateway:{}",signature.getMethod());

        //参数校验
        Result result = null;
        try{
            result = (Result) joinPoint.proceed();
        }catch (Throwable throwable){
            throw throwable;
        }
        long endTime = System.currentTimeMillis();
        long time = endTime-startTime;
        logger.info("出参：frontgateway{}",time);
        return result;
    }

    //只有一个advise时执行顺序  around -> before -> method -> around -> after -> afterReturn
    //有多个advise 时执行顺序 （优先级低的层层套在优先级高的里面） （利用order(1)标记优先级） around1 -> before1 -> method1 -> around2 -> after2 -> method2 -> after2 ->afterReturn2 ->afger1 -> afterReturn1

    /**
     *
     * @param joinPoint
     * @param <T>
     * @return
     * @throws Throwable
     */
    @Order(2)
    @Around(value = "bizService()")
    public <T extends BaseRequestDTO> Result LageLog(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //判断请求方法类型，根据类型打印日志
        logger.info("入参：webservice:{}",signature.getMethod());

        //参数校验
        Result result = null;
        try{
            result = (Result) joinPoint.proceed();
        }catch (Throwable throwable){
            throw throwable;
        }
        long endTime = System.currentTimeMillis();
        long time = endTime-startTime;
        logger.info("出参：webservice{}",time);
        return result;
    }


}
