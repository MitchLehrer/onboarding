package com.vivvo.onboarding.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.entity.Phone;
import com.vivvo.onboarding.exception.NotFoundException;
import com.vivvo.onboarding.exception.ValidationException;
import com.vivvo.onboarding.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PhoneService {
    private static final String AUTH_TOKEN = "";
    private static final String ACCOUNT_SID = "";

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

        dto.setVerified(false);

        PhoneDto newPhone = Optional.of(dto)
                .map(phoneAssembler::disassemble)
                .map(phoneRepository::save)
                .map(phoneAssembler::assemble)
                .orElseThrow(IllegalArgumentException::new);

        return newPhone;
    }


    public PhoneDto update(PhoneDto dto) {
        Map<String, String> errors = phoneValidator.validateForUpdate(dto);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return Optional.of(dto)
                .map(phoneAssembler::disassemble)
                .map(phoneRepository::save)
                .map(phoneAssembler::assemble)
                .orElseThrow(IllegalArgumentException::new);
    }

    public PhoneDto get(UUID userId, UUID phoneId) {
        return phoneRepository.findByUserIdAndPhoneId(userId, phoneId)
                .map(phoneAssembler::assemble)
                .collect(Collectors.toList())
                .orElseThrow(() -> new NotFoundException(phoneId));
    }


    public List<PhoneDto> getByUserId(UUID userId) {
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

    public PhoneDto makePrimary(UUID userId, UUID phoneId) {

        List<PhoneDto> allUserPhones = getByUserId(userId);
        for (PhoneDto userPhone : allUserPhones) {
            update(userPhone.setPrimary(false));
        }

        return update(get(userId, phoneId)
                .setPrimary(true));
    }

    public void startTwilioVerify(UUID phoneID){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        PhoneDto dto = get(phoneID);

        Random randomCode = new Random();

        String verificationCode = String.format("%04d", randomCode.nextInt(10000));

        update(get(dto.getPhoneId()).setVerificationCode(verificationCode));

        Message message = Message.creator(new PhoneNumber(dto.getPhoneNumber()),
                new PhoneNumber("+13069943159"),
                "Your verification code for is: " + verificationCode)
                .create();
    }

    public PhoneDto verifyPhoneNumber(UUID phoneId, String verificationCode){
        PhoneDto phone = get(phoneId);

        if(phone.getVerified()){
            return phone;
        }

        if(phone.getVerificationCode() != null && phone.getVerificationCode().equals(verificationCode)){
            return update(phone.setVerified(true).setVerificationCode(null));
        }else{
            throw new BadRequestException();
        }
    }

}
