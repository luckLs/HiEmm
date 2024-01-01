package com.lhc.emm.system.config;

import com.lhc.emm.db.sql.DBContext;
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