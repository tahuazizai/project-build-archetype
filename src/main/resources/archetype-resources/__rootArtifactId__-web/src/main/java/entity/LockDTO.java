#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version: 1.00.00
 * @description: 锁DTO
 * @copyright: Copyright (c) 2021 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2021-11-30 8:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockDTO {
    /**
     * 第一个锁名称
     */
    private String firstLockName;

    /**
     * 第二个锁名称
     */
    private String secondLockName;

    /**
     * 第三个锁名称
     */
    private String thirdLockName;
}
