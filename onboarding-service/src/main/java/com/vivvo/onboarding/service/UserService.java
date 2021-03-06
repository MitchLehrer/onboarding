package com.vivvo.onboarding.service;

import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.entity.User;
import com.vivvo.onboarding.exception.NotFoundException;
import com.vivvo.onboarding.exception.ValidationException;
import com.vivvo.onboarding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private PhoneValidator phoneValidator;

    public UserDto create(UserDto dto) {

        Map<String, String> errors = userValidator.validate(dto);
        List<PhoneDto> newPhones = dto.getPhoneList();

        if (newPhones != null) {
            errors.putAll(phoneValidator.validate(newPhones));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        UserDto newUser = Optional.of(dto)
                .map(userAssembler::disassemble)
                .map(userRepository::save)
                .map(userAssembler::assemble)
                .orElseThrow(IllegalArgumentException::new);

        //Generate phone number(s) for user since they weren't created in user assembly
        if (newPhones != null && !newPhones.isEmpty()) {
            for (PhoneDto phone : newPhones) {
                phone.setUserId(newUser.getUserId());
                phoneService.create(phone);
            }
        }

        //Get user by ID instead of returning Dto in case anything changed by adding to DB
        return get(newUser.getUserId());
    }

    public UserDto update(UserDto dto) {

        Map<String, String> errors = userValidator.validateForUpdate(dto);
        List<PhoneDto> phones = dto.getPhoneList();

        if (phones != null && !phones.isEmpty()) {
            for(PhoneDto phone : phones) {
                errors.putAll(phoneValidator.validateForUpdate(phone));
            }
        }
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
        if (updateUserPhones != null) {
            for (PhoneDto phone : updateUserPhones) {
                phoneService.update(phone);
            }
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
            List<PhoneDto> userPhones = userService.get(userId).getPhoneList();
            for (PhoneDto phone : userPhones) {
                phoneService.delete(phone.getPhoneId());
            }
            userRepository.delete(user.get());
        } else {
            throw new NotFoundException(userId);
        }
    }

    public Page<UserDto> getByPage(Integer page, Integer size, String search) {
        Integer pageSize = 10;

        if(size != null){
             pageSize = size;
        }

        Pageable paging = PageRequest.of(page,pageSize);

        if(search != null &&  !search.isEmpty()){
            return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(paging, search, search, search).map(userAssembler::assemble);
        }

        return userRepository.findAll(paging).map(userAssembler::assemble);
    }

    public void addTestUsers(Integer  numUsers){
        for (int i=0; i<numUsers; i++){
            create(getValidUserDto(i));
        }
    }

    private UserDto getValidUserDto(Integer i) {
        return new UserDto()
                .setFirstName("Test")
                .setLastName("Testerson")
                .setUsername("Test" + i);
    }



}
