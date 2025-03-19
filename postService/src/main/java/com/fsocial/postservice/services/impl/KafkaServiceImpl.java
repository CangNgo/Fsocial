package com.fsocial.postservice.services.impl;

import com.fsocial.event.NotificationRequest;
import com.fsocial.postservice.services.KafkaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendNotification(NotificationRequest request) {
        kafkaTemplate.send(request.getTopic(), request);
        log.info("Gửi thành công.");
    }

}
