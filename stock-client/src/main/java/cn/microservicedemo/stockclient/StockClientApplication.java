package cn.microservicedemo.stockclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"cn.microservicedemo.stockclient.client"})
@EnableEurekaClient
@EnableFeignClients("cn.microservicedemo.stockclient.client")
public class StockClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockClientApplication.class, args);
    }

}
