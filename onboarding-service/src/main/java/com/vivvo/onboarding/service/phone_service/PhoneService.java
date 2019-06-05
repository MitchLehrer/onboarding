package com.vivvo.onboarding.service.phone_service;

import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.entity.Phone;
import com.vivvo.onboarding.exception.NotFoundException;
import com.vivvo.onboarding.exception.ValidationException;
import com.vivvo.onboarding.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PhoneAssembler phoneAssembler;

    @Autowired
    private PhoneValidator phoneValidator;

    public PhoneDto create(PhoneDto dto) {
        Map<String, String> errors = phoneValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return Optional.of(dto)
                .map(phoneAssembler::disassemble)
                .map(phoneRepository::save)
                .map(phoneAssembler::assemble)
                .orElseThrow(IllegalArgumentException::new);
    }



    public PhoneDto update(PhoneDto  dto) {
     Map<String, String> errors = phoneValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return Optional.of(dto)
                .map(phoneAssembler::disassemble)
                .map(phoneRepository::save)
                .map(phoneAssembler::assemble)
                .orElseThrow(IllegalArgumentException::new);
    }

    public PhoneDto get(UUID phoneId) {
        return phoneRepository.findById(phoneId)
                .map(phoneAssembler::assemble)
                .orElseThrow(() -> new NotFoundException(phoneId));
    }


   public List<PhoneDto> getByUserId(UUID userId){
        return phoneRepository.findByUserId(userId)
                .stream()
                .map(phoneAssembler::assemble)
                .collect(Collectors.toList());
   }


    public void delete(UUID phoneId) {
        Optional<Phone> phone = phoneRepository.findById(phoneId);
        if (phone.isPresent()) {
            phoneRepository.delete(phone.get());
        } else {
            throw new NotFoundException(phoneId);
        }
    }

    public PhoneDto makePrimary(UUID phoneId){

        List<PhoneDto> allUserPhones = getByUserId(get(phoneId).getUserId());
        for (PhoneDto userPhone : allUserPhones) {
            userPhone.setPrimary(false);
        }

        return get(phoneId)
                .setPrimary(true);
    }

}
