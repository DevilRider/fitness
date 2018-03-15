package org.gorgeous.fitness.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring bean 工具类.
 *
 * @author l.yang on 2018/3/5 下午3:24
 */
@Component
public class SpringBeanHelper implements ApplicationContextAware {

    private static ApplicationContext ctx;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext ApplicationContext
     */
    @Override
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringBeanHelper.ctx = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T registerBean(String clazzName, String beanName, String property, Object value) {
        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory =
                (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();

        //创建bean信息.
        BeanDefinitionBuilder beanDefinitionBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(clazzName);

        beanDefinitionBuilder.addPropertyValue(property, value);
        //动态注册bean.
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        return getBean(beanName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) ctx.getBean(beanName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        return ctx.getBean(clazz);
    }
}
