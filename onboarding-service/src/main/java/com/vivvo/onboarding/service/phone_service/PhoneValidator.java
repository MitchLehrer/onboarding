package com.vivvo.onboarding.service.phone_service;

import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.repository.PhoneRepository;
import com.vivvo.onboarding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Component
public class PhoneValidator {

    private final PhoneRepository phoneRepository;
    public static String INVALID_PHONE_NUMBER = "phone.phoneNumber.INVALID_PHONE_NUMBER";
    public static String ONLY_ONE_PRIMARY = "phone.phonePrimary.ONLY_ONE_PRIMARY";

    @Autowired
    private PhoneService phoneService;

    @Autowired
    public PhoneValidator(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    public Map<String, String> validate(PhoneDto dto) {
        Map<String, String> allErrors = new LinkedHashMap<>();
        allErrors.putAll(validatePhoneNumber(dto));
        allErrors.putAll(validateOnlyOnePrimary(dto));

        return allErrors;
    }

    public Map<String, String> validatePhoneNumber(PhoneDto dto){

        Map<String, String> errors = new LinkedHashMap<>();
        String phoneNum = dto.getPhoneNumber();

        if(phoneNum.matches("\\d{10}") || phoneNum.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")|| phoneNum.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}") || phoneNum.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")){
        }else{
            errors.put("phoneNumber", INVALID_PHONE_NUMBER);
        }

        return errors;
    }

    public Map<String, String> validateOnlyOnePrimary(PhoneDto dto){
        Map<String, String> errors = new LinkedHashMap<>();
        List<PhoneDto> userPhones = phoneService.getByUserId(dto.getUserId());

//        for (PhoneDto phone : userPhones){
//            if(phone.getPrimary()){
//                errors.put("phonePrimary", ONLY_ONE_PRIMARY);
//            }
//        }

        return errors;
    }

}
