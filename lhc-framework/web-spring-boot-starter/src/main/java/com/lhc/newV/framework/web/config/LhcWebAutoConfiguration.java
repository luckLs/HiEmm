package com.lhc.newV.framework.web.config;

import com.lhc.newV.framework.web.handler.ResponseAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class LhcWebAutoConfiguration {

    @Bean
    public ResponseAdvice responseAdvice(){
        return new ResponseAdvice();
    }
}
