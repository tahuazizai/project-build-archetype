#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import ${groupId}.constant.Constant;
import ${groupId}.utils.CommonUtil;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Retryer;
import feign.Util;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static feign.Util.decodeOrDefault;

/**
 *
 * @version: 1.00.00
 * @description:  通用配置管理
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hongyiyong
 * @date: 2018-03-02 10:45:57
 * @history:
 */
@Configuration("feignConfig")
public class GenericConfiguration{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(${package}.config.GenericConfiguration.class);
    public static String responseLogDisable = Constant.FALSE;
    public static String fallbackLogFlag = Constant.FALSE;

    @Value("${symbol_dollar}{apache.httpclient.socketTimeout:60000}")
    private String socketTimeout;
    @Value("${symbol_dollar}{apache.httpclient.connTimeout:60000}")
    private String connTimeout;
    @Value("${symbol_dollar}{apache.httpclient.timeTolive:60}")
    private String timeTolive;
    @Value("${symbol_dollar}{apache.httpclient.maxTotal:200}")
    private String maxTotal;
    @Value("${symbol_dollar}{apache.httpclient.defaultMaxPerRoute:100}")
    private String defaultMaxPerRoute;
    @Value("${symbol_dollar}{apache.httpclient.idleTimeout:20}")
    private String idleTimeout;
    @Value("${symbol_dollar}{server.tomcat.maxKeepAliveRequests:300}")
    private String maxKeepAliveRequests;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    @ConditionalOnMissingBean(Logger.class)
    Logger feignLogger(){
        return new Logger(){

            @Override
            protected void log(String configKey, String format, Object... args) {
                // logger.info("------  {}" ,configKey);
            }
            @Override
            protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response,
                                                      long elapsedTime) throws IOException {
                if(!Constant.TRUE.equals(responseLogDisable)) {
                    String reason = response.reason() != null ?  response.reason() : "null";
                    int status = response.status();
                    String uuid = null;
                    String url = null;
                    if(response.request() != null) {
                        for (String value : Util.valuesOrEmpty(response.request().headers(), Constant.SERVICE_UUID)) {
                            uuid = value;
                        }
                        url = response.request().url();
                    }
                    if (response.body() != null && !(status == 204 || status == 205) && CommonUtil.isLogger(url)) {
                        byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                        logger.info("Receive feign response: feign_uuid: "+ uuid + ", cost: "+ + elapsedTime + "ms, reason: " +  reason +", body: " + decodeOrDefault(bodyData, Util.UTF_8, "Binary data"));
                        return response.toBuilder().body(bodyData).build();
                    } else{
                        logger.info("Receive feign response: feign_uuid: "+ uuid + ", cost: "+ + elapsedTime + "ms, reason: " +  reason );
                    }
                }
                return response;
            }
            @Override
            protected void logRequest(String configKey, Level logLevel, Request request) {
                if(!Constant.TRUE.equals(responseLogDisable)) {
                    String uuid = null;
                    for (String value : Util.valuesOrEmpty(request.headers(), Constant.SERVICE_UUID)) {
                        uuid = value;
                    }
                    String url = request.url();
                    String bodyText = null;
                    if (request.body() != null && CommonUtil.isLogger(url)) {
                        bodyText = request.charset() != null ? new String(request.body(), request.charset()) : null;
                    }
                    logger.info("Send feign request: feign_uuid: " + uuid + ", url: " + url + ", body: " +bodyText);
                }
                super.logRequest(configKey, logLevel, request);
            }
        };
    }

    @Bean
    public HttpClient httpClient() {
        // 生成默认请求配置
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        // 超时时间
        requestConfigBuilder.setSocketTimeout(Integer.parseInt(socketTimeout));
        // 连接时间
        requestConfigBuilder.setConnectTimeout(Integer.parseInt(connTimeout));
        RequestConfig defaultRequestConfig = requestConfigBuilder.build();
        // 连接池配置
        // 长连接保持30秒
        final PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(Integer.parseInt(timeTolive), TimeUnit.SECONDS);
        // 总连接数
        pollingConnectionManager.setMaxTotal(Integer.parseInt(maxTotal));
        // 同路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(Integer.parseInt(defaultMaxPerRoute));

        // httpclient 配置
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig);
        HttpClient client = httpClientBuilder.build();


        // 启动定时器，定时回收过期的连接
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
           pollingConnectionManager.closeExpiredConnections();
           pollingConnectionManager.closeIdleConnections(Integer.parseInt(idleTimeout), TimeUnit.SECONDS);
            }
        }, 10 * 1000, 5 * 1000);

        return client;
    }
    @Bean
    public ConfigurableServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector ->
                ((AbstractHttp11Protocol) connector.getProtocolHandler()).setMaxKeepAliveRequests(Integer.parseInt(maxKeepAliveRequests)));
        return factory;
    }
}
