#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import ${groupId}.constant.Constant;
import ${package}.entity.LeelenTransactionLocal;
import ${groupId}.utils.CommonUtil;
import ${package}.utils.ContextHolderUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class FeignInterceptor implements RequestInterceptor {

    //不能截取servlet-path，因为现有的servlet-path与serviceUuid不一致，代码有用serviceUuid做判断
    @Value("${symbol_dollar}{feign.service-uuid:}")
    private String serviceUuid;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String serviceUUID = serviceUuid +":" + CommonUtil.generateUnique();
        requestTemplate.header(Constant.SERVICE_UUID, serviceUUID);
        String protocol = ContextHolderUtils.getNetworkProtocolByHeader();
        if(!StringUtils.isEmpty(protocol)){
            requestTemplate.header(ContextHolderUtils.SCHEME_HEADER, ContextHolderUtils.getNetworkProtocolByHeader());
        }
        LeelenTransactionLocal txTransactionLocal = LeelenTransactionLocal.current();
        if (txTransactionLocal != null) {
            String groupId =  txTransactionLocal.getGroupId();
            requestTemplate.header("tx-group", groupId);
        }
    }

}