package com.lhc.newV.framework.web.config;

import com.lhc.newV.framework.web.filter.SqlInjectFilter;
import com.lhc.newV.framework.web.result.ResponseAdvice;
import com.lhc.newV.framework.web.result.ResultExceptionHandler;
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
    public CorsConfig corsConfig(){
        return new CorsConfig();
    }

    @Bean
    public SqlInjectFilter sqlInjectFilter(){
        return new SqlInjectFilter();
    }
}
