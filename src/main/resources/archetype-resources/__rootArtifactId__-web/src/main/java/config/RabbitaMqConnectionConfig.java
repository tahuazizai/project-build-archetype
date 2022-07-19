#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import ${package}.config.property.RabbitMqProperties;
import ${package}.handle.RabbitMqHandle;
import ${package}.utils.ConsistenceHashUtils;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @version: 1.00.00
 * @description: rabbitaMq连接配置
 * @copyright: Copyright (c) 2021 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2021-05-08 16:39
 */
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@ConditionalOnProperty(prefix = "spring.rabbitmq", name = "enable", havingValue = "true")
@RefreshScope
public class RabbitaMqConnectionConfig {


    @Bean("customerConnectionFactory")
    @RefreshScope
    public ConnectionFactory customerConnectionFactory(RabbitProperties rabbitProperties) {
        return getConnectionFactory(rabbitProperties);
    }

    @Bean("produceConnectionFactory")
    @RefreshScope
    @Primary
    public ConnectionFactory produceConnectionFactory(RabbitProperties rabbitProperties) {
        return getConnectionFactory(rabbitProperties);
    }

    private ConnectionFactory getConnectionFactory(RabbitProperties rabbitProperties) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(rabbitProperties.getHost());
        cachingConnectionFactory.setPort(rabbitProperties.getPort());
        cachingConnectionFactory.setUsername(rabbitProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitProperties.getPassword());
        cachingConnectionFactory.setVirtualHost("/");
        cachingConnectionFactory.setCacheMode(rabbitProperties.getCache().getConnection().getMode());
        return cachingConnectionFactory;
    }

    /**
     * 重写rabbitTemplate模板
     *
     * @return
     */
    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(@Qualifier("produceConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }


    @Bean("manualContainerFactory")
    public RabbitListenerContainerFactory manualContainerFactory(@Qualifier("customerConnectionFactory") ConnectionFactory connectionFactory, RabbitProperties rabbitProperties) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置消费者的消息确认模式
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 最大并发数
        factory.setMaxConcurrentConsumers(20);
        // 初始并发数
        factory.setConcurrentConsumers(10);
        long maxInterval = rabbitProperties.getListener().getSimple().getRetry().getMaxInterval().getSeconds();
        long initialInterval = rabbitProperties.getListener().getSimple().getRetry().getInitialInterval().getSeconds();
        double multiplier = rabbitProperties.getListener().getSimple().getRetry().getMultiplier();
        int maxAttmpts = rabbitProperties.getListener().getSimple().getRetry().getMaxAttempts();
        factory.setAdviceChain(RetryInterceptorBuilder
                .stateless()
                .maxAttempts(maxAttmpts)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .backOffOptions(initialInterval, multiplier, maxInterval)
                .build()
        );
        return factory;

    }

    @Bean("autoCommitFactory")
    public RabbitListenerContainerFactory autoCommitFactory(@Qualifier("customerConnectionFactory") ConnectionFactory connectionFactory, SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //设置消费者的消息确认模式
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        // 最大并发数
        factory.setMaxConcurrentConsumers(20);
        // 初始并发数
        factory.setConcurrentConsumers(10);
        configurer.configure(factory, connectionFactory);
        return factory;

    }
    @Bean
    @ConditionalOnMissingBean
    @RefreshScope
    public RabbitMqHandle rabbitMqHandle(AmqpAdmin amqpAdmin, RabbitMqProperties rabbitMqProperties) {
        RabbitMqHandle rabbitMqHandle = new RabbitMqHandle(amqpAdmin);
        rabbitMqHandle.initRabbitMqConfig(rabbitMqProperties);
        return rabbitMqHandle;
    }

    @Bean
    @RefreshScope
    public Map<String, List<String>> queueMap(RabbitMqHandle rabbitMqHandle, RabbitMqProperties rabbitMqProperties) {
        return rabbitMqHandle.getQueueNameByExchange(rabbitMqProperties);
    }

    @Bean
    @RefreshScope
    public Map<String, ConsistenceHashUtils> getConsistenceHashMap(RabbitMqHandle rabbitMqHandle, RabbitMqProperties rabbitMqProperties) {
        return rabbitMqHandle.getServerName(rabbitMqProperties);
    }

    @Bean("taskScheduler")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        return taskScheduler;
    }

    @Bean("batchTaskScheduler")
    public TaskScheduler batchTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        return taskScheduler;
    }

    @Bean("batchingRabbitTemplate")
    @RefreshScope
    public BatchingRabbitTemplate batchingRabbitTemplate(@Qualifier("produceConnectionFactory") ConnectionFactory connectionFactory, RabbitMqProperties rabbitMqProperties, @Qualifier("taskScheduler") TaskScheduler taskScheduler) {
        BatchingStrategy batchingStrategy = new SimpleBatchingStrategy(rabbitMqProperties.getProduceBatchSize(), 1024, rabbitMqProperties.getReceiveTimeout());
        return new BatchingRabbitTemplate(connectionFactory, batchingStrategy, taskScheduler);
    }
}
