package com.lhc.emm.framework.web.config;

import com.lhc.emm.framework.web.filter.SqlInjectFilter;
import com.lhc.emm.framework.web.result.ResponseAdvice;
import com.lhc.emm.framework.web.result.ResultExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author liaohaicheng
 */
@AutoConfiguration
public class LhcWebAutoConfiguration {

    @Bean
    public ResponseAdvice responseAdvice(){
        return new ResponseAdvice();
    }

    @Bean
    public ResultExceptionHandler restExceptionHandler(){
        return new ResultExceptionHandler();
    }

    @Bean
    public com.lhc.emm.framework.web.config.CorsConfig corsConfig(){
        return new com.lhc.emm.framework.web.config.CorsConfig();
    }

    @Bean
    public SqlInjectFilter sqlInjectFilter(){
        return new SqlInjectFilter();
    }
}
