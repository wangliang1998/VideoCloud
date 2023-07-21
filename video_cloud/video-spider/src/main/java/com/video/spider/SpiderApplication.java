package com.video.spider;

import com.video.spider.clients.MovieClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@SpringBootApplication
@EnableFeignClients(clients = {MovieClient.class})
public class SpiderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class,args);
    }
}
