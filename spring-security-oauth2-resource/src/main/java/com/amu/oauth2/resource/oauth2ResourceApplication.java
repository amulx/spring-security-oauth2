package com.amu.oauth2.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.amu.oauth2.resource.mapper")
public class oauth2ResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(oauth2ResourceApplication.class,args);
    }
}
