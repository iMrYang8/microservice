package cn.microservicedemo.productclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"cn.microservicedemo.productclient.controller","cn.microservicedemo.productclient.Client"})
@EnableEurekaClient
@EnableFeignClients("cn.microservicedemo.productclient.Client")
public class ProductClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductClientApplication.class, args);
    }

}
