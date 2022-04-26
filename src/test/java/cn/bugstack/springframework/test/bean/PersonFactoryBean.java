package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.beans.factory.config.FactoryBean;

import java.util.Objects;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/4/26 14:12
 */
public class PersonFactoryBean implements FactoryBean<Person> {

    /**
     * 初始化Str
     */
    private String initStr;

    @Override
    public Person getObject() throws Exception {
        //这里我需要获取对应参数
        Objects.requireNonNull(initStr);
        String[] split = initStr.split(",");
        Person p=new Person();
        p.setAge(Integer.parseInt(split[0]));
        p.setName(split[1]);
        //这里可能需要还要有其他复杂事情需要处理
        return p;
    }

    @Override
    public Class<?> getObjectType() {
        return Person.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public String getInitStr() {
        return initStr;
    }

    public void setInitStr(String initStr) {
        this.initStr = initStr;
    }
}
