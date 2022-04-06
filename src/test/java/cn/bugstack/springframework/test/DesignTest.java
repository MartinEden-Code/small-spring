package cn.bugstack.springframework.test;

import cn.bugstack.springframework.beans.context.support.ClassPathXmlApplicationContext;
import cn.bugstack.springframework.test.bean.A;
import cn.bugstack.springframework.test.bean.IUserService;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/4/3 18:44
 */
public class DesignTest {

    //三级缓存解决循环依赖问题（三个缓存分别存放成品对象、半成品对象和工厂对象。一级缓存，普通对象 、二级缓存，提前暴漏对象，没有完全实例化的对象 、三级缓存，存放代理对象）
    @Test
    public void test_autoScan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:autoSpring.xml");
        //类A 中 依赖了类B，类B中依赖了类A （@Autoware注解），产生了循环依赖 （还有两种情况：自身依赖，多组依赖等）
        A a = applicationContext.getBean("a",A.class);//报错（stackOverflowError 栈溢出）
        a.test();
    }

    private final static Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    public static void main(String[] args) throws Exception {
        System.out.println(getBean(C.class).getD());
        System.out.println(getBean(D.class).getC());
    }

    /**
     * 二级缓存
     * 核心思想：在C实例化后立即存入缓存，继续递归D对象实例化时就可以获得C的缓存对象（半成品对象，因为C还没有注入D），这时D对象注入完成，递归返回后C对象便可注入D
     * @param beanClass
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> T getBean(Class<T> beanClass) throws Exception {
        String beanName = beanClass.getSimpleName().toLowerCase();
        if (singletonObjects.containsKey(beanName)) {
            return (T) singletonObjects.get(beanName);
        }
        // 实例化对象入缓存
        Object obj = beanClass.newInstance();
        singletonObjects.put(beanName, obj);
        // 属性填充补全对象
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> fieldClass = field.getType();
            String fieldBeanName = fieldClass.getSimpleName().toLowerCase();
            field.set(obj, singletonObjects.containsKey(fieldBeanName) ? singletonObjects.get(fieldBeanName) : getBean(fieldClass));
            field.setAccessible(false);
        }
        return (T) obj;
    }


}

class C {

    private D d;

    public D getD() {
        System.out.println("C注入D");
        return d;
    }

    public void setD(D d) {
        this.d = d;
    }
}

class D {

    private C c;

    public C getC() {
        System.out.println("D注入C");
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }
}
