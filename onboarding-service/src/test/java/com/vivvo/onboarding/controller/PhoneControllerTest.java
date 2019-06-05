package com.vivvo.onboarding.controller;

import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.UserClient;
import com.vivvo.onboarding.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PhoneControllerTest {


    private UserClient userClient;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        userClient = new UserClient();
        userClient.setBaseUri("http://localhost:" + port);
    }

    @Test
    public void testCreatePhone_shouldSucceed(){
        UserDto createdUser = userClient.create(getValidUserDto());
        PhoneDto createdPhone = userClient.createPhone(createdUser.getUserId(), getValidPhoneDto());
        assertNotNull(createdPhone.getPhoneId());
    }

    @Test
    public void testCreateAndGetPhone_returnedObjectsShouldMatch() {
        UserDto createdUser = userClient.create(getValidUserDto());
        PhoneDto createdPhone = userClient.createPhone(createdUser.getUserId(), getValidPhoneDto());
        PhoneDto getPhone = userClient.getByPhoneId(createdPhone.getUserId(), createdPhone.getPhoneId());
        assertEquals(createdPhone, getPhone);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithInvalidId_shouldReturnNotFound() {
        UserDto createdUser = userClient.create(getValidUserDto());
        userClient.getByPhoneId(createdUser.getUserId(), new UUID(0,1));
    }

    @Test
    public void testGetPhonesByUserId(){
        UserDto createdUser = userClient.create(getValidUserDto());
        PhoneDto createdPhone = userClient.createPhone(createdUser.getUserId(), getValidPhoneDto());
        List<PhoneDto> getPhone = userClient.getPhonesByUserId(createdUser.getUserId());
        assertEquals(createdPhone, getPhone.get(0));
    }

/*

    @Test(expected = BadRequestException.class)
    public void testCreateTwiceWithSameUsername_shouldReturnBadRequest() {
        userClient.create(getValidUserDto());
        userClient.create(getValidUserDto());

    }*/

    private UserDto getValidUserDto() {
        return new UserDto()
                .setFirstName("Tim")
                .setLastName("Dodd")
                .setUsername("doddt");
    }


    private PhoneDto getValidPhoneDto() {
        return new PhoneDto()
                .setPhoneNumber("3065410371");
    }

}
