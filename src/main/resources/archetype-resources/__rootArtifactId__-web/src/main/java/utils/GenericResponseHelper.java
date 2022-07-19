#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.utils;

import ${package}.constant.CommunityConstant;
import ${package}.constant.ResultCodeConstant;
import ${groupId}.dto.generic.GenericResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;


/**
 * 构造http应答工具类
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: youwenfeng
 * @date: 2019/4/22
 * @history:
 */
@Component
public class GenericResponseHelper {
	public <T> GenericResponse<T> genSuccessResponse(Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		response.setResult(ResultCodeConstant.SUCCESS);
		response.setSeq(seq);
		response.setWaitNum(CommunityConstant.SYNC_ACK_WAIT_NUM_0);
		return response;
	}

	public <T> GenericResponse<T> genSuccessResponse(T params, Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		if (null != params) {
			response.setParams(params);
		}
		response.setWaitTime(0);
		response.setResult(ResultCodeConstant.SUCCESS);
		response.setSeq(seq);
		response.setWaitNum(CommunityConstant.SYNC_ACK_WAIT_NUM_0);
		return response;
	}

	public <T> GenericResponse<T> genSuccessResponse(String message, T params, Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		if (null != params) {
			response.setParams(params);
		}
		if (!StringUtils.isEmpty(message)) {
			response.setMessage(message);
		}
		response.setSeq(seq);
		response.setResult(ResultCodeConstant.SUCCESS);
		return response;
	}

	public <T> GenericResponse<T> genPreSuccessResponse(Integer waitTime, Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		if (null == waitTime) {
			waitTime = CommunityConstant.DEFAULT_SYNC_ACK_WAIT_TIME_5;
		}
		response.setWaitTime(waitTime);
		response.setWaitNum(CommunityConstant.DEFAULT_SYNC_ACK_WAIT_NUM);
		response.setResult(ResultCodeConstant.SUCCESS);
		response.setSeq(seq);
		return response;
	}

	public <T> GenericResponse<T> genPreSuccessResponse(Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		response.setWaitTime(CommunityConstant.DEFAULT_SYNC_ACK_WAIT_TIME_5);
		response.setWaitNum(CommunityConstant.DEFAULT_SYNC_ACK_WAIT_NUM);
		response.setResult(ResultCodeConstant.SUCCESS);
		response.setSeq(seq);
		return response;
	}

	public <T> GenericResponse<T> genPreSuccessResponse(Integer waitNum, Integer waitTime, Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		if (null == waitNum) {
			waitNum = CommunityConstant.DEFAULT_SYNC_ACK_WAIT_NUM;
		}
		if (null == waitTime) {
			waitTime = CommunityConstant.DEFAULT_SYNC_ACK_WAIT_TIME_5;
		}
		response.setWaitNum(waitNum);
		response.setWaitTime(waitTime);
		response.setResult(ResultCodeConstant.SUCCESS);
		response.setSeq(seq);
		return response;
	}

	public <T> GenericResponse<T> genPreSuccessResponse(Integer waitTime, String message, T params, Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		if (null != params) {
			response.setParams(params);
		}
		if (!StringUtils.isEmpty(message)) {
			response.setMessage(message);
		}
		if (null != waitTime) {
			response.setWaitTime(waitTime);
			response.setWaitNum(CommunityConstant.DEFAULT_SYNC_ACK_WAIT_NUM);
		}
		response.setSeq(seq);
		response.setResult(ResultCodeConstant.SUCCESS);
		return response;
	}

	public <T> GenericResponse<T> genPreSuccessResponse(Integer waitNum, Integer waitTime, String message, T params, Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		if (null != params) {
			response.setParams(params);
		}
		if (!StringUtils.isEmpty(message)) {
			response.setMessage(message);
		}
		if (null != waitNum) {
			response.setWaitNum(waitNum);
		}
		if (null != waitTime) {
			response.setWaitTime(waitTime);
		}
		response.setSeq(seq);
		response.setResult(ResultCodeConstant.SUCCESS);
		return response;
	}

	public <T> GenericResponse<T> genFailResponse(Integer result, Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		response.setResult(result);
		response.setSeq(seq);
		return response;
	}

	public <T> GenericResponse<T> genFailResponse(Integer result, String message, Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		if (!StringUtils.isEmpty(message)) {
			response.setMessage(message);
		}
		response.setResult(result);
		response.setSeq(seq);
		return response;
	}

	public <T> GenericResponse<T> genFailResponse(String result, String message, T params, Integer seq) {
		final GenericResponse<T> response = new GenericResponse<T>();
		if (null != params) {
			response.setParams(params);
		}
		if (!StringUtils.isEmpty(message)) {
			response.setMessage(message);
		}
		response.setSeq(seq);
		response.setResult(Integer.valueOf(result));
		return response;
	}
}
