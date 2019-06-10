package com.vivvo.onboarding;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="app")
@Data
public class ApplicationProperties {

    private TwilioProperties twilio = new TwilioProperties();

    @Data
    public static class TwilioProperties {
        //FIXME doesn't follow java variable naming conventions
        private String AUTH_TOKEN;
        private String ACCOUNT_SID;

    }
}
