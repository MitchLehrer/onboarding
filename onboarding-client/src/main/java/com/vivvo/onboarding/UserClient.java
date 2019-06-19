package com.vivvo.onboarding;

import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserClient {

    private Client client;
    @Setter
    private String baseUri;

    public UserClient() {
        this.client = ClientBuilder.newClient();
    }

    public UserDto create(UserDto dto) {
        return userTarget()
                .request()
                .post(Entity.json(dto), UserDto.class);
    }

    public UserDto update(UserDto dto) {
        return userTarget(dto.getUserId())
                .request()
                .put(Entity.json(dto), UserDto.class);
    }

    public void delete(UUID userId) {
        userTarget(userId)
                .request()
                .delete(Void.class);
    }

    public UserDto get(UUID userId) {
        return userTarget(userId)
                .request()
                .get(UserDto.class);
    }

    public List<UserDto> getAll() {
        return userTarget()
                .request()
                .get(new GenericType<List<UserDto>>(){});
    }

    public Page<UserDto> getByPage(Integer page, Integer size, String search) {
        return userTarget()
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("search", search)
                .request()
                .get(new GenericType<RestResponsePage<UserDto>>() {
                });
    }

    public List<UserDto> getByFirstName(String firstName) {
        return userTarget()
                .queryParam("firstName", firstName)
                .request()
                .get(new GenericType<List<UserDto>>(){});
    }

    public List<UserDto> getByLastName(String lastName) {
        return userTarget()
                .queryParam("lastName", lastName)
                .request()
                .get(new GenericType<List<UserDto>>(){});
    }

    private WebTarget userTarget() {
        return client.target(baseUri)
                .path("api")
                .path("v1")
                .path("users");
    }

    private WebTarget userTarget(UUID userId) {
        return userTarget()
                .path(userId.toString());
    }

    public PhoneDto getPhone(UUID userId, UUID phoneId){
        return phoneTarget(userId, phoneId)
                .request()
                .get(PhoneDto.class);
    }

    public List<PhoneDto> getPhonesByUserId(UUID userId){
        return phoneTarget(userId)
                .request()
                .get(new GenericType<List<PhoneDto>>(){});
    }

    public PhoneDto createPhone(UUID userId, PhoneDto dto) {
        return phoneTarget(userId)
                .request()
                .post(Entity.json(dto), PhoneDto.class);
    }

    public PhoneDto updatePhone(UUID userId, PhoneDto dto) {
        return phoneTarget(userId, dto.getPhoneId())
                .request()
                .put(Entity.json(dto), PhoneDto.class);
    }

    public void deletePhone(UUID userId, UUID phoneId) {
        phoneTarget(userId, phoneId)
                .request()
                .delete(Void.class);
    }

    public PhoneDto makePhonePrimary(UUID userId, UUID phoneId) {
        return phoneTarget(userId, phoneId)
                .path("set-primary")
                .request()
                .post(Entity.json(phoneId), PhoneDto.class);
    }

    public PhoneDto createVerificationCode(UUID userId, UUID phoneId){
        return phoneTarget(userId, phoneId)
                .path("sendVerificationCode")
                .request()
                .post(Entity.json(phoneId), PhoneDto.class);
    }

    public PhoneDto verifyPhone(UUID userId, UUID phoneId, String verificationCode){
        return phoneTarget(userId, phoneId)
                .path("submitVerificationCode")
                .path(verificationCode)
                .request()
                .post(Entity.json(verificationCode), PhoneDto.class);
    }

    private WebTarget phoneTarget(UUID userId) {
        return client
                .target(baseUri)
                .path("api")
                .path("v1")
                .path("users")
                .path(userId.toString())
                .path("phones");
    }

    private WebTarget phoneTarget(UUID userId, UUID phoneId) {
        return phoneTarget(userId)
                .path(phoneId.toString());
    }

}
