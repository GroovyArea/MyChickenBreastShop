package com.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class EmailApplication {
    public static void main(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(EmailApplication.class);
        springApplicationBuilder.properties("spring.config.location=" +
                "classpath:/application.yml" + ", classpath:/email.yml");
        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.run(args);
    }
}
