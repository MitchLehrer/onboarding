package com.vivvo.bff.controller;

import com.vivvo.onboarding.PhoneDto;

import com.vivvo.onboarding.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/phones")
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    @GetMapping
    public List<PhoneDto> find(@PathVariable UUID userId) {
        return phoneService.getByUserId(userId);
    }

    @GetMapping("/{phoneId}")
    public PhoneDto get(@PathVariable UUID userId, @PathVariable UUID phoneId) {
        return phoneService.get(userId, phoneId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneDto create(@RequestBody PhoneDto dto, @PathVariable UUID userId) {
        if(userId != null){dto.setUserId(userId);}
        return phoneService.create(dto);
    }

    @DeleteMapping("/{phoneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID userId, @PathVariable UUID phoneId) {
        phoneService.delete(phoneId);
    }

    @PutMapping("/{phoneId}")
    public PhoneDto update(@RequestBody PhoneDto dto, @PathVariable UUID userId, @PathVariable UUID phoneId) {
        dto.setUserId(userId);
        dto.setPhoneId(phoneId);
        return phoneService.update(dto);
    }

    @PostMapping("/{phoneId}/sendVerificationCode")
    public void sendVerificationCode(@PathVariable UUID userId, @PathVariable UUID phoneId){
        phoneService.startTwilioVerify(userId, phoneId);
    }

    @PostMapping("/{phoneId}/submitVerificationCode/{verificationCode}")
    public PhoneDto submitVerificationCode(@PathVariable UUID userId, @PathVariable UUID phoneId, @PathVariable String verificationCode) {
        return phoneService.verifyPhoneNumber(userId, phoneId, verificationCode);
    }

    @PostMapping("/{phoneId}/set-primary")
    public PhoneDto SetPrimary(@PathVariable UUID userId, @PathVariable UUID phoneId){
        return phoneService.makePrimary(userId, phoneId);
    }

}
