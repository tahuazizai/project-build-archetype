#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.utils;

import com.google.common.base.Strings;
import ${groupId}.constant.ValidationErrorCode;
import ${groupId}.dto.generic.ResponseDTO;
import ${groupId}.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static ${package}.emnums.ErrorCodeEnum.BUSINESS_DATA_EXCEPTION;
import static ${package}.emnums.ErrorCodeEnum.SERVICE_DEGRADE_EXCEPTION;


/**
 * @version: 1.00.00
 * @description: responseDTOUtils
 * @copyright: Copyright (c) 2021 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2021-12-29 8:12
 */
public class ResponseDTOUtils {

    /**
     * 校验responseDTO
     *
     * @param responseDTO
     */
    public static void validateResponseDTO(ResponseDTO<?> responseDTO) {
        Object object = getObject(responseDTO);
        if (object instanceof List) {
            List list = (List) object;
            if (CollectionUtils.isEmpty(list)) {
                throw new BusinessException(BUSINESS_DATA_EXCEPTION.getCode().toString(), BUSINESS_DATA_EXCEPTION.getDesc());
            }
        } else if (object instanceof String) {
            if (Strings.isNullOrEmpty(object.toString())) {
                throw new BusinessException(BUSINESS_DATA_EXCEPTION.getCode().toString(), BUSINESS_DATA_EXCEPTION.getDesc());
            }
        } else {
            if (Objects.isNull(object)) {
                throw new BusinessException(BUSINESS_DATA_EXCEPTION.getCode().toString(), BUSINESS_DATA_EXCEPTION.getDesc());
            }
        }
    }

    /**
     * 返回值包含null的校验
     * @param responseDTO
     */
    public static void validateResponseWithNullThrowException(ResponseDTO<?> responseDTO) {
        Object object = getObject(responseDTO);
        if (Objects.nonNull(object)) {
            if (object instanceof List) {
                List list = (List) object;
                if (CollectionUtils.isEmpty(list)) {
                    throw new BusinessException(BUSINESS_DATA_EXCEPTION.getCode().toString(), BUSINESS_DATA_EXCEPTION.getDesc());

                }
            } else if (object instanceof String) {
                if (Strings.isNullOrEmpty(object.toString())) {
                    throw new BusinessException(BUSINESS_DATA_EXCEPTION.getCode().toString(), BUSINESS_DATA_EXCEPTION.getDesc());

                }
            }
        }
    }

    private static Object getObject(ResponseDTO<?> responseDTO) {
        if (Objects.isNull(responseDTO)) {
            throw new BusinessException(SERVICE_DEGRADE_EXCEPTION.getCode().toString(), SERVICE_DEGRADE_EXCEPTION.getDesc());
        }
        if (!ValidationErrorCode.OK.equals(responseDTO.getResult().getCode())) {
            throw new BusinessException(responseDTO.getResult().getCode(), responseDTO.getResult().getMessage());
        }
        return responseDTO.getBody();
    }

    /**
     * 校验responseDTO
     *
     * @param responseDTO
     * @return
     */
    public static Boolean validateResponseWithNull(ResponseDTO<?> responseDTO) {
        if (Objects.isNull(responseDTO)) {
            return false;
        }
        if (!ValidationErrorCode.OK.equals(responseDTO.getResult().getCode())) {
            return false;
        }
        Object object = responseDTO.getBody();
        if (Objects.nonNull(object)) {
            if (object instanceof List) {
                List list = (List) object;
                if (CollectionUtils.isEmpty(list)) {
                    return false;
                }
            } else if (object instanceof String) {
                if (Strings.isNullOrEmpty(object.toString())) {
                    return false;
                }
            }
            return true;
        }
        return null;
    }


    /**
     * 校验responseDTO
     *
     * @param responseDTO
     * @return
     */
    public static Boolean validateResponse(ResponseDTO<?> responseDTO) {
        if (Objects.isNull(responseDTO)) {
            return false;
        }
        if (!ValidationErrorCode.OK.equals(responseDTO.getResult().getCode())) {
            return false;
        }
        Object object = responseDTO.getBody();
        return validateResponseParams(object);
    }

    /**
     * 校验response的params
     *
     * @param object
     * @return
     */
    private static boolean validateResponseParams(Object object) {
        boolean isFit = true;
        if (Objects.isNull(object)) {
            return false;
        }
        if (object instanceof List) {
            List list = (List) object;
            if (CollectionUtils.isEmpty(list)) {
                isFit = false;
            }
        } else if (object instanceof String) {
            if (Strings.isNullOrEmpty(object.toString())) {
                isFit = false;
            }
        } else {
            if (Objects.isNull(object)) {
                isFit = false;
            }
        }
        return isFit;
    }
}
