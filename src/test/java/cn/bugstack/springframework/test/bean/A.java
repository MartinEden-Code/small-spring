package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.beans.factory.annotation.Autowired;
import cn.bugstack.springframework.beans.stereotype.Component;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/4/3 18:20
 */
@Component
public class A {
    @Autowired
    B b;
    public void test(){
        System.out.println("输出A");
    }
}
