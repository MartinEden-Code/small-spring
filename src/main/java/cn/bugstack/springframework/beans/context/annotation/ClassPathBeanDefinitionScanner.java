package cn.bugstack.springframework.beans.context.annotation;

import cn.bugstack.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import cn.bugstack.springframework.beans.factory.config.BeanDefinition;
import cn.bugstack.springframework.beans.factory.support.BeanDefinitionRegistry;
import cn.bugstack.springframework.beans.stereotype.Component;
import cn.hutool.core.util.StrUtil;

import java.util.Set;

/**
 * @author hongxingyi
 * @description TODO 扫描核心类，配合XMLbeanDefinitionReader在加载xml文件时自动扫描注册包文件注册bean对象
 * @date 2022/4/1 10:34
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry=registry;
    }

    /**
     * 扫描包路径下的所有bean类
     * @param basePackages
     */
    public void doScan(String[] basePackages) {
        for(String basePackage: basePackages){
            //获取所有的候选类信息
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for(BeanDefinition beanDefinition:candidates) {
                //对候选类信息再次 进行 scope注解解析，解析bean的作用域，
                String scope = resolveBeanScope(beanDefinition);
                if(StrUtil.isNotEmpty(scope)){
                    beanDefinition.setScope(scope);
                }
                //最后注册bean
                registry.registerBeanDefinition(determineBeanName(beanDefinition),beanDefinition);
            }

        }

        //注解处理的BeanPostProcessor(@Autowired,@Value)
        registry.registerBeanDefinition("cn.bugstack.springframework.context.annotation.internalAutowiredAnnotationProcessor",new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));


    }

    /**
     * 找到bean 定义的scope注解值
     * @param beanDefinition
     * @return
     */
    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (null != scope) return scope.value();
        return StrUtil.EMPTY;
    }

    /**
     * beanName最终是要看 component注解中定义的值，如果未定义，则默认取获取类名值（第一个字母小写）
     * @param beanDefinition
     * @return
     */
    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }
}
