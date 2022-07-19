#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import ${package}.${componentName}Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Version: 1.0
 * @Description:
 * @copyright: Copyright (c) 2019 立林科技 All Rights Reserved
 * @company 厦门立林科技有限公司
 * @Author: hj
 * @date: 2022-06-21 16:21
 * @history:
 */
@SpringBootTest(classes = ${componentName}Application.class)
@Slf4j
class TestControllerTest {

    @Test
    void test1() {
        log.info("123");
    }
}