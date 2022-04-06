package cn.bugstack.springframework.test.bean;

import cn.bugstack.springframework.beans.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDao {

    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "小傅哥，北京，亦庄");
        hashMap.put("10002", "八杯水，上海，尖沙咀");
        hashMap.put("10003", "阿毛，香港，铜锣湾");
    }

    /**
     * 初始化方法
     */
    public void initDataMethod(){
        System.out.println("xml配置执行：init-method");
        hashMap.put("10004", "小傅哥");
        hashMap.put("10005", "八杯水");
        hashMap.put("10006", "阿毛");
    }

    public void destroyDataMethod(){
        System.out.println("xml配置执行：destroy-method");
        hashMap.clear();
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }

}
