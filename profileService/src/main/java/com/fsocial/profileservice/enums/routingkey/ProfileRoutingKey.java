package com.fsocial.accountservice.enums.routingkey;

import lombok.Getter;

@Getter
public enum ProfileRoutingKey {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete");

    private final String routingKey;
    ProfileRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
