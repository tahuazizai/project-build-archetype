#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import com.alibaba.fastjson.JSON;
import ${package}.config.property.KafkaTopicConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.BatchErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version: 1.00.00
 * @description: kafka配置
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-05-18 10:21
 */
@Configuration
@EnableConfigurationProperties(KafkaTopicConfigDTO.class)
@RefreshScope
@Slf4j
@EnableKafka
@ConditionalOnProperty(prefix = "spring.kafka", name = "enable", havingValue = "true")
public class KafKaMqConfig {

    @Autowired
    private KafkaProperties properties;

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Bean
    @RefreshScope
    public List<NewTopic> createTopic(KafkaTopicConfigDTO kafkaTopicConfigDTO) {
        if (CollectionUtils.isEmpty(kafkaTopicConfigDTO.getTopicConfigList())) {
            return null;
        }
        List<NewTopic> newTopicList = kafkaTopicConfigDTO.getTopicConfigList().stream()
                .map(topicConfig -> new NewTopic(topicConfig.getTopicName(), topicConfig.getPartitionNums(), topicConfig.getRepPartitionNums()))
                .collect(Collectors.toList());
        AdminClient adminClient = adminClient();
        CreateTopicsResult topics = adminClient.createTopics(newTopicList);
        log.info("topics:{}", JSON.toJSONString(topics));
        return newTopicList;
    }

    @Bean
    @Primary
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    @Primary
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
    }

    @Bean
    @Primary
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin.getConfig());
    }

    @Bean
    @Primary
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties(), new StringDeserializer(), new JsonDeserializer<>());
    }

    @Bean
    public ConsumerFactory<String, Object> batchConsumerFactory() {
        Map<String, Object> propMap = properties.buildConsumerProperties();
        //设置单次拉取的量，走公网访问时，该参数会有较大影响。
        propMap.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 32000);
        propMap.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 32000);
        //每次Poll的最大数量。
        //注意该值不要改得太大，如果Poll太多数据，而不能在下次Poll之前消费完，则会触发一次负载均衡，产生卡顿。
        propMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 30);
        return new DefaultKafkaConsumerFactory<>(propMap);
    }

    @Bean
    @Primary
    public KafkaListenerContainerFactory kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.setMissingTopicsFatal(false);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setAckOnError(false);
        factory.setBatchListener(false);
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory batchCustomerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(batchConsumerFactory());
        factory.setConcurrency(5);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckOnError(false);
        factory.setBatchErrorHandler(batchErrorHandler());
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        DeadLetterPublishingRecoverer deadLetterPublishingRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate());
        BackOff backOff = new ExponentialBackOff(5000L, 2.0D);
        return new SeekToCurrentErrorHandler(deadLetterPublishingRecoverer, backOff);
    }

    @Bean
    public BatchErrorHandler batchErrorHandler() {
        BackOff backOff = new ExponentialBackOff(5000L, 2.0D);
        SeekToCurrentBatchErrorHandler seekToCurrentBatchErrorHandler = new SeekToCurrentBatchErrorHandler();
        seekToCurrentBatchErrorHandler.setBackOff(backOff);
        return seekToCurrentBatchErrorHandler;
    }
}
