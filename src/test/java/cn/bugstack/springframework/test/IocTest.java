package cn.bugstack.springframework.test;

import cn.bugstack.springframework.beans.PropertyValue;
import cn.bugstack.springframework.beans.PropertyValues;
import cn.bugstack.springframework.beans.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.beans.factory.BeanFactory;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.config.BeanReference;
import cn.bugstack.springframework.beans.factory.config.FactoryBean;
import cn.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.bugstack.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import cn.bugstack.springframework.test.bean.*;
import cn.bugstack.springframework.test.common.MyBeanFactoryPostProcessor;
import cn.bugstack.springframework.test.common.MyBeanPostProcessor;
import cn.bugstack.springframework.test.event.CustomEvent;
import org.junit.Test;
/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/16 11:51
 *
 * todo Spring 源码中常用到的技巧 A 继承B 实现C 时，C 的接口方法由A 继承的父类 B 实现()这样的操作都蛮有意思的。也是可以复用到通常的业务系统开发中进行处理一些复杂逻辑的功能分层，做到程序的可扩展、易维护等特性。
 */
public class IocTest {

    @Test
    public void test_BeanFactory() throws IllegalAccessException, InstantiationException {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //在 Bean 注册的时候只注册一个类信息，而不会直接把实例化信息注册到 Spring 容器中。
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);

        //通过单例缓存bean
        beanFactory.createBean("userService",beanDefinition);

        UserService userService = (UserService) beanFactory.getBean("userService");

        UserService userService_singleton = (UserService) beanFactory.getBean("userService");

        System.out.println(userService.equals(userService_singleton)+" " + userService.getuId());

        //通过注册实例化bean
        BeanDefinition beanDefinition2 = new BeanDefinition(AdminService.class);

        beanFactory.registerBeanDefinition("userService_2",beanDefinition2);

        AdminService adminService = (AdminService) beanFactory.getBeanDefinition("userService_2").getBeanClass().newInstance();

