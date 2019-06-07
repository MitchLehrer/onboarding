package com.vivvo.onboarding;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="app")
@Data
public class ApplicationProperties {

    private TwillioProperties twillo = new TwillioProperties();

    @Data
    public static class TwillioProperties {
        //FIXME doesn't follow java variable naming conventions
        private String AUTH_TOKEN;
        private String ACCOUNT_SID;

    }
}
