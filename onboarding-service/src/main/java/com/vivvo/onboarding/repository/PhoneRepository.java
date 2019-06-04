package com.vivvo.onboarding.repository;

import com.vivvo.onboarding.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhoneRepository extends JpaRepository<User, UUID> {

}

