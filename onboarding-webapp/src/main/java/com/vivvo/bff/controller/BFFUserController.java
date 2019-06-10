package com.vivvo.bff.controller;


import com.vivvo.onboarding.UserClient;
import com.vivvo.onboarding.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class BFFUserController {

    @Autowired
    private UserClient userClient;

    @GetMapping
    public List<UserDto> find() {
        return userClient.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto dto) {
        return userClient.create(dto);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable UUID userId) {
        return userClient.get(userId);
    }

    @GetMapping(params = "firstName")
    public List<UserDto> getByFirstName(@RequestParam String firstName) {
        return userClient.getByFirstName(firstName);
    }

    @GetMapping(params = "lastName")
    public List<UserDto> getByLastName(@RequestParam String lastName) {
        return userClient.getByLastName(lastName);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID userId) {
        userClient.delete(userId);
    }

    @PutMapping("/{userId}")
    public UserDto update(@PathVariable UUID userId, @RequestBody UserDto dto) {
        dto.setUserId(userId);
        return userClient.update(dto);
    }
}
