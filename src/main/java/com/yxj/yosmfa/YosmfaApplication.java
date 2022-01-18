package com.yxj.yosmfa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yxj.yosmfa.mapper")
public class YosmfaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YosmfaApplication.class, args);
    }

}
