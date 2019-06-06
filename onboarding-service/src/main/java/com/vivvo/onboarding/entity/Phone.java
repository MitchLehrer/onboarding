package com.vivvo.onboarding.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Table(name = "phone")
@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Phone {
    @Id
    @Column(name = "phone_id")
    @Type(type = "uuid-char")
    private UUID phoneId;
    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userId;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "primary")
    private Boolean primary;
    @Column(name = "verified")
    private Boolean verified;
    @Column(name = "verification_code")
    @Type(type = "uuid-char")
    private UUID verificationCode;
}
