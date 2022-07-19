#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.seata;

import ${package}.constant.CommunityConstant;
import ${groupId}.dto.generic.ResponseDTO;
import ${groupId}.exception.SystemException;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransactionContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: yangyicong
 * @date: 2020-11-12 14:08
 */
public class SeataRollbackUtil {

    /**
     * seata建议抛异常来触发回滚，但由于当前项目没有handle来处理，
     * 故用这个类手动回滚
     */
    public static void rollBackByXid() {
        String xid = RootContext.getXID();
        if (StringUtils.isEmpty(xid)) {
            try {
                GlobalTransactionContext.reload(xid).rollback();
            } catch (TransactionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 由于某些组件feign异常无法捕获，
     * 例如A -> B  B通过exceptionHandle捕获异常后返回给A
     * 此时A无法感知到异常，故用此方法，根据返回结果判断是否需要回滚
     *
     * @param responseDTO
     */
    public static void rollBackByRespone(ResponseDTO responseDTO) {
        if (responseDTO == null || responseDTO.getResult() == null || !"200".equals(responseDTO.getResult().getCode())) {
            String message = "";
            if (responseDTO != null && responseDTO.getResult() != null) {
                message = responseDTO.getResult().getMessage();
            }
            throw new SystemException(CommunityConstant.SEATA_ROLLBACK + "", message);
        }
    }

}
