package com.fsocial.profileservice.enums.queue;

import lombok.Getter;

@Getter
public enum ProfileQueue {
    PROFILE_CREATE("profile.create");

    private final String queue;

    ProfileQueue(String queue) {
        this.queue = queue;
    }
}
