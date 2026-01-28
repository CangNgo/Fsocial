package com.fsocial.profileservice.enums.exchange;

import lombok.Getter;

@Getter
public enum Exchange {
    PROFILE("profile");
    private final  String exchange;

    Exchange(String exchange){
        this.exchange = exchange;
    }
}
