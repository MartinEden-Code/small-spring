package cn.bugstack.springframework.aop.framework;

import cn.bugstack.springframework.aop.AdvicedSupport;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/30 15:57
 */
public class ProxyFactory {

    AdvicedSupport advicedSupport;

    public ProxyFactory(AdvicedSupport advicedSupport){
        this.advicedSupport=advicedSupport;
    }

    public Object getProxy() throws Exception {
        return createAopProxy().getProxy();
    }

    public AopProxy createAopProxy() {
        if (advicedSupport.isProxyTargetClass()) {
            return new Cglib2AopProxy(advicedSupport);
        }
        return new JdkDynamicAopProxy(advicedSupport);
    }
}
