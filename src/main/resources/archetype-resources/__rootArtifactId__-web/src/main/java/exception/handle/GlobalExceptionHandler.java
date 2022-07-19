#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.exception.handle;

import ${groupId}.constant.Constant;
import ${package}.constant.ResultCodeConstant;
import ${groupId}.dto.generic.AbstractBaseDTO;
import ${groupId}.dto.generic.GenericResponse;
import ${groupId}.dto.generic.ResponseDTO;
import ${groupId}.dto.generic.ResultDTO;
import ${groupId}.exception.BusinessException;
import ${groupId}.exception.SystemException;
import ${groupId}.i18n.MessageSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.core.annotation.Order;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @version: 1.00.00
 * @description: 异常处理通用类
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hongyiyong
 * @date: 2018-07-24 10:45:57
 * @history:
 */
@ControllerAdvice
@Order(1)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Resource
    private MessageSourceService messageSourceService;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * This method is used for handling BusinessException
     *
     * @param ex
     * @param request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, final WebRequest request) {

        if (request.getHeader(Constant.SERVICE_UUID) != null) {//组件内部调用
            final ResponseDTO<AbstractBaseDTO> responseDto = new ResponseDTO<AbstractBaseDTO>();
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setCode(ex.getCode());
            if (ex.getMessage() != null || ex.getMessage().equals(ex.getCode())) {
                resultDTO.setMessage(messageSourceService.getMessage(ex.getCode()));
            } else {
                resultDTO.setMessage(ex.getMessage());
            }
            responseDto.setResult(resultDTO);
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<Object>(responseDto, HttpStatus.BAD_REQUEST);
        } else {
            final GenericResponse response = new GenericResponse();
            response.setResult(Integer.parseInt(ex.getCode()));
            if (!ex.getMessage().equals(ex.getCode())) {
                response.setMessage(ex.getMessage());
            }
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
    }

    /**
     * This method is used for handling SystemException
     *
     * @param ex
     * @param request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Object> handleSystemException(SystemException ex, final HttpServletRequest request) {
        final GenericResponse response = new GenericResponse();
        response.setResult(Integer.parseInt(ex.getCode()));
        if (request.getHeader(Constant.SERVICE_UUID) != null) {
            response.setMessage(ex.getMessage());
        } else {
            response.setMessage(messageSourceService.getMessage(ex.getCode()));
        }
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method is used for handling messageNotReadableexception
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final GenericResponse response = new GenericResponse();
        response.setResult(ResultCodeConstant.FAIL);
        logger.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * This method is used for handling invalid method argument
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final GenericResponse response = new GenericResponse();
        response.setResult(ResultCodeConstant.FAIL);
        logger.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * This method is used for handling common Exception
     *
     * @param ex
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> exceptionHandler(Exception ex, final WebRequest request) {
        final GenericResponse response = new GenericResponse();
        response.setResult(ResultCodeConstant.FAIL);
        if (request.getHeader(Constant.SERVICE_UUID) != null) {
            response.setMessage(ex.getMessage());
        }
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
