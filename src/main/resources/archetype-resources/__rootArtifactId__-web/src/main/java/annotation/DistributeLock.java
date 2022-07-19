#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2018-05-03 17:04
 * @history:
 */
@Target(ElementType.METHOD)//这个注解是应用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {
    /**
     * 锁名称,支持{}格式
     * @return
     */
    String lockName();

    /**
     * 是否采用看门狗
     * @return
     */
    boolean isWatch() default true;

    /**
     * 超时时间
     * @return
     */
    long timeout() default 10L;

    /**
     * 等待时间
     * @return
     */
    long waitTime() default 5L;


    /**
     * 是否阻塞
     * @return
     */
    boolean isBlock() default false;
}
