package com.fsocial.profileservice.comsumer;

import com.fsocial.profileservice.dto.request.ProfileRegisterRequest;
import com.fsocial.profileservice.dto.response.ProfileResponse;
import com.fsocial.profileservice.services.AccountProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileConsumer {

    AccountProfileService accountProfileService;

    @RabbitListener(queues = "${rabbitmq.queue.profile.create}")
    public void eventCreateProfile(ProfileRegisterRequest request) {
        ProfileResponse response = accountProfileService.createAccountProfile(request);

        System.out.println("Response: " +  response);
    }
}
