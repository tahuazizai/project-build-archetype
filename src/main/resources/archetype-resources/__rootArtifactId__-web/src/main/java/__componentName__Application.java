#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import ${groupId}.log.Log4jStdOutErrProxy;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @version: 1.00.00
 * @description: 日志组件
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hongyiyong
 * @date: 2018-03-02 10:45:57
 * @history:
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.leelen.cloud"})
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableDiscoveryClient
@EnableEncryptableProperties
@EnableCircuitBreaker
@EnableFeignClients
@EnableAsync
@ServletComponentScan(basePackages = "com.leelen.cloud")
@MapperScan(basePackages = {"${package}.*.dao", "com.leelen.cloud.framework.dao"})
public class ${componentName}Application {

	public static void main(String[] args)
	{
		Log4jStdOutErrProxy.bind();
		SpringApplication.run(${componentName}Application.class, args);
	}

}
