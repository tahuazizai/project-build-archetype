#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.seata;

import io.seata.spring.boot.autoconfigure.StarterConstants;
import io.seata.tm.api.FailureHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: yangyicong
 * @date: 2020-11-06 10:56
 */
@Configuration
public class SeataRollbackFailHandleConfig {

    @Bean({"failureHandler"})
    @ConditionalOnProperty(prefix = StarterConstants.SEATA_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
    public FailureHandler failureHandler() {
        return new SeataRollBackFailHandle();
    }


}
