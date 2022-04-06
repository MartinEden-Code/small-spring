package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.beans.context.ApplicationContext;
import cn.bugstack.springframework.beans.context.support.ApplicationConextAware;
import cn.bugstack.springframework.beans.factory.*;
import cn.bugstack.springframework.beans.factory.annotation.Autowired;
import cn.bugstack.springframework.beans.factory.annotation.Value;
import cn.bugstack.springframework.beans.stereotype.Component;

import java.util.Random;

/**
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack)
 */
@Component("userService")
public class UserService implements InitializingBean, DisposableBean, BeanFactoryAware,BeanNameAware, ApplicationConextAware ,IUserService{

    private String uId;
    private String company;
    private String location;

    @Autowired
    private UserDao userDao;

    private IUserDao iUserDao;//注入代理对象

    BeanFactory beanFactory;
    ApplicationContext applicationContext;

    @Value("${token}")
    private String token;





    @Override
    public void destroy() throws Exception {
        System.out.println("实现接口执行：UserService.destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("实现接口执行：UserService.afterPropertiesSet");
    }

    public String queryUserInfo() {
        //AOP
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //AOP 此时返回的是常量，未做属性注入，如需属性注入，需要在DefaultAdvisorAutoProxyCreator 的 postProcessAfterInitialization方法 中进行处理 （原来是在postProcessBeforeInstantiation中处理）
        //return "Martin,10001,杭州";

        //AOP代理对象属性注入
        return  "小傅哥，100001，深圳，" + company;

        //IOC
        //System.out.println("iuserDao: "+iUserDao.queryUserName(uId)+","+company+","+location);
        //return userDao.queryUserName(uId) + "," + company + "," + location;

        //annotation
        //return userDao.queryUserName("10001") + "，" + token;
    }

    @Override
    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + " success！";
    }


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory=beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("bean name:"+name);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public IUserDao getiUserDao() {
        return iUserDao;
    }

    public void setiUserDao(IUserDao iUserDao) {
        this.iUserDao = iUserDao;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserService#token = { " + token + " }";
    }
}

