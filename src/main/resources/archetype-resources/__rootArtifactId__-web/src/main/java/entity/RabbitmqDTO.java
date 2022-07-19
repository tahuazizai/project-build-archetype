#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version: 1.00.00
 * @description: rabbitmqDTO
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-03-21 20:03
 */
@Data
public class RabbitmqDTO implements Serializable {
    private static final long serialVersionUID = -8820134043227775641L;
    /**
     * 交换机名称
     */
    private String exchageName;

    /**
     * 队列名称
     */
    private List<String> queueList;
}
