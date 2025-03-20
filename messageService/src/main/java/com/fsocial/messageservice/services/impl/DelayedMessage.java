package com.fsocial.messageservice.services.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DelayedMessage implements Delayed {
    @Getter
    String conversationId;
    long triggerTime;

    public DelayedMessage(String conversationId, long delayMillis) {
        this.conversationId = conversationId;
        this.triggerTime = System.currentTimeMillis() + delayMillis;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(triggerTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.triggerTime, ((DelayedMessage) o).triggerTime);
    }
}
