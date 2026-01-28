package com.fsocial.accountservice.publisher;

import com.fsocial.accountservice.dto.request.ProfileRegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.routing.profile.create}")
    String createProfileRoutingKey;

    @Value("${rabbitmq.exchange.profile.create}")
    String profileExchange;

    public void createdProfile(ProfileRegisterRequest profileRequest) {
        rabbitTemplate.convertAndSend(
                profileExchange,
                createProfileRoutingKey,
                profileRequest);
    }
}
