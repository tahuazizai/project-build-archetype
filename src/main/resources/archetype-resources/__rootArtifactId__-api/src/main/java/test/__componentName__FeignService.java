#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.test;

import ${groupId}.dto.generic.ResponseDTO;
import ${package}.entity.TestDTO;
import ${package}.test.failback.${componentName}FeignServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @version: 1.00.00
 * @description: 乐比邻
 * @copyright: Copyright (c) 2020 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: liuhong
 * @date: 2019-12-28 10:09
 */
@FeignClient(value = "${symbol_dollar}{feign.service-id.${rootArtifactId}:${rootArtifactId}}", fallbackFactory = ${componentName}FeignServiceHystrix.class, path = "/rest/${urlPrefix}")
public interface ${componentName}FeignService {
    @PostMapping("/in/linkage/test")
    ResponseDTO<TestDTO> test(@RequestBody TestDTO testDTO);
}
