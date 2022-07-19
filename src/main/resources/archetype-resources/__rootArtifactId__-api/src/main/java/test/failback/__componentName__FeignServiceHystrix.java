#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.test.failback;

import ${groupId}.dto.generic.ResponseDTO;
import ${package}.entity.TestDTO;
import ${package}.test.${componentName}FeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: wushoujiang
 * @date: 2019-11-20 10:10
 */
@Component
@Slf4j
public class ${componentName}FeignServiceHystrix implements FallbackFactory<${componentName}FeignService> {

    @Override
    public ${componentName}FeignService create(Throwable cause) {
        log.error("${componentName} fallback; reason was: {}", cause.getMessage());
        log.error("${componentName}FeignServiceHystrix.create " + Thread.currentThread().getThreadGroup() + " - " + Thread.currentThread().getId() +
                " - " + Thread.currentThread().getName());
        return new ${componentName}FeignService() {
            @Override
            public ResponseDTO<TestDTO> test(TestDTO testDTO) {
                return null;
            }
        };
    }
}
