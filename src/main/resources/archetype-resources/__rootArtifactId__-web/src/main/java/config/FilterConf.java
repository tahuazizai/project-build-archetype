#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import ${groupId}.filter.ReqRespLoggerFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConf {
    @Value("${symbol_dollar}{application.servlet-path}")
    private String url;
    @Bean(name = "reqRespFilter")
    public FilterRegistrationBean reqRespFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new ReqRespLoggerFilter());
        filterRegistration.addUrlPatterns(url);
        return filterRegistration;
    }
}
