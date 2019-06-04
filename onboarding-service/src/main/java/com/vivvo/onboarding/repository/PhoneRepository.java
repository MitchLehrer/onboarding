package com.vivvo.onboarding.repository;

import com.vivvo.onboarding.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {

    List<Phone> findByUserId(UUID userId);
}

