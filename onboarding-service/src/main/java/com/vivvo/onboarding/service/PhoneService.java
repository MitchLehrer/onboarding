package com.vivvo.onboarding.service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vivvo.onboarding.ApplicationProperties;
import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.entity.Phone;
import com.vivvo.onboarding.exception.NotFoundException;
import com.vivvo.onboarding.exception.PhoneVerificationException;
import com.vivvo.onboarding.exception.ValidationException;
import com.vivvo.onboarding.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PhoneAssembler phoneAssembler;

    @Autowired
    private PhoneValidator phoneValidator;

    @Autowired
    private ApplicationProperties applicationProperties;

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
        return  Optional.ofNullable(phoneRepository.findByUserIdAndPhoneId(userId, phoneId))
                .map(phoneAssembler::assemble)
                .orElseThrow(()->new NotFoundException(phoneId));
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

    public void startTwilioVerify(UUID userId, UUID phoneID){
        Twilio.init(applicationProperties.getTwilio().getAccountSID(), applicationProperties.getTwilio().getAuthToken());

        PhoneDto dto = get(userId, phoneID);

        SecureRandom randomCode = new SecureRandom();

        String verificationCode = String.format("%04d", randomCode.nextInt(10000));

        try {
            Message.creator(new PhoneNumber(dto.getPhoneNumber()),
                    new PhoneNumber("+13069943159"),
                    "Your verification code for is: " + verificationCode)
                    .create();
        } catch (TwilioException e) {
            throw new PhoneVerificationException(e.getMessage(), e);
        }

        update(get(dto.getUserId(), dto.getPhoneId()).setVerificationCode(verificationCode));
    }

    public PhoneDto verifyPhoneNumber(UUID userId, UUID phoneId, String verificationCode){
        PhoneDto phone = get(userId, phoneId);

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
