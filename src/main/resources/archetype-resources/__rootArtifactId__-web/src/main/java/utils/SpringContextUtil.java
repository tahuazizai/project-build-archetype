#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.utils;

import groovy.util.logging.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @version: 1.00.00
 * @description: 获取applicationContext对象
 * @copyright: Copyright (c) 2021 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2021-05-08 10:13
 */
@Component
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过类对象获取bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过类对象和beanName获取bean
     *
     * @param clazz
     * @param beanName
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz, String beanName) {
        return getApplicationContext().getBean(beanName, clazz);
    }

    /**
     * 通过beanName获取bean
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }

    public static <T> Map<String, T> getBeanMap(Class<T> clazz) {
        Map<String, T> beansOfType = null;
        try {
            beansOfType = getApplicationContext().getBeansOfType(clazz);
        } catch (BeansException e) {

        }
        return beansOfType;
    }
}
