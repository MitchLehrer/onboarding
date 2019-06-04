package com.vivvo.onboarding.controller;

import com.vivvo.onboarding.PhoneDto;

import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.service.phone_service.PhoneService;
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
    public PhoneDto get(@PathVariable UUID phoneId) {
        return phoneService.get(phoneId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneDto create(@RequestBody PhoneDto dto) {
        System.out.println(dto.getUserId());
        return phoneService.create(dto);
    }

    @DeleteMapping("/{phoneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID phoneId) {
        phoneService.delete(phoneId);
    }

    @PostMapping("/{phoneId}")
    public PhoneDto update(@RequestBody PhoneDto dto, @PathVariable UUID userId, @PathVariable UUID phoneId) {
        dto.setUserId(userId);
        dto.setPhoneId(phoneId);
        return phoneService.update(dto);
    }

}
