package com.vivvo.onboarding.service;


import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.entity.Phone;
import com.vivvo.onboarding.repository.PhoneRepository;
import com.vivvo.onboarding.repository.UserRepository;
import com.vivvo.onboarding.service.phone_service.PhoneValidator;
import com.vivvo.onboarding.service.user_service.UserValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PhoneValidatorTest {

    private UserValidator userValidator;
    private PhoneValidator phoneValidator;

    private UserRepository userRepository;
    private PhoneRepository phoneRepository;

    @Before
    public void init() {
        userRepository = getMockUserRepository();
        phoneRepository = getMockPhoneRepository();
        userValidator = new UserValidator(userRepository);
        phoneValidator = new PhoneValidator(phoneRepository);
    }

    /*
    @Test
    public void testValidPhoneNumbers_shouldSucceed() {
        UserDto user = getValidUserDto();
        PhoneDto phone = getValidPhoneDto().setUserId(user.getUserId());

        phone.setPhoneNumber("3065410371");

        Map<String, String> errors = phoneValidator.validate(phone);

        assertEquals(0, errors.size());
    }

    @Test
    public void testFirstNameGreaterThan50Characters() {
        UserDto dto = getValidUserDto()
                .setFirstName("12345678901234567890123456789012345678901234567890X");

        Map<String, String> errors = userValidator.validate(dto);

        assertEquals(1, errors.size());
        assertTrue(errors.containsKey("firstName"));
        assertEquals(UserValidator.FIRST_NAME_LT_50, errors.get("firstName"));
    }

    @Test
    public void testLastNameRequired() {
        UserDto dto = getValidUserDto()
                .setLastName(null);

        Map<String, String> errors = userValidator.validate(dto);

        assertEquals(1, errors.size());
        assertTrue(errors.containsKey("lastName"));
        assertEquals(UserValidator.LAST_NAME_REQUIRED, errors.get("lastName"));
    }


    @Test
    public void testUsernameTaken() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        UserDto dto = getValidUserDto();

        Map<String, String> errors = userValidator.validate(dto);

        assertEquals(1, errors.size());
        assertTrue(errors.containsKey("username"));
        assertEquals(UserValidator.USERNAME_TAKEN, errors.get("username"));
    }*/

    private UserDto getValidUserDto() {
        return new UserDto()
                .setUserId(UUID.randomUUID())
                .setFirstName("Tim")
                .setLastName("Dodd")
                .setUsername("doddt");
    }

    private PhoneDto getValidPhoneDto() {
        return new PhoneDto()
                .setPhoneNumber("3065410371");
    }

    private UserRepository getMockUserRepository() {
        return mock(UserRepository.class);
    }

    private PhoneRepository getMockPhoneRepository() {
        return mock(PhoneRepository.class);
    }
}
