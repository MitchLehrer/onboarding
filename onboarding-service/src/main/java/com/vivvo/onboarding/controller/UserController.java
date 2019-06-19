package com.vivvo.onboarding.controller;

import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDto> find() {
        return userService.getAll();
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

    @GetMapping(params={"page"})
    Page<UserDto> getUsersPage(@RequestParam("page") Integer page,
                               @RequestParam(value="size", required = false) Integer size,
                               @RequestParam(value="search", required = false) String search){
        return userService.getByPage(page, size, search);
    }

    @PostMapping("/addTestUsers/{numUsers}")
    public void update(@PathVariable Integer numUsers) {
        userService.addTestUsers(numUsers);
    }
}
