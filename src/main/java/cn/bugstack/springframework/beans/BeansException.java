package cn.bugstack.springframework.beans;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/16 10:53
 */
public class BeansException extends RuntimeException{

    public BeansException(String msg){
        super(msg);
    }

    public BeansException(String msg,Throwable cause){

        super(msg,cause);
    }
}
