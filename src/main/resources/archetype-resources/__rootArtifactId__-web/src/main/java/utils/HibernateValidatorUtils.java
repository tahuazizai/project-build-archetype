#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.utils;

import com.alibaba.fastjson.JSON;
import ${groupId}.exceptions.SendQueueException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @version: 1.00.00
 * @description: servedInfoDTO校验
 * @copyright: Copyright (c) 2021 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2021-06-16 14:16
 */
public class HibernateValidatorUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory()
            .getValidator();

    public static <T> Map<String, String> validate(T obj, Class<?>... var2) {
        Map<String, StringBuilder> errorMap = new HashMap<>();
        Set<ConstraintViolation<T>> set = validator.validate(obj, var2);
        if (set != null && set.size() > 0) {
            String property = null;
            for (ConstraintViolation<T> cv : set) {
                //这里循环获取错误信息，可以自定义格式
                property = cv.getPropertyPath().toString();
                if (errorMap.get(property) != null) {
                    errorMap.get(property).append("," + cv.getMessage());
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(cv.getMessage());
                    errorMap.put(property, sb);
                }
            }
        }
        return errorMap.entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().toString()));
    }


    public static <T> void validateObj(T obj, Class<?>... var2) {
        Map<String, String> map = validate(obj, var2);
        if (!map.isEmpty()) {
            throw new SendQueueException(1001, "校验异常:" + JSON.toJSONString(map));
        }
    }
}
