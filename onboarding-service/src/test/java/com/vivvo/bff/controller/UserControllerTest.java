package com.vivvo.bff.controller;

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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerTest {

    private UserClient userClient;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        userClient = new UserClient();
        userClient.setBaseUri("http://localhost:" + port);
    }

    @Test
    public void testCreate_shouldSucceed() {
        UserDto createdUser = userClient.create(getValidUserDto());
        assertNotNull(createdUser.getUserId());
    }

    @Test(expected = BadRequestException.class)
    public void testCreateTwiceWithSameUsername_shouldReturnBadRequest() {
        userClient.create(getValidUserDto());
        userClient.create(getValidUserDto());
    }

    @Test
    public void testCreateAndGet_returnedObjectsShouldMatch() {
        UserDto createdUser = userClient.create(getValidUserDto());
        UserDto getUser = userClient.get(createdUser.getUserId());
        assertEquals(createdUser, getUser);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithInvalidId_shouldReturnNotFound() {
        userClient.get(new UUID(0,1));
    }


    @Test
    public void testGetByFirstName(){
        UserDto createdUser = userClient.create(getValidUserDto());
        List<UserDto> getUser = userClient.getByFirstName("Tim");
        assertEquals(createdUser, getUser.get(0));
    }

    @Test
    public void testGetByLastName(){
        UserDto createdUser = userClient.create(getValidUserDto());
        List<UserDto> getUser = userClient.getByLastName("Dodd");
        assertEquals(createdUser, getUser.get(0));
    }

    @Test
    public void testUpdateUser(){
        UserDto createdUser = userClient.create(getValidUserDto());
        String newName = "Tom";
        UserDto updatedPhone = userClient.update(createdUser.setFirstName(newName));
        assertEquals(newName, updatedPhone.getFirstName());
    }

    @Test(expected = NotFoundException.class)
    public void testDeletePhone(){
        UserDto createdUser = userClient.create(getValidUserDto());
        userClient.delete(createdUser.getUserId());
        userClient.get(createdUser.getUserId());
    }


    private UserDto getValidUserDto() {
        return new UserDto()
                .setFirstName("Tim")
                .setLastName("Dodd")
                .setUsername("doddt");
    }

}
