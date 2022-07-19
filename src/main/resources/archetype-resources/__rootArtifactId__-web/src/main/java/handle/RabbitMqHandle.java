#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.handle;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ${package}.config.property.RabbitInfo;
import ${package}.config.property.RabbitMqProperties;
import ${package}.constant.CommonConstant;
import ${package}.constant.ResultCodeConstant;
import ${package}.emnums.ExchangeTypeEnum;
import ${package}.entity.RabbitmqDTO;
import ${groupId}.exception.BusinessException;
import ${package}.utils.ConsistenceHashUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.scheduling.TaskScheduler;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ${package}.emnums.ExchangeTypeEnum.FANOUT;

/**
 * @version: 1.00.00
 * @description: rabbitmq处理器
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-03-21 8:51
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMqHandle {

    private AmqpAdmin amqpAdmin;

    private final Pattern pattern = Pattern.compile("[0-9]*");


    /**
     * 初始化rabbitmq配置
     */
    public void initRabbitMqConfig(RabbitMqProperties rabbitMqProperties) {
        log.info("开始初始化rabbitmq配置");
        //校验配置信息
        validateConfig(rabbitMqProperties);
        //获取rabbitmq配置
        List<RabbitInfo> rabbitInfoList = getRabbitInfoList(rabbitMqProperties);
        //定义rabbitmq队列和交换机
        rabbitInfoList.forEach(rabbitInfo -> {
            Exchange exchange = convertExchange(rabbitInfo.getExchange());
            amqpAdmin.declareExchange(exchange);
            rabbitInfo.getQueueList().forEach(queue -> {
                Queue queue1 = convertQueue(queue);
                amqpAdmin.declareQueue(queue1);
                if (!Strings.isNullOrEmpty(rabbitInfo.getRouteKey())) {
                    Binding binding = BindingBuilder.bind(queue1).to(exchange).with(rabbitInfo.getRouteKey()).noargs();
                    amqpAdmin.declareBinding(binding);
                }
            });
        });
        log.info("完成初始化rabbitmq配置");
    }

    /**
     * 获取批量提交rabbitmq模板
     *
     * @param connectionFactory
     * @param rabbitMqProperties
     * @param taskScheduler
     * @return
     */
    public Map<String, BatchingRabbitTemplate> getBatchingRabbitTemplateMap(ConnectionFactory connectionFactory, RabbitMqProperties rabbitMqProperties, TaskScheduler taskScheduler) {
        Map<String, BatchingRabbitTemplate> batchingRabbitTemplateMap = rabbitMqProperties.getRabbitInfoList().stream()
                .filter(rabbitInfo -> Objects.nonNull(rabbitInfo.getBaseNum()))
                .map(this::getRabbitMqConfig)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(rabbitInfo -> rabbitInfo.getExchange().getName(), rabbitInfo -> {
                    BatchingStrategy batchingStrategy = new SimpleBatchingStrategy(rabbitMqProperties.getProduceBatchSize(), 1024, rabbitMqProperties.getReceiveTimeout());
                    return new BatchingRabbitTemplate(connectionFactory, batchingStrategy, taskScheduler);
                }, (k1, k2) -> k2));
        return batchingRabbitTemplateMap;
    }

    /**
     * 获取rabbitmq配置
     *
     * @return
     */
    private List<RabbitInfo> getRabbitInfoList(RabbitMqProperties rabbitMqProperties) {
        List<RabbitInfo> rabbitInfoList = Lists.newArrayList();
        rabbitMqProperties.getRabbitInfoList().forEach(rabbitInfo -> {
            if (Objects.nonNull(rabbitInfo.getBaseNum())) {
                List<RabbitInfo> rabbitInfos = getRabbitMqConfig(rabbitInfo);
                if (CollectionUtils.isNotEmpty(rabbitInfos)) {
                    rabbitInfoList.addAll(rabbitInfos);
                } else {
                    rabbitInfoList.add(rabbitInfo);
                }
            } else {
                rabbitInfoList.add(rabbitInfo);
            }
        });
        return rabbitInfoList;
    }

    /**
     * 获取rabbitmq配置
     *
     * @param info
     * @return
     */
    private List<RabbitInfo> getRabbitMqConfig(RabbitInfo info) {
        List<RabbitInfo> rabbitInfoList = Lists.newArrayList();
        boolean isMatch = Arrays.stream(ExchangeTypeEnum.values())
                .anyMatch(exchangeTypeEnum -> exchangeTypeEnum.getCode().equals(info.getExchange().getType()));
        if (isMatch) {
            createRabbitInfo(info, rabbitInfoList);
        }
        return rabbitInfoList;
    }

    private void createRabbitInfo(RabbitInfo info, List<RabbitInfo> rabbitInfoList) {
        for (int i = 0; i < info.getBaseNum(); i++) {
            RabbitInfo rabbitInfo = new RabbitInfo();
            rabbitInfo.setRouteKey(info.getRouteKey() + CommonConstant.SPOT + i);
            rabbitInfo.setExchange(getExchange(info.getExchange(), i));
            rabbitInfo.setQueueList(getQueueList(info.getQueueList(), i));
            rabbitInfoList.add(rabbitInfo);
        }
    }

    /**
     * 获取交换机
     *
     * @param rExchange
     * @param i
     * @return
     */
    private RabbitInfo.Exchange getExchange(RabbitInfo.Exchange rExchange, int i) {
        RabbitInfo.Exchange exchange = new RabbitInfo.Exchange();
        exchange.setArguments(rExchange.getArguments());
        exchange.setAutoDelete(rExchange.isAutoDelete());
        exchange.setDurable(rExchange.isDurable());
        exchange.setType(rExchange.getType());
        exchange.setName(rExchange.getName() + CommonConstant.SPOT + i);
        return exchange;
    }


    /**
     * 批量生成队列
     *
     * @param queueList
     * @param i
     * @return
     */
    private List<RabbitInfo.Queue> getQueueList(List<RabbitInfo.Queue> queueList, int i) {
        List<RabbitInfo.Queue> queues = queueList.stream().map(queue -> getQueue(queue, i)).collect(Collectors.toList());
        return queues;
    }

    /**
     * 生成队列
     *
     * @param rQueue
     * @param i
     * @return
     */
    private RabbitInfo.Queue getQueue(RabbitInfo.Queue rQueue, int i) {
        RabbitInfo.Queue queue = new RabbitInfo.Queue();
        queue.setArguments(rQueue.getArguments());
        queue.setAutoDelete(rQueue.isAutoDelete());
        queue.setDurable(rQueue.isDurable());
        queue.setExclusive(rQueue.isExclusive());
        queue.setName(rQueue.getName() + CommonConstant.SPOT + i);
        return queue;
    }

    /**
     * 获取一致性hash生成器
     * @param rabbitMqProperties
     * @return
     */
    public Map<String, ConsistenceHashUtils> getServerName(RabbitMqProperties rabbitMqProperties) {
        Map<String, ConsistenceHashUtils> map = Maps.newHashMap();
        List<RabbitInfo> rabbitInfoList = rabbitMqProperties.getRabbitInfoList()
                .stream()
                .filter(rabbitInfo -> Objects.nonNull(rabbitInfo.getBaseNum()))
                .collect(Collectors.toList());
        rabbitInfoList.forEach(rabbitInfo -> {
            map.put(rabbitInfo.getExchange().getName(), getServer(rabbitInfo.getBaseNum(), rabbitInfo.getExchange().getName()));
        });
        return map;
    }

    /**
     * 构造一致性hash环
     *
     * @param baseNum
     * @param name
     * @return
     */
    private ConsistenceHashUtils getServer(Integer baseNum, String name) {
        List<String> nodeList = Lists.newArrayList();
        for (int i = 0; i < baseNum; i++) {
            String exchangeName = Joiner.on(CommonConstant.SPOT).join(name, i);
            nodeList.add(exchangeName);
        }
        ConsistenceHashUtils consistenceHashUtils = new ConsistenceHashUtils();
        consistenceHashUtils.buildNodeNum(nodeList, null);
        return consistenceHashUtils;
    }

    /**
     * 获取队列名称
     *
     * @return
     */
    public Map<String, List<String>> getQueueNameByExchange(RabbitMqProperties rabbitMqProperties) {
        List<RabbitInfo> rabbitInfoList = getRabbitInfoList(rabbitMqProperties);
        Map<String, List<String>> queueMap = rabbitInfoList.stream().map(rabbitInfo -> {
            RabbitmqDTO rabbitmqDTO = new RabbitmqDTO();
            List<String> queueList = rabbitInfo.getQueueList()
                    .stream()
                    .map(RabbitInfo.Queue::getName)
                    .distinct()
                    .collect(Collectors.toList());
            rabbitmqDTO.setQueueList(queueList);
            int index = rabbitInfo.getExchange().getName().lastIndexOf(CommonConstant.SPOT);
            String exchangeName = rabbitInfo.getExchange().getName();
            if (index > -1) {
                String num = rabbitInfo.getExchange().getName().substring(index + 1);
                if (isNumeric(num)) {
                    exchangeName = rabbitInfo.getExchange().getName().substring(CommonConstant.ZERO_INT, index);
                }
//                exchangeName = ReUtil.replaceAll(exchangeName, "${symbol_escape}${symbol_escape}.", CommonConstant.UNDERLINE);
            }
            rabbitmqDTO.setExchageName(exchangeName);
            return rabbitmqDTO;
        }).collect(Collectors.groupingBy(RabbitmqDTO::getExchageName,
                Collectors.mapping(RabbitmqDTO::getQueueList,
                        Collectors.collectingAndThen(Collectors.toList(), t -> t.stream().flatMap(Collection::stream).collect(Collectors.toList())))));
        log.info("rabbitmq配置信息queueMap={}", JSON.toJSONString(queueMap));
        return queueMap;
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {

        return pattern.matcher(str).matches();
    }

    /**
     * 校验配置信息
     *
     * @return
     */
    private void validateConfig(RabbitMqProperties rabbitMqProperties) {
        String separator = System.getProperty("line.separator");
        List<String> errorInfoList = Lists.newArrayList();
        List<RabbitInfo> rabbitInfoList = rabbitMqProperties.getRabbitInfoList();
        if (CollectionUtils.isEmpty(rabbitInfoList)) {
            errorInfoList.add(String.format("没有配置rabbitmq配置"));
            throw new BusinessException(String.valueOf(ResultCodeConstant.ILLEGAL), Joiner.on(separator).join(errorInfoList));
        }
        int count = 1;
        for (RabbitInfo rabbitInfo : rabbitInfoList) {
            if (Objects.isNull(rabbitInfo.getExchange())) {
                errorInfoList.add(String.format("第%s个配置交换机不能为空", count));
            } else {
                if (Strings.isNullOrEmpty(rabbitInfo.getExchange().getName())) {
                    errorInfoList.add(String.format("第%s个配置交换机名称不能为空", count));
                }
                if (Objects.isNull(rabbitInfo.getExchange().getType())) {
                    errorInfoList.add(String.format("第%s个配置交换机类型不能为空", count));
                } else {
                    if (FANOUT.getCode().equals(rabbitInfo.getExchange().getType()) && Strings.isNullOrEmpty(rabbitInfo.getRouteKey())) {
                        errorInfoList.add(String.format("第%s个配置路由键不能为空", count));
                    }
                }

            }
            if (CollectionUtils.isEmpty(rabbitInfo.getQueueList())) {
                errorInfoList.add(String.format("第%s个配置队列不能为空", count));
            } else {
                for (int i = 0; i < rabbitInfo.getQueueList().size(); i++) {
                    if (Strings.isNullOrEmpty(rabbitInfo.getQueueList().get(i).getName())) {
                        errorInfoList.add(String.format("第%s个配置的第%s个队列名称不能为空", count, i + 1));
                    }
                }
            }


        }
        if (CollectionUtils.isNotEmpty(errorInfoList)) {
            throw new BusinessException(String.valueOf(ResultCodeConstant.ILLEGAL), Joiner.on(separator).join(errorInfoList));
        }

    }

    /**
     * 转换交换机
     *
     * @param exchange
     * @return
     */
    private Exchange convertExchange(RabbitInfo.Exchange exchange) {
        AbstractExchange abstractExchange = null;
        switch (exchange.getType()) {
            case CommonConstant.DIRECT:
                abstractExchange = new DirectExchange(exchange.getName(), exchange.isDurable(), exchange.isAutoDelete(), exchange.getArguments());
                break;
            case CommonConstant.FANOUT:
                abstractExchange = new FanoutExchange(exchange.getName(), exchange.isDurable(), exchange.isAutoDelete(), exchange.getArguments());
                break;
            case CommonConstant.TOPIC:
                abstractExchange = new TopicExchange(exchange.getName(), exchange.isDurable(), exchange.isAutoDelete(), exchange.getArguments());
                break;
            case CommonConstant.HEADERS:
                abstractExchange = new HeadersExchange(exchange.getName(), exchange.isDurable(), exchange.isAutoDelete(), exchange.getArguments());
                break;
            default:
                abstractExchange = new CustomExchange(exchange.getName(), exchange.getType(), exchange.isDurable(), exchange.isAutoDelete(), exchange.getArguments());
                break;

        }
        return abstractExchange;
    }

    /**
     * 转换队列
     *
     * @param queue
     * @return
     */
    private Queue convertQueue(RabbitInfo.Queue queue) {
        return new Queue(queue.getName(), queue.isDurable(), queue.isExclusive(), queue.isAutoDelete(), queue.getArguments());
    }
}
