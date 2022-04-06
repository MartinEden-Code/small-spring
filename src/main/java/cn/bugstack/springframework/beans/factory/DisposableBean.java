package cn.bugstack.springframework.beans.factory;

/**
 * @author hongxingyi
 * @description TODO 销毁
 * @date 2022/3/22 18:17
 */
public interface DisposableBean {

    void destroy() throws Exception;
}
