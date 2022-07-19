#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import com.alibaba.fastjson.JSON;
import ${groupId}.dto.generic.ResponseDTO;
import ${groupId}.dto.generic.ResultDTO;
import ${groupId}.exception.BusinessException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @version: 1.00.00
 * @description: feign调用异常处理
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hongyiyong
 * @date: 2018-07-24 10:45:57
 * @history:
 */
@Component
public class FeignErrorDecode implements ErrorDecoder {

    private static final Logger logger = LoggerFactory.getLogger(FeignErrorDecode.class);
    @Override
    public Exception decode(String methodKey, Response response) {
        // 这一部分的异常将会变成子系统的异常, 不会进入hystrix的fallback方法，将会进入ErrorFilter的过滤链路
        String url = response.request()!=null? response.request().url():null;
        if (response.status() >= HttpStatus.BAD_REQUEST.value()
                && response.status() < HttpStatus.INTERNAL_SERVER_ERROR.value()
                && response.status() != HttpStatus.UNAUTHORIZED.value()
                && response.status() != HttpStatus.FORBIDDEN.value()) {
            try {
                String body = Util.toString(response.body().asReader());
                logger.error("URL: " + url + ",Method: "+ methodKey +", Error Body: "+ body);
                ResponseDTO responseDTO = JSON.parseObject(body, ResponseDTO.class);
                //ResultDTO resultDTO = JSON.parseObject(body, ResultDTO.class);
                ResultDTO resultDTO = responseDTO.getResult();
                if(resultDTO != null) {
                    return new BusinessException(resultDTO.getCode(), resultDTO.getMessage());
                }
            } catch (Exception e) {
                logger.error("Feign ErrorDecoder: ", e);
            }
        }
        // 这一部分会进入fallback

        methodKey = "URL: " + url +", Method: " + methodKey;
        return feign.FeignException.errorStatus(methodKey, response);
    }

}
