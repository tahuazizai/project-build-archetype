#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @version: 1.00.00
 * @description: kafka的topic配置类
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-05-18 13:13
 */
@Data
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopicConfigDTO implements Serializable {
    private static final long serialVersionUID = -2769435473825996256L;

    /**
     * topic配置信息
     */
    private List<TopicConfig> topicConfigList;

    @Data
    public static class TopicConfig implements Serializable {
        private static final long serialVersionUID = 8909936048488468688L;
        /**
         * topic名称
         */
        private String topicName;

        /**
         * 分区数量
         */
        private Integer partitionNums;

        /**
         * 分区备份数量
         */
        private Short repPartitionNums;
    }
}
