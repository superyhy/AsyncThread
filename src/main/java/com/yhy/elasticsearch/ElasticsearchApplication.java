package com.yhy.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticsearchApplication {

    public static void main(String[] args) {

        //解决ES连接时，netty连接数不足的问题
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(ElasticsearchApplication.class, args);
    }

}
