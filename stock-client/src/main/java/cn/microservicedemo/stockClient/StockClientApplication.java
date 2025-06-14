package cn.microservicedemo.stockClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = { "cn.microservicedemo.stockClient.Client"})
@EnableEurekaClient
@EnableFeignClients("cn.microservicedemo.stockClient.Client")
public class StockClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockClientApplication.class, args);
    }

}
