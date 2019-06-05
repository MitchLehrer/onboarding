package com.vivvo.onboarding.service.phone_service;

import com.sun.org.apache.bcel.internal.generic.DUP;
import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.entity.Phone;
import com.vivvo.onboarding.repository.PhoneRepository;
import com.vivvo.onboarding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class PhoneValidator {

    private final PhoneRepository phoneRepository;
    public static String INVALID_PHONE_NUMBER = "phone.phoneNumber.INVALID_PHONE_NUMBER";
    public static String ONLY_ONE_PRIMARY = "phone.phonePrimary.ONLY_ONE_PRIMARY";
    public static String DUPLICATE_PHONE_NUMBER = "phone.phoneNumber.DUPLICATE_PHONE_NUMBER";

    @Autowired
    private PhoneService phoneService;

    @Autowired
    public PhoneValidator(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    public Map<String, String> validate(PhoneDto dto) {
        Map<String, String> allErrors = new LinkedHashMap<>();
        List<PhoneDto> userPhones = phoneService.getByUserId(dto.getUserId());
        userPhones.add(dto);

        allErrors.putAll(validatePhoneNumber(userPhones));
        allErrors.putAll(validateOnlyOnePrimary(userPhones));
        allErrors.putAll(validateNoDuplicates(userPhones));
        return allErrors;
    }

    public Map<String, String> validate(List<PhoneDto> newPhones) {
        Map<String, String> allErrors = new LinkedHashMap<>();
        List<PhoneDto> userPhones = phoneService.getByUserId(newPhones.get(0).getUserId());
        userPhones.addAll(newPhones);

        allErrors.putAll(validatePhoneNumber(userPhones));
        allErrors.putAll(validateOnlyOnePrimary(userPhones));
        allErrors.putAll(validateNoDuplicates(userPhones));
        return allErrors;
    }

    public Map<String, String> validatePhoneNumber(List<PhoneDto> phoneList) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (PhoneDto phone : phoneList) {
            String phoneNum = phone.getPhoneNumber();

            if (phoneNum.matches("\\d{10}") || phoneNum.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}") || phoneNum.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}") || phoneNum.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            } else {
                errors.put("phoneNumber", INVALID_PHONE_NUMBER);
            }
        }

        return errors;
    }

    public Map<String, String> validateOnlyOnePrimary(List<PhoneDto> phoneList) {
        Map<String, String> errors = new LinkedHashMap<>();

        List<PhoneDto> primaryPhones = new ArrayList<>();

        for (PhoneDto phone : phoneList) {
            if (phone.getPrimary()) {
                primaryPhones.add(phone);
            }
        }

        if (primaryPhones.size() > 1) {
            errors.put("phonePrimary", ONLY_ONE_PRIMARY);
        }

        return errors;
    }

    public Map<String, String> validateNoDuplicates(List<PhoneDto> phoneList) {
        Map<String, String> errors = new LinkedHashMap<>();

        Set<String> seenValues = new HashSet();
        for (PhoneDto phone : phoneList) {
            if (seenValues.contains(phone.toString())) {
                errors.put("phoneNumber", DUPLICATE_PHONE_NUMBER);
            }
        else{
                seenValues.add(phone.toString());
            }
        }
        return errors;
    }

}
