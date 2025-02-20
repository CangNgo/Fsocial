package com.fsocial.accountservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum RedisKeyType {
    REGISTER("REGIS_", "REGISTER"),
    RESET("RESET_", "RESET"),
    VALUE_AFTER_VERIFY("CHECKED");

    final String redisKeyPrefix;
    final String type;

    RedisKeyType(String redisKeyPrefix, String type) {
        this.redisKeyPrefix = redisKeyPrefix;
        this.type = type;
    }

    RedisKeyType(String valueAfterVerify) {
        this.redisKeyPrefix = null;
        this.type = valueAfterVerify;
    }
}
