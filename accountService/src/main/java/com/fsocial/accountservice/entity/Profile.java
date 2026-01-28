package com.fsocial.accountservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile extends AbstractEntity<String> {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
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
