package com.vivvo.onboarding.service;

import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.UserClient;
import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.repository.PhoneRepository;
import com.vivvo.onboarding.repository.UserRepository;
import com.vivvo.onboarding.service.phone_service.PhoneValidator;
import com.vivvo.onboarding.service.user_service.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PhoneValidatorTest {


    private PhoneValidator phoneValidator;
    private UserRepository userRepository;
    private PhoneRepository phoneRepository;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        phoneRepository = getMockPhoneRepository();
        phoneValidator = new PhoneValidator(phoneRepository);
    }

    @Test
    public void testValidPhoneNumberValidation_shouldSucceed(){
        PhoneDto createdPhone = getValidPhoneDto();
        Map<String, String> errors = phoneValidator.validatePhoneNumber(createdPhone);

        assertEquals(0, errors.size());
    }

    @Test
    public void testInvalidPhoneNumber_shouldFail(){
        PhoneDto createdPhone = getValidPhoneDto().setPhoneNumber("1234567890XX");
        Map<String, String> errors = phoneValidator.validatePhoneNumber(createdPhone);

        assertEquals(1, errors.size());
        assertTrue(errors.containsKey("phoneNumber"));
        assertEquals(PhoneValidator.INVALID_PHONE_NUMBER, errors.get("phoneNumber"));
    }


    private PhoneDto getValidPhoneDto() {
        return new PhoneDto()
                .setUserId(UUID.randomUUID())
                .setPhoneNumber("3065410371");
    }

    private PhoneRepository getMockPhoneRepository() {
        return mock(PhoneRepository.class);
    }

}
