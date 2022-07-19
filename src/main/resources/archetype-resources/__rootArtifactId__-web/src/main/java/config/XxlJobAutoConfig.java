#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: yangyicong
 * @date: 2020-05-27 14:21
 */
@Configuration
@ConditionalOnProperty(prefix = "xxl", name = "enabled", havingValue = "true")
public class XxlJobAutoConfig {
    private Logger logger = LoggerFactory.getLogger(XxlJobAutoConfig.class);

    @Value("${symbol_dollar}{xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${symbol_dollar}{xxl.job.accessToken:}")
    private String accessToken;

    @Value("${symbol_dollar}{xxl.job.executor.appname}")
    private String appname;

    @Value("${symbol_dollar}{xxl.job.executor.address:}")
    private String address;

    @Value("${symbol_dollar}{xxl.job.executor.ip:}")
    private String ip;

    @Value("${symbol_dollar}{xxl.job.executor.port}")
    private int port;

    @Value("${symbol_dollar}{xxl.job.executor.logpath}")
    private String logPath;

    @Value("${symbol_dollar}{xxl.job.executor.logretentiondays}")
    private int logRetentionDays;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(){

        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${symbol_dollar}{version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */


}