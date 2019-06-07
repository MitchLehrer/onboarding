package com.vivvo.onboarding.service;

import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.entity.User;
import com.vivvo.onboarding.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static String LAST_NAME_REQUIRED = "user.lastName.REQUIRED";
    public static String LAST_NAME_LT_50 = "user.lastName.LT_50";
    public static String FIRST_NAME_REQUIRED = "user.firstName.REQUIRED";
    public static String FIRST_NAME_LT_50 = "user.firstName.LT_50";
    public static String USERNAME_REQUIRED = "user.usename.REQUIRED";
    public static String USERNAME_TAKEN = "user.usename.TAKEN";
    public static String USERNAME_CANNOT_BE_CHANGED = "user.usename.CANNOT_BE_CHANGED";

    public Map<String, String> validate(UserDto dto) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.putAll(validateFirstName(dto));
        errors.putAll(validateLastName(dto));
        errors.putAll(validateUsername(dto));
        return errors;
    }

    public Map<String, String> validateForUpdate(UserDto dto) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.putAll(validateFirstName(dto));
        errors.putAll(validateLastName(dto));
        errors.putAll(validateUsernameForUpdate(dto));
        return errors;
    }

    private Map<String, String> validateLastName(UserDto dto) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (StringUtils.isBlank(dto.getLastName())) {
            errors.put("lastName", LAST_NAME_REQUIRED);
        } else if (dto.getLastName().length() > 50) {
            errors.put("lastName", LAST_NAME_LT_50);
        }
        return errors;
    }

    private Map<String, String> validateFirstName(UserDto dto) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (StringUtils.isBlank(dto.getFirstName())) {
            errors.put("firstName", FIRST_NAME_REQUIRED);
        } else if (dto.getFirstName().length() > 50) {
            errors.put("firstName", FIRST_NAME_LT_50);
        }
        return errors;
    }

    private Map<String, String> validateUsername(UserDto dto) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (StringUtils.isBlank(dto.getUsername())) {
            errors.put("username", USERNAME_REQUIRED);
        } else if (userRepository.existsByUsername(dto.getUsername())) {
            errors.put("username", USERNAME_TAKEN);
        }
        return errors;
    }

    private Map<String, String> validateUsernameForUpdate(UserDto dto) {
        Map<String, String> errors = new LinkedHashMap<>();
        User user = findByUserId(dto.getUserId());
        if (!user.getUsername().equals(dto.getUsername())) {
            errors.put("username", USERNAME_CANNOT_BE_CHANGED);
        }
        return errors;
    }

    private User findByUserId(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
