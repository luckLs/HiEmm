package com.example.newv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan("com.example.*.mapper")
@ServletComponentScan("com.example.newv.config.SqlInjectFilter")
@SpringBootApplication
public class NewVApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewVApplication.class, args);
    }

}
