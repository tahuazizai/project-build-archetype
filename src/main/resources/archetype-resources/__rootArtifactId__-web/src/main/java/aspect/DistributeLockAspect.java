#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import ${package}.annotation.DistributeLock;
import ${package}.emnums.ErrorCodeEnum;
import ${package}.entity.LockDTO;
import ${groupId}.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @version: 1.00.00
 * @description: Redission分布式锁
 * @copyright: Copyright (c) 2021 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2021-11-30 8:23
 */
@Component
@Slf4j
@Aspect
public class DistributeLockAspect {
    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(${package}.annotation.DistributeLock)")
    public void operate() {

    }

    @Around("operate()")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        Object object = null;
        Object[] objectArr = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);
        Class[] classArr = method.getParameterTypes();
        List<Class> classList = new ArrayList<>(Arrays.asList(classArr));
        String lockName = distributeLock.lockName();
        List<String> lockNameList = ReUtil.findAll("${symbol_escape}${symbol_escape}{(.+?)${symbol_escape}${symbol_escape}}", lockName, 0);
        if (classList.contains(LockDTO.class)) {
            if (CollectionUtils.isNotEmpty(lockNameList)) {
                lockNameList = lockNameList.stream().map(name -> name.substring(1, name.length() - 1)).collect(Collectors.toList());
                LockDTO lockDTO = Convert.convert(LockDTO.class, objectArr[classList.indexOf(LockDTO.class)]);
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(lockDTO));
                for (String name : lockNameList) {
                    String value = jsonObject.getString(name);
                    if (Strings.isNullOrEmpty(value)) {
                        log.warn("分布式锁不能为空lockName={}, key={},value={}", lockName, name, value);
                        throw new BusinessException(ErrorCodeEnum.DISTRIBUTE_LOCK_NAME_NOT_NULL_EXCEPTION.getCode().toString(), ErrorCodeEnum.DISTRIBUTE_LOCK_NAME_NOT_NULL_EXCEPTION.getDesc());
                    }
                    lockName = ReUtil.replaceAll(lockName, "${symbol_escape}${symbol_escape}{" + name + "${symbol_escape}${symbol_escape}}", value);
                }
            }

        } else {
            if (CollectionUtils.isNotEmpty(lockNameList)) {
                log.warn("分布式锁不能为空lockName={}", lockName);
                throw new BusinessException(ErrorCodeEnum.DISTRIBUTE_LOCK_NAME_NOT_NULL_EXCEPTION.getCode().toString(), ErrorCodeEnum.DISTRIBUTE_LOCK_NAME_NOT_NULL_EXCEPTION.getDesc());
            }
        }
        RLock lock = redissonClient.getLock(lockName);
        boolean isLock;
        if (distributeLock.isWatch()) {
            if (distributeLock.isBlock()) {
                lock.lock();
                isLock = true;
            } else {
                isLock = lock.tryLock(distributeLock.waitTime(), TimeUnit.SECONDS);
            }

        } else {
            if (distributeLock.isBlock()) {
                lock.lock(distributeLock.timeout(), TimeUnit.SECONDS);
                isLock = true;
            } else {
                isLock = lock.tryLock(distributeLock.waitTime(), distributeLock.timeout(), TimeUnit.SECONDS);
            }

        }
        if (isLock) {
            try {
                object = joinPoint.proceed();
            } catch (Throwable throwable) {
                log.error("业务操作异常", throwable);
                throw new BusinessException(ErrorCodeEnum.DISTRIBUTE_LOCK_BUSINESS_EXCEPTION.getCode().toString(), ErrorCodeEnum.DISTRIBUTE_LOCK_BUSINESS_EXCEPTION.getDesc());
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(ErrorCodeEnum.DISTRIBUTE_LOCK_FAIL_EXCEPTION.getCode().toString(), ErrorCodeEnum.DISTRIBUTE_LOCK_FAIL_EXCEPTION.getDesc());
        }
        return object;
    }
}
