#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @version: 1.00.00
 * @description: 测试
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-06-21 14:04
 */
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class TestDTO implements Serializable {
    private static final long serialVersionUID = -870988780436382976L;

    private String name;

    private Integer age;
}
