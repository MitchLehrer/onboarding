package com.vivvo.onboarding.controller;

import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.service.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDto> find() {
        return Collections.singletonList(new UserDto()
            .setFirstName("Tim")
            .setLastName("Dodd")
            .setUserId(UUID.randomUUID())
            .setUsername("doddt"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable UUID userId) {
        return userService.get(userId);
    }

    @GetMapping(params = "firstName")
    public List<UserDto> getByFirstName(@RequestParam String firstName) {
        return userService.getByFirstName(firstName);
    }

    @GetMapping(params = "lastName")
    public List<UserDto> getByLastName(@RequestParam String lastName) {
        return userService.getByLastName(lastName);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID userId) {
        userService.delete(userId);
    }

    @PutMapping("/{userId}")
    public UserDto update(@PathVariable UUID userId, @RequestBody UserDto dto) {
        dto.setUserId(userId);
        return userService.update(dto);
    }
}