        adminService.AdminInfo();


    }

    //给Bean注入属性
    @Test
    public void test_BeanProperyValues() {
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. UserDao 注册
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        // 3. UserService 设置属性[uId、userDao]
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uId", "10003"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

        // 4. UserService 注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 5. UserService 获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }

    //资源加载器解析注册对象
    @Test
    public void test_ClassPathLoader(){

        //1.初始化beanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();


        //2.读取配置文件，资源加载器，&注册Bean(相比test_BeanProperyValues种，集成了 2、3、4步骤，不用手动去注册bean，加载配置文件去加载bean)
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");


        //3.获取Bean对象调用方法
        UserService userService = (UserService) beanFactory.getBean("userService");
        System.out.println(userService.queryUserInfo());


    }

    //上下文修改bean属性、资源加载
    @Test
    public void test_BeanFactoryPostProcessorAndBeanPostProcessor(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. 读取配置文件&注册Bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        // 3. BeanDefinition 加载完成 & Bean实例化之前，修改 BeanDefinition 的属性值
        MyBeanFactoryPostProcessor beanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        // 4. Bean实例化之后，修改 Bean 属性信息
        MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(beanPostProcessor);

        // 5. 获取Bean对象调用方法
        UserService userService = beanFactory.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }

    @Test
    public void test_ApplicationContextXml() {
        // 1.聚合test_BeanFactoryPostProcessorAndBeanPostProcessor中 1、2、3、4步：初始化 BeanFactory等(并且加载配置文件,注册beanDefinition、将修改bean属性等类也定义成bean对象)
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springPostProcessor.xml");


        // 2. 获取Bean对象调用方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);

    }

    /**
     * 初始化销毁方法
     */
    @Test
    public void test_DestroyAndInitMethod(){
        //加载上下文
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();//此方法里的RunTime.getRuntime.addShutdownHook里执行了close方法（destroy方法）

        // 2. 获取Bean对象调用方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);

        //主动销毁
        //applicationContext.close();

        //Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("close！")));

    }


    /**
     * 初始化销毁方法
     */
    @Test
    public void test_Aware(){
        //加载上下文
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();//此方法里的RunTime.getRuntime.addShutdownHook里执行了close方法（destroy方法）

        // 2. 获取Bean对象调用方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);

        System.out.println("BeanFactory: "+userService.getBeanFactory());
        System.out.println("ApplicationContext: "+userService.getApplicationContext());

    }


    /**
     * 对象作用域和factoryBean
     * 新增 添加  Bean  的实例化是单例还是原型模式以及FactoryBean  的实现。
     * 应用：MyBatis 就是实现了一个 MapperFactoryBean 类，在 getObject 方法中提供 SqlSession 对执行CRUD 方法的操作
     *
     * 单例和原型区别（非单例singleton类型的bean不执行销毁方法）
     */
    @Test
    public void test_prototype() throws Exception {

        /*// 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取 Bean 对象调用方法
        UserService userService01 = applicationContext.getBean("userService", UserService.class);
        UserService userService02 = applicationContext.getBean("userService", UserService.class);

        // 3. 配置 scope="prototype/singleton"
        System.out.println(userService01);
        System.out.println(userService02);

        // 4. 打印十六进制哈希
        System.out.println(userService01 + " 十六进制哈希： " + Integer.toHexString(userService01.hashCode()));*/


        //测试beanFatory和FactoryBean
        BeanFactory beanFactory  = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Person person = (Person) beanFactory.getBean("demo");
        System.out.println(person);

        //demoFromFactory 实现了FactoryBean接口
        Person demoFromFactory = beanFactory.getBean("demoFromFactory",Person.class);
        System.out.println(demoFromFactory);

        //获取对应的personFactory
        //PersonFactoryBean factoryBean = beanFactory.getBean("demoFromFactory",PersonFactoryBean.class);
        //如果beanName不加&则获取到对应bean的实例;如果beanName加上&，则获取到BeanFactory本身的实例
        //这里+&会获取失败，因为自己写的beanFactroy功能没有实现完全,
        PersonFactoryBean factoryBean = (PersonFactoryBean) beanFactory.getBean("&demoFromFactory");
        System.out.println(factoryBean);
        //System.out.println("初始化参数为："+factoryBean.getInitStr());


    }

    @Test
    public void test_FactoryBean(){
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        UserService userService = applicationContext.getBean("userService",UserService.class);

        //定义一个IUserDao接口，之所这样做是为了通过FactoryBean  做一个自定义对象的代理操作。
        System.out.println(userService.queryUserInfo());

    }


    //容器事件和事件监听器，观察者模式（发布事件时向所有监听器广播，广播器观察事件发布变化）
    @Test
    public void test_event(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");

        applicationContext.publishEvent(new CustomEvent(applicationContext, 1019129009086763L, "成功了！"));

        applicationContext.registerShutdownHook();
    }



    //todo 问题1. 实例化和初始化区别，分别在代码哪一步


    //问题2. beanFactory 和 FactoryBean区别
    //BeanFactory:负责生产和管理Bean的一个工厂接口，提供一个Spring Ioc容器规范,
    //FactoryBean: 一种Bean创建的一种方式，对Bean的一种扩展。对于复杂的Bean对象初始化创建使用其可封装对象的创建细节。
    //BeanFactory是个Factory，也就是IOC容器或对象工厂，而FactoryBean就是个Bean。但对FactoryBean而言，这个Bean不是简单的Bean，而是一个能生产或者修饰对象生成的工厂Bean
    //  FactoryBean接口：它是一个可以返回bean的实例的工厂bean，实现这个接口可以对bean进行一些额外的操作，例如根据不同的配置类型返回不同类型的bean，简化xml配置等。
    //  在某些情况下，对于实例Bean对象比较复杂的情况下，使用传统方式创建bean会比较复杂，例如（使用xml配置），这样就出现了FactoryBean接口，可以让用户通过实现该接口来自定义该Bean接口的实例化过程。
    //  即包装一层，将复杂的初始化过程包装，让调用者无需关系具体实现细节。



    //问题3. beanFactoryPostProcessor 和 beanPostProcessor 分别的作用和区别

    //重点关注 上下文的 reflush 方法、实例化类AbstractAutowireCapbleBeanFactory 注册、实例化、初始化bean对象
    // BeanFactoryPostProcessor 在所有的 BeanDefinition 加载完成后，实例化 Bean 对象之前，提供修改 BeanDefinition 属性的机制









}
