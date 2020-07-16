package com.bianyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.bianyi.item.mapper") //开启mapper接口扫描
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class);

    }
}
