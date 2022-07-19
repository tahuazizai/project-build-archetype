#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version: 1.00.00
 * @description: rabbitmq配置属性
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-03-21 8:43
 */
@Component
@ConfigurationProperties(prefix = "rabbitmq.config")
@Data
public class RabbitMqProperties {

    /**
     * rabbitmq配置
     */
    private List<RabbitInfo> rabbitInfoList;
    /**
     * 消费端批处理大小
     */
    private Integer customerBatchSize;

    /**
     * 生产端批处理大小
     */
    private Integer produceBatchSize;

    /**
     * 批处理超时时间
     */
    private Long receiveTimeout;
}
