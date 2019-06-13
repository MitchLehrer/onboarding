package com.vivvo.onboarding.service;

import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.repository.PhoneRepository;
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

        allErrors.putAll(validatePhoneNumber(dto));

        List<PhoneDto> userPhones = phoneService.getByUserId(dto.getUserId());
        userPhones.add(dto);

        allErrors.putAll(validateOnlyOnePrimary(userPhones));
        allErrors.putAll(validateNoDuplicates(userPhones));
        return allErrors;
    }

    public Map<String, String> validateForUpdate(PhoneDto dto) {
        Map<String, String> allErrors = new LinkedHashMap<>();

        allErrors.putAll(validatePhoneNumber(dto));

        List<PhoneDto> userPhones = phoneService.getByUserId(dto.getUserId());

        allErrors.putAll(validateOnlyOnePrimaryForUpdate(dto, userPhones));
        allErrors.putAll(validateNoDuplicates(userPhones));
        return allErrors;
    }

    public Map<String, String> validate(List<PhoneDto> dtos) {

        Map<String, String> allErrors = new LinkedHashMap<>();

        for(PhoneDto dto : dtos) {
            allErrors.putAll(validatePhoneNumber(dto));
        }

        List<PhoneDto> userPhones = phoneService.getByUserId(dtos.get(0).getUserId());
        userPhones.addAll(dtos);

        allErrors.putAll(validateOnlyOnePrimary(userPhones));
        allErrors.putAll(validateNoDuplicates(userPhones));
        return allErrors;
    }

    public Map<String, String> validatePhoneNumber(PhoneDto dto) {
        Map<String, String> errors = new LinkedHashMap<>();

            String phoneNum = dto.getPhoneNumber().replaceAll("[^\\d]", "" );

            if (phoneNum.matches("\\d{10}") || phoneNum.matches("\\d{11}") ) {
            } else {
                errors.put("phoneNumber", INVALID_PHONE_NUMBER);
            }

        return errors;
    }

    public Map<String, String> validateOnlyOnePrimary(List<PhoneDto> phoneList) {
        Map<String, String> errors = new LinkedHashMap<>();

        List<PhoneDto> primaryPhones = new ArrayList<>();
        if (phoneList != null) {
            for (PhoneDto phone : phoneList) {
                if (phone.getPrimary() != null && phone.getPrimary()) {
                    primaryPhones.add(phone);
                }
            }

            if (primaryPhones.size() > 1) {
                errors.put("phonePrimary", ONLY_ONE_PRIMARY);
            }
        }

        return errors;
    }

    public Map<String, String> validateOnlyOnePrimaryForUpdate(PhoneDto dto, List<PhoneDto> phoneList) {
        Map<String, String> errors = new LinkedHashMap<>();

        List<PhoneDto> primaryPhones = new ArrayList<>();
        System.out.println(primaryPhones);
        System.out.println(dto);
        if (dto.getPrimary()){
            primaryPhones.add(dto);
        }

        if (phoneList != null) {
            for (PhoneDto phone : phoneList) {
                if (phone.getPrimary() != null && phone.getPrimary() && !phone.getPhoneId().equals(dto.getPhoneId())) {
                    System.out.println(phone.getPhoneId());
                    System.out.println(dto.getPhoneId());
                    primaryPhones.add(phone);
                }
            }

            if (primaryPhones.size() > 1) {
                errors.put("phonePrimary", ONLY_ONE_PRIMARY);
            }
        }
        System.out.println(primaryPhones);
        return errors;
    }

    public Map<String, String> validateNoDuplicates(List<PhoneDto> phoneList) {
        Map<String, String> errors = new LinkedHashMap<>();

        Set<String> seenValues = new HashSet();
        for (PhoneDto phone : phoneList) {
            if (seenValues.contains(phone.getPhoneNumber().replaceAll("[^\\d]", "" ))) {
                errors.put("phoneNumber", DUPLICATE_PHONE_NUMBER);
            }
        else{
                seenValues.add(phone.getPhoneNumber().replaceAll("[^\\d]", "" ));
            }
        }
        return errors;
    }

}
