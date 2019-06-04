package com.vivvo.onboarding.repository;

import com.vivvo.onboarding.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

}

