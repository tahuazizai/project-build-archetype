#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: yangyicong
 * @date: 2020-11-10 14:37
 */
@Slf4j
public class ContextHolderUtils {
    public final static String SCHEME_DEFAULT_HTTPS = "http";
    public final static String SCHEME_HTTPS = "https";
    public final static String SCHEME_HEADER = "X-Forwarded-Proto";
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes ==null){
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request;
    }

    public static String getNetworkProtocol() {
        HttpServletRequest request = getRequest();
        if(request==null){
            return null;
        }
        return request.getScheme();
    }

    public static String getNetworkProtocolByHeader() {
        HttpServletRequest request = getRequest();
        if(request==null){
            return null;
        }
        return request.getHeader(SCHEME_HEADER);
    }

    public static Boolean isHttps() {
        HttpServletRequest request = getRequest();
        if(request==null){
            return null;
        }
        /*return request.getScheme().toLowerCase().startsWith(SCHEME_HTTPS) ||
                (!StringUtil.isEmpty(request.getHeader(SCHEME_HEADER))&&request.getHeader(SCHEME_HEADER).toLowerCase().contains(SCHEME_HTTPS));*/
        return false;

    }

    public static String convertByScheme(String url) {
       /* if(!StringUtil.isEmpty(url) && ContextHolderUtils.isHttps()){
            url = url.replace(SCHEME_DEFAULT_HTTPS,SCHEME_HTTPS);
            String port = null;
            try {
                //https端口默认为http端口+1
                port = url.substring(url.indexOf(":", 6) + 1, url.indexOf(":", 6) + 5);
                url = url.replace(":"+port,":"+(Integer.parseInt(port)+1)+"");
            }catch (Exception e){
                log.error("convert port from http to https fail, reason url:{}, port:{}",url,port);
            }

        }*/
        return url;
    }
}
