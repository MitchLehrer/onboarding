package com.vivvo.onboarding;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class UserDto {

    private UUID userId;
    private String username;
    private String firstName;
    private String lastName;
    private List<PhoneDto> phoneList;

}
