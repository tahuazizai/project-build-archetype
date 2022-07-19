#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import ${groupId}.dto.generic.ResponseDTO;
import ${package}.entity.TestDTO;
import ${groupId}.generic.GenericController;
import ${package}.utils.GenericResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: 1.00.00
 * @description: 测试
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-06-21 13:51
 */
@RestController
@RequestMapping("/in/linkage")
@Slf4j
public class TestController extends GenericController {

    @Autowired
    private GenericResponseHelper genericResponseHelper;

    @PostMapping("/test")
    public ResponseDTO<TestDTO> test(@RequestBody TestDTO testDTO) {
        log.info("testDTO={}", testDTO);
        return responseHelper.getSuccessResponse(testDTO);
    }
}
