package com.fsocial.accountservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicUpdate;

@Table(name = "account_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@DynamicUpdate
public class AccountProfile extends AbstractEntity<String> {

    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id")
    Account account;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "display_name")
    String displayName;

    @Column(name = "bio")
    String bio;

    @Column(name = "avatar")
    String avatar;

    @Column(name = "banner")
    String banner;

    @Column(name = "gender")
    int gender;

    @Column(name = "address")
    String address;
}
