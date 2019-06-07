package com.vivvo;


import com.vivvo.onboarding.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BFF {

    public static void main(String[] args) {
        SpringApplication.run(BFF.class, args);
    }

    @Bean
    public UserClient userClient() {
        return new UserClient();
    }
}





