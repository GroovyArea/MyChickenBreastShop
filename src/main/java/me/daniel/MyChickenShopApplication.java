package me.daniel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MyChickenShopApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(MyChickenShopApplication.class);
        springApplicationBuilder.properties("spring.config.location=" +
                "classpath:/application.yml" + ", classpath:/kakaoPay.yml");
        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.run(args);
    }

}
