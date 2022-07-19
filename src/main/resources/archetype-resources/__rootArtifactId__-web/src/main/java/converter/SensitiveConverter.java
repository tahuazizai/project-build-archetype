#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.converter;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2020 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: chenlingtao
 * @date: 2020/8/25
 */
public class SensitiveConverter extends MessageConverter {

    /**
     * 手机号正则匹配(通用版)
     */
    private Pattern phonePattern;

    /**
     * 身份证正则匹配(通用版)
     */
    private Pattern idPattern;

    /**
     * 替代
     */
    private String replaceKey = "*";

    /**
     * 最大长度
     */
    private int maxLength = 4 * 1024;

    @Override
    public String convert(ILoggingEvent event){
        // 获取原始日志
        String log = event.getFormattedMessage();

        // 获取返回脱敏后的日志
        return convertSensitive(log);
    }

    /**
     * 转换日志内容-进行脱敏
     * @param log
     * @return
     */
    private String convertSensitive(String log) {
        return validateLog(log) ? doConvertSensitive(log) : log;
    }

    /**
     * 校验日志内容长度
     * @param log
     * @return
     */
    private boolean validateLog(String log) {
        if (StringUtils.isBlank(log)) {
           return false;
        }

        return this.maxLength >= log.length();
    }

    private String doConvertSensitive(String log) {
        String result;
        String property = getContext().getProperty("sensitivity-level");
        switch (property) {
            case "1":
                result = filterSensitive(log,getPhonePattern(),4,4);
                break;
            case "2":
                result = filterSensitive(log,getIdPattern(),8,4);
                break;
            case "12":
                String content = filterSensitive(log,getPhonePattern(),4,4);
                result = filterSensitive(content,getIdPattern(),8,4);
                break;
            default:
                result = log;
                break;
        }

        return result;
    }

    private String filterSensitive(String content, Pattern pattern, int hideLength,int rightLength){
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()){
            String matchGroup = matcher.group();

            matcher.appendReplacement(sb,
                    StringUtils.left(matchGroup, matchGroup.length() - hideLength - rightLength)
                            .concat(StringUtils.leftPad(StringUtils.right(matchGroup, rightLength), hideLength + rightLength, replaceKey)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private Pattern getPhonePattern() {
        if (this.phonePattern == null) {
            phonePattern = Pattern.compile(getContext().getProperty("phone-pattern"));
        }
        return phonePattern;
    }

    private Pattern getIdPattern() {
        if (this.idPattern == null) {
            idPattern = Pattern.compile(getContext().getProperty("id-pattern"));
        }
        return idPattern;
    }
}
