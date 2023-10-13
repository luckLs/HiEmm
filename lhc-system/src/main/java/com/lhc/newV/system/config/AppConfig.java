package com.lhc.newV.system.config;

import com.lhc.newV.db.sql.DBContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liaohaicheng
 */
@Configuration
public class AppConfig {
    
    @Bean
    public DBContext dBcontext() {
        return new DBContext();
    }
}