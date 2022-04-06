package cn.bugstack.springframework.beans.utils;

/**
 * @author hongxingyi
 * @description TODO 字符串解析器
 * @date 2022/4/1 14:26
 */
public interface StringValueResolver {

    //PropertyPlaceholderConfigurer实现了具体的方法
    String resolveStringValue(String strVal);

}
