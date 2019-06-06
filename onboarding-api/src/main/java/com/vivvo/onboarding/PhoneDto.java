package com.vivvo.onboarding;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class PhoneDto {

    private UUID phoneId;
    private UUID userId;
    private String phoneNumber;
    private Boolean primary;
    private Boolean verified;
    private UUID verificationCode;

}
