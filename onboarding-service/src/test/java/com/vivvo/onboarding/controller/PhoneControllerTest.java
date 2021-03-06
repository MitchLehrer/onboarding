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

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

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
        PhoneDto getPhone = userClient.getPhone(createdPhone.getUserId(), createdPhone.getPhoneId());
        assertEquals(createdPhone, getPhone);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithInvalidId_shouldReturnNotFound() {
        UserDto createdUser = userClient.create(getValidUserDto());
        UUID randomId =  new UUID(0,1);
        assertNotEquals(createdUser.getUserId(), randomId);
        userClient.getPhone(createdUser.getUserId(), randomId);
    }

    @Test
    public void testGetPhonesByUserId_shouldSucceed(){
        UserDto createdUser = userClient.create(getValidUserDto());
        PhoneDto createdPhone = userClient.createPhone(createdUser.getUserId(), getValidPhoneDto());
        List<PhoneDto> getPhone = userClient.getPhonesByUserId(createdUser.getUserId());
        assertEquals(createdPhone, getPhone.get(0));
    }

    @Test
    public void testMakePhonePrimaryThroughAction_shouldReturnTrue() {
        UserDto createdUser = userClient.create(getValidUserDto());
        PhoneDto createdPhone = userClient.createPhone(createdUser.getUserId(), getValidPhoneDto().setPrimary(false));
        PhoneDto getPhone = userClient.makePhonePrimary(createdPhone.getUserId(), createdPhone.getPhoneId());
        assertTrue(getPhone.getPrimary());
    }

    @Test
    public void testUpdatePhone_NewAndReturnedObjectNameShouldMAtch(){
        UserDto createdUser = userClient.create(getValidUserDto());
        PhoneDto createdPhone = userClient.createPhone(createdUser.getUserId(), getValidPhoneDto());
        String newNumber = "1234567890";
        PhoneDto updatedPhone = userClient.updatePhone(createdPhone.getUserId(), createdPhone.setPhoneNumber(newNumber));
        assertEquals(newNumber, updatedPhone.getPhoneNumber());
    }

    @Test(expected = NotFoundException.class)
    public void testDeletePhone_shouldReturnNotFound(){
        UserDto createdUser = userClient.create(getValidUserDto());
        PhoneDto createdPhone = userClient.createPhone(createdUser.getUserId(), getValidPhoneDto());
        userClient.deletePhone(createdUser.getUserId(), createdPhone.getPhoneId());
        userClient.getPhone(createdUser.getUserId(), createdPhone.getPhoneId());
    }

    @Test
    public void testCreateVerificationCode_shouldReturnNotNull(){
        UserDto createdUser = userClient.create(getValidUserDto());
        PhoneDto createdPhone = userClient.createPhone(createdUser.getUserId(), getValidPhoneDto());
        assertNull(userClient.getPhone(createdPhone.getUserId(), createdPhone.getPhoneId()).getVerificationCode());
        userClient.createVerificationCode(createdPhone.getUserId(),createdPhone.getPhoneId());
        assertNotNull(userClient.getPhone(createdPhone.getUserId(), createdPhone.getPhoneId()).getVerificationCode());
    }

    //test verify phone number
    @Test
    public void testVerifyPhoneNumber_shouldSucceed(){
        UserDto createdUser = userClient.create(getValidUserDto());
        PhoneDto createdPhone = userClient.createPhone(createdUser.getUserId(), getValidPhoneDto());
        userClient.createVerificationCode(createdPhone.getUserId(),createdPhone.getPhoneId());
        PhoneDto updatedPhone = userClient.getPhone(createdPhone.getUserId(),createdPhone.getPhoneId());
        assertTrue(userClient.verifyPhone(updatedPhone.getUserId(), updatedPhone.getPhoneId(), updatedPhone.getVerificationCode()).getVerified());

    }

    //test invalid verify phone number

    private UserDto getValidUserDto() {
        return new UserDto()
                .setFirstName("Tim")
                .setLastName("Dodd")
                .setUsername("doddt");
    }


    private PhoneDto getValidPhoneDto() {
        return new PhoneDto()
                .setPhoneNumber("13065410371");
    }

}
