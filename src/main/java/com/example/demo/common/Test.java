package com.example.demo.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/9 13:29
 */
public class Test extends AbstractTest{
    @Override
    public void hospCode(String config) {

    }

    //进行权限判断
    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    @Override
    public String queryPatientInfo(String name) {
        return "业务需要子类实现";
    }

}
