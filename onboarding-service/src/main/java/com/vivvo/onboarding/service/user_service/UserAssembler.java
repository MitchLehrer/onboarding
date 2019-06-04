package com.vivvo.onboarding.service.user_service;

import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserAssembler {

    public UserDto assemble(User entity) {
        return new UserDto()
                .setUserId(entity.getUserId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setUsername(entity.getUsername());
    }

    public User disassemble(UserDto dto) {
        return new User()
                .setUserId(dto.getUserId() == null ? UUID.randomUUID() : dto.getUserId())
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setUsername(dto.getUsername());
    }

}
