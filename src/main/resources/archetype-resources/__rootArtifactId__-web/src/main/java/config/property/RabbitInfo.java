#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.property;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @version: 1.00.00
 * @description: 交换机
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-03-21 8:28
 */
@Data
public class RabbitInfo implements Serializable {
    /**
     * 路由键
     */
    private String routeKey;
    /**
     * 队列集合
     */
    private List<Queue> queueList;
    /**
     * 交换机
     */
    private Exchange exchange;

    /**
     * 队列、交换机、路由键的取模基数
     */
    private Integer baseNum;

    @Data
    public static class Exchange {
        /**
         * 交换机名称
         */
        private String name;

        /**
         * 交换机类型
         */
        private String type;

        /**
         * 是否持久化
         */
        private boolean durable = true;

        /**
         * 当所有绑定队列都不在使用时，是否自动 删除交换器
         * 默认值false：不自动删除，推荐使用。
         */
        private boolean autoDelete = false;

        /**
         * 交互机的额外参数
         */
        private Map<String, Object> arguments;

    }

    @Data
    public static class Queue {
        /**
         * 队列名称
         */
        private String name;
        /**
         * 是否持久化
         * 默认true：当RabbitMq重启时，消息不丢失
         */
        private boolean durable = true;

        /**
         * 是否具有排他性
         * 默认false：可以多个消息者消费同一个队列
         */
        private boolean exclusive = false;

        /**
         * 当消费者客户端均断开连接，是否自动删除队列
         * 默认值false：不自动删除，推荐使用，避免消费者断开后，队列中丢弃消息
         */
        private boolean autoDelete = false;

        /**
         * 交互机的额外参数
         */
        private Map<String, Object> arguments;
    }
}
