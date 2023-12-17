package com.lhc.newV.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan("com.lhc.*.*.*.mapper")
@ServletComponentScan("com.lhc.newV.framework.web.filter.SqlInjectFilter")
@SpringBootApplication
public class NewVApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewVApplication.class, args);
    }

}
