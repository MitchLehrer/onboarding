package com.vivvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.MessageSourceAccessor;

@SpringBootApplication
public class OnboadingApplication {

    //TODO implement find for users
    // implement phone validation
    //      Only one primary validation check
    //      No duplicate phone number
    // implement phone tests
    // use twillio to send an sms code and verify a phone number (more actions)



    @Autowired
    private MessageSource messageSource;

    public static void main(String[] args) {
        SpringApplication.run(OnboadingApplication.class, args);
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(messageSource);
    }
}





