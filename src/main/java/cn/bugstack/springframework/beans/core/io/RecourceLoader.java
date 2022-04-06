package cn.bugstack.springframework.beans.core.io;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/17 10:32
 */
public interface RecourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";

    Recource getResource(String location);
}
