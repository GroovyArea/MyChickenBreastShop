package com.daniel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyChickenShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyChickenShopApplication.class, args);
    }

}
