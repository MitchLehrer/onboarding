package com.vivvo.bff.controller;

import com.vivvo.onboarding.PhoneDto;

import com.vivvo.onboarding.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/phones")
public class BFFPhoneController {

    @Autowired
    private UserClient userClient;

    @GetMapping
    public List<PhoneDto> find(@PathVariable UUID userId) {
        return userClient.getPhonesByUserId(userId);
    }

    @GetMapping("/{phoneId}")
    public PhoneDto get(@PathVariable UUID userId, @PathVariable UUID phoneId) {
        return userClient.getPhone(userId, phoneId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneDto create(@RequestBody PhoneDto dto, @PathVariable UUID userId) {
        if(userId != null){dto.setUserId(userId);}
        return userClient.createPhone(userId, dto);
    }

    @DeleteMapping("/{phoneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID userId, @PathVariable UUID phoneId) {
        userClient.deletePhone(userId, phoneId);
    }

    @PutMapping("/{phoneId}")
    public PhoneDto update(@RequestBody PhoneDto dto, @PathVariable UUID userId, @PathVariable UUID phoneId) {
        dto.setUserId(userId);
        dto.setPhoneId(phoneId);
        return userClient.updatePhone(userId, dto);
    }

    @PostMapping("/{phoneId}/sendVerificationCode")
    public void sendVerificationCode(@PathVariable UUID userId, @PathVariable UUID phoneId){
        userClient.createVerificationCode(userId, phoneId);
    }

    @PostMapping("/{phoneId}/submitVerificationCode/{verificationCode}")
    public PhoneDto submitVerificationCode(@PathVariable UUID userId, @PathVariable UUID phoneId, @PathVariable String verificationCode) {
        return userClient.verifyPhone(userId, phoneId, verificationCode);
    }

    @PostMapping("/{phoneId}/set-primary")
    public PhoneDto SetPrimary(@PathVariable UUID userId, @PathVariable UUID phoneId){
        return userClient.makePhonePrimary(userId, phoneId);
    }

}
