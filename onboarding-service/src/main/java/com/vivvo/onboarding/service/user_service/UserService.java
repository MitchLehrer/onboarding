package com.vivvo.onboarding.service.user_service;

import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.entity.User;
import com.vivvo.onboarding.exception.NotFoundException;
import com.vivvo.onboarding.exception.ValidationException;
import com.vivvo.onboarding.repository.UserRepository;
import com.vivvo.onboarding.service.phone_service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAssembler userAssembler;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private PhoneService phoneService;

    public UserDto create(UserDto dto) {
        Map<String, String> errors = userValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        UserDto newUser = Optional.of(dto)
                .map(userAssembler::disassemble)
                .map(userRepository::save)
                .map(userAssembler::assemble)
                .orElseThrow(IllegalArgumentException::new);

        //Generate phone number(s) for user since they weren't created in user assembly
        List<PhoneDto> newUserPhones = dto.getPhoneList();
        for (PhoneDto phone : newUserPhones){
            phone.setUserId(newUser.getUserId());
            phoneService.create(phone);
        }

        //Get user by ID instead of returning Dto in case anything changed by adding to DB
        return get(newUser.getUserId());
    }

    public UserDto update(UserDto dto) {
        Map<String, String> errors = userValidator.validateForUpdate(dto);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        UserDto updateUser = Optional.of(dto)
                .map(userAssembler::disassemble)
                .map(userRepository::save)
                .map(userAssembler::assemble)
                .orElseThrow(IllegalArgumentException::new);

        //Generate phone number(s) for user since they weren't created in user assembly
        List<PhoneDto> updateUserPhones = dto.getPhoneList();
        for (PhoneDto phone : updateUserPhones){
            phoneService.update(phone);
        }

        //Get user by ID instead of returning Dto in case anything changed by adding to DB
        return get(updateUser.getUserId());
    }

    public UserDto get(UUID userId) {
        return userRepository.findById(userId)
                .map(userAssembler::assemble)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userAssembler::assemble)
                .collect(Collectors.toList());
    }

    public List<UserDto> getByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName)
                .stream()
                .map(userAssembler::assemble)
                .collect(Collectors.toList());
    }

    public List<UserDto> getByLastName(String lastName) {
        return userRepository.findByLastName(lastName)
                .stream()
                .map(userAssembler::assemble)
                .collect(Collectors.toList());
    }

    public void delete(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new NotFoundException(userId);
        }

        List<PhoneDto> userPhones = userService.get(userId).getPhoneList();
        for (PhoneDto phone : userPhones){
            phoneService.delete(phone.getPhoneId());
        }
    }

}
