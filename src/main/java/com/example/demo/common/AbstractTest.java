package com.example.demo.common;

import com.example.demo.annotation.MoniterRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/9 13:24
 */
public abstract class AbstractTest implements InterfaceTest{

    public AbstractTest(){}

    public String queryPatientInfo(String name) {
        return "业务需要子类实现";
    }

    public abstract void hospCode(String config);

    //进行日志访问或者权限判断
    @MoniterRequest
    public final void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //记录访问日志
        // 进行权限判断
        if (request.isAsyncStarted()) {
            try {
                doService(request, response);
            } catch (Exception e) {
                throw new ServletException("权限异常",e);
            }
        }
    }
    // 注意访问权限定义成protected，显得既专业，又严谨，因为它是专门给子类用的
    protected abstract void doService(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException;
}
