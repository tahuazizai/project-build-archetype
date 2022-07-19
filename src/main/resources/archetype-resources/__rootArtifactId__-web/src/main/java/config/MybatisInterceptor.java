#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import cn.hutool.core.util.ReUtil;
import ${package}.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间
 *
 * @Author dengshichuan@leelen.cn
 * @Date 2022-05-26
 * @Version: V1.0
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisInterceptor implements Interceptor {

	private final String reg = "param[0-9]+";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		String sqlId = mappedStatement.getId();
		log.debug("------sqlId------" + sqlId);
		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
		Object parameter = invocation.getArgs()[1];
		log.debug("------sqlCommandType------" + sqlCommandType);

		if (parameter == null) {
			return invocation.proceed();
		}
		setCommonField(parameter, sqlCommandType);
		Object proceed = invocation.proceed();
		return proceed;
	}

	private void setCommonField(Object parameter, SqlCommandType sqlCommandType) {
		Class<?> parameterClass = parameter.getClass();
		boolean primitiveOrWrapper = ClassUtils.isPrimitiveOrWrapper(parameterClass);
		boolean primitiveWrapperArray = ClassUtils.isPrimitiveWrapperArray(parameterClass);
		boolean isBasicType = primitiveOrWrapper || primitiveWrapperArray;
		if (!isBasicType) {
			if (parameter instanceof Collection) {
				Collection collection = (Collection) parameter;
				collection.forEach(obj -> {
					setCommonField(obj, sqlCommandType);
				});
			} else if (parameter instanceof Map) {
				Map<String, Object> parameterMap = (Map) parameter;
				boolean isSet = valueSet(parameterMap, sqlCommandType);
				if (!isSet) {
					parameterMap.forEach((key, value) -> {
						boolean match = ReUtil.isMatch(reg, key);
						if (!match) {
							setCommonField(value, sqlCommandType);
						}
					});
				}
			} else if (ObjectUtils.isArray(parameter)) {
				Object[] arr = (Object[]) parameter;
				for (Object obj : arr) {
					setCommonField(obj, sqlCommandType);
				}
			} else {

				valueSet(parameterClass, parameter, sqlCommandType);
			}
		}
	}


	private void valueSet(Class<?> parameterClass, Object parameter, SqlCommandType sqlCommandType) {
		Field updateTimeField = ReflectionUtils.findField(parameterClass, CommonConstant.UPDATE_TIME);
		Field createTimeField = ReflectionUtils.findField(parameterClass, CommonConstant.CREATE_TIME);
		Field updateByField = ReflectionUtils.findField(parameterClass, CommonConstant.UPDATE_BY);
		Field createByField = ReflectionUtils.findField(parameterClass, CommonConstant.CREATE_BY);
		Field versionField = ReflectionUtils.findField(parameterClass, CommonConstant.VERSION);
		Date date = new Date();
		hasSetValue(updateTimeField, parameter, date);
		hasSetValue(createTimeField, parameter, date);
		if (SqlCommandType.INSERT == sqlCommandType) {
			hasSetValue(updateByField, parameter, CommonConstant.DEFAULT_USER_NAME);
			hasSetValue(createByField, parameter, CommonConstant.DEFAULT_USER_NAME);
			hasSetValue(versionField, parameter, CommonConstant.ONE_INT);
		}

	}

	private void hasSetValue(Field field, Object parameter, Object newValue) {
		if (Objects.nonNull(field)) {
			ReflectionUtils.makeAccessible(field);
			Object oldValue = ReflectionUtils.getField(field, parameter);
			if (Objects.isNull(oldValue)) {
				ReflectionUtils.setField(field, parameter, newValue);
			}

		}
	}

	private boolean hasField(Map<String, Object> parameterMap, String fieldName, Object newValue, boolean isSet) {
		if (parameterMap.containsKey(fieldName)) {
			Object oldValue = parameterMap.get(fieldName);
			if (Objects.isNull(oldValue)) {
				parameterMap.put(fieldName, newValue);
				isSet = true;
			}

		}
		return isSet;
	}

	private boolean valueSet(Map<String, Object> parameterMap, SqlCommandType sqlCommandType) {
		boolean isSet = false;
		Date date = new Date();
		isSet = hasField(parameterMap, CommonConstant.UPDATE_TIME, date, isSet);
		isSet = hasField(parameterMap, CommonConstant.CREATE_TIME, date, isSet);
		if (SqlCommandType.INSERT == sqlCommandType) {
			isSet = hasField(parameterMap, CommonConstant.UPDATE_BY, date, isSet);
			isSet = hasField(parameterMap, CommonConstant.CREATE_BY, date, isSet);
			isSet = hasField(parameterMap, CommonConstant.VERSION, date, isSet);
		}
		return isSet;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}

/*	private void handleFieldUpdateBy(Field field, Object parameter) {
		// 登录账号
		field.setAccessible(true);
		// TODO: 获取登录用户并自动注入创建人
		field.set(parameter, "admin");
		field.setAccessible(false);
	}*/
}
